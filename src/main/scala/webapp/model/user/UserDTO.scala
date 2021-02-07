package webapp.model.user

import java.time.LocalDate

/**
  * Created by Alfred on 12.01.2021.
  */
case class UserDTO(
                            email: Option[String],
                            password: Option[Array[Byte]],
                            confirmPassword: Option[Array[Byte]],
                            name: Option[String],
                            surname: Option[String],
                            dateOfBirth: Option[LocalDate],
                            streetAddress: Option[String],
                            buildingNumber: Option[String],
                            postalCode: Option[String],
                            country: Option[String],
                            pesel: Option[String]
                          )