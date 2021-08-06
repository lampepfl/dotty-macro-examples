# Pass arguments as varargs into the AST that represents the method

Sometimes we need to construct the AST for some method and pass our parameters as varargs in it (e.g. pass `10`, `str`, `5.2` into the method `def foo(args: Any*)`). Firstly, to pass our parameters properly, we need to construct the term for each element. In our case for constants `10`, `str`, `5.2` terms will look like: `Literal(IntConstant(10))`, `Literal(StringConstant("str"))`, `Literal(DoubleConstant(5.2))`. Then we need to create a type tree for type `Any*` and to implement it we construct type tree for `<repeated>` and then apply `Any` type to it. We can do it in the next way: `Applied(TypeIdent(defn.RepeatedParamClass), List(TypeTree.of[Any]))`.

Now we can create the AST for our varargs arguments. According to this pull request: https://github.com/lampepfl/dotty/pull/10729 and `Varagrs` implementation in `quoted` package the AST of varargs has next pattern: `Typed(Inlined(EmptyTree, List(), Repeated(elems, _)), _)`. So, our AST of passing varargs into `foo` method will be constructed in this way:
```scala
Apply(Ref(Symbol.requiredMethod("dummy.foo")), List(Typed(Inlined(None, Nil, Repeated(List(Literal(IntConstant(10)), Literal(StringConstant("str")), Literal(DoubleConstant(5.2))),  TypeTree.of[Any])), Applied(TypeIdent(defn.RepeatedParamClass), List(TypeTree.of[Any])))))
```