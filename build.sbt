name := "Broker"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  //  javaEbean,
  cache,
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
  javaJpa,
  javaJpa.exclude("org.hibernate.javax.persistence", "hibernate-jpa-2.0-api"),
  "org.hibernate" % "hibernate-entitymanager" % "4.3.10.Final",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "com.typesafe.play" %% "play-mailer" % "2.4.1"
)

ebeanEnabled := false

play.Project.playJavaSettings



