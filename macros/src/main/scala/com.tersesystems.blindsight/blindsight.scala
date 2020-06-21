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

            // Filter into throwables and arguments
            val (t, a) = args.partition(t => c.WeakTypeTag(t.tree.tpe).tpe <:< typeOf[Throwable])
            val arguments: Seq[c.Expr[Argument]] = a.map { t =>
                val nextElement = t.tree
                val tag = c.WeakTypeTag(nextElement.tpe)
                val field = q"""implicitly[com.tersesystems.blindsight.ToArgument[${tag.tpe}]].toArgument($nextElement)"""
                c.Expr[Argument](field)
            }

            val format = partz.map { case Literal(Constant(const: String)) => const }.mkString("{}")
            if (t.isEmpty) {
              c.Expr(q"com.tersesystems.blindsight.Statement($format, ..$arguments)")
            } else {
              val throwable = t.head
              c.Expr(q"com.tersesystems.blindsight.Statement($format, ..$arguments, $throwable)")
            }
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
