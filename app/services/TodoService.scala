package services

import javax.inject.{Inject, Singleton}
import java.time.LocalDateTime
import scala.concurrent.{Future, ExecutionContext}
import lib.model.{Todo,Category}
import lib.persistence.onMySQL._
import lib.persistence.onMySQL.TodoRepository._

@Singleton()
class TodoService @Inject()()(implicit ec: ExecutionContext) {

  def getAll(): Future[Seq[Todo]] =  TodoRepository.getAll() map (n => 
    n map(_.v)
  )

  def get(id: Todo.Id): Future[Either[TodoError, EntityEmbeddedId]] = {
    TodoRepository.get(id).map(_.toRight(TodoNotFound(id)))
  }
  
  def create(category_id: Long, title: String, body: String): Future[Id] = {
    val todo = Todo.apply(Category.Id(category_id),title,body)
	  TodoRepository.add(todo)
  }

  def update(id: Todo.Id,
             category_id: Category.Id, 
             title: String,
             body: String,
             state: Todo.Status): Future[Either[TodoError, EntityEmbeddedId]] = {

    get(id).flatMap {
      _.fold(
        notFound => Future.successful(Left(notFound)),
        todo => {
          val newTodo   = todo.map(_.copy(
						title 		  = title,
						body        = body,
						category_id = category_id,
						state		    = state))

          TodoRepository.update(newTodo) map (v => {
            v match {
              case None         => Left(TodoNotFound(id))
              case Some(entity) => Right(entity)
				    }
          })
        }
      )
    }
  }

  def delete(id: Todo.Id): Future[Either[TodoError, EntityEmbeddedId]] = {
    TodoRepository.remove(id).map(_.toRight(TodoNotFound(id)))
  }
}