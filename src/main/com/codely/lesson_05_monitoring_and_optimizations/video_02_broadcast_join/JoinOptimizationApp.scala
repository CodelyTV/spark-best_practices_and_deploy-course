package com.codely.lesson_05_monitoring_and_optimizations.video_02_broadcast_join

import org.apache.spark.sql.functions.broadcast

object JoinOptimizationApp extends SparkApp {

  // ./bin/spark-shell --master spark://spark-master:7077 --driver-memory 3g --conf spark.sql.adaptive.enabled=false

  spark.sparkContext.setLogLevel("WARN")

  import spark.implicits._

  spark.sparkContext.setJobGroup("join without optimization", "join without optimization")

  val largeDF = spark
    .range(0, 10000000L) // 10M
    .map(i => (i, s"Name$i"))
    .toDF("id", "fieldA")

  val veryLargeDF = spark // 50 M
    .range(0, 50000000L)
    .map(i => (i, s"Other$i"))
    .toDF("id", "fieldB")

  veryLargeDF.join(largeDF, "id").count()

  spark.sparkContext.clearJobGroup()

  spark.sparkContext.setJobGroup("join with 12 shuffle partitions", "join with 12 shuffle partitions")

  spark.conf.set("spark.sql.shuffle.partitions", "12")

  veryLargeDF.join(largeDF, "id").count()

  spark.sparkContext.clearJobGroup()

  spark.sparkContext.setJobGroup("join with optimization", "join with optimization")

  veryLargeDF.join(broadcast(largeDF), "id").count()

  spark.sparkContext.clearJobGroup()

  Thread.sleep(1000000)
}
