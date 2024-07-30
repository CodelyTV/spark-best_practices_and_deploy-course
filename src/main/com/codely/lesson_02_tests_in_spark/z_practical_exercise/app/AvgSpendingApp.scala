package com.codely.lesson_02_tests_in_spark.z_practical_exercise.app

import com.codely.lesson_02_tests_in_spark.z_practical_exercise.config.AppConfig
import com.codely.lesson_02_tests_in_spark.z_practical_exercise.job.AvgSpendingJob
import com.codely.lesson_02_tests_in_spark.z_practical_exercise.service.{Reader, StreamWriter}
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
