The objective is to implement a macro-powered typeclass derivation for sum and product types. We want to derive the Show type class. Please note that you can do the same with https://dotty.epfl.ch/docs/reference/contextual/derivation.html – see [example](https://github.com/anatoliykmetyuk/dotty-macro-examples/tree/master/typeclassDerivation). This example is to illustrate the alternative approach involving macros that may be suitable for more advanced use cases.

First, we look at the type for which the macro is derived to see if it is a case class or a sealed trait. The derivation for each of these cases is then handled separately.

## Case classes
A Show for a case class should return the following string representation: `{ field1: value1, field2: value2, ..., fieldn: valuen }`. The algorithm is a loop over all the fields of the case class – so first we obtain the list of such fields by inspecting the symbol of the case class. For each field, we then compute the expression of its String representation as follows.

To derive `valuen`, we need the `Show` typeclass for its type. We then obtain the type of the field via inspecting the tree of its definitions. Next, we resolve the Show typeclass for that type. Using that typeclass, we compute the String representation of the value. We do so by constructing and then sealing an typed AST tree which calls the `show` method of the typeclass on the field in question.

## Sealed traits
A Show for a sealed trait must look at the class of the passed value. Naturally the value must be an instance of one of the children of the sealed trait. We determine which exactly class the value is an instance of, resolve the Show typeclass for this class, and use the typeclass to compute the String representation.
