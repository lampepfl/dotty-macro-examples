package dummy

def f(g: Int => Int): Int = g(10)

@main def test(): Unit =
  val result = mcr(f)
  println(s"Result is $result")
