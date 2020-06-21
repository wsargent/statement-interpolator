package com.tersesystems

package object blindsight {

  //https://stackoverflow.com/a/23509652
  implicit class StatementContext(val sc: StringContext) extends AnyVal {
    def st(args: Any*): Statement = macro impl.statement
  }

  private object impl {

    import scala.reflect.macros.blackbox

    def statement(c: blackbox.Context)(args: c.Expr[Any]*): c.Expr[Statement] = {
      import c.universe._

      if (args.nonEmpty) {
        c.prefix.tree match {
          case Apply(_, List(Apply(_, partz))) =>
            val res: Seq[c.Expr[Argument]] = args.map { t =>
              val nextElement = t.tree
              val tag = c.WeakTypeTag(nextElement.tpe)

              // We want to handle throwable as well...
              //if (tag.tpe <:< typeOf[Throwable]) q"(null: String)" else tree

              val field = q"""implicitly[com.tersesystems.blindsight.ToArgument[${tag.tpe}]].toArgument($nextElement)"""
              c.Expr[Argument](field)
            }

            val parts = partz.map {
              case Literal(Constant(const: String)) => const
            }.mkString("{}")
            c.Expr(q"com.tersesystems.blindsight.Statement(${parts}, ..$res)")
          case _ =>
            c.abort(c.prefix.tree.pos, "The pattern can't be used with the interpolation.")
        }
      } else {
        val constants = (c.prefix.tree match {
          case Apply(_, List(Apply(_, literals))) => literals
        }).map { case Literal(Constant(s: String)) => s }
        c.Expr(q"com.tersesystems.blindsight.Statement(${constants.mkString})")
      }
    }
  }

}
