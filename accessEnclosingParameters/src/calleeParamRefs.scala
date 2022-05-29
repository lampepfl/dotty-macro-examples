package macros

import scala.quoted.*

def calleeParamRefs(using quotes: Quotes)(callee: quotes.reflect.Symbol): List[List[quotes.reflect.Term]] =
  import quotes.reflect.*
  val termParamss = callee.paramSymss.filterNot(_.headOption.exists(_.isType))
  termParamss.map(_.map(Ref.apply))
