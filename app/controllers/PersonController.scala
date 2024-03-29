package controllers

import cats.effect.{Blocker, IO, Resource}
import doobie._
import doobie.implicits._
import javax.inject._
import javax.sql.DataSource
import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.db.Database
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class PersonController @Inject()(cc: MessagesControllerComponents, db: Database)(
  implicit ec: ExecutionContext
) extends MessagesAbstractController(cc) {

  implicit val cs = IO.contextShift(ec)

  import UserRepositoryOnJDBC._

  def transactor(ds: DataSource): Resource[IO, DataSourceTransactor[IO]] =
    for {
      ce <- ExecutionContexts.fixedThreadPool[IO](32) // our connect EC
      be <- Blocker[IO] // our blocking EC
    } yield Transactor.fromDataSource[IO](ds, ce, be)

  /**
    * The mapping for the person form.
    */
  val personForm: Form[CreatePersonForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "age" -> number.verifying(min(0), max(140))
    )(CreatePersonForm.apply)(CreatePersonForm.unapply)
  }

  /**
    * The index action.
    */
  def index = Action { implicit request =>
    Ok(views.html.index(personForm))
  }

  def test = Action { implicit request =>
    val res = transactor(db.dataSource)
      .use { xa =>
        UserRepository[ConnectionIO].resolveByName("Kris").transact(xa)
      }
      .unsafeRunSync()
    Ok("" + res)
  }

  /**
    * The add person action.
    *
    * This is asynchronous, since we're invoking the asynchronous methods on PersonRepository.
    */
  def addPerson = Action.async { implicit request =>
    // Bind the form first, then fold the result, passing a function to handle errors, and a function to handle succes.
    personForm.bindFromRequest.fold(
      // The error function. We return the index page with the error form, which will render the errors.
      // We also wrap the result in a successful future, since this action is synchronous, but we're required to return
      // a future because the person creation function returns a future.
      errorForm => {
        Future.successful(Ok(views.html.index(errorForm)))
      },
      // There were no errors in the from, so create the person.
      person => {
        val res = transactor(db.dataSource)
          .use { xa =>
            UserRepository[ConnectionIO].store(Person(1L, person.name, person.age)).transact(xa)
          }
          .unsafeToFuture()
          .map { _ =>
            // If successful, we simply redirect to the index page.
            Redirect(routes.PersonController.index)
              .flashing("success" -> "user.created")
          }
        res
      }
    )
  }

  /**
    * A REST endpoint that gets all the people as JSON.
    */
  def getPersons = Action.async { implicit request =>
    Future.successful(Ok(""))
  }
}

/**
  * The create person form.
  *
  * Generally for forms, you should define separate objects to your models, since forms very often need to present data
  * in a different way to your models.  In this case, it doesn't make sense to have an id parameter in the form, since
  * that is generated once it's created.
  */
case class CreatePersonForm(name: String, age: Int)
