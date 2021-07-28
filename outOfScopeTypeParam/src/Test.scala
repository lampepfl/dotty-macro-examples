package stuff

object Foo:
  object nestedScope:
    class Bar
    val bar = new Bar
  def run = mcr(nestedScope.bar)

def cast[T](target: Any): T = target.asInstanceOf[T]

@main def Test =
  println(Foo.run)
