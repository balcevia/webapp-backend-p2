package webapp.auth

import webapp.model.user.UserRole

/**
  * Created by Alfred on 12.01.2021.
  */
case class AuthToken(userId: Long, email: String, token: String, role: UserRole)