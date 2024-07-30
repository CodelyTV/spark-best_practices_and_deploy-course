package com.codely.lesson_02_tests_in_spark.video_01__end_to_end_testing.job

import com.codely.lesson_02_tests_in_spark.video_01__end_to_end_testing.config.AppContext
import org.apache.spark.sql.SparkSession
import com.codely.lesson_02_tests_in_spark.video_01__end_to_end_testing.extensions.DataFrameExtensions._
import com.codely.lesson_02_tests_in_spark.video_01__end_to_end_testing.service.{Reader, Writer}

case class AvgSpendingJob(
    config: AppContext,
    reader: Reader,
    writer: Writer
)(implicit spark: SparkSession) {

  def run(): Unit = {

    val data = reader.read(config.source.format, config.source.options)

    val sumByNameDataFrame = data.calculateSumByName

    writer.write(sumByNameDataFrame, config.sink.mode, config.sink.format, config.sink.path)
  }

}
