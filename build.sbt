import org.specs2.analysis.Dependencies

name := "Broker"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
//  javaEbean,
  cache,
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc41"
)



//TUTAJ NIE ZNAJDUJE Dependencies
//JAK ZAKOMENTUJE PONIZSZE LINIJKI,
//TO WSZYSTKO ZWIĄZANE Z HIBERNATE'em W PLIKU persistence.xml
//ZACZYNA ŚWIECIC NA CZERWONO
libraryDependencies ++= Seq(
  javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final" // replace by your jpa implementation
)


play.Project.playJavaSettings

ebeanEnabled := false

