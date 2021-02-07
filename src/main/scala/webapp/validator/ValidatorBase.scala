package webapp.validator

/**
  * Created by Alfred on 12.01.2021.
  */
import scala.concurrent.Future
import scala.util.Try

trait ValidatorBase {
  protected def validateRequired[T](value: Option[T], message: => String): Future[T] = Future.fromTry(Try {
    value.getOrElse(throw new Exception(message))
  })

  protected def validateAlphabeticString(value: String): Future[String] = {
    value.find(c => !c.isLetter) match {
      case Some(_) => Future.failed(new Exception("Only letters are allowed"))
      case _ => Future.successful(value)
    }
  }

}