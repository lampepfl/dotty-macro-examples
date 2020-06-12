case class OnItsOwn(x: Int)

trait Foo
case class FooMember(x: Int) extends Foo

sealed trait Bar
case class BarMember(x: Int) extends Bar
trait Nested extends Bar
case class NestedMember(x: Int) extends Nested

@main def Test =
  println("OnItsOwn is member of a sealed trait hierarchy: " +
    isMemberOfSealedHierarchy[OnItsOwn])
  println("FooMember is member of a sealed trait hierarchy: " +
    isMemberOfSealedHierarchy[FooMember])
  println("BarMember is member of a sealed trait hierarchy: " +
    isMemberOfSealedHierarchy[BarMember])
  println("NestedMember is member of a sealed trait hierarchy: " +
    isMemberOfSealedHierarchy[NestedMember])
