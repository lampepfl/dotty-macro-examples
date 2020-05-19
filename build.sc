import mill._, scalalib._

val crossVersions = List(
  "0.25.0-bin-SNAPSHOT",
  "0.25.0-bin-20200518-b5eded6-NIGHTLY",
  "0.24.0-RC1",
)

class DottyModule(val crossScalaVersion: String) extends CrossScalaModule

object abstractTypeclassBody extends Cross[DottyModule](crossVersions:_*)
object accessMembersByName extends Cross[DottyModule](crossVersions:_*)
object defaultParamsInference extends Cross[DottyModule](crossVersions:_*)
object macroTypeclassDerivation extends Cross[DottyModule](crossVersions:_*)
