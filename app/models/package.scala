package object models {
    implicit val x = doobie.postgres.implicits.optionBigDecimalMeta
}
