package foo

import scala.quoted._

inline def fullClassName[T]: String =
  ${ fullClassNameImpl[T] }

def fullClassNameImpl[T](using quotes: Quotes, tpe: Type[T]): Expr[String] =
  import quotes.reflect._
  Expr(TypeTree.of[T].symbol.fullName)
