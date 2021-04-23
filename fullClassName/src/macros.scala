package foo

import scala.quoted.*

inline def fullClassName[T]: String =
  ${ fullClassNameImpl[T] }

def fullClassNameImpl[T](using quotes: Quotes, tpe: Type[T]): Expr[String] =
  import quotes.reflect.*
  Expr(TypeTree.of[T].symbol.fullName)
