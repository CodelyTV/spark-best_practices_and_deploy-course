package com.codely.lesson_02_tests_in_spark.video_02__unit_testing.app

import com.codely.lesson_02_tests_in_spark.video_02__unit_testing.config.AppConfig
import com.codely.lesson_02_tests_in_spark.video_02__unit_testing.job.AvgSpendingJob
import com.codely.lesson_02_tests_in_spark.video_02__unit_testing.service.{Reader, StreamWriter}
import org.apache.spark.sql.SparkSession

object AvgSpendingApp extends App {

  private val context = AppConfig.load(args)

  implicit val spark: SparkSession = SparkSession
    .builder()
    .appName(context.spark.appName)
    .enableHiveSupport()
    .getOrCreate()

  private val reader = Reader()
  private val deltaWriter = StreamWriter()

  val job = AvgSpendingJob(context, reader, deltaWriter)

  job.run()

}
