package webapp.model.user

import webapp.model.CustomEnum

/**
  * Created by Alfred on 07.02.2021.
  */
sealed trait UserRole extends CustomEnum[UserRole]

object UserRole {
  case object Courier extends UserRole {
    val value = "courier"
  }
  case object Sender extends UserRole {
    val value = "sender"
  }
}