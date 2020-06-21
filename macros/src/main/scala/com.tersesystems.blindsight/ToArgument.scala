package com.tersesystems.blindsight


trait LowPriorityToArgumentImplicits {

  implicit val argumentToArgument: ToArgument[Argument] = ToArgument(identity)

  implicit val unitToArguments: ToArgument[Unit] = ToArgument { unit => new Argument(unit) }

  implicit val stringToArgument: ToArgument[String] = ToArgument { string => new Argument(string) }

  implicit val booleanToArgument: ToArgument[Boolean] = ToArgument { bool => new Argument(bool) }

  implicit val shortToArgument: ToArgument[Short] = ToArgument { short => new Argument(short) }

  implicit val intToArgument: ToArgument[Int] = ToArgument { int => new Argument(int) }

  implicit val longToArgument: ToArgument[Long] = ToArgument { long => new Argument(long) }

  implicit val floatToArgument: ToArgument[Float] = ToArgument { float => new Argument(float) }

  implicit val doubleToArgument: ToArgument[Double] = ToArgument { double => new Argument(double) }

  // Note that an exception is **not** a valid argument, and exceptions are
  // handled explicitly as [[java.lang.Throwable]] in the APIs.
}

trait ToArgument[T] {
  def toArgument(instance: T): Argument
}

object ToArgument extends LowPriorityToArgumentImplicits {
  def apply[T](f: T => Argument): ToArgument[T] =
    new ToArgument[T] {
      override def toArgument(instance: T): Argument = f(instance)
    }
}

trait ArgumentEnrichment {
  implicit class RichToArgument[A: ToArgument](instance: A) {
    def asArgument: Argument = implicitly[ToArgument[A]].toArgument(instance)
  }
}

object ArgumentEnrichment extends ArgumentEnrichment