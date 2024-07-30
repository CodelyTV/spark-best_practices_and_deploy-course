package com.codely.lesson_02_tests_in_spark.video_01__end_to_end_testing.app

import com.codely.lesson_02_tests_in_spark.video_01__end_to_end_testing.config.AppContext
import com.codely.lesson_02_tests_in_spark.video_01__end_to_end_testing.job.AvgSpendingJob
import com.codely.lesson_02_tests_in_spark.video_01__end_to_end_testing.service.{Reader, Writer}

object AvgSpendingApp extends SparkApp {

  private val appName = "avg-spending-app"

  private val context = AppContext.load(args)

  spark.conf.set("spark.app.name", appName)

  private val reader = Reader()

  private val writer = Writer()

  private val job = AvgSpendingJob(context, reader, writer)

  job.run()
}
