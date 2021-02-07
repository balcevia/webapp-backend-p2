package webapp.model.user

import java.time.LocalDate

import webapp.model.Identifiable

/**
  * Created by Alfred on 05.02.2021.
  */
case class UserDetails(
                        id: Option[Int],
                        userId: Long,
                        name: String,
                        surname: String,
                        dateOfBirth: LocalDate,
                        streetAddress: String,
                        buildingNumber: String,
                        postalCode: String,
                        country: String,
                        pesel: String
                      ) extends Identifiable[Int, UserDetails] {

  override def withId(id: Option[Int]) = copy(id = id)
  
}