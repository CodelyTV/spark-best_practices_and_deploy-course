package com.codely.spark_best_practices_and_deploy.lesson_02_tests_in_spark.video_02__unit_testing.scala.job

import com.codely.lesson_02_tests_in_spark.video_02__unit_testing.extensions.DataFrameExtensions._
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.execution.streaming.MemoryStream

class DataFrameExtensionsTest extends SparkTestHelper {

  "DataFrameExtensionsTest" should "calculate average spending correctly" in {

    import testSQLImplicits._
    implicit val sqlCtx: SQLContext = spark.sqlContext

    val events   = MemoryStream[String]
    val sessions = events.toDS
    assert(sessions.isStreaming, "sessions must be a streaming Dataset")

    val transformedSessions =
      sessions.toDF().calculateCompleteAvgSpending

    val streamingQuery = transformedSessions.writeStream
      .format("memory")
      .queryName("queryName")
      .outputMode("complete")
      .start

    val offset = events.addData(DataFrameExtensionsTest.testPurchase)

    streamingQuery.processAllAvailable()
    events.commit(offset)

    val result = spark.sql("select * from queryName")
    result.show()
    assert(
      result.collect().head === Row("user456", "Electronics", 6, 599.98)
    )
  }
}

object DataFrameExtensionsTest {

  val testPurchase: String =
    """
      |{
      |  "eventType": "purchase",
      |  "timestamp": "2024-06-28T14:35:00Z",
      |  "userId": "user456",
      |  "transactionId": "trans789",
      |  "products": [
      |    {
      |      "productId": "prod123",
      |      "quantity": 2,
      |      "description": "Sample product description",
      |      "category": "Electronics",
      |      "price": 299.99
      |    }
      |  ],
      |  "eventId": "event012"
      |}
      |""".stripMargin
}
