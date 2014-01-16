/*   __                          __                                          *\
*   / /____ ___ ____  ___  ___ _/ /       resourceparser                      *
*  / __/ -_) _ `/ _ \/ _ \/ _ `/ /        contributed by tegonal              *
*  \__/\__/\_, /\___/_//_/\_,_/_/         http://tegonal.com/                 *
*         /___/                                                               *
*                                                                             *
* This program is free software: you can redistribute it and/or modify it     *
* under the terms of the GNU Lesser General Public License as published by    *
* the Free Software Foundation, either version 3 of the License,              *
* or (at your option) any later version.                                      *
*                                                                             *
* This program is distributed in the hope that it will be useful, but         *
* WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY  *
* or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for *
* more details.                                                               *
*                                                                             *
* You should have received a copy of the GNU General Public License along     *
* with this program. If not, see http://www.gnu.org/licenses/                 *
*                                                                             *
\*                                                                           */
package com.tegonal.play.plugin.messagescompiler

import sbt.IO
import sbt._
import java.io.File
import java.nio.charset.Charset
import com.tegonal.resourceparser.generator._
import play.PlayExceptions._

object MessagesCompiler {
  def compile(src: File, options: Seq[String]): (String, Option[String], Seq[File]) = {
    try {
      val messages = scala.io.Source.fromFile(src).mkString

      val result = s"""// @SOURCE:${src.getAbsolutePath()}
                      |// @DATE:${new java.util.Date}
                      |${ResourceToScalaGenerator.generateSource(messages, "conf", "messages").get}""".stripMargin

      (result, None, Seq(src))
    } catch {
      case e: Exception =>
        e.printStackTrace
        throw new AssetCompilationException(Some(src), "Internal messages compiler error (see logs)", None, None)
    }
  }

}