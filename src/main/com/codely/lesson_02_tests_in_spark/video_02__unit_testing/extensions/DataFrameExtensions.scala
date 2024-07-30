package com.codely.lesson_02_tests_in_spark.video_02__unit_testing.extensions

import com.codely.lesson_02_tests_in_spark.video_02__unit_testing.commons.Schemas
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{avg, col, explode, from_json, month, to_date}

object DataFrameExtensions {
  implicit class DataFrameOps(df: DataFrame) {

    def parseJson: DataFrame = {
      df.select(from_json(col("value").cast("string"), Schemas.purchasedSchema).as("value"))
        .select("value.*")
    }

    def addDateColum: DataFrame = {
      df.withColumn("date", to_date(col("timestamp"), "yyyy-MM-dd'T'HH:mm:ss'Z'"))
    }

    def explodeProducts: DataFrame = {
      df.select(
        col("userId"),
        explode(col("products")).as("product"),
        col("date")
      )
    }

    def transformForAggregation: DataFrame = {
      df.select(
        col("userId"),
        col("product.category"),
        month(col("date")).alias("month"),
        (col("product.price") * col("product.quantity")).alias("totalSpent")
      )
    }

    def calculateAvgSpending: DataFrame = {
      df.groupBy(col("userId"), col("category"), col("month"))
        .agg(avg("totalSpent").alias("AvgSpending"))
    }

    def calculateCompleteAvgSpending: DataFrame = {
      df.parseJson.addDateColum.explodeProducts.transformForAggregation.calculateAvgSpending
    }
  }
}
