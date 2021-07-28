package stuff

import scala.quoted.*

inline def mcr(toCast: Any): Any = ${ mcrImpl('toCast) }
def mcrImpl(toCast: Expr[Any])(using Quotes): Expr[Any] =
  import quotes.reflect.*
  val nestedScopeSym = Symbol.requiredModule("stuff.Foo.nestedScope")
  val castSym = Symbol.requiredMethod("stuff.cast")
  val barClassSym = Symbol.requiredClass("stuff.Foo.nestedScope.Bar")

  val tree = Apply(
    TypeApply(Ref(castSym), List(TypeIdent(barClassSym))),
    List(toCast.asTerm))
  tree.asExpr
