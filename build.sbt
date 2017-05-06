name := "github-pullreq"

version := "1.0"

scalaVersion := "2.12.2"


libraryDependencies ++= Seq(
  // http client
  "net.databinder.dispatch" %% "dispatch-core" % "0.12.0",

  // JSON parser
  "net.databinder.dispatch" %% "dispatch-json4s-native" % "0.12.0",


  // use config file
  "com.typesafe" % "config" % "1.3.1"
)