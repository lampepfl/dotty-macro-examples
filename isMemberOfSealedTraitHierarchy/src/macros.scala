package dummy

import scala.quoted._

inline def isMemberOfSealedHierarchy[T]: Boolean =
  ${ isMemberOfSealedHierarchyImpl[T] }

def isMemberOfSealedHierarchyImpl[T](using quotes: Quotes,
                                           tpe: Type[T]): Expr[Boolean] =
  import quotes.reflect._

  val parents = TypeRepr.of[T].baseClasses
  Expr(parents.exists(_.flags.is(Flags.Sealed)))
