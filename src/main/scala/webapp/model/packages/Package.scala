package webapp.model.packages

import java.time.LocalDate

import webapp.model.Identifiable

/**
  * Created by Alfred on 07.02.2021.
  */
case class Package(
                    id: Option[Int],
                    userId: Long,
                    courierId: Option[Long],
                    senderName: String,
                    senderSurname: String,
                    senderAddress: String,
                    senderPhoneNumber: String,
                    receiverName: String,
                    receiverSurname: String,
                    receiverAddress: String,
                    receiverPhoneNumber: String,
                    image: String,
                    attachmentPath: Option[String],
                    creationDate: LocalDate,
                    status: PackageStatus
                  ) extends Identifiable[Int, Package] {
  override def withId(id: Option[Int]) = copy(id = id)
}