# Obtain References To The Arguments of the Enclosing Method using Macros

We utilize the TASTy Reflect `Symbol` API to call the `spliceOwner` method which accesses the
top level splice that called the macro. We then call `owner` on that Symbol to get the definition
that calls the macro method.

This technique is useful for boilerplate free access to the arguments passed to a "facade" method
to a foreign function interface.

e.g. to call a Python method, we can implement a `native` macro that collects the arguments of
the method and passes them to the Python runtime. Here is an example of the usage:
```scala
def foo(i: Int, s: String, b: Boolean): Unit = scalapy.native
```

`scalapy.native` will expand into the following:

```scala
def foo(i: Int, s: String, b: Boolean): Unit =
  scalapy.ScalaPy.applyDynamic("foo")(i, s, b).asInstanceOf[Unit]
```
