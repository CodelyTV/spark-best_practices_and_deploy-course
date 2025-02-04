package com.codely.spark_best_practices_and_deploy.lesson_02_tests_in_spark.video_01__end_to_end_testing.app

import org.apache.commons.io.FileUtils
import org.apache.spark.sql.{SQLContext, SQLImplicits, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}

import java.io.File
import java.nio.file.Files
import scala.reflect.io.Directory

trait SparkTestHelper extends AnyFlatSpec with BeforeAndAfterEach with BeforeAndAfterAll {

  private val sparkSession = SparkSession
    .builder()
    .master("local[*]")
    .appName("test-spark-session")
    .config(sparkConfiguration)
    .enableHiveSupport()
    .getOrCreate()

  protected var tempDir: String = _

  protected implicit def spark: SparkSession = sparkSession

  protected def sc: SparkContext = sparkSession.sparkContext

  protected def sparkConfiguration: SparkConf =
    new SparkConf()
      .set("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
      .set(
        "spark.sql.catalog.spark_catalog",
        "org.apache.spark.sql.delta.catalog.DeltaCatalog"
      )

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    clearTemporaryDirectories()
  }

  override protected def beforeEach(): Unit = {
    super.beforeEach()
    tempDir = Files.createTempDirectory(this.getClass.toString).toString.replaceAll("\\s", "")
  }

  override protected def afterAll(): Unit = {
    super.afterAll()
    sparkSession.stop()
    clearTemporaryDirectories()
  }

  override protected def afterEach(): Unit = {
    super.afterEach()
    new Directory(new File(tempDir)).deleteRecursively()
  }

  protected object testSQLImplicits extends SQLImplicits {
    protected override def _sqlContext: SQLContext = sparkSession.sqlContext
  }

  private def clearTemporaryDirectories(): Unit = {
    val warehousePath = new File("spark-warehouse").getAbsolutePath
    FileUtils.deleteDirectory(new File(warehousePath))

    val metastoreDbPath = new File("metastore_db").getAbsolutePath
    FileUtils.deleteDirectory(new File(metastoreDbPath))
  }
}
