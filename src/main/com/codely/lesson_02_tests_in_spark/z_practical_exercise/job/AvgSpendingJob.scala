package com.codely.lesson_02_tests_in_spark.z_practical_exercise.job

import com.codely.lesson_02_tests_in_spark.z_practical_exercise.extensions.DataFrameExtensions._
import com.codely.lesson_02_tests_in_spark.z_practical_exercise.config.AppConfig
import com.codely.lesson_02_tests_in_spark.z_practical_exercise.service.{Reader, StreamWriter}

case class AvgSpendingJob(
    context: AppConfig,
    reader: Reader,
    writer: StreamWriter
) {

  def run(): Unit = {

    val data = reader.read(context.source.format, context.source.options)

    val avgSpendingPerUserDF =
      data.parseJson.addDateColum.explodeProducts.transformForAggregation.calculateAvgSpending

    val query = writer.write(avgSpendingPerUserDF, context.sink.mode, context.sink.format, context.sink.options)

    query.awaitTermination()
  }

}
