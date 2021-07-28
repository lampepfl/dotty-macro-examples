package example

def myPythonMethod(xs: List[Int]): Unit = scalapy.native
def myPythonVal: Unit = scalapy.native

class Enclosing {
  def foo(i: Int, s: String, b: Boolean): Unit = scalapy.native
}

@main def test(): Unit =
  myPythonMethod(List(1,2,3))
  myPythonVal
  Enclosing().foo(23, "hello", false)
