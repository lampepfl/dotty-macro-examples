package stuff

trait Bar:
  def something: String

given Bar with
  def something = "Hello World"

def f(using b: Bar) = println(b.something)

@main def Test =
  mcr  // f
