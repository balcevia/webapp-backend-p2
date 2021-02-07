package webapp.model

/**
  * Created by Alfred on 12.01.2021.
  */
import slick.jdbc.MySQLProfile.api._
import slick.lifted.ProvenShape

abstract class TableWithId[T, ID](tag: Tag, tableName: String) extends Table[T](tag, tableName) {
  def id: Rep[Option[ID]]
  def * : ProvenShape[T]
}