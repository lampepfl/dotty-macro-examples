package foo

final class Foo(val value: String) extends AnyVal

@main def test(): Unit =
  val ctor: String => Foo = stringValueClassConstructor[Foo]
  assert(ctor("bar") == new Foo("bar"))
