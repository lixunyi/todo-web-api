package services

import lib.model.{Todo,Category}

abstract class ServiceError(val message: String, val args: Any*)

abstract sealed class TodoError(message: String, args: Any*) extends ServiceError(message, args: _*)
case class TodoNotFound(todoId: Todo.Id) extends TodoError("error.Todo.notFound",  todoId)


abstract sealed class CategoryError(message: String, args: Any*) extends ServiceError(message, args: _*)
case class CategoryNotFound(categoryId: Category.Id) extends CategoryError("error.Category.notFound",  categoryId)


case class SystemError(override val message: String, override val args: Any*) extends ServiceError(message, args: _*)
