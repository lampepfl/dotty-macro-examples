package dummy

case class Person(name: String, address: String = "Zuricch", foo: Int, age: Int = 26)

object Person:
  val x = 10

@main def Test =
  val p1 = Person("John", foo = 10)
  println(p1)
  println(defaultParams[Person])
  assert(defaultParams[Person] == Map("address" -> "Zuricch", "age" -> 26))
