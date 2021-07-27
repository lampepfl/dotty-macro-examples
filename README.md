# Scala 3 Metaprogramming Examples

![CI](https://github.com/anatoliykmetyuk/dotty-macro-examples/workflows/CI/badge.svg)

This repo contains a bunch of examples of doing metaprogramming in Scala 3 using the low level reflection API.

Every folder is a separate example. Each example contains a README.md file with a description of what the example does.

To run an example:

1. Clone and `cd` into the repo using
   `git clone https://github.com/anatoliykmetyuk/dotty-macro-examples.git && cd dotty-macro-examples`

2. Use `./mill <example_name>.run` command to run the example you are interested in.
   E.g. `./mill macroTypeClassDerivation.run` runs `macroTypeClassDerivation` example.

## Examples

- [abstractTypeClassBody](https://github.com/anatoliykmetyuk/dotty-macro-examples/tree/master/abstractTypeClassBody) – how to abstract a body of a function inside a macro-generated class into a separate macro.
- [accessMembersByName](https://github.com/anatoliykmetyuk/dotty-macro-examples/tree/master/accessMembersByName) – access an arbitrary member of a value given this member's name as a `String`.
- [accessEnclosingParameters](https://github.com/lampepfl/dotty-macro-examples/tree/master/accessEnclosingParameters) - access the arguments passed to the enclosing method of the macro call.
- [defaultParamsInference](https://github.com/anatoliykmetyuk/dotty-macro-examples/tree/master/defaultParamsInference) – given a case class with default parameters, obtain the values of these default parameters.
- [fullClassName](https://github.com/anatoliykmetyuk/dotty-macro-examples/tree/master/fullClassName) - get a fully qualified name of a class.
- [isMemberOfSealedTraitHierarchy](https://github.com/anatoliykmetyuk/dotty-macro-examples/tree/master/isMemberOfSealedTraitHierarchy) - check if a class inherits from a sealed trait.
- [macroTypeClassDerivation](https://github.com/anatoliykmetyuk/dotty-macro-examples/tree/master/macroTypeClassDerivation) – typeclass construction done with Quotes Reflection.
- [outOfScopeMethodCall](https://github.com/anatoliykmetyuk/dotty-macro-examples/tree/master/outOfScopeMethodCall) – get a reference to `this` where the type of `this` may not be known on macro definition site, and call a method on `this`.
- [outOfScopeTypeParam](https://github.com/anatoliykmetyuk/dotty-macro-examples/tree/master/outOfScopeTypeParam) – get a reference to a type that is not available to the macro definition's scope. Then use this reference as a type parameter to call a method.
- [outOfScopeClassConstructor](https://github.com/anatoliykmetyuk/dotty-macro-examples/tree/master/outOfScopeClassConstructor) – get a reference to a type that is not available to the macro definition's scope. Then use this reference to construct an instance of that type via `new`.
- [buildingCustomASTs](https://github.com/anatoliykmetyuk/dotty-macro-examples/tree/master/buildingCustomASTs) – Quotes Reflection ASTs are powerful, but how do you know the right way to build one? This example demonstrates how to inspect compiler-generated ASTs for a given Scala code. You can then mimic the compiler when constructing similar ASTs.

## Tips and Tricks

- When working with reflect API, all of the API available to you is defined in the `dotty/library/src/scala/quoted/Quotes.scala`.
  As a rule, for every reflected type `X`, you have a group of extension methods in `trait XMethods` which defines all of the methods you can call on `X`.
  For example, for `Symbol`, you can search for `SymbolMethods` in `Quotes.scala` to see what you can do with it.
- `TypeTree.of[T]` gives you a `quotes.reflect.TypeTree`. To get a `quotes.reflect.TypeRepr`, you can call `TypeRepr.of[T]`.
- Most of the interesting data about the types reside in their `Symbol`s. To get a `Symbol` given a `tpe: quoted.Type[T]`, use `TypeTree.of[T].symbol`.
- If you have a `Symbol`, you can use `Ref` to get a `Tree` referring to that symbol – either `Ident` or `Select`.
  For example, if you have a `sym: Symbol` that refers to a `DefDef` method definition and you want to obtain a tree referring to that method, you can get this tree via `Ref(sym)`.
