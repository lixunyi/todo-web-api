package json

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Reads._

case class TodoUpdatingInput(title: String, 
                             body: String,
                             category_id : String,
                             state: String)

object TodoUpdatingInput {
  implicit val reads = (
    (JsPath \ "title").read[String](minLength[String](1) keepAnd maxLength[String](255)) and
    (JsPath \ "body").read[String](minLength[String](1) keepAnd maxLength[String](255)) and
    (JsPath \ "category_id").read[String](minLength[String](1) keepAnd maxLength[String](255)) and
    (JsPath \ "state").read[String](minLength[String](1) keepAnd maxLength[String](255)) 
  )(apply _)
}