package webapp.model

import java.time.{LocalDate, LocalDateTime}
import java.time.format.DateTimeFormatter

import slick.ast.BaseTypedType
import slick.jdbc.JdbcType
import slick.jdbc.MySQLProfile.api._
import webapp.utils.JsonUtils

object ColumnMappers {
  implicit def enumColumnMapper[T <: CustomEnum[T]](implicit m: Manifest[T]): JdbcType[T] with BaseTypedType[T] = CustomEnum.mappedEnumColumnType

  implicit def listMapper[T](implicit m: Manifest[T]): JdbcType[List[T]] with BaseTypedType[List[T]] = JsonUtils.mappedJsonColumnType[List[T]]

  val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

  implicit val dateTimeColumnType: BaseColumnType[LocalDateTime] = MappedColumnType.base[LocalDateTime, String] (
    l => l.format(dateTimeFormatter),
    d => LocalDateTime.parse(d, dateTimeFormatter)
  )

  implicit val dateColumnType: BaseColumnType[LocalDate] = MappedColumnType.base[LocalDate, String] (
    l => l.toString,
    d => LocalDate.parse(d)
  )
}
