package com.codely.spark_best_practices_and_deploy.lesson_02_tests_in_spark.video_01__end_to_end_testing.app

import com.codely.lesson_02_tests_in_spark.video_01__end_to_end_testing.app.AvgSpendingApp
import com.codely.lesson_02_tests_in_spark.video_01__end_to_end_testing.service.{Reader, Writer}
import com.codely.spark_best_practices_and_deploy.lesson_02_tests_in_spark.video_01__end_to_end_testing.app.utils.TestUtils
import com.dimafeng.testcontainers.{ForAllTestContainer, PostgreSQLContainer}

class AvgSpendingAppTest extends SparkTestHelper with ForAllTestContainer {

  val reader = new Reader
  val writer = new Writer

  override val container: PostgreSQLContainer = {
    PostgreSQLContainer().configure { c =>
      c.withInitScript("init_scripts.sql")
      c.withDatabaseName("test-database")
      c.withUsername("admin")
      c.withPassword("secret")
    }
  }

  "AvgSpendingApp" should "process messages from Kafka and write results to Delta Lake" in {

    val configFile =
      TestUtils.createTempConfFile(replacements = Map(":URL:" -> container.jdbcUrl, ":PATH:" -> tempDir))

    AvgSpendingApp.main(Array("--configFile", configFile))

    val result =
      spark.read
        .format("delta")
        .load(s"$tempDir/delta")

    import testSQLImplicits._

    val expected = Seq(("Charlie", 50), ("Bob", 20), ("Alice", 30)).toDF(
      "name",
      "total_spending"
    )

    assert(result.collect() sameElements expected.collect())
  }

}
