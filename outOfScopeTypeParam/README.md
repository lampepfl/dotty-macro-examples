This example demonstrates how to call a method with type parameters if you do not have the type parameters available on macro compilation time. E.g. `cast[Bar]` where `Bar` type is not available inside the macro definition.

In this situation, the type parameter for `Bar` can be obtained via symbol lookup: `val barClassSym = Symbol.requiredClass("stuff.Foo.nestedScope.Bar")` gives us the symbol for the `Bar` class, and `TypeIdent(barClassSym)` turns it into a type parameter AST.

Later, we use this type parameter AST inside a `TypeApply` tree to construct a type-parameterised function call.