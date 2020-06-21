package com.tersesystems.blindsight

final class Argument(val value: Any) {
  def arguments: Arguments   = new Arguments(Seq(this))
}

object Argument {

  /**
    * Converts an instance into an argument.
    */
  def apply[A: ToArgument](instance: A): Argument =
    implicitly[ToArgument[A]].toArgument(instance)
}


final class Arguments(private val elements: Seq[Argument]) {

  def size: Int = elements.size

  def isEmpty: Boolean = elements.isEmpty

  def nonEmpty: Boolean = elements.nonEmpty

  def add[T: ToArgument](instance: T): Arguments = {
    new Arguments(elements :+ Argument(instance))
  }

  def +[T: ToArgument](instance: T): Arguments = add(instance)

  def placeholders: String = " {}" * elements.size

  def toSeq: Seq[Any] = elements.map(_.value)

  def toArray: Array[Any] = toSeq.toArray

  override def toString: String = s"Arguments(${toSeq.mkString(",")})"
}

// this is a cheap way to set up a hetrogenous list of arguments
final class AsArgument(val argument: Argument)

object AsArgument {
  implicit def toAsArgument[A: ToArgument](a: A): AsArgument = {
    val arguments = implicitly[ToArgument[A]].toArgument(a)
    new AsArgument(arguments)
  }
}

object Arguments {
  val empty: Arguments = new Arguments(Seq.empty)

  def apply(els: AsArgument*): Arguments = {
    els.foldLeft(Arguments.empty)((acc, el) => acc + el.argument)
  }
}
