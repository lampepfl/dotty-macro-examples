package dummy

import scala.quoted._

inline def deriveShow[T]: Show[T] = ${ deriveShowImpl[T] }

def deriveShowImpl[T](using qctx: QuoteContext, tpe: Type[T]): Expr[Show[T]] =
  import qctx.tasty._
  val tpeSym = tpe.unseal.symbol
  if tpeSym.flags.is(Flags.Case) then deriveCaseClassShow[T]
  else if tpeSym.flags.is(Flags.Trait & Flags.Sealed) then deriveTraitShow[T]
  else throw RuntimeException(s"Unsupported combination of flags: ${tpeSym.flags.show}")

def deriveTraitShow[T](using qctx: QuoteContext, tpe: Type[T]): Expr[Show[T]] =
  import qctx.tasty._
  val children: List[Symbol] = tpe.unseal.symbol.children

  def showBody(t: Expr[T]): Expr[String] =
    val selector: Term = t.unseal
    val ifBranches: List[(Term, Term)] = children.map { sym =>
      val childTpe: TypeTree = TypeIdent(sym)
      val condition: Term = TypeApply(
        Select.unique(selector, "isInstanceOf"), childTpe :: Nil)
      val action: Term =
        applyShow(lookupShowFor(childTpe.tpe), selector)
      condition -> action
    }
    mkIfStatement(ifBranches).seal.cast[String]

  '{
    type X = $tpe
    new Show[X] {
      override def show(t: X): String = ${showBody('{t})}
    }
  }

def deriveCaseClassShow[T](using qctx: QuoteContext, tpe: Type[T]): Expr[Show[T]] =
  import qctx.tasty._
  // Getting the case fields of the case class
  val fields: List[Symbol] = tpe.unseal.symbol.caseFields

  /** Create a quoted String representation of a given field of the case class */
  def showField(caseClassTerm: Term, field: Symbol): Expr[String] =
    val fieldValDef: ValDef = field.tree.asInstanceOf[ValDef]  // TODO remove cast
    val fieldTpe: Type = fieldValDef.tpt.tpe
    val fieldName: String = fieldValDef.name

    val tcl: Term = lookupShowFor(fieldTpe) // Show[$fieldTpe]
    val fieldValue: Term = Select(caseClassTerm, field)  // v.field
    val strRepr: Expr[String] = applyShow(tcl, fieldValue).seal.cast[String]
    '{s"${${Expr(fieldName)}}: ${${strRepr}}"}  // summon[Show[$fieldTpe]].show(v.field)

  def showBody(v: Expr[T]): Expr[String] =
    val vTerm: Term = v.unseal
    val valuesExprs: List[Expr[String]] = fields.map(showField(vTerm, _))
    val exprOfList: Expr[List[String]] = Expr.ofList(valuesExprs)
    '{$exprOfList.mkString(", ")}

  '{
    type X = $tpe
    new Show[X] {
      override def show(t: X): String =
        s"{ ${${showBody('{t})}} }"
    }
  }


/** Look up the Shot[$t] typeclass for a given type t */
def lookupShowFor(using qctx: QuoteContext)(t: qctx.tasty.Type): qctx.tasty.Term =
  import qctx.tasty._
  val showTpe = Type(classOf[Show[_]])
  val tclTpe = AppliedType(showTpe, t :: Nil)
  searchImplicit(tclTpe) match
    case res: ImplicitSearchSuccess => res.tree

/** Composes the tree: $tcl.show($arg) */
def applyShow(using qctx: QuoteContext)(tcl: qctx.tasty.Term, arg: qctx.tasty.Term): qctx.tasty.Term =
  import qctx.tasty._
  Apply(Select.unique(tcl, "show"), arg :: Nil)

/** Takes a list of branches of the form (condition, action).
    From that, composes an if statement of the form:
    if $condition1 then $action1
    else if $condition2 then $action2
    ...
    else throw RuntimeException("Unhandled condition encountered")
  */
def mkIfStatement(using qctx: QuoteContext)(
  branches: List[(qctx.tasty.Term, qctx.tasty.Term)]): qctx.tasty.Term =
  import qctx.tasty._
  branches match
    case (p1, a1) :: xs =>
      If(p1, a1, mkIfStatement(xs))
    case Nil => '{throw RuntimeException("Unhandled condition encountered " +
      "during Show derivation")}.unseal
