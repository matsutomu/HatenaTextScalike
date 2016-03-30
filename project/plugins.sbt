

addSbtPlugin("org.flywaydb" % "flyway-sbt" % "3.2.1")

resolvers += "Flyway" at "http://flywaydb.org/repo"

libraryDependencies += "com.h2database" % "h2" % "1.4.190"

addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "2.3.4")