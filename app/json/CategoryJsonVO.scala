package json

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Reads._

case class CategoryJsonVO(name: String, slug: String,color : String)

object CategoryJsonVO {
  implicit val reads = (
    (JsPath \ "name").read[String](nameValidate) and
    (JsPath \ "slug").read[String](slugValidate) and
    (JsPath \ "color").read[String](colorValidate)
  )(apply _)
}