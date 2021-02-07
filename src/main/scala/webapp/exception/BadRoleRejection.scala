package webapp.exception

import akka.http.javadsl.server
import akka.http.scaladsl.server.Rejection

/**
  * Created by Alfred on 07.02.2021.
  */
case class BadRoleRejection(message: String) extends server.AuthorizationFailedRejection with Rejection