package webapp

/**
  * Created by Alfred on 12.01.2021.
  */
import java.util.Date

import akka.actor.ActorSystem
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import akka.http.scaladsl.server.{Directive0, Directive1, Route}
import akka.http.scaladsl.unmarshalling.{FromRequestUnmarshaller, Unmarshaller}
import akka.http.scaladsl.server.Directives._
import webapp.AppRejectionHandler.AuthFailedRejection
import webapp.api.{ErrorResponse, OkResponse}
import webapp.auth.AuthToken
import webapp.exception.{AuthenticationException, BadRoleRejection}
import webapp.model.user.UserRole
import webapp.service.SessionService
import webapp.utils.JsonUtils

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

trait AppDirectives {

  implicit val executionContext: ExecutionContext =  AppActorSystem.executionContext
  implicit val actorSystem: ActorSystem = AppActorSystem.system

  private def rightNow(): String = new Date(System.currentTimeMillis()).toString

  implicit def json4sUnmarshaller[T: Manifest]: FromRequestUnmarshaller[T] = {
    Unmarshaller(_ => request => {
      Unmarshaller.stringUnmarshaller(request.entity).map(JsonUtils.parse[T])
    })
  }

  def completeFuture[T](token: Option[AuthToken])(future: => Future[T]): Route = onComplete(future) {
    case Success(result) =>
      token match {
        case Some(t) =>
          onComplete(SessionService.generateNewToken(t.userId)) {
            case Success(newToken) =>
              ok(result, Some(newToken.token))
            case Failure(ex) => throw ex
          }
        case None => ok(result, None)
      }
    case Failure(exception) => throw exception
  }

  def ok[T](body: T, token: Option[String]): Route = {
    val response = OkResponse(body, rightNow(), token)
    complete(StatusCodes.OK, JsonUtils.format(response))
  }

  def error(exception: Throwable, statusCode: StatusCode): Route = {
    val response = ErrorResponse(exception.getMessage, rightNow())
    complete(statusCode, JsonUtils.format(response))
  }

  def extractToken: Directive1[Option[AuthToken]] = {
    optionalHeaderValueByName("Authorization").flatMap {
      case Some(authToken) => provide(SessionService.getToken(authToken))
      case None => provide(None)
    }
  }

  def authenticatedRequests: Directive1[AuthToken] = {
    extractToken flatMap {
      case Some(token) => provide(token)
      case _ => reject(AuthFailedRejection("No valid user found!"))
    }
  }

  def withRole(roles: Set[UserRole]): Directive1[AuthToken] = authenticatedRequests.flatMap { token =>
    if(roles.contains(token.role)) {
      provide(token)
    } else {
      reject(BadRoleRejection("bad role"))
    }
  }

}

object AppDirectives extends AppDirectives