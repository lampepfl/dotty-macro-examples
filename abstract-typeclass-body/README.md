The objective is to abstract the macro-generated typeclass body into a separate function. Good for keeping the code modular.

The techniques used is quotes and splices. We see what to do if you want to generate a part of a quote from another function while having access to the variables defined in that quote as expressions.