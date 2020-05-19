$ rm -rv *.class *.tasty dummy || true

dotty-bootstrapped/dotc -d $here
  # -Xprint:typer
  $here/macro.scala

dotty-bootstrapped/dotc -d $here -classpath $here
  # -Xprint:typer
  $here/Test.scala

dotty-bootstrapped/dotr -classpath $here dummy.Test
