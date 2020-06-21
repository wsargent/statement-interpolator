/*
 * Copyright 2020 Terse Systems
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tersesystems.blindsight

final case class Statement private (
  message: String,
  arguments: Arguments,
  throwable: Option[Throwable]
) {

  def withArguments(args: Arguments): Statement = {
    copy(arguments = args)
  }
}

object Statement {
  def apply(): Statement = new Statement("", Arguments.empty, None)

  def apply(s: String): Statement = new Statement(s, Arguments.empty, None)

  def apply(s: String, varargs: Argument*): Statement = new Statement(s, new Arguments(varargs), None)

  def apply(s: String, arguments: Arguments): Statement = new Statement(s, arguments, None)

  def apply(s: String, t: Throwable): Statement = new Statement(s, Arguments.empty, throwable = Some(t))

  def apply(s: String, arguments: Arguments, t: Throwable): Statement = new Statement(s, arguments, throwable = Some(t))
}
