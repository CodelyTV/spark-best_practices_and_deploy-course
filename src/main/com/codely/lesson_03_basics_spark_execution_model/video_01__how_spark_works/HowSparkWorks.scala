package com.codely.lesson_03_basics_spark_execution_model.video_01__how_spark_works

object HowSparkWorks extends App {

  // 1. docker exec -it spark-ecosystem-cluster-spark-master-1 bash
  // 2. ./bin/spark-shell --master spark://spark-master:7077 --total-executor-cores 2 --executor-memory 1024m

  val spark = org.apache.spark.sql.SparkSession.builder
    .master("local")
    .appName("Spark Example")
    .getOrCreate()

  val sc      = spark.sparkContext
  val numbers = sc.parallelize(1 to 1000)
  numbers.count()

  // localhost:4040

  val doubledNumbers = numbers.map(_ * 2)
  doubledNumbers.count()

  val groupedNumbers = doubledNumbers.groupBy(_ % 2)
  groupedNumbers.count()

}
