import scala.quoted.*

inline def mcr[T]: Base = ${ mcrImpl[T] }
def mcrImpl[T: Type](using Quotes): Expr[Base] =
  import quotes.reflect.*

  class MyTreeMap extends TreeMap:
    override def transformStatement(tree: Statement)(owner: Symbol): Statement =
      println(s"Traversing tree ${tree.getClass}")
      tree match
        case t@ClassDef(name, constr, parents, selfOpt, body) =>
          ClassDef.copy(t)(name, constr, parents :+ TypeTree.of[T], selfOpt, body)
        case _ =>
          super.transformStatement(tree)(owner)

    override def transformTerm(tree: Term)(owner: Symbol): Term =
      tree match
        case Typed(expr, tpt) =>
          val newTpe = tpt.tpe match
            case t@AndType(base, foo) => AndType(t, TypeTree.of[T].tpe)
          val newTpt = Inferred(newTpe)
          Typed(expr, newTpt)
        case _ =>
          super.transformTerm(tree)(owner)
  end MyTreeMap

  val expr = '{new Base with Foo}
  val newTree = new MyTreeMap().transformTree(expr.asTerm)(Symbol.spliceOwner)
  val newExpr = newTree.asExpr
  println(newExpr.show)
  println(newTree.show(using Printer.TreeStructure))
  newExpr.asExprOf[Base]
