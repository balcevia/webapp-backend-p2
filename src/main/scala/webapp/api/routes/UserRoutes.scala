package webapp.api.routes

import webapp.AppDirectives
import webapp.auth.AuthToken
import webapp.mappers.UserMapper
import webapp.model.user.UserDTO
import webapp.service.UserService
import webapp.validator.UserValidator

import scala.concurrent.Future

/**
  * Created by Alfred on 12.01.2021.
  */

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

object UserRoutes extends AppDirectives {

  def routes: Route = concat(
    path("user") {
      concat(
        post {
          entity(as[UserDTO]) { request =>
            completeFuture(None) {
              createUser(request)
            }
          }
        },
        authenticatedRequests { token =>
          get {
            completeFuture(Some(token)) {
              getUserData(token)
            }
          }
        }
      )
    }
  )

  def createUser(request: UserDTO): Future[Unit] = {
    for {
      validRequest <- UserValidator.validatePostRequest(request)
      _ <- UserService.createNewUser(validRequest)
    } yield ()
  }

  def getUserData(token: AuthToken): Future[UserDTO] = {
    UserService.getUserDetailsByUserId(token.userId).map(UserMapper.toDTO)
  }
}
