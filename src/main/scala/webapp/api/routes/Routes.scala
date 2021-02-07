package webapp.api.routes

/**
  * Created by Alfred on 12.01.2021.
  */
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import webapp.{AppDirectives, AppExceptionHandler, AppRejectionHandler}

object Routes extends CORSHandler with AppDirectives {

  val routes: Route = corsHandler {
    handleExceptions(AppExceptionHandler.handler) {
      handleRejections(AppRejectionHandler.handler) {
        concat(
          SessionRoutes.routes,
          UserRoutes.routes,
          PackageRoutes.routes
        )
      }
    }
  }
}