class Base:
  def say() = println(value)
  def value = "World"

trait Foo:
  this: Base =>
  override def value = "Me"

trait Bar:
  this: Base =>
  override def value = "Stuff"

@main def Test =
  val instance = mcr[Bar]
  instance.say()