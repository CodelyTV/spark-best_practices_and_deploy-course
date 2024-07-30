package com.codely.spark_best_practices_and_deploy.lesson_02_tests_in_spark.video_01__end_to_end_testing.app.utils

import java.io.{File, FileWriter}
import scala.collection.mutable
import scala.io.Source

object TestUtils {

  def createTempConfFile(
      configFile: String = "application.conf",
      replacements: Map[String, String]
  ): String = {
    val content        = readApplicationConfFile(configFile)
    val updatedContent = replacePlaceholders(content, replacements)

    val tempFile = File.createTempFile("temp-application", ".conf")
    val writer   = new FileWriter(tempFile)
    try {
      writer.write(updatedContent)
    } finally {
      writer.close()
    }

    tempFile.getAbsolutePath
  }

  private def readApplicationConfFile(fileName: String): String = {
    readFileAsString(s"/$fileName")
  }

  def readFileAsString(file: String): String = {
    val fileURI = getClass.getResource(file).toURI

    val bufferedSource = Source.fromFile(fileURI)
    val lines          = new mutable.StringBuilder
    try {
      bufferedSource.getLines().foreach(line => lines.append(line).append("\n"))
    } finally {
      bufferedSource.close()
    }

    lines.toString
  }

  private def replacePlaceholders(content: String, replacements: Map[String, String]): String = {
    replacements.foldLeft(content) { case (acc, (placeholder, replacement)) =>
      acc.replace(placeholder, replacement)
    }
  }
}
