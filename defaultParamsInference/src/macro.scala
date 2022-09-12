package dummy

import scala.annotation.experimental
import scala.quoted.*

inline def defaultParams[T]: Map[String, Any] = ${ defaultParmasImpl[T] }

@experimental // because .typeArgs is @experimental
def defaultParmasImpl[T](using quotes: Quotes, tpe: Type[T]): Expr[Map[String, Any]] =
  import quotes.reflect.*
  val typ = TypeRepr.of[T]
  val sym = typ.typeSymbol
  val typeArgs = typ.typeArgs
  val comp = sym.companionClass
  val mod = Ref(sym.companionModule)
  val names =
    for p <- sym.caseFields if p.flags.is(Flags.HasDefault)
    yield p.name
  val namesExpr: Expr[List[String]] =
    Expr.ofList(names.map(Expr(_)))

  val body = comp.tree.asInstanceOf[ClassDef].body
  val idents: List[Term] =
    for case deff @ DefDef(name, _, _, _) <- body
    if name.startsWith("$lessinit$greater$default")
    yield mod.select(deff.symbol).appliedToTypes(typeArgs)
  val identsExpr: Expr[List[Any]] =
    Expr.ofList(idents.map(_.asExpr))

  '{ $namesExpr.zip($identsExpr).toMap }
