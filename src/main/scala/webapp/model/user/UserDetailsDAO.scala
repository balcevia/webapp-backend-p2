package webapp.model.user

import webapp.model.DAO
import webapp.model.DB.session.profile.api._

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Alfred on 05.02.2021.
  */
trait UserDetailsDAO extends DAO[UserDetails, Int, UserDetailsTable] {

  def getByUserId(id: Long)(implicit ec: ExecutionContext): Future[UserDetails] = getByQuery(_.userId === id).map(_.head)
}

object UserDetailsDAO extends UserDetailsDAO {
  override protected val tableQuery: TableQuery[UserDetailsTable] = TableQuery[UserDetailsTable]
}
