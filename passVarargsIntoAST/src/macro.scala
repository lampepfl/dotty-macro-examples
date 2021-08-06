package dummy

import scala.quoted.*

inline def mcr[T]: T = ${ mcrImpl[T] }

def mcrImpl[T: Type](using Quotes): Expr[T] = {
  import quotes.reflect.*

  val fooMethodSymbol = Symbol.requiredMethod("dummy.foo")

  // constructing term for each element and creating list of parameters from those terms
  val argumentsAsTerms = List(Literal(IntConstant(10)), Literal(StringConstant("str")), Literal(DoubleConstant(5.2)))

  // creating type tree for varargs type Any*
  val repeatedAnyTypeTree = Applied(TypeIdent(defn.RepeatedParamClass), List(TypeTree.of[Any]))

  // creating the AST for varargs
  val varargsTerm = Typed(Inlined(None, Nil, Repeated(argumentsAsTerms,  TypeTree.of[Any])), repeatedAnyTypeTree)

  // passing varargs into foo method AST
  val term = Apply(Ref(fooMethodSymbol), List(varargsTerm))

  return term.asExprOf[T]
}