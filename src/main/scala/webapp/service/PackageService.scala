package webapp.service

import java.io.File

import webapp.model.packages.{Package, PackageDAO}

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
    packageDAO.getByIdAndUserId(packageId, userId).flatMap {
      case Some(pckg) =>
        pckg.attachmentPath match {
          case Some(path) =>
            val file = new File(path)
            if(file.exists()) {
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
}

object PackageService extends PackageService {
  override protected val packageDAO: PackageDAO = PackageDAO
}