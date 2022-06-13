package dummy

import java.sql.*
import scala.quoted.*

/**
 * ScalaSql is a demo-porting of the [scala-sql](https://github.com/wangzaixiang/scala-sql) project into Scala3.
 */
object ScalaSql:
  trait ResultSetMapper[T]:
    def from(rs: ResultSet): T

  trait JdbcAccessor[T]:
    def get(rs: ResultSet, name: String): T

  extension (rs: ResultSet)
    def get[T: JdbcAccessor](name: String): T =
      summon[JdbcAccessor[T]].get(rs, name)

  given JdbcAccessor[String] with
    def get(rs: ResultSet, name: String): String =
      rs.getString(name)

  given JdbcAccessor[Int] with
    def get(rs: ResultSet, name: String): Int =
      rs.getInt(name)

object SqlMacro:
  import ScalaSql.{given, *}

  inline def resultSetMapper[T]: ResultSetMapper[T] = ${ rsMapperImpl[T] }

  private def rsMapperImpl[T: Type](using Quotes): Expr[ResultSetMapper[T]] = {
    import quotes.reflect.*

    def exprForField[T: Type](field: Symbol)(using Quotes): Expr[?] =
      val typetree = TypeTree.of[T]
      val nullObj = Typed(Literal(NullConstant()), typetree)
      val fieldTypeTree = field.tree.asInstanceOf[ValDef].tpt
      Typed( Select.unique (nullObj, field.name), fieldTypeTree).asExpr

    def rsGetField[T:Type](rs: Expr[ResultSet], field: Symbol)(using quotes: Quotes): (Symbol, ValDef) =
      val name = field.name
      val nameExpr = Expr(name)
      val tree = field.tree
      val tpe: TypeRepr =  tree.asInstanceOf[ValDef].tpt.tpe

      val expr = exprForField[T](field)

      val rsGet = expr match {
        case '{ $x: t } =>
          val typet = TypeTree.of[t]
          Expr.summon[JdbcAccessor[t]] match {
            case Some(accessor) =>
              '{ $rs.get[t]($nameExpr)(using $accessor) }
            case None =>
              ??? // scala.compiletime.error("No JdbcAccessor for type ")
          }
      }

      val variable = Symbol.newVal(Symbol.spliceOwner, name, tpe, Flags.EmptyFlags, Symbol.noSymbol)
      val valdef = ValDef(variable, Some(rsGet.asTerm))
      (variable, valdef)


    def buildBeanFromRs(rs: Expr[ResultSet]) = {

      val tpeSym = TypeTree.of[T].symbol
      val children = tpeSym.caseFields

      val varsAndDefs: List[(Symbol, ValDef)] = children.map(rsGetField[T](rs, _))

      val companion = tpeSym.companionModule
      val applyMethod = companion.memberMethod("apply").apply(0)

      val args: List[Ref] = varsAndDefs.map(x => Ref(x._1))
      val stmts: List[ValDef] = varsAndDefs.map(x => x._2)

      val apply = Apply(
        Select( Ref(companion), applyMethod),
        args )

      val block = Block(stmts, apply)
      block.asExpr.asInstanceOf[Expr[T]]
    }

    '{
      new ResultSetMapper[T]:
        def from(rs: ResultSet): T = ${buildBeanFromRs('{rs})}
    }

  }