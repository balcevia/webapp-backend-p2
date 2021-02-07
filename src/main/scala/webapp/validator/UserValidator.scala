package webapp.validator

import java.time.LocalDate

import webapp.AppConfig
import webapp.model.user.UserDTO
import webapp.service.UserService

import scala.concurrent.{ExecutionContext, Future}

trait UserValidator extends ValidatorBase {

  protected val entropyThreshold: Int
  protected val userService: UserService

  def validatePostRequest(request: UserDTO)(implicit ec: ExecutionContext): Future[UserDTO] = {
    for {
      email <- validateRequired(request.email, "Username is required")
      password <- validateRequired(request.password, "Password is required")
      _ <- validatePassword(password)
      _ <- validateEmail(email)
      _ <- validateEmailUniqueness(email)
      name <- validateRequired(request.name, "Name is required")
      _ <- validateAlphabeticString(name)
      surname <- validateRequired(request.surname, "Surname is required")
      _ <- validateAlphabeticString(surname)
      dateOfBirth <- validateRequired(request.dateOfBirth, "Date Of Birth is required")
      _ <- validateRequired(request.streetAddress, "Street Address is required")
      _ <- validateRequired(request.buildingNumber, "Building Number is required")
      _ <- validateRequired(request.postalCode, "Postal Code is required")
      _ <- validateRequired(request.country, "Country is required")
      pesel <- validateRequired(request.pesel, "Pesel is required")
      _ <- validatePesel(pesel, dateOfBirth)
    } yield request
  }

  private def validateEmail(email: String): Future[String] = {
    val emailRegex = """^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$""".r
    if (emailRegex.findFirstMatchIn(email).isDefined) {
      Future.successful(email)
    } else {
      Future.failed(new Exception("Email address is not valid"))
    }
  }

  private def validateEmailUniqueness(email: String)(implicit ec: ExecutionContext): Future[Unit] = {
    userService.getByEmail(email).flatMap {
      case Some(_) => Future.failed(new Exception(s"User with email $email already exists"))
      case None => Future.successful(())
    }
  }

  private def validatePassword(password: Array[Byte]): Future[Array[Byte]] = {
    import webapp.utils.MathUtils._

    val charArray = password.map(_.toChar)
    val poolOfUniqueCharacters = UserValidator.passwordConditions.map(c => c(charArray)).sum

    val passwordEntropy = entropy(poolOfUniqueCharacters, password.length)

    if (passwordEntropy >= entropyThreshold) {
      Future.successful(password)
    } else {
      Future.failed(new Exception("Password is weak"))
    }
  }

  private def validatePesel(pesel: String, date: LocalDate): Future[String] = {
    if(pesel.length != 11) {
      Future.failed(new Exception("Invalid Pesel"))
    }
    val lastTwoNumbersOfYear = s"${date.getYear % 100}"
    val month = "%02d".format(date.getMonthValue)
    val day = "%02d".format(date.getDayOfMonth)

    val w = List(1,3,7,9,1,3,7,9,1,3)
    val subStr = pesel.substring(0, 10)
    val sum = subStr.map(_.toString.toInt).zip(w).map(v => v._1 * v._2).sum

    var controlNumber = sum
    controlNumber = controlNumber % 10
    controlNumber = 10 - controlNumber
    controlNumber = controlNumber % 10
    if(pesel.substring(0, 2) == lastTwoNumbersOfYear && pesel.substring(2, 4) == month && pesel.substring(4, 6) == day && pesel.substring(10, 11) == controlNumber.toString) {
      Future.successful(pesel)
    } else Future.failed(new Exception("Invalid Pesel"))
  }

}

object UserValidator extends UserValidator {
  override protected val entropyThreshold: Int = AppConfig.getInt("auth.jwt.entropyThreshold")
  override protected val userService: UserService = UserService

  private val lowerUpperCasePoolLength = 26
  private val numericPoolLength = 10
  private val specialCharPoolLength = 9

  private val passwordConditions = List(
    (chars: Array[Char]) => if (chars.exists(_.isLower)) lowerUpperCasePoolLength else 0,
    (chars: Array[Char]) => if (chars.exists(_.isUpper)) lowerUpperCasePoolLength else 0,
    (chars: Array[Char]) => if (chars.exists(_.isDigit)) numericPoolLength else 0,
    (chars: Array[Char]) => if (chars.exists(!_.isLetterOrDigit)) specialCharPoolLength else 0
  )
}