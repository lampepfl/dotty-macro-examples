package foo

import scala.quoted._

inline def fullClassName[T]: String = ${ fullClassNameImpl[T] }

def fullClassNameImpl[T](using qctx: QuoteContext,
  tpe: Type[T]): Expr[String] =
  import qctx.tasty._
  Expr(tpe.unseal.symbol.fullName)
