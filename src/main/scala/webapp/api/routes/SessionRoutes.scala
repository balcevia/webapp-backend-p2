package webapp.api.routes

/**
  * Created by Alfred on 12.01.2021.
  */
import akka.http.scaladsl.model.RemoteAddress
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import webapp.AppDirectives
import webapp.auth.AuthToken
import webapp.model.auth.AuthRequest
import webapp.service.SessionService
import webapp.validator.SessionValidator

import scala.concurrent.Future

object SessionRoutes extends AppDirectives {

  def routes: Route = {
    path("auth") {
      extractClientIP { ip =>
        post {
          entity(as[AuthRequest]) { request =>
            completeFuture(None) {
              authenticateUser(request, ip)
            }
          }
        }
      }
    }
  }

  def authenticateUser(request: AuthRequest, ip: RemoteAddress): Future[AuthToken] = {
    for {
      (username, password) <- SessionValidator.validateAuthRequest(request)
      ipAddress = ip.toOption.map(_.getHostAddress).getOrElse("Unknown")
      token <- SessionService.authenticate(username, password, ipAddress)
    } yield token
  }

}
