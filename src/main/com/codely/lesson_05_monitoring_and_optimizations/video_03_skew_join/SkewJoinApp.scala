package com.codely.lesson_05_monitoring_and_optimizations.video_03_skew_join

import org.apache.spark.sql.functions.when

object SkewJoinApp extends SparkApp {

  // ./bin/spark-shell --master spark://spark-master:7077  --driver-memory 4g --executor-memory 1024mb --conf spark.sql.autoBroadcastJoinThreshold=-1 --conf spark.sql.adaptive.enabled=false

  spark.sparkContext.setLogLevel("WARN")

  import spark.implicits._

  spark.sparkContext.setJobGroup("skewed data", "skewed data")

  val skewedData = spark
    .range(0, 10000000) // 10M
    .withColumn("key", when($"id" < 10, $"id").otherwise(999))
    .withColumn("value", $"id")

  val uniformData = spark
    .range(0, 1000000) // 1M
    .withColumn("key", $"id")
    .withColumn("value", $"id")

  val joined = skewedData.join(uniformData, "key")

  val res = joined.filter($"key" === 999).count()
  println(s"Count for skew key (999): $res")

  spark.sparkContext.clearJobGroup()

  spark.sparkContext.setJobGroup("adaptative query execution", "adaptative query execution")

  spark.conf.set("spark.sql.adaptive.enabled", "true")

  import org.apache.spark.sql.functions._

  val skewedDataAQE = spark
    .range(0, 10000000) // 10M
    .withColumn("key", when($"id" < 10, $"id").otherwise(999))
    .withColumn("value", $"id")

  val uniformDataAQE = spark
    .range(0, 1000000) // 1M
    .withColumn("key", $"id")
    .withColumn("value", $"id")

  val joinedAQE = skewedDataAQE.join(uniformDataAQE, "key")

  joinedAQE.explain(true)

  val resAQE = joinedAQE.filter($"key" === 999).count()
  println(s"Count for skew key (999): $resAQE")

  spark.sparkContext.clearJobGroup()

  Thread.sleep(1000000)
}
