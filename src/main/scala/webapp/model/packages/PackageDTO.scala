package webapp.model.packages

import java.time.LocalDate

/**
  * Created by Alfred on 07.02.2021.
  */
case class PackageDTO(id: Option[Int],
                      userId: Option[Long],
                      senderName: Option[String],
                      senderSurname: Option[String],
                      senderAddress: Option[String],
                      senderPhoneNumber: Option[String],
                      receiverName: Option[String],
                      receiverSurname: Option[String],
                      receiverAddress: Option[String],
                      receiverPhoneNumber: Option[String],
                      creationDate: Option[LocalDate],
                      image: Option[String],
                      status: Option[PackageStatus]
                     )