import javax.inject.{Inject, Provider}

import play.api._
import play.api.http.DefaultHttpErrorHandler
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.mvc.Results._
import play.api.mvc._
import play.api.routing.Router
import play.core.SourceMapper
import play.api.libs.json.{JsPath, Json, Writes, JsValue}

import scala.concurrent._

class ErrorHandler(environment: Environment,
                   configuration: Configuration,
                   sourceMapper: Option[SourceMapper] = None,
                   optionRouter: => Option[Router] = None)
    extends DefaultHttpErrorHandler(environment,
                                    configuration,
                                    sourceMapper,
                                    optionRouter) {

  @Inject
  def this(environment: Environment,
           configuration: Configuration,
           sourceMapper: OptionalSourceMapper,
           router: Provider[Router]) = {
    this(environment,
         configuration,
         sourceMapper.sourceMapper,
         Some(router.get))
  }

  override def onClientError(request: RequestHeader,
                             statusCode: Int,
                             message: String): Future[Result] = {
    Future.successful {
      val result = statusCode match {
        case BAD_REQUEST =>
          Results.BadRequest(message)
        case FORBIDDEN =>
          Results.Forbidden(message)
        case NOT_FOUND =>
          Results.NotFound(message)
        case clientError if statusCode >= 400 && statusCode < 500 =>
          Results.Status(statusCode)
        case nonClientError =>
          val msg =
            s"onClientError invoked with non client error status code $statusCode: $message"
          throw new IllegalArgumentException(msg)
      }
      result
    }
  }

  override protected def onDevServerError(
      request: RequestHeader,
      exception: UsefulException): Future[Result] = {
    Future.successful({
      val json: JsValue = Json.obj(
        "code" -> "0",
        "message" -> exception.toString,
      )
      InternalServerError(json)
    })
  }

  override protected def onProdServerError(
      request: RequestHeader,
      exception: UsefulException): Future[Result] = {
      Future.successful(InternalServerError)
  }
}