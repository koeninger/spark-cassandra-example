import sbt._
import Keys._
import sbtassembly.Plugin._
import AssemblyKeys._

object CassandraExampleBuild extends Build {
  lazy val buildSettings = Defaults.defaultSettings ++ Seq(
    version := "0.1-SNAPSHOT",
    organization := "org.koeninger",
    scalaVersion := "2.10.4"
  )

  lazy val app = Project(
    "cassandra-example",
    file("."),
    settings = buildSettings ++ assemblySettings ++ Seq(
      parallelExecution in Test := false,
      libraryDependencies ++= Seq(
        "com.datastax.spark" %% "spark-cassandra-connector" % "1.1.0-alpha3",
        // spark will already be on classpath when using spark-submit.
        // marked as provided, so that it isn't included in assembly.
        "org.apache.spark" %% "spark-catalyst" % "1.1.0" % "provided",
        "org.scalatest" %% "scalatest" % "2.1.5" % "test"
      )
    )
  )
}
