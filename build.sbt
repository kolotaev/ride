name := "Ride"
 
version := "1.0.0"
 
scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.4" % "test"
)

licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT"))

homepage := Some(url("https://github.com/kolotaev/ride"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/kolotaev/ride"),
    "scm:git@github.com:kolotaev/ride.git"
  )
)

publishArtifact in Test := false

developers := List(
  Developer(
    id    = "kolotaev",
    name  = "Egor Kolotaev",
    email = "ekolotaev@gmail.com",
    url   = url("https://github.com/kolotaev")
  )
)
//
//publishTo := {
//  if (version.value.trim.endsWith("SNAPSHOT"))
//    Opts.resolver.sonatypeSnapshots
//  else
//    Opts.resolver.sonatypeStaging
//}
