package services

import javax.inject.{Inject, Singleton}
import java.time.LocalDateTime
import scala.concurrent.{Future, ExecutionContext}
import lib.model.{Todo}
import lib.persistence.onMySQL._

@Singleton()
class TodoService @Inject()()(implicit ec: ExecutionContext) {
  def getAll(): Future[Seq[Todo]] =  TodoRepository.getAll() map (n => {
      n map(_.v)
    })
}
