package json

import java.time.LocalDateTime
import play.api.libs.json.{JsPath, Json, Writes, JsString}
import lib.model.{Todo}
import play.api.libs.functional.syntax._

trait TodoWrites {
  implicit val todoIdWrites = Writes[Todo.Id](id => JsString(id.toString))
  implicit def defaultTodoWrites: Writes[Todo] = publicTodoWrites

  val publicTodoWrites: Writes[Todo] = (
    (JsPath \ "id").write[Todo.Id] and 
    (JsPath \ "title").write[String] and 
    (JsPath \ "createdAt").write[LocalDateTime] and 
    (JsPath \ "updatedAt").write[LocalDateTime]
  )(todo => (todo.id.get, todo.title, todo.createdAt, todo.updatedAt))

}
