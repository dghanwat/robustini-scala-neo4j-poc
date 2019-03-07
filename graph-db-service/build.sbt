
// Enable Plugins - RPM, Java App Server and Systemd.
enablePlugins(RpmPlugin, JavaServerAppPackaging, SystemdPlugin, UniversalPlugin)

// Name of the project or module.
name := "graph-db-service"

// Name of the organization. ThisBuild means it will apply to all sub-modules.
organization in ThisBuild := "Worldline"

// Project or module version.
version := "0.9.0"

// The Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.1"

// Service locator port
lagomServiceLocatorPort in ThisBuild := 10000

// MAC Wire dependency for Lagom Application Loader.
val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"


// Dependencies for implementing in-memory caching.
val scalacache = "com.github.cb372" % "scalacache-core_2.12" % "0.9.4"
val scalaguava = "com.github.cb372" % "scalacache-guava_2.12" % "0.9.4"

// Scalatest Dependency for writing unit tests.
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test

// For creating mocks for tests.
val mockito = "org.mockito" % "mockito-core"% "2.7.11"% Test

// Log4j2 Scala API
val log4jScalaApi = "org.apache.logging.log4j" %% "log4j-api-scala" % "11.0"

val neotypesV = "0.4.0"

// Disable Cassandra Support.
lagomCassandraEnabled in ThisBuild := false

// Disable Kafka Support.
lagomKafkaEnabled in ThisBuild := false

// Disable parallel test execution.
parallelExecution in Test := false

lazy val `graph-db` = (project in file("."))
  .enablePlugins(LagomScala, LagomLog4j2)
  .disablePlugins(LagomLogback)
  .aggregate(`graph-db-api`, `graph-db-impl`)
  .dependsOn(`graph-db-impl`)
  
// Configuration for graph-db-api Module.
lazy val `graph-db-api` = (project in file("graph-db-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

// Configuration for graph-db-impl Module.
lazy val `graph-db-impl` = (project in file("graph-db-impl"))
  .enablePlugins(LagomScala, LagomLog4j2,BuildInfoPlugin)
  .disablePlugins(LagomLogback)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "com.worldline.robustini.graphdb.impl",
    libraryDependencies ++= Seq(
      log4jScalaApi,
      filters,
      lagomScaladslTestKit,
      lagomScaladslServer % Optional,
      macwire,
      scalacache,
      scalaguava,
      scalaTest,
      mockito,
      "com.github.pureconfig" %% "pureconfig" % "0.9.1",
      "com.dimafeng" %% "neotypes" % neotypesV
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`graph-db-api`)

// Scala Compiler Options
scalacOptions in ThisBuild ++= Seq(
  "-target:jvm-1.8", // JVM version to be used.
  "-encoding", "UTF-8", // Project encoding.
  "-deprecation", // Warning and location for usages of deprecated APIs.
  "-feature", // Warning and location for usages of features that should be imported explicitly.
  "-unchecked", // Additional warnings where generated code depends on assumptions.
  "-Xlint", // Recommended additional warnings.
  "-Xcheckinit", // Runtime error when a val is not initialized due to trait hierarchies.
  "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver.
  "-Ywarn-value-discard", // Warn when non-Unit expression results are unused.
  "-Ywarn-inaccessible",
  "-Ywarn-dead-code"
)

scriptClasspath := Seq("*")

mainClass in (Compile, run) := Some("play.core.server.ProdServerStart")

val exportFullResolvers = taskKey[Unit]("debug resolvers")

exportFullResolvers := {
  for {
    (resolver,idx) <- fullResolvers.value.zipWithIndex
  } println(s"${idx}.  ${resolver.name}")
}

// Logic to copy the log4j2.xml to the conf folder.
mappings in Universal += {
  (packageBin in Compile).value
  val base = baseDirectory.value
  val conf = base / "graph-db-impl" / "src" / "main" / "resources" / "log4j2.xml"
  conf -> "conf/log4j2.xml"
}
