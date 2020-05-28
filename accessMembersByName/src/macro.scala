package dummy

import scala.quoted._

// We have a Term, an Expr and a String name
// 1. Call the field with the given String name on Expr
// 2. Pass the result to a method called on a Term
// 3. Make the types of the result of (1) and the input to (2) generic
// 4. Call the method without knowing the generic type on compile time
class Show[T](value: T):
  def field: T = value
  def show[T](t: T): String = s"The value is $t"

inline def mcr(expr: Show[_], inline name: String): Any = ${ mcrImpl('expr, 'name) }

def mcrImpl(expr: Expr[Show[_]], nameExpr: Expr[String])(using QuoteContext): Expr[Any] =
  import qctx.tasty.{ Type => TType, _ }

  // Select field by name – use '{$expr.field}.unseal to see what tree needs to be created
  val exprTree: Term = expr.unseal
  val name: String = Unlifted.unapply(nameExpr).get
  val field: Term = Select.unique(exprTree, name)

  // Pass the result to a method call – same technique
  val showWithType = TypeApply(
    Select.unique(exprTree, "show"), field.tpe.widen.seal.unseal :: Nil)
  val call = Apply(showWithType, field :: Nil).seal
  println(call.show)
  call
