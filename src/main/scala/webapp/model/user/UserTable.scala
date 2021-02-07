package webapp.model.user
import webapp.model.DB.session.profile.api._
import webapp.model.TableWithId

class UserTable(tag: Tag) extends TableWithId[User, Long](tag, "USERS") {

  def id = column[Option[Long]]("ID", O.PrimaryKey, O.AutoInc)

  def email = column[String]("EMAIL")

  def password = column[Array[Byte]]("PASSWORD")

  def salt = column[Array[Byte]]("SALT")

  def loginCount = column[Int]("INVALID_LOGIN_COUNT")

  def * = (id, email, password, salt, loginCount) <> (User.tupled, User.unapply)
}