package json

import java.time.LocalDateTime
import play.api.libs.json.{JsPath, Json, Writes, JsString}
import lib.model.{Todo,Category}
import play.api.libs.functional.syntax._

trait TodoWrites extends CategoryWrites{
  implicit val todoIdWrites = Writes[Todo.Id](id => JsString(id.toString))
  implicit def defaultTodoWrites: Writes[Todo] = publicTodoWrites

  val publicTodoWrites: Writes[Todo] = (
    (JsPath \ "id").write[Todo.Id] 
    and (JsPath \ "title").write[String] 
    and (JsPath \ "body").write[String] 
    and (JsPath \ "state").write[String] 
    and (JsPath \ "category").write[Category]
  )(todo => (todo.id.get, todo.title, todo.body, todo.state.name, todo.category))

}
