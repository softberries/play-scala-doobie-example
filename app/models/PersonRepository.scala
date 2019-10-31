package models

import doobie._
import doobie.implicits._
import cats.implicits._

trait PersonRepository[F[_]] {
  def store(user: Person): F[Person]
  def resolveByName(name: String): F[Option[Person]]
}

object PersonRepository {
  def apply[F[_]](implicit F: PersonRepository[F]): PersonRepository[F] = F
}

object PersonRepositoryOnJDBC {
  implicit def doobiePersonRepository: PersonRepository[ConnectionIO] = new PersonRepository[ConnectionIO] {
    override def store(user: Person): ConnectionIO[Person] = {
      sql"insert into people (name, age) values (${user.name}, ${user.age})".update
        .withUniqueGeneratedKeys[Long]("id").map(id => user.copy(id = id.some))
    }

    override def resolveByName(name: String): ConnectionIO[Option[Person]] = {
      sql"select id, name, age from people where name = $name".query[Person].option
    }
  }
}