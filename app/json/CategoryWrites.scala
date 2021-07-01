package json

import play.api.libs.json.{JsPath, Json, Writes, JsString}
import lib.model.{Category}
import play.api.libs.functional.syntax._

trait CategoryWrites {
  implicit val CategoryIdWrites = Writes[Category.Id](id => JsString(id.toString))
  implicit def defaultCategoryWrites: Writes[Category] = publicCategoryWrites

  val publicCategoryWrites: Writes[Category] = (
    (JsPath \ "id").write[Category.Id] and 
    (JsPath \ "name").write[String] and 
    (JsPath \ "slug").write[String] and 
    (JsPath \ "color").write[String]
  )(category => {
    category match{
      case null => (Category.Id(0), "未選択", "","")
      case _    => (category.id.get, category.name, category.slug, category.color.code.toString)
    }
  })
}