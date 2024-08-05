package com.codely.lesson_02_tests_in_spark.video_02__unit_testing.config

import com.typesafe.config.ConfigFactory

import java.io.File

case class AppContext(
    spark: SparkConfig,
    source: SourceConfig,
    sink: SinkConfig
)

case class SparkConfig(appName: String)
case class SourceConfig(format: String, options: Map[String, String])
case class SinkConfig(format: String, mode: String, options: Map[String, String])

object AppContext {
  def load(args: Array[String]): AppContext = {

    val cmdArgs    = ArgumentsParser.parse(args).getOrElse(CmdArgs())
    val configFile = new File(cmdArgs.configFile.get)
    val config     = ConfigFactory.parseFile(configFile)

    val sourceOptions = Map(
      "kafka.bootstrap.servers" -> config.getString("source.options.server"),
      "startingOffsets"         -> config.getString("source.options.startingOffsets"),
      "subscribe"               -> config.getString("source.options.subscribe")
    )

    val sinkOptions = Map(
      "path"       -> config.getString("sink.options.path"),
      "checkpoint" -> config.getString("sink.options.checkpoint")
    )

    AppContext(
      spark = SparkConfig(
        appName = config.getString("spark.appName")
      ),
      source = SourceConfig(
        format = config.getString("source.format"),
        options = sourceOptions
      ),
      sink = SinkConfig(
        format = config.getString("sink.format"),
        mode = config.getString("sink.mode"),
        options = sinkOptions
      )
    )
  }
}
