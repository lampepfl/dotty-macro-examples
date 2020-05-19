$ git status || git init
$ rm -rv out || true
$ mkdir out

val src = $here/src

dotty-bootstrapped/dotc -d out
  $src/macro.scala

dotty-bootstrapped/dotc -d out -classpath out
  -Xprint:typer
  $src/Test.scala

dotty-bootstrapped/dotr -classpath out dummy.Test
