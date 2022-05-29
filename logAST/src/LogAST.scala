package dummy

import scala.quoted.*

// Macro entry point - an inline function. The argument needs to be also inlined to get
// the information about AST of the code that should be printed not its reference.
inline def logAST[T](inline expression: T) = ${ logASTImpl('expression) }

// An utility to print generic code, both AST and the code itself.
// Useful when creating/matching AST in own macros to see how the
// valid code looks as AST.
def logASTImpl[T: Type](expression: Expr[T])(using q: Quotes) : Expr[T]= {
  import quotes.reflect.*
  val term = expression.asTerm
  println(s"===========Tree of type ${Type.show}=========:")
  println("TreeAnsiCode")
  println(term.show(using Printer.TreeAnsiCode))
  println("TreeStructure")
  println(term.show(using Printer.TreeStructure))
  println()
  println("===========================")
  expression
}