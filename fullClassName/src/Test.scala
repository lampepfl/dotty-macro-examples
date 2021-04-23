package foo

trait Foo
object Foo:
  trait Nested

case class Bar(x: Int)

@main def test(): Unit =
  println("Full name of Foo is " + fullClassName[Foo])
  println("Full name of Bar is " + fullClassName[Bar])
  println("Full name of Foo is " + fullClassName[Foo.type])
  println("Full name of Nested is " + fullClassName[Foo.Nested])
