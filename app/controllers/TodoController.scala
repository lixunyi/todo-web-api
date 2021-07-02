package controllers

import java.time.LocalDateTime
import javax.inject._
import scala.concurrent.{Future, ExecutionContext}
import play.api._
import play.api.mvc._
import play.api.libs.json._
import lib.model.{Todo,Category}
import services.{ServiceError,TodoService, CategoryService,TodoError, TodoNotFound}

import json._
import lib.persistence.onMySQL._

@Singleton
class TodoController @Inject()(todoService: TodoService,
                               categoryService: CategoryService, 
                               cc: ControllerComponents)(implicit ec: ExecutionContext)
    extends AbstractController(cc)
    with TodoWrites 
    with ServiceErrorWrites {

  def index() = Action.async {
    
    for{
			todos <- todoService.getAll()
			cates <- categoryService.getAll()
		}yield {
      val todoList = todos.map(todo  => {
        todo.category = cates.find(c => c.id.get == todo.category_id).getOrElse(null)
				todo
		  })
      Ok(Json.toJson(todos))
    }
    
  }  

  def show(id: Long) = Action.async {

    for{
			cates <- categoryService.getAll()
      todoEh  <- todoService.get(Todo.Id(id))
		}yield {
      todoEh.fold(
        handleError,
        entity => {
          val todo = entity.v
          todo.category = cates.find(c => c.id.get == todo.category_id).getOrElse(null)
          Ok(Json.toJson(todo))
        }
          
      ) 
    }
  }

  def create() = Action.async(parse.json) { implicit request =>
    validatedJson[TodoCreatingInput] { input =>
      todoService.create(input.category_id.toLong, input.title,input.body).map { todo =>
        Created(Json.toJson(todo))
      }
    }
  }

  def update(id: Long) = Action.async(parse.json) { implicit request =>
    
    validatedJson[TodoUpdatingInput] { input =>
      todoService
        .update(Todo.Id(id), 
                Category.Id(input.category_id.toLong),
                input.title, 
                input.body,
                Todo.Status(input.state.toShort))
        .map {
          _.fold(
            handleError,
            entity => Ok(Json.toJson(entity.v))
          )
        }
    }
  }

  def delete(id: Long) = Action.async { request =>
    todoService.delete(Todo.Id(id)).map {
      _.fold(
        handleError,
        entity => Ok(Json.toJson(entity.v))
      )
    }
  }

  private def handleError(error: ServiceError) = {
    error match {
      case _: TodoNotFound => NotFound(Json.toJson(error))
    }
  }
}