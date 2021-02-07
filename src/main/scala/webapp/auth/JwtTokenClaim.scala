package webapp.auth

/**
  * Created by Alfred on 12.01.2021.
  */
import java.time.LocalDateTime

import webapp.model.user.UserRole

case class JwtTokenClaim(userId: Long, email: String, expiration: LocalDateTime, role: UserRole)