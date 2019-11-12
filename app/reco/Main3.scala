package reco

import cats.effect.IO

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Main3 extends App {

  //1. wrap pure values in IO to compose them with other IOs
  val res = IO.pure(25).flatMap(n => IO(println(s"Number is: $n")))
  //needs to be evaluated
  res.unsafeRunSync()

  //2. an alias for IO.pure is IO.unit
  val unit: IO[Unit] = IO.pure(())
  //can be used to signal the end of effectful routines

  //3. use IO.apply for synchronous effects
  def putStrlLn(value: String) = IO(println(value))
  val readLn = IO(scala.io.StdIn.readLine)

  val res2 = for {
    _ <- putStrlLn("What's your name?")
    n <- readLn
    _ <- putStrlLn(s"Hello, $n!")
  } yield ()

  res2.unsafeRunSync()

  //4. IO.async for asynchronous operations
  implicit val cs = IO.contextShift(global)
  //but again be carefull with futures
  val fut = Future { println("hey fut!") }
  val ioa = IO.fromFuture(IO {
    fut
  })
  val program =
    for {
      _ <- ioa
      _ <- ioa
    } yield ()
  program.unsafeRunSync()

  //one more try:
  val ioa2 = IO.fromFuture(IO {
    Future { println("hey fut 2!") }
  })
  val program2 =
    for {
      _ <- ioa2
      _ <- ioa2
    } yield ()
  program2.unsafeRunSync()
}
