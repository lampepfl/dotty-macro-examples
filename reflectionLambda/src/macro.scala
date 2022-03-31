package dummy

import scala.quoted.*

inline def mcr(f: (Int => Int) => Int): Int = ${ mcrImpl('f) }

def mcrImpl(ef: Expr[(Int => Int) => Int])(using Quotes): Expr[Int] =
  import quotes.reflect.*
  val customLambda = Lambda(
    owner = Symbol.spliceOwner,
    tpe = MethodType(List("x"))(
      paramInfosExp = methodType => List(TypeRepr.of[Int]),
      resultTypeExp = methodType => TypeRepr.of[Int]
    ),
    rhsFn = (sym: Symbol, paramRefs: List[Tree]) => {
      val x = paramRefs.head.asExprOf[Int]
      '{ $x * $x * $x }.asTerm
    }
  )
  '{ $ef(${customLambda.asExprOf[Int => Int]}) }
