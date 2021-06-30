package services

import lib.model.{Todo}

abstract class ServiceError(val message: String, val args: Any*)

abstract sealed class TodoError(message: String, args: Any*) extends ServiceError(message, args: _*)
case class TodoNotFound(todoId: Todo.Id) extends TodoError("error.Todo.notFound",  todoId)
case object TodoPermissionDenied extends TodoError("error.Todo.permissionDenied")