package com.codely.lesson_02_tests_in_spark.video_01__end_to_end_testing.service

import org.apache.spark.sql.{DataFrame, SparkSession}

case class Reader()(implicit spark: SparkSession) {
  def read(format: String, options: Map[String, String]): DataFrame = {
    spark.read
      .format(format)
      .options(options)
      .load()
  }
}
