package json

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Reads._

case class CategoryJsonVO(name: String, slug: String,color : String)

object CategoryJsonVO {
  implicit val reads = (
    (JsPath \ "name").read[String](minLength[String](1) keepAnd maxLength[String](255)) and
    (JsPath \ "slug").read[String](minLength[String](1) keepAnd maxLength[String](255)) and
    (JsPath \ "color").read[String](minLength[String](1) keepAnd maxLength[String](255))
  )(apply _)
}