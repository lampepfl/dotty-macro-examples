# Check Whether A Type Inherits from A Sealed Trait

We first obtain the list of all the parents of the given type. Then, we see if any of the parents
has the `Sealed` flag.

To obtain the list of all the parents, we utilize the Quotes Reflection `TypeRepr` API. We first obtain
a `TypeRepr` via calling `TypeRepr.of[T]`. We then call `baseClasses` defined in the API of
`TypeRepr` which gives us a `List[Symbol]` with all the parents of the given type.

Given a `Symbol`, we can use the `Symbol` API to check if it has a given flag. The flag of interest
for us is `Sealed` as all the sealed traits carry it. We can check whether a symbol has a flag via
`sym.flags.is(flagInQuestion)` idiom. We use this idiom to see if any of the parents has the
`Sealed` flag.
