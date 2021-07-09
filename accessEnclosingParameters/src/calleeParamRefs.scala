package macros

import scala.quoted.*

def calleeParamRefs(using Quotes)(callee: quotes.reflect.Symbol): List[List[quotes.reflect.Term]] =
  import quotes.reflect.*
  val termParamss = callee.paramSymss.filterNot(_.headOption.exists(_.isType))
  termParamss.map(_.map(Ref.apply))
