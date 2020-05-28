package dummy

import scala.quoted._

trait Show:
  def show(t: Int): String

// Objective: to see how calling a function and splicing
// the result works in a macro
inline def mcr: Show = ${ mcrImpl }

def mcrImpl(using QuoteContext): Expr[Show] =
  def process(x: Expr[Int]): Expr[String] =
    println(s"""\u001b[43;1m\u001b[30mDEBUG:\u001b[0m x = ${x.show}""")
    '{"Random"}

  '{
    new Show {
      override def show(t: Int): String =
        val spliced: String = ${process('{t})}
        s"The value is ${spliced}" // Note the here the $ is from the s""
    }
  }
