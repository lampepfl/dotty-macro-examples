The objective of this example is to derive the `Show` typeclass without using macros. To do so, we need to do three methods:

- Implement typeclass derivation for product types (case classes).
- Implement typeclass derivation for sum types (traits).
- Implement the `derives` method that will be able to derive the typeclass for both products and sums by delegation to the previously defined two methods.

Before walking through this tutorial, it is highly advisable to look through the [typeclass derivation documentation](https://dotty.epfl.ch/docs/reference/contextual/derivation.html).

# Product derivation
To display a case class `T`, we need to be able to display each field of the case class. Hence we need to obtain the `Show` typeclass for each field's type. We can obtain the fields' types by accessing the `m.MirroredElemTypes` where `m` is the `Mirror.ProductOf[T]`.

`m.MirroredElemTypes` gives us the tuple of all the fields' types – from this tuple we need to obtain a collection of `Show` typeclasses for each field's type. The following snipped does this: `summonAll[Tuple.Map[m.MirroredElemTypes, Show]]`. `summonAll` here accepts a tuple as a type argument and summons each type member of that tuple. `Tuple.Map` converts a tuple of field types into the tuple of typeclass types. For example, if for `Cat` the `m.MirroredElemTypes` is `String *: Int *: EmptyTuple` than `Tuple.Map[m.MirroredElemTypes, Show]` will be `Show[String] *: Show[Int] *: EmptyTuple`.

Other two things we need to display a case class are:

- The name of the case class
- The names of its fields

The former can be obtained by calling `constValue[m.MirroredLabel]`. `m.MirroredLabel` is a `String` singleton type that represents the name of the case class. E.g. for `Cat`, it is `"Cat"`. Note that it is a type and not a value, so we must call `constValue` to convert it to a value.

The names of the fields can be obtained by calling `constValueTuple[m.MirroredElemLabels]`. Similarly to the name of the case class, `m.MirroredElemLabels` store the names of the fields as singleton types, and `constValueTuple` is used to convert a tuple of types to a tuple value.

# Sum derivation
To derive a typeclass for a trait, we need to be able to derive a typeclass for every subtype of that trait. Once that is done, we need to check which subtype we are dealing with and return a typeclass for that subtype.

For example, if we have `Cat <: Animal` and `Dog <: Animal` and the goal is to derive the `Show[Animal]`, we first derive the typeclasses for both `Cat` and `Dog`. Then, on runtime, we check the exact type of the value supplied to the `show` function and display it using its precise type's typeclass.

We can obtain the typeclasses for all the subtypes of a trait similarly to how we did it for all the fields of a case class: `summonAll[Tuple.Map[m.MirroredElemTypes, Show]]`. For a trait, `m.MirroredElemTypes` will return the types of all its children.

A mirror of a trait has also a method `ordinal` defined on it. Once called on a trait's child instance, it will return an ordinal nubmer of the child's type in the trait's list of childern. Knowing this ordinal, it is possible to obtain the correct typeclass from the list of typeclasses of all the children – the required typeclass will have the same id in the list as the ordinal of the child's type.
