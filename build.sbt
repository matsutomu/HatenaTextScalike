name := """HatenaTextScalike"""

version := "1.0"

scalaVersion := "2.11.8"

// Change this to another test framework if you prefer
libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc"       % "2.4.2",
  "org.scalikejdbc" %% "scalikejdbc-config" % "2.4.2",
  "org.scalikejdbc" %% "scalikejdbc-interpolation" % "2.4.2",
  "com.h2database"  %  "h2"                % "1.4.190",
  "ch.qos.logback"  %  "logback-classic"   % "1.1.3",
  "org.scalikejdbc"      %% "scalikejdbc-test"              % scalikejdbcVersion  % "test",
  "org.specs2" %% "specs2-core" % "3.7" % "test"
)
// Uncomment to use Akka
//libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.11"
seq(flywaySettings: _*)

flywayUrl := "jdbc:h2:file:./db/hatenatextscalikedb"

flywayUser := "SA"

scalikejdbcSettings

lazy val scalikejdbcVersion = scalikejdbc.ScalikejdbcBuildInfo.version

