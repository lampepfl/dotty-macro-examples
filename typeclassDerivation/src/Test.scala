import deriving._, compiletime._

trait Show[T]:
  def (x: T) show: String

object Show:
  inline def product[T](using m: Mirror.ProductOf[T]): Show[T] = new {
    def (x: T) show: String =
      val memberTypeclasses: List[Show[_]] =
        summonAll[Tuple.Map[m.MirroredElemTypes, Show]]
          .toList.asInstanceOf[List[Show[_]]]
      val productLabel: String = constValue[m.MirroredLabel]
      val memberLabels: List[String] = constValueTuple[m.MirroredElemLabels]
        .toList.asInstanceOf[List[String]]

      val members: List[_] =
        x.asInstanceOf[Product].productIterator.toList
      val shownMembers: List[String] =
        members.zip(memberTypeclasses).zip(memberLabels).map {
          case ((member, showTcl: Show[a]), label) =>
            val representation: String =
              showTcl.show(member.asInstanceOf[a])
            s"$label = $representation"
        }

      s"$productLabel: ${shownMembers.mkString(", ")}"
    end show
  }

  inline def sum[T](using m: Mirror.SumOf[T]): Show[T] = new {
    def (x: T) show: String =
      val ordinal: Int = m.ordinal(x)
      val typeclass: Show[T] =
        summonAll[Tuple.Map[m.MirroredElemTypes, Show]]
          .toArray(ordinal).asInstanceOf[Show[T]]
      typeclass.show(x)
    end show
  }

  inline given derived[T](using m: Mirror.Of[T]) as Show[T] =
    inline m match
      case s: Mirror.SumOf[T] => sum(using s)
      case p: Mirror.ProductOf[T] => product(using p)
  end derived

  given as Show[String] = identity
  given as Show[Int] = _.toString
end Show

sealed trait Animal derives Show
case class Cat(name: String, age: Int) extends Animal
case class Dog(name: String, owner: String) extends Animal

@main def Test =
  val cat: Cat = Cat("Tom", 3)
  val dog: Dog = Dog("Ball", "Tom")
  val animal: Animal = cat
  println(cat.show)
  println(dog.show)
  println(animal.show)
end Test
