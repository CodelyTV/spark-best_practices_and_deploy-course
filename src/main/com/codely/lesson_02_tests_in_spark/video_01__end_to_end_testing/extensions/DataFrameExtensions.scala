package com.codely.lesson_02_tests_in_spark.video_01__end_to_end_testing.extensions

import org.apache.spark.sql.DataFrame

object DataFrameExtensions {
  implicit class DataFrameOps(df: DataFrame) {
    def calculateSumByName: DataFrame = {
      df.groupBy("name")
        .sum("value")
        .withColumnRenamed("sum(value)", "total_spending")
    }
  }
}
