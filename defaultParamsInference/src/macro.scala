package dummy

import scala.quoted.*

inline def defaultParams[T]: Map[String, Any] = ${ defaultParmasImpl[T] }

def defaultParmasImpl[T](using quotes: Quotes, tpe: Type[T]): Expr[Map[String, Any]] =
  import quotes.reflect.*
  val sym = TypeTree.of[T].symbol
  val comp = sym.companionClass
  val mod = Ref(sym.companionModule)
  val names =
    for p <- sym.caseFields if p.flags.is(Flags.HasDefault)
    yield p.name
  val namesExpr: Expr[List[String]] =
    Expr.ofList(names.map(Expr(_)))

  val body = comp.tree.asInstanceOf[ClassDef].body
  val idents: List[Ref] =
    for case deff @ DefDef(name, _, _, _) <- body
    if name.startsWith("$lessinit$greater$default")
    yield mod.select(deff.symbol)
  val identsExpr: Expr[List[Any]] =
    Expr.ofList(idents.map(_.asExpr))

  '{ $namesExpr.zip($identsExpr).toMap }
