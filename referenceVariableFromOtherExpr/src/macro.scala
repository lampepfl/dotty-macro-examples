package dummy

import scala.quoted.*

class StateContainer(var value: String)

inline def dummy: Any = ${ dummyImpl }

def dummyImpl(using Quotes): Expr[Any] =
  import quotes.reflect.*
  val stuffDef = '{ val stuff = StateContainer("FoobBar") }
  stuffDef.asTerm match
    case Inlined(_, _, Block((valDef: Statement) :: Nil, _)) =>
      val stuffDefStat = valDef                // This statement defines `val` and needs to be
                                               // eventually in scope at the site we use `val`
      val stuffRef = Ref(stuffDefStat.symbol)  // This is a reference to `val` via which we can
                                               // use it later

      val stuffRefExpr = stuffRef.asExprOf[StateContainer]
      val exprThatWorksOnValueItDoesntDefine: Expr[Unit] = '{
        $stuffRefExpr.value = "Hello World"
        println($stuffRefExpr.value)
      }

      val result = Block(stuffDefStat :: Nil, exprThatWorksOnValueItDoesntDefine.asTerm).asExpr
      result