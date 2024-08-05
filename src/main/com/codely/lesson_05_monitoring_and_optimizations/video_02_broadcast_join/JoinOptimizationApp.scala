package com.codely.lesson_05_monitoring_and_optimizations.video_02_broadcast_join

import org.apache.spark.sql.functions.{broadcast, col}

object JoinOptimizationApp extends SparkApp {

  // ./bin/spark-shell --master spark://spark-master:7077 --driver-memory 4g --executor-memory 1024mb --conf spark.sql.adaptive.enabled=false

  spark.sparkContext.setLogLevel("WARN")

  import spark.implicits._

  spark.sparkContext.setJobGroup("join without optimization", "join without optimization")

  val largeDF = spark
    .range(0, 10000000L, 3) // 3.3 M
    .map(i => (i, s"Name$i"))
    .toDF("id", "name")

  val veryLargeDF = spark
    .range(0, 100000000L, 2) // 50 M
    .map(i => (i, s"Other$i"))
    .toDF("id", "other")

  veryLargeDF.join(largeDF, "id").filter(col("id") === 1).show(false)

  spark.sparkContext.clearJobGroup()

  spark.sparkContext.setJobGroup("join with optimization", "join with optimization")

  veryLargeDF.join(broadcast(largeDF), "id").show()

  spark.sparkContext.clearJobGroup()

  Thread.sleep(1000000)
}
