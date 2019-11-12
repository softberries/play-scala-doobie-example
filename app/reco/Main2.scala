package reco

import cats.effect.IO

/*
1. lazy evaluation
2. referential transparency
 */
object Main2 extends App {

  val ioa = IO { println("hey!") }

  val program: IO[Unit] =
    for {
      _ <- ioa
      _ <- ioa
    } yield ()

  program.unsafeRunSync()
  ()

}
