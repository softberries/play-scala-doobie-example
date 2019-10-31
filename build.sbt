name := """play-scala-doobie-example"""

version := "2.7.x"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.0"

libraryDependencies += guice
libraryDependencies += jdbc
libraryDependencies += evolutions
//libraryDependencies += "com.typesafe.play" %% "play-slick" % "4.0.2"
//libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "4.0.2"

libraryDependencies += "org.typelevel" %% "cats-core" % "2.0.0"

libraryDependencies += "org.tpolecat" %% "doobie-core"      % "0.8.4"

// And add any of these as needed
libraryDependencies += "org.tpolecat" %% "doobie-h2"        % "0.8.4"// H2 driver 1.4.199 + type mappings.
libraryDependencies += "org.tpolecat" %% "doobie-hikari"    % "0.8.4"          // HikariCP transactor.
//libraryDependencies += "org.tpolecat" %% "doobie-postgres"  % "0.8.4",          // Postgres driver 42.2.8 + type mappings.
//libraryDependencies += "org.tpolecat" %% "doobie-quill"     % "0.8.4",          // Support for Quill 3.4.9
//libraryDependencies += "org.tpolecat" %% "doobie-specs2"    % "0.8.4" % "test", // Specs2 support for typechecking statements.
//libraryDependencies += "org.tpolecat" %% "doobie-scalatest" % "0.8.4" % "test"

libraryDependencies += "com.h2database" % "h2" % "1.4.199"

libraryDependencies += specs2 % Test

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-Xfatal-warnings",
  "-language:higherKinds"
//  "-Ypartial-unification"
)
