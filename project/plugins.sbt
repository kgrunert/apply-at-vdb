addSbtPlugin(
  "org.scalameta" % "sbt-scalafmt" % "2.4.0"
) // "2.4.0" is just sbt plugin version
addSbtPlugin("ch.epfl.scala" % "sbt-bloop" % "1.4.2")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.6")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.7.3")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.10.0-RC1")
