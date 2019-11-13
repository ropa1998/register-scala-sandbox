package com.example.register.server

import java.time._
import java.time.format.DateTimeFormatter

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.example.register.actor.RegisterActor.{GetStats, Measurement, SearchResponse}
import spray.json.{DefaultJsonProtocol, _}

import scala.util.Try

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val localDateFormat: LocalDateFormat.type = LocalDateFormat
  implicit val searchResponseFormat: RootJsonFormat[SearchResponse] = jsonFormat4(SearchResponse)
  implicit val getStatsFormat: RootJsonFormat[GetStats] = jsonFormat1(GetStats)
  implicit val measurementFormat: RootJsonFormat[Measurement] = jsonFormat2(Measurement)

}

object LocalDateFormat extends JsonFormat[LocalDate] {

  override def write(obj: LocalDate): JsValue = JsString(formatter.format(obj))

  override def read(json: JsValue): LocalDate = {
    json match {
      case JsString(lDString) =>
        Try(LocalDate.parse(lDString, formatter)).getOrElse(deserializationError(deserializationErrorMessage))
      case _ => deserializationError(deserializationErrorMessage)
    }
  }

  private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

  private val deserializationErrorMessage =
    s"Expected date time in ISO offset date time format ex. ${LocalDate.now().format(formatter)}"
}