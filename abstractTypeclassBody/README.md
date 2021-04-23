# Abstract The Macro-generated Type Class Body into A Separate Function

Good for keeping the code modular.

The techniques used is _quotes_ and _splices_.
We see what to do if you want to generate a part of a _quote_ from another function while having
access to the variables defined in that _quote_ as expressions.
