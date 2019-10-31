package models

import javax.inject.{ Inject, Singleton }

import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class PersonRepository @Inject() ()(implicit ec: ExecutionContext) {


  def create(name: String, age: Int): Future[Person] = ???

  /**
   * List all the people in the database.
   */
  def list(): Future[Seq[Person]] = ???
}
