package com.codely.lesson_03_basics_spark_execution_model.z_practical_exercise

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.col

object QueryPlanExercise extends App {

  val spark = SparkSession
    .builder()
    .appName("AvgSpendingCalculation")
    .master("local[*]")
    .getOrCreate()

  val filePath =
    "src/main/com/codely/lesson_03_basics_spark_execution_model/z_practical_exercise/data/some_csv.csv"

  val rawData = spark.read.option("header", "true").csv(filePath)

  val filteredData =
    rawData.filter(col("colA") === 1).selectExpr("upper(colB) as colB")

  filteredData.explain()

  filteredData.show()

}
