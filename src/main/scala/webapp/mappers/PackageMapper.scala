package webapp.mappers

import java.time.LocalDate

import webapp.model.packages.{Package, PackageDTO, PackageStatus}

/**
  * Created by Alfred on 07.02.2021.
  */
trait PackageMapper {

  def toDomain(pckg: PackageDTO, userId: Long, status: PackageStatus): Package = Package(
    id = pckg.id,
    userId = userId,
    senderName = pckg.senderName.get,
    senderSurname = pckg.senderSurname.get,
    senderAddress = pckg.senderAddress.get,
    senderPhoneNumber = pckg.senderPhoneNumber.get,
    receiverName = pckg.receiverName.get,
    receiverSurname = pckg.receiverSurname.get,
    receiverAddress = pckg.receiverAddress.get,
    receiverPhoneNumber = pckg.receiverPhoneNumber.get,
    image = pckg.image.get,
    creationDate = pckg.creationDate.getOrElse(LocalDate.now),
    attachmentPath = None,
    status = status,
    courierId = None
  )

  def toDTO(pckg: Package): PackageDTO = PackageDTO(
    id = pckg.id,
    userId = Some(pckg.userId),
    senderName = Some(pckg.senderName),
    senderSurname = Some(pckg.senderSurname),
    senderAddress = Some(pckg.senderAddress),
    senderPhoneNumber = Some(pckg.senderPhoneNumber),
    receiverName = Some(pckg.receiverName),
    receiverSurname = Some(pckg.receiverSurname),
    receiverAddress = Some(pckg.receiverAddress),
    receiverPhoneNumber = Some(pckg.receiverPhoneNumber),
    image = Some(pckg.image),
    creationDate = Some(pckg.creationDate),
    status = Some(pckg.status)
  )
}

object PackageMapper extends PackageMapper
