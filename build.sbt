name := "Ride"
 
version := "1.0.0-SNAPSHOT"
 
scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.4" % "test"
)
//
//publishTo := {
//  val nexus = "https://oss.sonatype.org/"
//  if (version.value.trim.endsWith("SNAPSHOT"))
//    Some("snapshots" at nexus + "content/repositories/snapshots")
//  else
//    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
//}
