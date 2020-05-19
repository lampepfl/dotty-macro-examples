$ rm -rv *.class *.tasty dummy || true

dotty-bootstrapped/dotc -d $here
  $here/macro.scala $here/Show.scala

dotty-bootstrapped/dotc -d $here -classpath $here
  $here/Test.scala

dotty-bootstrapped/dotr -classpath $here dummy.Test
