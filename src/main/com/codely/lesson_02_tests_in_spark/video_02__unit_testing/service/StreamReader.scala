package com.codely.lesson_02_tests_in_spark.video_02__unit_testing.service

import org.apache.spark.sql.{DataFrame, SparkSession}

case class StreamReader()(implicit spark: SparkSession) {
  def read(format: String, options: Map[String, String]): DataFrame = {
    spark.readStream
      .format(format)
      .options(options)
      .load()
  }
}
