package reco

import cats.effect.IO

object Main4 extends App {

  //1. the current thread will block awaiting the results of the async computation
  IO(println("Sync!")).unsafeRunSync()

  //2.Passes the result of the encapsulated effects to the given callback by running them as impure side effects.
  //Any exceptions raised within the effect will be passed to the callback in the Either.
  IO(println("Async!")).unsafeRunAsync(_ => ())

  //3. possibly cancellable task
  IO(println("Potentially cancelable!")).unsafeRunCancelable(_ => ())

  //4. Set the explicit timeout for async operation
  import scala.concurrent.duration._
  IO(println("Timed!")).unsafeRunTimed(5.seconds)

}
