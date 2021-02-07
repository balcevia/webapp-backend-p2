package webapp.service

import java.io.File

import webapp.model.packages.{Package, PackageDAO, PackageStatus}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Alfred on 07.02.2021.
  */
trait PackageService {

  protected val packageDAO: PackageDAO

  def addPackage(pckg: Package): Future[Int] = {
    packageDAO.insert(pckg)
  }

  def getPackages(userId: Long)(implicit ec: ExecutionContext): Future[Seq[Package]] = {
    packageDAO.getByUserId(userId)
  }

  def getAttachment(packageId: Int, userId: Long)(implicit ec: ExecutionContext): Future[File] = {
    packageDAO.getByIdAndUserIdOrCourierId(packageId, userId).flatMap {
      case Some(pckg) =>
        pckg.attachmentPath match {
          case Some(path) =>
            val file = new File(path)
            if (file.exists()) {
              Future.successful(file)
            } else {
              generatePDF(pckg)
            }
          case None =>
            generatePDF(pckg)
        }
      case None => Future.failed(new Exception("Unable to find attachment"))
    }
  }

  private def generatePDF(pckg: Package)(implicit ec: ExecutionContext) = {
    val filePath = s"/var/www/html/pdf/${System.currentTimeMillis()}.pdf"
    PDFGenerator.generate(pckg, filePath)
    packageDAO.insertOrUpdate(pckg.copy(attachmentPath = Some(filePath))).map(_ => new File(filePath))
  }

  def removePackage(userId: Long, packageId: Int)(implicit ec: ExecutionContext): Future[Unit] = {
    packageDAO.getByIdAndUserId(packageId, userId).flatMap {
      case Some(pckg) => pckg.status match {
        case PackageStatus.New =>
          pckg.attachmentPath.map(path => new File(path).delete())
          packageDAO.deleteById(packageId).map(_ => ())
      }

      case None => Future.failed(new Exception("Unable to find package"))
    }
  }

  def handPackageToCourier(packageId: Int, courierId: Long)(implicit ec: ExecutionContext): Future[Unit] = {
    packageDAO.getById(packageId).flatMap {
      case Some(pckg) =>
        if (pckg.status == PackageStatus.New) {
          packageDAO.insertOrUpdate(pckg.copy(courierId = Some(courierId), status = PackageStatus.HandedToTheCourier)).map(_ => ())
        } else Future.failed(new Exception("Invalid package id or package is already taken"))
      case _ => Future.failed(new Exception("Invalid package id or package is already taken"))
    }
  }

  def getCourierPackages(courierId: Long)(implicit ec: ExecutionContext): Future[Seq[Package]] = {
    packageDAO.getByCourierId(courierId)
  }
}

object PackageService extends PackageService {
  override protected val packageDAO: PackageDAO = PackageDAO
}