package com.codely.lesson_05_monitoring_and_optimizations.z_practical_exercise

trait SparkApp extends App {
  val spark = org.apache.spark.sql.SparkSession.builder
    .master("local[*]")
    .appName("Spark Broadcast Join")
    //.config("spark.sql.autoBroadcastJoinThreshold", -1) descomentar primera vez
    //.config("spark.sql.adaptive.enabled", "false")  descomentar primera vez
    .getOrCreate()
}
