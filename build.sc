import mill._, scalalib._

trait DottyModule extends ScalaModule {
  def scalaVersion = "0.25.0-bin-SNAPSHOT"
}

object abstractTypeclassBody extends DottyModule
object accessMembersByName extends DottyModule
object defaultParamsInference extends DottyModule
object macroTypeclassDerivation extends DottyModule
