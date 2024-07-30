package com.codely.lesson_02_tests_in_spark.z_practical_exercise.config

import scopt.OParser

object ArgumentsParser {
  val builder = OParser.builder[CmdArgs]
  val argsParser = {
    import builder._
    OParser.sequence(
      programName("Scala Application"),
      head("Scala Application", "1.0"),
      opt[String]('c', "configFile")
        .required()
        .valueName("<file>")
        .action((x, c) => c.copy(configFile = Some(x)))
        .text("Path to the configuration file."),
      help("help").text("Prints this usage text")
    )
  }

  def parse(args: Array[String]): Option[CmdArgs] = {
    OParser.parse(argsParser, args, CmdArgs())
  }
}
