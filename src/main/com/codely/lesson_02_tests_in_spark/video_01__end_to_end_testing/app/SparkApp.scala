package com.codely.lesson_02_tests_in_spark.video_01__end_to_end_testing.app

import org.apache.spark.sql.SparkSession

trait SparkApp extends App {

  implicit val spark: SparkSession = SparkSession
    .builder()
    .enableHiveSupport()
    .getOrCreate()

}
