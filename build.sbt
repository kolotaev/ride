name := "Ride"
 
version := "1.0.0-SNAPSHOT"
 
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
//  val nexus = "https://oss.sonatype.org/"
//  if (version.value.trim.endsWith("SNAPSHOT"))
//    Some("snapshots" at nexus + "content/repositories/snapshots")
//  else
//    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
//}

publishArtifact in Test := false
