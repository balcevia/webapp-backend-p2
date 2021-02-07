package webapp.validator

import webapp.model.packages.PackageDTO

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Alfred on 07.02.2021.
  */
trait PackageValidator extends ValidatorBase {

  def validate(request: PackageDTO)(implicit ec: ExecutionContext): Future[PackageDTO] = {
    for {
      _ <- validateRequired(request.senderName, "Sender Name is required")
      _ <- validateRequired(request.senderSurname, "Sender Surname is required")
      _ <- validateRequired(request.senderAddress, "Sender Address is required")
      _ <- validateRequired(request.receiverName, "Receiver Name is required")
      _ <- validateRequired(request.receiverSurname, "Receiver Surname is required")
      _ <- validateRequired(request.receiverAddress, "Receiver Address is required")
      _ <- validateRequired(request.image, "Image is required")
      _ <- validateRequired(request.senderPhoneNumber, "Sender Phone Number is required")
      _ <- validateRequired(request.receiverPhoneNumber, "Receiver Phone Number is required")
    } yield request
  }
}

object PackageValidator extends PackageValidator
