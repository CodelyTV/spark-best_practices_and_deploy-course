package com.codely.lesson_02_tests_in_spark.video_01__end_to_end_testing.config

import com.typesafe.config.ConfigFactory

import java.io.File

case class AppConfig(
    spark: SparkConfig,
    source: SourceConfig,
    sink: SinkConfig
)

case class SparkConfig(appName: String)
case class SourceConfig(format: String, options: Map[String, String])
case class SinkConfig(format: String, mode: String, path: String)

object AppConfig {
  def load(args: Array[String], appName: String): AppConfig = {

    val cmdArgs    = ArgumentsParser.parse(args).getOrElse(CmdArgs())
    val configFile = new File(cmdArgs.configFile.get)
    val config     = ConfigFactory.parseFile(configFile)

    val sourceOptions = Map(
      "url"      -> config.getString("source.options.url"),
      "user"     -> config.getString("source.options.user"),
      "password" -> config.getString("source.options.password"),
      "dbtable"  -> config.getString("source.options.dbtable"),
      "driver"   -> config.getString("source.options.driver")
    )

    AppConfig(
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
        path = config.getString("sink.path")
      )
    )
  }
}
