package dummy

import scala.quoted._

inline def defaultParams[T]: Map[String, Any] = ${ defaultParmasImpl[T] }

def defaultParmasImpl[T](using qctx: QuoteContext,
  tpe: Type[T]): Expr[Map[String, Any]] =
  import qctx.tasty._
  val sym = tpe.unseal.symbol
  val comp = sym.companionClass
  val names =
    for p <- sym.caseFields if p.flags.is(Flags.HasDefault)
    yield p.name
  val namesExpr: Expr[List[String]] =
    Expr.ofList(names.map(Expr(_)))

  val body = comp.tree.asInstanceOf[ClassDef].body
  val idents: List[Ref] =
    for case deff @ DefDef(name, _, _, _, tree) <- body
    if name.startsWith("$lessinit$greater$default")
    yield Ref(deff.symbol)
  val identsExpr: Expr[List[Any]] =
    Expr.ofList(idents.map(_.seal))

  '{ $namesExpr.zip($identsExpr).toMap }
