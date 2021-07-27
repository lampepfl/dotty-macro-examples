package stuff

import scala.quoted.*

inline def mcr: Any = ${ mcrImpl }
def mcrImpl(using Quotes): Expr[Any] =
  import quotes.reflect.*
  val barClassSym = Symbol.requiredClass("stuff.Foo.Bar")
  val tree = Apply(Select.unique(New(TypeIdent(barClassSym)), "<init>"), Nil)
  tree.asExpr
