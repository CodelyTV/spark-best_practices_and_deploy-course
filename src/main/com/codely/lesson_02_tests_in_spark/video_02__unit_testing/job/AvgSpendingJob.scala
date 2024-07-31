package com.codely.lesson_02_tests_in_spark.video_02__unit_testing.job

import com.codely.lesson_02_tests_in_spark.video_02__unit_testing.config.AppContext
import com.codely.lesson_02_tests_in_spark.video_02__unit_testing.extensions.DataFrameExtensions._
import com.codely.lesson_02_tests_in_spark.video_02__unit_testing.service.{StreamReader, StreamWriter}

case class AvgSpendingJob(
    context: AppContext,
    streamReader: StreamReader,
    streamWriter: StreamWriter
) {

  def run() = {

    val data = streamReader.read(context.source.format, context.source.options)

    val avgSpendingPerUserDF =
      data.parseJson.addDateColum.explodeProducts.transformForAggregation.calculateAvgSpending

    val query = streamWriter.write(avgSpendingPerUserDF, context.sink.mode, context.sink.format, context.sink.options)

    query.awaitTermination()
  }

}
