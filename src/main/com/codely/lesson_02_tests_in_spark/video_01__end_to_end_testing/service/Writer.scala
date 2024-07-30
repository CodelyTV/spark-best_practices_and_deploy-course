package com.codely.lesson_02_tests_in_spark.video_01__end_to_end_testing.service

import org.apache.spark.sql.DataFrame

case class Writer() {
  def write(
      df: DataFrame,
      mode: String,
      format: String,
      path: String
  ): Unit = {
    df.write.mode(mode).format(format).save(path)
  }
}
