package dummy

import scala.compiletime.summonInline

object Inlines {
  given context:String = "definition"

  inline def f(inline p: Int) = {
    val ctx = summonInline[String]      // search in inline-context
    println(s"Inline1: parameter: $p, given $ctx")
    println(s"Inline2: parameter: $p, given $ctx")
  }

  def g(p: Int) = {
    val ctx = summonInline[String]    // search in g's context
    println(s"Regular1: parameter: $p, given $ctx")
    println(s"Regular2: parameter: $p, given $ctx")
  }
}

@main() def inlineTest() = {
  import Inlines.*

  given context:String = "usage"

  var a = 1;
  def sideEffect() : Int = {
    a += 1
    a
  }
  val b = 2;
  f(sideEffect())
  g(sideEffect())
}