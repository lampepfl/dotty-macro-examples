import scala.quoted.*

inline def mcr: Any = ${ mcrImpl }
def mcrImpl(using Quotes): Expr[Any] =
  import quotes.reflect.*
  val referenceSnippet: Expr[Unit] = '{println("Hello World")}
  val referenceAST: Term = referenceSnippet.asTerm
  println(s"AST of the reference code snippet is:\n${referenceAST}")
  '{()}
