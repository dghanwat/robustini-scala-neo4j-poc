// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository
resolvers ++= Seq(
	"Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/",
    "Scala SBT Plugins" at "https://repo.scala-sbt.org/scalasbt/sbt-plugin-releases",
	"Sonatype" at "https://oss.sonatype.org/content/repositories/releases",
	"DL Binary" at "https://dl.bintray.com/sbt/sbt-plugin-releases/")

// SBT Scalastyle Plugin
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")

// SBT Assembly Plugin
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.5")

// SBT SCoverage Plugin
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.1")

// SBT Native Packager Plugin
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.2.2")

// SBT Lagom Plugin.
addSbtPlugin("com.lightbend.lagom" % "lagom-sbt-plugin" % "1.4.4")

// SBT Eclipse Plugin
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "5.2.4")

// SBT Plugin info
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.7.0")

