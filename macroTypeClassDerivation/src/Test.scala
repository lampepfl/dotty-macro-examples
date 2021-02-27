package dummy

sealed trait People
case class Person(name: String, address: String, age: Int) extends People
case class Couple(person1: Person, person2: Person)        extends People

object Implicits extends LowPrioImplcits:
  given Show[String] with
    def show(t: String) = t

  given Show[Int] with
    def show(t: Int) = t.toString
end Implicits

trait LowPrioImplcits:
  inline given [T]: Show[T] = deriveShow[T]
end LowPrioImplcits

import Implicits.given

def display(p: People)(using s: Show[People]) =
  println(s"Show: ${s.show(p)}")

@main def test(): Unit =
  val p1 = Person("John", "Zurich", 26)
  val p2 = Person("Ann", "Bern", 23)
  val c: People = Couple(p1, p2)
  display(p1)
  display(p2)
  display(c)
