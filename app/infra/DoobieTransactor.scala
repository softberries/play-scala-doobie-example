package infra

import cats.effect.{Blocker, IO, Resource}
import doobie.{DataSourceTransactor, ExecutionContexts, Transactor}
import javax.inject.{Inject, Singleton}
import play.api.db.Database

import scala.concurrent.ExecutionContext

@Singleton
class DoobieTransactor @Inject() (db: Database)(implicit val ec: ExecutionContext){

  implicit val cs = IO.contextShift(ec)

  def tx(): Resource[IO, DataSourceTransactor[IO]] =
    for {
      ce <- ExecutionContexts.fixedThreadPool[IO](32) // our connect EC
      be <- Blocker[IO] // our blocking EC
    } yield Transactor.fromDataSource[IO](db.dataSource, ce, be)
}
