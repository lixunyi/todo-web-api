package json

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Reads._

case class TodoCreatingInput(title: String, body: String,category_id : String)

object TodoCreatingInput {
  implicit val reads = (
    (JsPath \ "title").read[String](titleValidation) and
    (JsPath \ "body").read[String](bodyValidation) and
    (JsPath \ "category_id").readWithDefault("0")
  )(apply _)
}
