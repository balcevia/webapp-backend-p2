package webapp.api.routes

import java.io.File

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import webapp.AppDirectives
import webapp.auth.AuthToken
import webapp.mappers.PackageMapper
import webapp.model.packages.PackageDTO
import webapp.service.PackageService
import webapp.validator.PackageValidator

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Created by Alfred on 07.02.2021.
  */
object PackageRoutes extends AppDirectives {

  def routes: Route = {
    pathPrefix("package") {
      authenticatedRequests { token =>
        concat(
          pathEndOrSingleSlash {
            concat(
              post {
                entity(as[PackageDTO]) { pckg =>
                  completeFuture(Some(token)) {
                    addPackage(pckg, token)
                  }
                }
              },
              get {
                completeFuture(Some(token)) {
                  getPackages(token)
                }
              }
            )
          }
          ,
          path("attachment" / IntNumber) { id =>
            get {
              onComplete(getAttachment(id, token)) {
                case Success(file) =>
                  complete(HttpEntity.fromFile(ContentTypes.`application/octet-stream`, file))
                case Failure(ex) => throw ex
              }
            }
          }
        )
      }
    }
  }

  def addPackage(pckg: PackageDTO, token: AuthToken): Future[Unit] = {
    for {
      validPackage <- PackageValidator.validate(pckg)
      domain = PackageMapper.toDomain(validPackage, token.userId)
      _ <- PackageService.addPackage(domain)
    } yield ()
  }

  def getPackages(token: AuthToken): Future[Seq[PackageDTO]] = {
    PackageService.getPackages(token.userId).map(_.map(PackageMapper.toDTO))
  }

  def getAttachment(id: Int, token: AuthToken): Future[File] = {
    PackageService.getAttachment(id, token.userId)
  }

}
