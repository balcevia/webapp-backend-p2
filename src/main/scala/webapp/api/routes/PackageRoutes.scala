package webapp.api.routes

import java.io.File

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import webapp.AppDirectives
import webapp.auth.AuthToken
import webapp.mappers.PackageMapper
import webapp.model.packages.{PackageDTO, PackageStatus}
import webapp.model.user.UserRole
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
          withRole(Set(UserRole.Sender)) { _ =>
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
          }, delete {
            withRole(Set(UserRole.Sender)) { _ =>
              path(IntNumber) { id =>
                completeFuture(Some(token)) {
                  removePackage(id, token)
                }
              }
            }
          },
          path("attachment" / IntNumber) { id =>
            get {
              onComplete(getAttachment(id, token)) {
                case Success(file) =>
                  complete(HttpEntity.fromFile(ContentTypes.`application/octet-stream`, file))
                case Failure(ex) => throw ex
              }
            }
          }, pathPrefix("courier") {
            concat(
              path(IntNumber) { id =>
                withRole(Set(UserRole.Courier)) { _ =>
                  get {
                    completeFuture(Some(token)) {
                      handPackageToCourier(id, token)
                    }
                  }
                }
              },
              pathEndOrSingleSlash {
                withRole(Set(UserRole.Courier)) { _ =>
                  get {
                    completeFuture(Some(token)) {
                      getCourierPackages(token)
                    }
                  }
                }
              }
            )
          }
        )
      }
    }
  }

  def addPackage(pckg: PackageDTO, token: AuthToken): Future[Unit] = {
    for {
      validPackage <- PackageValidator.validate(pckg)
      domain = PackageMapper.toDomain(validPackage, token.userId, PackageStatus.New)
      _ <- PackageService.addPackage(domain)
    } yield ()
  }

  def getPackages(token: AuthToken): Future[Seq[PackageDTO]] = {
    PackageService.getPackages(token.userId).map(_.map(PackageMapper.toDTO))
  }

  def getAttachment(id: Int, token: AuthToken): Future[File] = {
    PackageService.getAttachment(id, token.userId)
  }

  def removePackage(id: Int, token: AuthToken): Future[Unit] = {
    PackageService.removePackage(token.userId, id)
  }

  def handPackageToCourier(id: Int, token: AuthToken): Future[Unit] = {
    PackageService.handPackageToCourier(id, token.userId)
  }

  def getCourierPackages(token: AuthToken): Future[Seq[PackageDTO]] = {
    PackageService.getCourierPackages(token.userId).map(_.map(PackageMapper.toDTO))
  }
}
