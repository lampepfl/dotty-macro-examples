# Implement A Macro-powered Type Class Derivation for Sum and Product Types

We want to derive the `Show` type class. Please note that you can probably do the same with
[Type Class Derivation](https://dotty.epfl.ch/docs/reference/contextual/derivation.html). This
example is to illustrate the alternative approach involving macros that may be suitable for more
advanced use cases.

First, we look at the type for which the macro is derived to see if it is a _case class_ or
a _sealed trait_. The derivation for each of these cases is then handled separately.

## Case classes

A `Show` for a _case class_ should return the following `String` representation:
`{ field1: value1, field2: value2, ..., fieldn: valueN }`.
The algorithm is a loop over all the fields of the _case class_ – so first we obtain the list of such
fields by inspecting the symbol of the _case class_. For each field, we then compute the expression of
its `String` representation as follows.

To derive `valueN`, we need the `Show` type class for its type. We then obtain the type of the field
via inspecting the tree of its definitions. Next, we resolve the `Show` type class for that type.
Using that type class, we compute the `String` representation of the value. We do so by constructing
and then sealing an typed AST tree which calls the `show` method of the type class on the field in
question.

## Sealed traits

A `Show` for a _sealed trait_ must look at the class of the passed value. Naturally the value must
be an instance of one of the children of the _sealed trait_. We determine which exactly class the
value is an instance of, resolve the `Show` type class for this class, and use the type class to
compute the `String` representation.
