package dummy

def foo(args: Any*): Unit =  println(s"arguments: ${args.mkString("(", ", ", ")")}")

def bar: Unit = mcr

@main def Test = bar