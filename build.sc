import mill._, scalalib._

trait DottyModule extends ScalaModule {
  def scalaVersion = "3.1.0"
  def scalacOptions = Seq("-Xcheck-macros")
}

object abstractTypeclassBody          extends DottyModule
object accessMembersByName            extends DottyModule
object defaultParamsInference         extends DottyModule
object fullClassName                  extends DottyModule
object isMemberOfSealedTraitHierarchy extends DottyModule
object macroTypeClassDerivation       extends DottyModule
object accessEnclosingParameters      extends DottyModule
object outOfScopeMethodCall           extends DottyModule
object outOfScopeTypeParam            extends DottyModule
object outOfScopeClassConstructor     extends DottyModule
object buildingCustomASTs             extends DottyModule
object contextParamResolution         extends DottyModule
object passVarargsIntoAST             extends DottyModule
object primaryConstructor             extends DottyModule
object referenceVariableFromOtherExpr extends DottyModule

object test extends Module {
  def all = List(
    abstractTypeclassBody,
    accessMembersByName,
    defaultParamsInference,
    fullClassName,
    isMemberOfSealedTraitHierarchy,
    macroTypeClassDerivation,
    accessEnclosingParameters,
    outOfScopeMethodCall,
    outOfScopeTypeParam,
    outOfScopeClassConstructor,
    buildingCustomASTs,
    contextParamResolution,
    passVarargsIntoAST,
    primaryConstructor,
    referenceVariableFromOtherExpr,
  )

  def run = T {
    T.sequence(all.map(_.run()))
  }
}
