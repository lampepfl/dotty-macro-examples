The objective is, given an expression, to be able to access an arbitrary field or method by its String name on that expression.

We first access a single field in that manner. Then we call a method knowing only its String name, and pass the value of the field in question to that method. Then we discuss the case when a method has generic parameters. The generic parameters can be either known or unknown on macro call time. In case they are unknown, we may still be able to infer them manually from the arguments to the method call. We discuss how to call the method using such a manual inference.

The techniques used here are:

- TASTy Reflect for typed AST tree manipulation
- Unlifting a literal from an expression to a value
- Learning which exactly trees to construct via `println('{ ... }.unseal)` technique
- Working with the TASTy Reflection API, learning how exactly to construct the TASTy nodes
- Widening a TASTy type to prevent it from been to narrow