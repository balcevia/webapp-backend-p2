package webapp.service

import webapp.auth.PasswordHash
import webapp.mappers.UserMapper
import webapp.model.user._

import scala.concurrent.{ExecutionContext, Future}

trait UserService {

  protected val userDAO: UserDAO
  protected val userDetailsDAO: UserDetailsDAO
  protected val passwordHash: PasswordHash
  protected val userMapper: UserMapper

  def createNewUser(request: UserDTO)(implicit ec: ExecutionContext): Future[Unit] = {
    val salt = passwordHash.generateSalt()
    val hashedPassword = passwordHash.generateHash(request.password.get, salt)
    val user = User(None, request.email.get, hashedPassword, salt, 0, UserRole.Sender)

    val userDetails = UserDetails(
      id = None,
      userId = -1,
      name = request.name.get,
      surname = request.surname.get,
      dateOfBirth = request.dateOfBirth.get,
      streetAddress = request.streetAddress.get,
      buildingNumber = request.buildingNumber.get,
      postalCode = request.postalCode.get,
      country = request.country.get,
      pesel = request.pesel.get
    )

    for {
      insertedUser <- userDAO.insertWithAutoIncrement(user)
      _ <- userDetailsDAO.insert(userDetails.copy(userId = insertedUser.id.get))
    } yield ()

  }

  def getByEmail(email: String)(implicit ec: ExecutionContext): Future[Option[User]] = userDAO.getByEmail(email)

  def insertOrUpdate(user: User)(implicit ec: ExecutionContext): Future[Int] = userDAO.insertOrUpdate(user)

  def getByIdOrFail(id: Long): Future[User] = userDAO.getByIdOrFail(id)

  def getByIds(ids: Seq[Long]): Future[Seq[User]] = userDAO.getByIds(ids)

  def getUserDetailsByUserId(id: Long)(implicit ec: ExecutionContext): Future[UserDetails] = userDetailsDAO.getByUserId(id)
}

object UserService extends UserService {
  override protected val userDAO: UserDAO = UserDAO
  override protected val userDetailsDAO: UserDetailsDAO = UserDetailsDAO
  override protected val passwordHash: PasswordHash = PasswordHash
  override protected val userMapper: UserMapper = UserMapper
}