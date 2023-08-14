lazy val root = (project in file("."))
	.settings(
    name := "mylab",
    version := "1.0",
    scalaVersion := "2.11.8"
  )
  libraryDependencies ++= Seq(
		"org.apache.spark" %% "spark-core" % "2.3.2" % "provided"
  )