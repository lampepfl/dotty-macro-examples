package foo

import scala.quoted.*

/* Returns a `String => A` for any Value class with a `String` field */
inline def stringValueClassConstructor[A <: AnyVal]: String => A =
  ${ stringValueClassConstructorImpl[A] }

def stringValueClassConstructorImpl[A: Type](using q: Quotes): Expr[String => A] = {
  import q.reflect.*

  val aTpr = TypeRepr.of[A]
  val ctor = aTpr.typeSymbol.primaryConstructor

  ctor.paramSymss match {
    case List(v: Symbol) :: Nil =>
      v.tree match {
        case vd: ValDef if vd.tpt.tpe <:< TypeRepr.of[String] => {
          def stringToA(in: Expr[String]): Expr[A] =
            New(Inferred(aTpr)).select(ctor).appliedTo(in.asTerm).asExprOf[A]

          '{ (value: String) => ${ stringToA('value) } }
        }

        case _ =>
          report.errorAndAbort(
            s"Constructor String parameter expected, found: ${v}"
          )
      }

    case _ =>
      report.errorAndAbort(
        s"Cannot resolve value function for '${aTpr.typeSymbol.name}'"
      )

  }
}
