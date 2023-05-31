val username = "kolotaev"
val repo = "ride"

val SCALA_2_12 = "2.12.15"
val SCALA_2_13 = "2.13.10"
val SCALA_3 = "3.2.2"

name := "ride"
organization := s"com.github.$username"
version := "1.2.0"

scalaVersion := SCALA_2_12
crossScalaVersions := Seq(SCALA_2_12, SCALA_2_13, SCALA_3)
javacOptions ++= Seq(
  "-source", "1.8", "-target", "1.8", "-Xlint"
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.16" % Test
)

licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT"))
homepage := Some(url(s"https://github.com/$username/$repo"))

publishMavenStyle := true
Test / publishArtifact := false

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

pomExtra :=
  <scm>
    <connection>
      scm:git:git://github.com/kolotaev/ride.git
    </connection>
    <url>
      https://github.com/kolotaev/ride
    </url>
  </scm>
  <developers>
    <developer>
      <id>kolotaev</id>
      <name>Egor Kolotaev</name>
      <email>ekolotaev@gmail.com</email>
    </developer>
  </developers>
