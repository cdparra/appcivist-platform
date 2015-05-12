name := "pruebamultiproject"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.1"

lazy val deliberation = (project in file("modules/deliberation")).enablePlugins(PlayJava)

lazy val `pruebamultiproject` = (project in file(".")).enablePlugins(PlayJava).dependsOn(deliberation).aggregate(deliberation)

libraryDependencies ++= Seq( javaJdbc , javaEbean , cache , javaWs )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  