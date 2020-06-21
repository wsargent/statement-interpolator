package com.tersesystems.blindsight

object Main extends App {
  sealed trait Food

  case class Pizza(topping: String) extends Food
  case class Burrito(filling: String) extends Food

  implicit val pizzaToArgument: ToArgument[Pizza] = ToArgument[Pizza] { pizza =>
    Argument("pizza with " + pizza.topping + " toppings")
  }

  implicit val burritoToArgument: ToArgument[Burrito] = ToArgument[Burrito] { burrito =>
    Argument("burrito with " + burrito.filling + " fillings")
  }

  implicit val foodToArgument: ToArgument[Food] = ToArgument[Food] { food =>
    Argument("<insert food here>")
  }

  val pizza = Pizza("sweetcorn")
  val burrito = Burrito("chicken")
  val anything = "anything"

  (st"I like a thing ${burrito}")

  println(st"") // nothing at all
  println(st"I like food") // constant
  println(st"I like ${pizza}")
  println(st"I like ${burrito}")
  println(st"I like both ${pizza} and ${burrito}")
  println(st"I like ${anything}")
  println(st"I like ${burrito: Food} which is a food") // require the food type

  val ex = new IllegalStateException("illegal state")
  //println(st"this is an ${ex}") // exception should be handled specially.

}

