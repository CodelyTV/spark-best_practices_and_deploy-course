package com.codely.lesson_02_tests_in_spark.z_practical_exercise.service

import org.apache.spark.sql.{DataFrame, SparkSession}

case class Reader()(implicit spark: SparkSession) {
  def read(format: String, options: Map[String, String]): DataFrame = {
    spark.read
      .format(format)
      .options(options)
      .load()
  }
}
