package controllers

import javax.inject._
import scala.concurrent.{Future, ExecutionContext}
import play.api._
import play.api.mvc._
import play.api.libs.json._
import lib.model.{Category,Todo}
import services.{ServiceError,CategoryService, CategoryError, CategoryNotFound}

import json._
import lib.persistence.onMySQL._

@Singleton
class CategoryController @Inject()(categoryService: CategoryService,
                               cc: ControllerComponents)(implicit ec: ExecutionContext)
    extends AbstractController(cc)
    with CategoryWrites 
    with ServiceErrorWrites {

  def index() = Action.async {
    categoryService.getAll().map { categorys =>
      Ok(Json.toJson(categorys))
    }
  }

  def show(id: Long) = Action.async {
    categoryService.get(Category.Id(id)).map {
      _.fold(
        handleError,
        entity => Ok(Json.toJson(entity.v))
      )
    }
  }

  def create() = Action.async(parse.json) { implicit request =>
    validatedJson[CategoryJsonVO] { input =>
      categoryService.create(input.name, input.slug,input.color).map { category =>
        Created(Json.toJson(category))
      }
    }
  }

  def update(id: Long) = Action.async(parse.json) { implicit request =>
    
    validatedJson[CategoryJsonVO] { input =>
      categoryService
        .update(Category.Id(id), 
                input.name,
                input.slug, 
                Category.Color(input.color.toShort))
        .map {
          _.fold(
            handleError,
            entity => Ok(Json.toJson(entity.v))
          )
        }
    }
  }

  def delete(id: Long) = Action.async { request =>
    categoryService.delete(Category.Id(id)).map {
      _.fold(
        handleError,
        entity => Ok(Json.toJson(entity.v))
      )
    }
  }

  private def handleError(error: ServiceError) = {
    error match {
      case _: CategoryNotFound => NotFound(Json.toJson(error))
    }
  }
}