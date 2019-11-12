package reco

import cats.effect.IO

import scala.concurrent.ExecutionContext.Implicits.global

object Main5 extends App {
  //1. Bracket
  import java.io._

  def javaReadFirstLine(file: File): String = {
    val in = new BufferedReader(new FileReader(file))
    try {
      in.readLine()
    } finally {
      in.close()
    }
  }

  //with bracket
  def readFirstLine(file: File): IO[String] =
    IO(new BufferedReader(new FileReader(file))).bracket { in =>
      // Usage (the try block)
      IO(in.readLine())
    } { in =>
      // Releasing the reader (the finally block)
      IO(in.close())
    }


  //2. run an arbitrary number of IOs in parallel
  import cats.implicits._

  implicit val cs = IO.contextShift(global)

  val ioA = IO(println("Running ioA"))
  val ioB = IO(println("Running ioB"))
  val ioC = IO(println("Running ioC"))

  // make sure that you have an implicit ContextShift[IO] in scope. We created one earlier in this document.
  val program = (ioA, ioB, ioC).parMapN { (_, _, _) => () }

  program.unsafeRunSync()
  //=> Running ioB
  //=> Running ioC
  //=> Running ioA
  ()

}
