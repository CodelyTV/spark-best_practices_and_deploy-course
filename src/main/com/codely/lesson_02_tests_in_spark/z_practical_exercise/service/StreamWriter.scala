package com.codely.lesson_02_tests_in_spark.z_practical_exercise.service

import org.apache.spark.sql.streaming.StreamingQuery
import org.apache.spark.sql.DataFrame

case class StreamWriter() {
  def write(
             df: DataFrame,
             mode: String,
             format: String,
             options: Map[String, String]
           ): StreamingQuery = {
    df.writeStream.outputMode(mode).format(format).options(options).start()
  }
}
