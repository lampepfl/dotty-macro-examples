# Create a Lambda via TASTy Reflection
This example demonstrates how you can create a lambda via TASTy Reflection. To do that, you need to utilize the `Lambda` module. Its `apply` method that constructs the lambda takes the following parmeters:

- `owner` – the symbol of the place where the lambda is created, use `Symbol.spliceOwner` to create it where the macro is expanded.
- `tpe` – use this to specify the type of the lambda's parameters and its result type.
- `rhsFn` – defines the body of the lambda using the parameter reference trees.
