package com.codely.lesson_02_tests_in_spark.video_01__end_to_end_testing.app

import com.codely.lesson_02_tests_in_spark.video_01__end_to_end_testing.config.AppConfig
import com.codely.lesson_02_tests_in_spark.video_01__end_to_end_testing.job.AvgSpendingJob
import com.codely.lesson_02_tests_in_spark.video_01__end_to_end_testing.service.{Reader, Writer}
import org.apache.spark.sql.SparkSession

object AvgSpendingApp extends App {

  private val appName = "avg-spending-app"

  private val context = AppConfig.load(args, appName)

  implicit val spark: SparkSession = SparkSession
    .builder()
    .appName(context.spark.appName)
    .enableHiveSupport()
    .getOrCreate()

  private val reader      = Reader()
  private val deltaWriter = Writer()

  val job = AvgSpendingJob(context, reader, deltaWriter)

  job.run()

}
