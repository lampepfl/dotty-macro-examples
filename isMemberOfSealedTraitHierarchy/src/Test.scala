package dummy

case class OnItsOwn(x: Int)

trait Foo
case class FooMember(x: Int) extends Foo

sealed trait Bar
case class BarMember(x: Int) extends Bar
trait Nested extends Bar
case class NestedMember(x: Int) extends Nested

@main def test(): Unit =
  def printResult(name: String, result: Boolean): Unit =
    println(f"$name is a member of a sealed trait hierarchy: $result")

  printResult("OnItsOwn", isMemberOfSealedHierarchy[OnItsOwn])
  printResult("FooMember", isMemberOfSealedHierarchy[FooMember])
  printResult("BarMember", isMemberOfSealedHierarchy[BarMember])
  printResult("NestedMember", isMemberOfSealedHierarchy[NestedMember])
