package scalapy

import scala.quoted.*
import scala.language.dynamics

object ScalaPy extends scala.Dynamic {
  def applyDynamic(name: String)(args: Any*): Any = println(s"called $name${args.mkString("(", ", ", ")")}")
  def selectDynamic(name: String): Any = println(s"selected $name")
}

inline def native[T]: T = ${ nativeImpl[T] }

def nativeImpl[T: Type](using Quotes): Expr[T] = {
  import quotes.reflect.*
  val callee = Symbol.spliceOwner.owner
  val refss = macros.calleeParamRefs(callee)
  if refss.length > 1 then
    report.throwError(s"callee $callee has curried parameter lists.")
  val args = refss.headOption.toList.flatten
  if args.isEmpty then
    '{ScalaPy.selectDynamic(${Expr(callee.name)}).asInstanceOf[T]}
  else
    '{ScalaPy.applyDynamic(${Expr(callee.name)})(${Expr.ofSeq(args.map(_.asExpr))}*).asInstanceOf[T]}
}
