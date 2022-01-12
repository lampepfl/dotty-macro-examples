This example illustrates how to reference a variable defined in an expression from an AST.

1. We first define a variable as a quote: `'{ val stuff = StateContainer("FoobBar") }`. `StateContainer` is a class that contains a mutable variable. We can later change and print that variable from the macro, thus demonstrating the usage of `stuff` at an `Expr` that doesn't contain its definition.
2. We turn the quote from (1) into a `Term` and deconstruct it to obtain the statement defining the variable and the reference to that variable. We need the statement to eventually put it somewhere the usage site will see it. The reference is needed to access the variable later.
3. We then construct an `Expr` that does something on the variable while referring to that variable via the reference obtained at step (2).
4. Finally we construct a block that contains the variable definition obtained at step (2) and variable usage constructed at step (3).