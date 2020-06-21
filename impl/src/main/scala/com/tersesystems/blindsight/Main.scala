package com.tersesystems.blindsight

import java.time.Instant

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

  val pizza = Pizza("sweetcorn")
  val burrito = Burrito("chicken")
  val anything = "anything"
  val instant = Instant.now

  println(st"I like food")
  println(st"I like ${pizza}")
  println(st"I like ${burrito}")
  println(st"I like both ${pizza} and ${burrito}")
  println(st"I like ${anything}")
  //println(st"I like ${instant} coffee") // should not compile
}

