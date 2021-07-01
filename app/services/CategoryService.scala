package services
import java.time.LocalDateTime
import javax.inject.{Inject, Singleton}
import java.time.LocalDateTime
import scala.concurrent.{Future, ExecutionContext}
import lib.model.{Category}
import lib.persistence.onMySQL._
import lib.persistence.onMySQL.CategoryRepository._

@Singleton()
class CategoryService @Inject()()(implicit ec: ExecutionContext) {

  def getAll(): Future[Seq[Category]] =  CategoryRepository.getAll() map (n => 
    n map(_.v)
  )

  def get(id: Category.Id): Future[Either[CategoryError, EntityEmbeddedId]] = {
    CategoryRepository.get(id).map(_.toRight(CategoryNotFound(id)))
  }
  
  def create(name: String, slug: String, color: String): Future[Id] = {
    val category = Category.apply(name,slug,Category.Color(color.toShort))
	  CategoryRepository.add(category)
  }

  def update(id: Category.Id, 
             name: String,
             slug: String,
             color: Category.Color): Future[Either[CategoryError, EntityEmbeddedId]] = {

    get(id).flatMap {
      _.fold(
        notFound => Future.successful(Left(notFound)),
        category => {
          val newCategory = category.map(_.copy(
						name 		      = name,
						slug          = slug,
						color 		    = color,
            updatedAt     = LocalDateTime.now()))

          CategoryRepository.update(newCategory) map (v => {
            v match {
              case None         => Left(CategoryNotFound(id))
              case Some(entity) => Right(entity)
				    }
          })
        }
      )
    }
  }

  def delete(id: Category.Id): Future[Either[CategoryError, EntityEmbeddedId]] = {
    CategoryRepository.remove(id).map(_.toRight(CategoryNotFound(id)))
  }
}