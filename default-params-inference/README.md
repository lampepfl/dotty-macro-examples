In this example, the objective is to obtain the default parameters of the case class parameters via metaprogramming. We first locate where these parameters are stored by compiling a dummy case class with `-Xprint:typer`. Then, we write a metaprogram that accesses these parameters.

We first obtain the list of the parameters that have default values. To do so, we obtain the list of all the parameters from the symbol of the case class and check their flags to determine if they have a default value.

Then we need to obtain the references to the `def`s containing default parameters. Normally you could just construct a `Select` node to reference a member of a value. However these fields are "hidden": you cannot access them by their string name, only by their symbol. This is done to prevent access from the user code.

To obtain the symbols of the `def`s we look at the symbol of the companion module class. We first obtain the symbol of the companion module class, then we inspect the body of that class and thus obtain the trees of the target `def`s. From these trees, we obtain the symbols of the `def`s. Then we construct ASTs pointing to them via `Ref`s.
