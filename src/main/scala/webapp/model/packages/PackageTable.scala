package webapp.model.packages

import java.time.LocalDate

import webapp.model.TableWithId
import webapp.model.DB.session.profile.api._
import slick.lifted.Tag

/**
  * Created by Alfred on 07.02.2021.
  */
class PackageTable(tag: Tag) extends TableWithId[Package, Int](tag, "PACKAGES") {

  def id = column[Option[Int]]("ID", O.PrimaryKey, O.AutoInc)

  def userId = column[Long]("USER_ID")

  def senderName = column[String]("SENDER_NAME")

  def senderSurname = column[String]("SENDER_SURNAME")

  def senderAddress = column[String]("SENDER_ADDRESS")

  def senderPhoneNumber = column[String]("SENDER_PHONE_NUMBER")

  def receiverName = column[String]("RECEIVER_NAME")

  def receiverSurname = column[String]("RECEIVER_SURNAME")

  def receiverAddress = column[String]("RECEIVER_ADDRESS")

  def receiverPhoneNumber = column[String]("RECEIVER_PHONE_NUMBER")

  def image = column[String]("IMAGE")

  def attachmentPath = column[Option[String]]("ATTACHMENT_PATH")

  def creationDate = column[LocalDate]("CREATION_DATE")

  def * = (id, userId, senderName, senderSurname, senderAddress, senderPhoneNumber, receiverName, receiverSurname, receiverAddress, receiverPhoneNumber, image, attachmentPath, creationDate) <> (Package.tupled, Package.unapply)
}
