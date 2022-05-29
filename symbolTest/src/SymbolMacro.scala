package dummy

import scala.quoted.*

// Example to use symbols.
object Symbols {

  def printSymbolsImpl(using q: Quotes) : Expr[Function0[Unit]] = {
    import quotes.reflect._

    val owner = Expr(Symbol.spliceOwner.name)
    val parent = Expr(Symbol.spliceOwner.owner.name)
    val grandParent = Expr(Symbol.spliceOwner.owner.owner.name)

    // The body of the function can be created with quoting and splicing.
    // It can be also created with use of reflection module.
    val functionBody: Expr[Unit] = '{
    println(s"Splice owner: ${$owner}, parent ${$parent}, grandParent ${$grandParent}")
    }

    // To create a new method Symbol function is used.
    val functionDefSymbol = Symbol.newMethod(
      Symbol.spliceOwner,  //Symbol - owner of the method
      "printSymbolsGenerated",  // The name of the function
      MethodType(Nil)( // Argument names - in this case empty list
        _ => Nil, // Argument definitions - in this case empty list
        _ => TypeRepr.of[Unit] // Return type
      )
    )

    // The definition of the function.
    val functionDef = DefDef(functionDefSymbol, { case _ => Some(functionBody.asTerm.changeOwner(functionDefSymbol)) })

    // The DefDef will not evaluate to Expr (call to isExpr of this term returns false) so it needs to be
    // included into the AST below. It was found with use of logAST on an empty function.
    Block(List(functionDef), Closure(Ref(functionDefSymbol), None)).asExprOf[Function0[Unit]]
  }

  inline def printSymbols() : Function0[Unit] = ${ printSymbolsImpl }
}