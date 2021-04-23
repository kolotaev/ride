val username = "kolotaev"
val repo = "ride"

val SCALA_2_12 = "2.12.2"
val SCALA_2_13 = "2.13.1"

name := "ride"
organization := s"com.github.$username"
version := "1.1.1"

scalaVersion := SCALA_2_12
crossScalaVersions := Seq(SCALA_2_12, SCALA_2_13)
javacOptions ++= Seq(
  "-source", "1.8", "-target", "1.8", "-Xlint"
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.8" % Test
)

licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT"))
homepage := Some(url(s"https://github.com/$username/$repo"))

publishMavenStyle := true
publishArtifact in Test := false

publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

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
