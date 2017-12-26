name := "Ride"
 
version := "1.0.0"
 
scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.4" % "test"
)

licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT"))

homepage := Some(url("https://github.com/kolotaev/ride"))

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


publishTo := Some(
  "bintray" at
    "https://api.bintray.com/maven/kolotaev/" +
      "org.kolotaev/ride/;publish=1")
credentials += Credentials(Path.userHome / ".sbt" / ".credentials")
publishMavenStyle := true

publishArtifact in Test := false
