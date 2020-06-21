import Dependencies._

ThisBuild / scalaVersion     := "2.13.2"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val macros = (project in file("macros"))
  .settings(
    scalacOptions += "-language:experimental.macros",
    libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value,

    libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value, // enable this for debugging
    autoScalaLibrary := true, // enable this for debugging

    scalacOptions += "-Ymacro-debug-lite",
    libraryDependencies += scalaTest % Test
  )

lazy val impl = (project in file("impl"))
  .settings(
    name := "impl",
    fork in run := true,
    javaOptions += "-Dscala.usejavacp=true"
  ).dependsOn(macros)

lazy val root = (project in file("."))
  .settings(
    name := "statement-interpolator"
  ).aggregate(macros, impl)
