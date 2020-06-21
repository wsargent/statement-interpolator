https://github.com/tersesystems/blindsight/issues/86

It should be possible to build up a macro / StringContext solution to accept something only if it has a `ToArgument` type class:

```scala
val foo: Foo = Foo()
object Foo {
  implicit val fooToArgument: ToArgument[Foo] = ...
}
logger.info(st"I am a statement with a $foo")
// logger.info(st"I don't compile because $bar doesn't have a ToArgument")
```

And have that convert to SLF4J style at compile time:

```scala
logger.info("I am a statement with an {}", argument)
```

Logstage [also has this](https://izumi.7mind.io/latest/release/doc/logstage/index.html#overview) but has an implicit mapping of the variable name as the key, which we want to avoid -- see the nblumhardt discussion on string interpolation for rationale.

It looks like scala-logging will do automatic conversion of string interpolation!

https://github.com/lightbend/Scala-Logging#string-interpolation

https://github.com/lightbend/scala-logging/blob/master/src/main/scala/com/typesafe/scalalogging/LoggerMacro.scala#L277

https://github.com/plokhotnyuk/fast-string-interpolator

https://github.com/outr/perfolation#type-safe-alternatives-to-string-format is type safe

Using the f interpolator is not what we want to do as it's very slow https://docs.scala-lang.org/overviews/core/string-interpolation.html#the-f-interpolator

https://github.com/afsalthaj/safe-string-interpolation looks pretty good 

> Being able to pass anything on to scala string interpolations might have messed up your logs, exposed your secrets, and what not! I know you hate it. We may also forget stringifying domain objects when using scala string interpolations, but stringifying it manually is a tedious job. Instead we just do toString which sometimes can spew out object hash, your valuable secrets and in fact many useless messages.

https://stackoverflow.com/questions/23504492/type-safe-string-interpolation-in-scala

Anorm does this: http://playframework.github.io/anorm/#passing-parameters

Goggles: https://github.com/kenbot/goggles

Slick sqlu interpolator: https://github.com/slick/slick/blob/master/samples/slick-plainsql/src/main/scala/Interpolation.scala

Circe: https://github.com/circe/circe/blob/master/modules/literal/src/test/scala/io/circe/literal/interpolator/JsonInterpolatorSuite.scala

https://stackoverflow.com/questions/15329027/string-interpolation-and-macro-how-to-get-the-stringcontext-and-expression-loca

https://propensive.com/opensource/contextual 

> Contextual is a small Scala library for defining your own string interpolators—prefixed string literals like url”https://propensive.com/”, which determine how they are interpreted at compile-time, including any custom checks and compile errors that should be reported, while only writing very ordinary ”user” code: no macros!
Also says:

> Note: Scala also allows the definition of string interpolators which make use of generics (i.e. accepting type parameters). Unfortunatly it’s not possible to define a generic string interpolator using Contextual, and the macro would need to be defined manually in order to achieve that.

Which brings up the idea of having a message template be more explicit about what it'll take:

```scala
val template = template[Foo, Bar]"I am a message template that has an foo=${0} bar=${1}"

val foo: Foo = Foo()
val bar: Bar = Bar()
logger.info(template(foo, bar))
```

When using structured logging, should also use `@template`  so that the structure can be reconstituted:

```json
{
  "argKey": 42,
  "@template": "This is an argument ${argKey}"
}
```

From messagetemplates:

https://messagetemplates.org/

https://nblumhardt.com/2015/01/c-6-string-interpolation-and-serilog/

https://github.com/travisbrown/type-provider-examples

https://github.com/travisbrown/expressier

https://docs.scala-lang.org/overviews/macros/typeproviders.html

