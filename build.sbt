Global / idePackagePrefix := Some("dummy")

def DottyProject(name: String): Project =
  Project.apply(name, file(name)).settings(
    scalaVersion := "3.1.0",
    Compile / scalaSource := baseDirectory.value / "src",
    libraryDependencies += "org.scala-lang" %% "scala3-compiler" % scalaVersion.value,
  )

lazy val abstractTypeclassBody = DottyProject("abstractTypeclassBody")
lazy val accessMembersByName = DottyProject("accessMembersByName")
lazy val defaultParamsInference = DottyProject("defaultParamsInference")
lazy val fullClassName = DottyProject("fullClassName")
lazy val isMemberOfSealedTraitHierarchy = DottyProject("isMemberOfSealedTraitHierarchy")
lazy val macroTypeClassDerivation = DottyProject("macroTypeClassDerivation")
lazy val accessEnclosingParameters = DottyProject("accessEnclosingParameters")
lazy val outOfScopeMethodCall = DottyProject("outOfScopeMethodCall")
lazy val outOfScopeTypeParam = DottyProject("outOfScopeTypeParam")
lazy val outOfScopeClassConstructor = DottyProject("outOfScopeClassConstructor")
lazy val buildingCustomASTs = DottyProject("buildingCustomASTs")
lazy val contextParamResolution = DottyProject("contextParamResolution")
lazy val passVarargsIntoAST = DottyProject("passVarargsIntoAST")
lazy val primaryConstructor = DottyProject("primaryConstructor")
lazy val referenceVariableFromOtherExpr = DottyProject("referenceVariableFromOtherExpr")
lazy val reflectionLambda = DottyProject("reflectionLambda")

lazy val inlineDemo = DottyProject("inlineDemo")
lazy val logAST = DottyProject("logAST")
lazy val symbolTest = DottyProject("symbolTest").dependsOn(logAST)
lazy val sqlDemo = DottyProject("sqlDemo")

