package com.codely.lesson_02_tests_in_spark.video_02__unit_testing.job

import com.codely.lesson_02_tests_in_spark.video_02__unit_testing.config.AppConfig
import com.codely.lesson_02_tests_in_spark.video_02__unit_testing.extensions.DataFrameExtensions._
import com.codely.lesson_02_tests_in_spark.video_02__unit_testing.service.{Reader, StreamWriter}
import org.apache.spark.sql.SparkSession

case class AvgSpendingJob(
    context: AppConfig,
    reader: Reader,
    writer: StreamWriter
)(implicit spark: SparkSession) {

  def run(): Unit = {

    val data = reader.read(context.source.format, context.source.options)

    val avgSpendingPerUserDF =
      data.parseJson.addDateColum.explodeProducts.transformForAggregation.calculateAvgSpending

    val query = writer.write(avgSpendingPerUserDF, context.sink.mode, context.sink.format, context.sink.options)

    query.awaitTermination()
  }

}
