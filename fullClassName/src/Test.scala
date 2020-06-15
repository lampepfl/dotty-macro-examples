package foo

trait Foo
case class Bar(x: Int)
object Foo:
  trait Nested

@main def Test =
  println("Full name of Foo is " + fullClassName[Foo])
  println("Full name of Bar is " + fullClassName[Bar])
  println("Full name of Foo is " + fullClassName[Foo.type])
  println("Full name of Nested is " + fullClassName[Foo.Nested])
