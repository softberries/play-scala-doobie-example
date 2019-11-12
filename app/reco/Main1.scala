package reco

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object Main1 extends App {
  val fut = Future { println("hey!") }

  val program: Future[Unit] =
    for {
      _ <- fut
      _ <- fut
    } yield ()

  program.onComplete {
    case Success(value) => value
    case Failure(exception) => println(exception.getMessage)
  }

}
