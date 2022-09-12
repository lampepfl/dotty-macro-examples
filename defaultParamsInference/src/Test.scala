package dummy

case class Person[T](name: String, address: String = "Zuricch", foo: Int, age: Int = 26, bar: List[T] = Nil)

object Person:
  val x = 10

@main def test(): Unit =
  val p1 = Person("John", foo = 10)
  println(p1)
  println(defaultParams[Person[Double]])
  assert(defaultParams[Person[Double]] == Map("address" -> "Zuricch", "age" -> 26, "bar" -> Nil))
