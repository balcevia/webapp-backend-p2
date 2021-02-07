package webapp.model.user

import java.time.LocalDate

import webapp.model.TableWithId
import webapp.model.DB.session.profile.api._
import slick.lifted.Tag

/**
  * Created by Alfred on 05.02.2021.
  */
class UserDetailsTable(tag: Tag) extends TableWithId[UserDetails, Int](tag, "USER_DETAILS") {

  import webapp.model.ColumnMappers._

  def id = column[Option[Int]]("ID", O.PrimaryKey, O.AutoInc)

  def userId = column[Long]("USER_ID")

  def name = column[String]("NAME")

  def surname = column[String]("SURNAME")

  def dateOfBirth = column[LocalDate]("DATE_OF_BIRTH")

  def streetAddress = column[String]("STREET_ADDRESS")

  def buildingNumber = column[String]("BUILDING_NUMBER")

  def postalCode = column[String]("POSTAL_CODE")

  def country = column[String]("COUNTRY")

  def pesel = column[String]("PESEL")

  def * = (id, userId, name, surname, dateOfBirth, streetAddress, buildingNumber, postalCode, country, pesel) <> (UserDetails.tupled, UserDetails.unapply)
}
