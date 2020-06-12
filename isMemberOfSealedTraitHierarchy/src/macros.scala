import scala.quoted._

inline def isMemberOfSealedHierarchy[T]: Boolean = ${ isMemberOfSealedHierarchyImpl[T] }

def isMemberOfSealedHierarchyImpl[T](using qctx: QuoteContext,
  tpe: Type[T]): Expr[Boolean] =
  import qctx.tasty._

  val parents = tpe.unseal.tpe.baseClasses
  Expr(parents.exists { p => p.flags.is(Flags.Sealed) })
