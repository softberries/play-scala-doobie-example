package infra

import cats.effect._
import doobie.{DataSourceTransactor, ExecutionContexts, Transactor}
import javax.inject.{Inject, Singleton}
import play.api.db.Database

import scala.concurrent.ExecutionContext

@Singleton
class DoobieTransactor @Inject()(db: Database)(implicit val ec: ExecutionContext) {


  def tx[F[_] : Async : ContextShift](): Resource[F, DataSourceTransactor[F]] =
    for {
      ce <- ExecutionContexts.fixedThreadPool[F](32) // our connect EC
      be <- Blocker[F] // our blocking EC
    } yield Transactor.fromDataSource[F](db.dataSource, ce, be)
}
