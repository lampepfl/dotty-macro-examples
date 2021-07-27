object Foo:
  def bar(name: String) = println(s"Hello $name")
  def run = mcr  // mcr expands to this.bar("World")

object Bar:
  def bar(name: String) = println(s"Hello from Bar $name")
  def run = mcr  // mcr expands to this.bar("World")

@main def Test =
  Foo.run
  Bar.run