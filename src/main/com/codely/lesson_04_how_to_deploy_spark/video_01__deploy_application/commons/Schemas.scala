package com.codely.lesson_04_how_to_deploy_spark.video_01__deploy_application.commons

object Schemas {

  import org.apache.spark.sql.types._

  private val productType = new StructType()
    .add("productId", StringType)
    .add("quantity", IntegerType)
    .add("description", StringType)
    .add("category", StringType)
    .add("price", DoubleType)

  val purchasedSchema: StructType = new StructType()
    .add("eventType", StringType)
    .add("timestamp", StringType)
    .add("userId", StringType)
    .add("transactionId", StringType)
    .add("products", ArrayType(productType))
    .add("eventId", StringType)

  val viewedSchema: StructType = new StructType()
    .add("eventType", StringType)
    .add("timestamp", StringType)
    .add("userId", StringType)
    .add("productId", StringType)
    .add("eventId", StringType)

}
