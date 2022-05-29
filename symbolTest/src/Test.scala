package dummy

import dummy.logAST

@main def symbolTest() = {

  def printSymbols: () => Unit = Symbols.printSymbols()

  // It will print: Splice owner: macro, parent printSymbols, grandParent symbolTest
  printSymbols()

  // To find how the function definition should be constructed in the macro.
  logAST {
    // Splice owner: macro parent symbolTest, grandParent Test$package$
    Symbols.printSymbols()
    def f() = {

    }
  }
}