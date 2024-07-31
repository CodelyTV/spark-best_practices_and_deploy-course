package com.codely.lesson_03_basics_spark_execution_model.video_02__reading_query_plans

object QueryPlans extends App {

  val spark = org.apache.spark.sql.SparkSession.builder
    .master("local[*]")
    .appName("Spark Query Plans")
    .getOrCreate()

  spark.sparkContext.setLogLevel("WARN")

  val rangeDs = spark.range(1000)

  rangeDs.explain()

  val mappedDs = rangeDs.selectExpr("id * 2 as id")

  mappedDs.explain(extended = true)

  val mappedAnfFilteredDs = rangeDs.selectExpr("id * 2 as id").filter("id = 2")

  mappedAnfFilteredDs.explain(extended = true)

  val bigRangeDs = spark.range(2000000000)

  val anotherBigDs = spark.range(2000000000)

  val joinedBigDs = bigRangeDs.join(anotherBigDs, "id")

  joinedBigDs.explain(extended = true)

}
