package com.codely.lesson_02_tests_in_spark.video_01__end_to_end_testing.app

import com.codely.lesson_02_tests_in_spark.video_01__end_to_end_testing.config.AppContext
import com.codely.lesson_02_tests_in_spark.video_01__end_to_end_testing.job.TotalSpendingJob
import com.codely.lesson_02_tests_in_spark.video_01__end_to_end_testing.service.{Reader, Writer}

object TotalSpendingApp extends SparkApp {

  private val context = AppContext.load(args)

  private val reader = Reader()

  private val writer = Writer()

  private val job = TotalSpendingJob(context, reader, writer)

  job.run()
}
