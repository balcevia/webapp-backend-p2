package webapp.service

import java.time.LocalDateTime

import webapp.AppConfig
import webapp.auth.{AuthToken, JwtTokenClaim, JwtTokenService, PasswordHash}
import webapp.exception.AuthenticationException
import webapp.model.auth.AuthRequest
import webapp.model.user.{User, UserDAO}
import webapp.validator.SessionValidator

import scala.concurrent.{ExecutionContext, Future}

trait SessionService {

  import webapp.AppActorSystem.system

  protected val userService: UserService
  protected val jwtTokenService: JwtTokenService
  protected val loginCountThreshold: Int
  protected val loginDelayMilliseconds: Int

  def authenticate(username: String, password: Array[Byte], ipAddress: String)(implicit ec: ExecutionContext): Future[AuthToken] = {
    authenticateUser(username, password, ipAddress).map(buildNewToken)
  }

  private def buildNewToken(user: User): AuthToken = {
    val expirationDate = LocalDateTime.now().plusMinutes(5)
    val token = jwtTokenService.generateToken(JwtTokenClaim(user.id.get, user.email, expirationDate, user.role))
    AuthToken(user.id.get, user.email, token, user.role)
  }

  def generateNewToken(userId: Long)(implicit ec: ExecutionContext): Future[AuthToken] = {
    userService.getByIdOrFail(userId).map(buildNewToken)
  }


  private def authenticateUser(username: String, password: Array[Byte], ipAddress: String)(implicit ec: ExecutionContext): Future[User] = {
    import scala.concurrent.duration._
    akka.pattern.after(loginDelayMilliseconds.millis)(
      userService.getByEmail(username).flatMap {
        case Some(user) =>
          if (user.invalidLoginCount == loginCountThreshold) {
            Future.failed(AuthenticationException("Exceeded maximum number of logins"))
          }
          else if (isPasswordValid(user, password)) {
            userService.insertOrUpdate(user.copy(invalidLoginCount = 0)).map(_ => user)
          } else {
            userService.insertOrUpdate(user.copy(invalidLoginCount = user.invalidLoginCount + 1))
              .flatMap(_ => Future.failed(AuthenticationException("Username or password is invalid")))
          }
        case None => Future.failed(AuthenticationException("Username or password is invalid"))
      })
  }

  private def isPasswordValid(user: User, password: Array[Byte]): Boolean = {
    val passwordHash = PasswordHash.generateHash(password, user.salt)
    passwordHash.deep == user.password.deep
  }

  def getToken(token: String): Option[AuthToken] = {
    jwtTokenService.decode(token).toOption.map(claim => AuthToken(claim.userId, claim.email, token, claim.role))
  }
}

object SessionService extends SessionService {
  override protected val userService: UserService = UserService
  override protected val jwtTokenService: JwtTokenService = JwtTokenService
  override protected val loginCountThreshold: Int = AppConfig.getInt("loginCountThreshold")
  override protected val loginDelayMilliseconds: Int = AppConfig.getInt("loginDelayMilliseconds")
}