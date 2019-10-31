package services

import cats.effect._
import doobie.implicits._
import doobie.ConnectionIO
import infra.DoobieTransactor
import javax.inject.Inject
import models.{Person, PersonRepository, PersonRepositoryOnJDBC}

import scala.concurrent.ExecutionContext

class PersonService @Inject()(doobie: DoobieTransactor)(implicit val ec: ExecutionContext) {

  implicit val cs = IO.contextShift(ec)

  import PersonRepositoryOnJDBC._

  def addPerson(person: Person): IO[Person] = {
    doobie.tx[IO]().use { xa =>
      PersonRepository[ConnectionIO].store(Person(None, person.name, person.age)).transact(xa)
    }
  }

  def findPersonByName(name: String): IO[Option[Person]] = {
    doobie.tx[IO]().use { xa =>
      PersonRepository[ConnectionIO].resolveByName(name).transact(xa)
    }
  }

  def updatePerson(person: Person): IO[Person] = ???

  def listPeople(): IO[List[Person]] = ???

  def deletePerson(id: Long): IO[Unit] = ???

}
