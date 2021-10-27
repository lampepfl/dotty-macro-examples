package dummy

import scala.quoted.*

inline def deriveShow[T]: Show[T] = ${ deriveShowImpl[T] }

def deriveShowImpl[T](using quotes: Quotes, tpe: Type[T]): Expr[Show[T]] =
  import quotes.reflect.*
  val tpeSym = TypeTree.of[T].symbol
  if tpeSym.flags.is(Flags.Case) then deriveCaseClassShow[T]
  else if tpeSym.flags.is(Flags.Trait & Flags.Sealed) then deriveTraitShow[T]
  else throw RuntimeException(s"Unsupported combination of flags: ${tpeSym.flags.show}")

def deriveTraitShow[T](using quotes: Quotes, tpe: Type[T]): Expr[Show[T]] =
  import quotes.reflect.*
  val children: List[Symbol] = TypeTree.of[T].symbol.children

  def showBody(t: Expr[T]): Expr[String] =
    val selector: Term = t.asTerm
    val ifBranches: List[(Term, Term)] = children.map { sym =>
      val childTpe: TypeTree = TypeIdent(sym)
      val condition: Term = TypeApply(
        Select.unique(selector, "isInstanceOf"), childTpe :: Nil)
      val action: Term =
        applyShow(lookupShowFor(childTpe.tpe), Select.unique(selector, "asInstanceOf").appliedToType(childTpe.tpe))
      condition -> action
    }
    mkIfStatement(ifBranches).asExprOf[String]

  '{
    new Show[T] {
      override def show(t: T): String = ${showBody('{t})}
    }
  }

def deriveCaseClassShow[T](using quotes: Quotes, tpe: Type[T]): Expr[Show[T]] =
  import quotes.reflect.*
  // Getting the case fields of the case class
  val fields: List[Symbol] = TypeTree.of[T].symbol.caseFields

  /** Create a quoted String representation of a given field of the case class */
  def showField(caseClassTerm: Term, field: Symbol): Expr[String] =
    val fieldValDef: ValDef = field.tree.asInstanceOf[ValDef]  // TODO remove cast
    val fieldTpe: TypeRepr = fieldValDef.tpt.tpe
    val fieldName: String = fieldValDef.name

    val tcl: Term = lookupShowFor(fieldTpe)  // Show[$fieldTpe]
    val fieldValue: Term = Select(caseClassTerm, field)  // v.field
    val strRepr: Expr[String] = applyShow(tcl, fieldValue).asExprOf[String]
    '{s"${${Expr(fieldName)}}: ${${strRepr}}"}  // summon[Show[$fieldTpe]].show(v.field)

  def showBody(v: Expr[T]): Expr[String] =
    val vTerm: Term = v.asTerm
    val valuesExprs: List[Expr[String]] = fields.map(showField(vTerm, _))
    val exprOfList: Expr[List[String]] = Expr.ofList(valuesExprs)
    '{$exprOfList.mkString(", ")}

  '{
    new Show[T] {
      override def show(t: T): String =
        s"{ ${${showBody('{t})}} }"
    }
  }


/** Look up the Show[$t] typeclass for a given type t */
def lookupShowFor(using quotes: Quotes)(t: quotes.reflect.TypeRepr): quotes.reflect.Term =
  import quotes.reflect.*
  val showTpe = TypeRepr.of[Show]
  val tclTpe = showTpe.appliedTo(t)
  Implicits.search(tclTpe) match
    case res: ImplicitSearchSuccess => res.tree

/** Composes the tree: $tcl.show($arg) */
def applyShow(using quotes: Quotes)(tcl: quotes.reflect.Term, arg: quotes.reflect.Term): quotes.reflect.Term =
  import quotes.reflect.*
  Apply(Select.unique(tcl, "show"), arg :: Nil)

/** Takes a list of branches of the form (condition, action).
    From that, composes an if statement of the form:
    if $condition1 then $action1
    else if $condition2 then $action2
    ...
    else throw RuntimeException("Unhandled condition encountered during Show derivation")
  */
def mkIfStatement(using quotes: Quotes)(
  branches: List[(quotes.reflect.Term, quotes.reflect.Term)]): quotes.reflect.Term =
  import quotes.reflect.*
  branches match
    case (p1, a1) :: xs =>
      If(p1, a1, mkIfStatement(xs))
    case Nil => ('{throw RuntimeException("Unhandled condition encountered during Show derivation")}).asTerm
