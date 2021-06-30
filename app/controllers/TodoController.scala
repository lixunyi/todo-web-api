package controllers

import javax.inject._
import scala.concurrent.{Future, ExecutionContext}
import play.api._
import play.api.mvc._
import play.api.libs.json._
import lib.model.{Todo}
import services._
import json._
import lib.persistence.onMySQL._

@Singleton
class TodoController @Inject()(taskService: TodoService,
                               cc: ControllerComponents)(implicit ec: ExecutionContext)
    extends AbstractController(cc)
    with TodoWrites {

  def index() = Action.async {
    
    for{
      todo <- taskService.getAll() 
    }yield{
       Ok(Json.obj(
        "code" -> "1",
        "data" -> Json.toJson(todo)
      ))

    }
  }
}