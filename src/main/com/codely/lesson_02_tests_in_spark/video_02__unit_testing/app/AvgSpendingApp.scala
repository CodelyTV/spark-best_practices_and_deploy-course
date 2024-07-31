package com.codely.lesson_02_tests_in_spark.video_02__unit_testing.app

import com.codely.lesson_02_tests_in_spark.video_02__unit_testing.config.AppContext
import com.codely.lesson_02_tests_in_spark.video_02__unit_testing.job.AvgSpendingJob
import com.codely.lesson_02_tests_in_spark.video_02__unit_testing.service.{StreamReader, StreamWriter}

object AvgSpendingApp extends SparkApp {

  private val context = AppContext.load(args)

  private val reader       = StreamReader()
  private val streamWriter = StreamWriter()

  private val job = AvgSpendingJob(context, reader, streamWriter)

  job.run()

}
