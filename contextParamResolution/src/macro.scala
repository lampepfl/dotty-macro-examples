package stuff

import scala.quoted.*

inline def mcr: Any = ${ mcrImpl }
def mcrImpl(using Quotes): Expr[Any] =
  import quotes.reflect.*
  val fSym = Symbol.requiredMethod("stuff.f")
  val barTpeRepr = TypeIdent(Symbol.requiredClass("stuff.Bar")).tpe

  val tree = Implicits.search(barTpeRepr) match
    case success: ImplicitSearchSuccess =>
      Apply(Ref(fSym), List(success.tree))

  tree.asExpr
