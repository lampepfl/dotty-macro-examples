import scala.quoted.*

inline def mcr: Any = ${ mcrImpl }
def mcrImpl(using Quotes): Expr[Any] =
  import quotes.reflect.*
  val tree = Apply(Select.unique(resolveThis, "bar"), List(Literal(StringConstant("World"))))
  tree.asExpr

def resolveThis(using Quotes): quotes.reflect.Term =
  import quotes.reflect.*
  var sym = Symbol.spliceOwner  // symbol of method where the macro is expanded
  while sym != null && !sym.isClassDef do
    sym = sym.owner  // owner of a symbol is what encloses it: e.g. enclosing method or enclosing class
  This(sym)
