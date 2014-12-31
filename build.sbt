import AssemblyKeys._

name := "cchat"

version := "1.0"

scalaVersion := "2.11.4"

resolvers in ThisBuild ++= Seq(
  "sonatype-releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2/",
  "Sonatype-repository" at "https://oss.sonatype.org/content/groups/public",
  "typesafe" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.8",
  "com.typesafe.akka" %% "akka-slf4j" % "2.3.8",
  "jline" % "jline" % "2.12",
  "ch.qos.logback" % "logback-classic" % "1.1.2",
  "ch.qos.logback" % "logback-core" % "1.1.2"
)

assemblySettings

jarName in assembly := "cchat-standalone.jar"

mainClass in assembly := Some("cchat.Main")
