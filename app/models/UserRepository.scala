package models

import doobie._
import doobie.implicits._

trait UserRepository[F[_]] {
  def store(user: Person): F[Unit]
  def resolveByName(name: String): F[Option[Person]]
}

object UserRepository {
  def apply[F[_]](implicit F: UserRepository[F]): UserRepository[F] = F
}

object UserRepositoryOnJDBC {
  implicit def doobieUserRepository: UserRepository[ConnectionIO] = new UserRepository[ConnectionIO] {
    override def store(user: Person): ConnectionIO[Unit] = {
      sql"insert into people (name, age) values (${user.name}, ${user.age})".update.run.map(_=>())
    }

    override def resolveByName(name: String): ConnectionIO[Option[Person]] = {
      sql"select id, name, age from people where name = $name".query[Person].option
    }
  }
}