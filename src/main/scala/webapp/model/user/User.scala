package webapp.model.user

import webapp.model.Identifiable

/**
  * Created by Alfred on 12.01.2021.
  * */

case class User(id: Option[Long],
                email: String,
                password: Array[Byte],
                salt: Array[Byte],
                invalidLoginCount: Int,
                role: UserRole
               ) extends Identifiable[Long, User] {
  override def withId(id: Option[Long]): User = copy(id = id)
}