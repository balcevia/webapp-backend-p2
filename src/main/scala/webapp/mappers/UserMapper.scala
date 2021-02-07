package webapp.mappers

import webapp.model.user.{UserDTO, UserDetails}

trait UserMapper {
  def toDTO(details: UserDetails): UserDTO = UserDTO (
    email = None,
    password = None,
    confirmPassword = None,
    name = Some(details.name),
    surname = Some(details.surname),
    dateOfBirth = Some(details.dateOfBirth),
    streetAddress = Some(details.streetAddress),
    buildingNumber = Some(details.buildingNumber),
    postalCode = Some(details.postalCode),
    country = Some(details.country),
    pesel = Some(details.pesel)
  )
}

object UserMapper extends UserMapper
