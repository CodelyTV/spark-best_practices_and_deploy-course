import sbt._

object Dependencies {
  private val prod = Seq(
    "com.github.nscala-time" %% "nscala-time"          % "2.34.0",
    "com.lihaoyi"            %% "pprint"               % "0.9.0",
    "org.apache.spark"       %% "spark-core"           % "3.5.5" % Provided,
    "org.apache.spark"       %% "spark-sql"            % "3.5.5" % Provided,
    "org.apache.spark"       %% "spark-streaming"      % "3.5.5" % Provided,
    "org.apache.spark"       %% "spark-hive"           % "3.5.5" % Provided,
    "org.apache.hadoop"       % "hadoop-aws"           % "3.2.2" % Provided,
    "io.delta"               %% "delta-spark"          % "3.1.0" % Provided,
    "org.apache.spark"       %% "spark-sql-kafka-0-10" % "3.5.5",
    "com.typesafe"            % "config"               % "1.4.3",
    "com.github.scopt"       %% "scopt"                % "4.1.0"
  )
  private val test = Seq(
    "org.scalatest" %% "scalatest"                       % "3.2.19",
    "org.mockito"   %% "mockito-scala"                   % "1.17.37",
    "com.dimafeng"  %% "testcontainers-scala"            % "0.41.4",
    "com.dimafeng"  %% "testcontainers-scala-postgresql" % "0.41.4",
    "org.postgresql" % "postgresql"                      % "42.7.4"
  ).map(_ % Test)

  val all: Seq[ModuleID] = prod ++ test
}
