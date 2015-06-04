name := "Broker"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  //  javaEbean,
  cache,
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
  javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final",
  "org.hibernate.javax.persistence" % "hibernate-jpa-2.0-api" % "1.0.1.Final",
  "org.mindrot" % "jbcrypt" % "0.3m"
)

ebeanEnabled := false

play.Project.playJavaSettings



