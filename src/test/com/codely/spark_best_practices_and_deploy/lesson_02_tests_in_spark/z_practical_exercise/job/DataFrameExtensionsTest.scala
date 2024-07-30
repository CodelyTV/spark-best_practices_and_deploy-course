package com.codely.spark_best_practices_and_deploy.lesson_02_tests_in_spark.z_practical_exercise.job

import com.codely.lesson_02_tests_in_spark.z_practical_exercise.extensions.DataFrameExtensions._
import org.apache.spark.sql.execution.streaming.MemoryStream
import org.apache.spark.sql.{Row, SQLContext}
import org.scalatest.matchers.should.Matchers

class DataFrameExtensionsTest extends SparkTestHelper with Matchers {

  implicit val sqlCtx: SQLContext = spark.sqlContext

  "DataFrameExtensions" should "parse JSON correctly" in {
    import testSQLImplicits._

    val events   = MemoryStream[String]
    val sessions = events.toDS

    val transformedSessions = sessions.toDF().parseJson

    val streamingQuery = transformedSessions.writeStream
      .format("memory")
      .queryName("queryName")
      .outputMode("append")
      .start()

    val offset = events.addData(DataFrameExtensionsTest.testPurchase)
    streamingQuery.processAllAvailable()
    events.commit(offset)

    val result = spark.sql("select * from queryName")
    result.show()
    result.collect().head shouldEqual Row(
      "purchase",
      "2024-06-28T14:35:00Z",
      "user456",
      "trans789",
      Array(Row("prod123", 2, "Sample product description", "Electronics", 299.99)),
      "event012"
    )
  }

  it should "add date column correctly" in {
    import testSQLImplicits._

    val events   = MemoryStream[String]
    val sessions = events.toDS

    val transformedSessions = sessions.toDF().parseJson.addDateColum

    val streamingQuery = transformedSessions.writeStream
      .format("memory")
      .queryName("queryName")
      .outputMode("append")
      .start()

    val offset = events.addData(DataFrameExtensionsTest.testPurchase)
    streamingQuery.processAllAvailable()
    events.commit(offset)

    val result = spark.sql("select * from queryName")
    result.show()
    result.collect().head.getAs[String]("date") shouldEqual "2024-06-28"
  }

  it should "explode products correctly" in {
    import testSQLImplicits._

    val events   = MemoryStream[String]
    val sessions = events.toDS

    val transformedSessions = sessions.toDF().parseJson.addDateColum.explodeProducts

    val streamingQuery = transformedSessions.writeStream
      .format("memory")
      .queryName("queryName")
      .outputMode("append")
      .start()

    val offset = events.addData(DataFrameExtensionsTest.testPurchase)
    streamingQuery.processAllAvailable()
    events.commit(offset)

    val result = spark.sql("select * from queryName")
    result.show()
    result.collect().head shouldEqual Row(
      "user456",
      Row("prod123", 2, "Sample product description", "Electronics", 299.99),
      "2024-06-28"
    )
  }

  it should "transform for aggregation correctly" in {
    import testSQLImplicits._

    val events   = MemoryStream[String]
    val sessions = events.toDS

    val transformedSessions = sessions.toDF().parseJson.addDateColum.explodeProducts.transformForAggregation

    val streamingQuery = transformedSessions.writeStream
      .format("memory")
      .queryName("queryName")
      .outputMode("append")
      .start()

    val offset = events.addData(DataFrameExtensionsTest.testPurchase)
    streamingQuery.processAllAvailable()
    events.commit(offset)

    val result = spark.sql("select * from queryName")
    result.show()
    result.collect().head shouldEqual Row("user456", "Electronics", 6, 599.98)
  }

  it should "calculate average spending correctly" in {
    import testSQLImplicits._

    val events   = MemoryStream[String]
    val sessions = events.toDS

    val transformedSessions =
      sessions.toDF().parseJson.addDateColum.explodeProducts.transformForAggregation.calculateAvgSpending

    val streamingQuery = transformedSessions.writeStream
      .format("memory")
      .queryName("queryName")
      .outputMode("complete")
      .start()

    val offset = events.addData(DataFrameExtensionsTest.testPurchase)
    streamingQuery.processAllAvailable()
    events.commit(offset)

    val result = spark.sql("select * from queryName")
    result.show()
    result.collect().head shouldEqual Row("user456", "Electronics", 6, 599.98)
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
