package webapp.utils

import java.time.LocalDate

import org.json4s.CustomSerializer
import org.json4s.JsonAST.JString

/**
  * Created by Alfred on 05.02.2021.
  */
class LocalDateSerializer extends CustomSerializer[LocalDate](_ => ( {
  case json: JString =>
    LocalDate.parse(json.values)
}, {
  case date: LocalDate =>
    JString(date.toString)
}))