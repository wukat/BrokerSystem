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
  "com.typesafe.play" %% "play-mailer" % "2.4.1",
  "org.bitbucket.b_c" % "jose4j" % "0.4.2",
  "org.apache.commons" % "commons-io" % "1.3.2",
  "com.lowagie" % "itext" % "4.2.1",
  "org.xhtmlrenderer" % "flying-saucer-pdf" % "9.0.7"
)

ebeanEnabled := false

play.Project.playJavaSettings



