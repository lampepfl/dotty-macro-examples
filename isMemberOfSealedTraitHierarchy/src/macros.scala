package dummy

import scala.quoted.*

inline def isMemberOfSealedHierarchy[T]: Boolean =
  ${ isMemberOfSealedHierarchyImpl[T] }

def isMemberOfSealedHierarchyImpl[T](using quotes: Quotes,
                                           tpe: Type[T]): Expr[Boolean] =
  import quotes.reflect.*

  val parents = TypeRepr.of[T].baseClasses
  Expr(parents.exists(_.flags.is(Flags.Sealed)))
