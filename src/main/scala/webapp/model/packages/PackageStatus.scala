package webapp.model.packages
import webapp.model.CustomEnum
/**
  * Created by Alfred on 07.02.2021.
  */
sealed trait PackageStatus extends CustomEnum[PackageStatus]

object PackageStatus {
  case object New extends PackageStatus {
    val value = "new"
  }

  case object WaitingInTheLocker extends PackageStatus {
    val value = "waitingInTheLocker"
  }

  case object Received extends PackageStatus {
    val value = "received"
  }

  case object HandedToTheCourier extends PackageStatus {
    val value = "handedToTheCourier"
  }
}

