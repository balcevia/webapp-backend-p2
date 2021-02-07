package webapp.model.packages

import webapp.model.DAO
import slick.lifted.TableQuery
import webapp.model.DB.session.profile.api._

import scala.concurrent.{ExecutionContext, Future}


/**
  * Created by Alfred on 07.02.2021.
  */
trait PackageDAO extends DAO[Package, Int, PackageTable] {

  def getByUserId(userId: Long): Future[Seq[Package]] = {
    getByQuery(_.userId === userId)
  }

  def getByIdAndUserId(id: Int, userId: Long)(implicit ec: ExecutionContext): Future[Option[Package]] = {
    getByQuery(p => p.id === id && p.userId === userId).map(_.headOption)
  }

  def getByIdAndUserIdOrCourierId(id: Int, userId: Long)(implicit ec: ExecutionContext): Future[Option[Package]] = {
    getByQuery(p => p.id === id && (p.userId === userId || p.courierId === userId)).map(_.headOption)
  }

  def getByCourierId(id: Long): Future[Seq[Package]] = {
    getByQuery(_.courierId === id)
  }
}


object PackageDAO extends PackageDAO {
  override protected val tableQuery: TableQuery[PackageTable] = TableQuery[PackageTable]
}