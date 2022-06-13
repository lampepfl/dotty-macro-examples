# A Demo Porting for scala-sql's macro into scala3

- `{$x: t}` extract the type of expr `x` and continue to expand the macro.
- `Expr.summon` search the implicit value
- using tasty reflection to create the method tree