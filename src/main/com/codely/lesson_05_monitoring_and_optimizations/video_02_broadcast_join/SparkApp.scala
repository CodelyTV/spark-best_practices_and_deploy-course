package com.codely.lesson_05_monitoring_and_optimizations.video_02_broadcast_join

trait SparkApp extends App {
  val spark = org.apache.spark.sql.SparkSession.builder
    .master("local[*]")
    .appName("Spark Broadcast Join")
    .config("spark.sql.adaptive.enabled", "false")
    .getOrCreate()
}
