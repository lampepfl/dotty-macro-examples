import mill._, scalalib._

trait DottyModule extends ScalaModule {
  // Since the latest official release "3.0.0-RC1" includes a bug that breaks
  // one of our example `macroTypeClassDerivation`, we have to use
  // 3.0.0-RC2 NIGHTLY whose version is not older than the version below.
  def scalaVersion = "3.0.0-RC2-bin-20210304-5fa55b1-NIGHTLY"
}

object abstractTypeclassBody extends DottyModule
object accessMembersByName extends DottyModule
object defaultParamsInference extends DottyModule
object fullClassName extends DottyModule
object isMemberOfSealedTraitHierarchy extends DottyModule
object macroTypeClassDerivation extends DottyModule

object test extends Module {
  def all = List(
    abstractTypeclassBody,
    accessMembersByName,
    defaultParamsInference,
    fullClassName,
    isMemberOfSealedTraitHierarchy,
    macroTypeClassDerivation,
  )

  def run = T {
    T.sequence(all.map(_.run()))
  }
}
