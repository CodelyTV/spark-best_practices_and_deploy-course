package com.codely.lesson_05_monitoring_and_optimizations.video_03_skew_join

import org.apache.spark.sql.functions.when

object SkewJoinApp extends SparkApp {

  // ./bin/spark-shell --master spark://spark-master:7077  --driver-memory 3g --executor-memory 1024mb --conf spark.sql.autoBroadcastJoinThreshold=-1 --conf spark.sql.adaptive.enabled=false

  spark.sparkContext.setLogLevel("WARN")

  import spark.implicits._

  spark.sparkContext.setJobGroup("skewed data", "skewed data")

  val uniformData = spark
    .range(0, 10000000) // 10M
    .withColumn("key", $"id")
    .withColumn("value", $"id")

  val skewedData = spark
    .range(0, 200000000) // 200M
    .withColumn("key", when($"id" < 10000000, $"id").otherwise(999))
    .withColumn("value", $"id")

  skewedData.join(uniformData, "key").count()

  spark.sparkContext.clearJobGroup()

  spark.sparkContext.setJobGroup("adaptative query execution", "adaptative query execution")

  spark.conf.set("spark.sql.adaptive.enabled", "true")
  spark.conf.set("spark.sql.adaptive.skewJoin.skewedPartitionFactor", "1")
  spark.conf.set("spark.sql.adaptive.skewJoin.skewedPartitionThresholdInBytes", "20MB")
  spark.conf.set("spark.sql.adaptive.advisoryPartitionSizeInBytes", "15MB")

  val joinedAQE = skewedData.join(uniformData, "key")

  joinedAQE.explain(true)

  joinedAQE.count()

  spark.sparkContext.clearJobGroup()

  Thread.sleep(1000000)
}
