  spark/bin/spark-submit \
    --class com.codely.lesson_04_how_to_deploy_spark.video_01__deploy_application.DeploySparkApp \
    --deploy-mode client \
    --master spark://spark-master:7077 \
    --conf spark.sql.uris=thrift://hive-metastore:9083 \
    --conf spark.sql.extensions=io.delta.sql.DeltaSparkSessionExtension \
    --conf spark.sql.catalog.spark_catalog=org.apache.spark.sql.delta.catalog.DeltaCatalog \
    --conf spark.hadoop.fs.s3a.access.key=test \
    --conf spark.hadoop.fs.s3a.secret.key=test \
    --conf spark.hadoop.fs.s3a.endpoint=http://localstack:4566 \
    --conf spark.hadoop.fs.s3a.path.style.access=true \
    --conf spark.hadoop.fs.s3a.impl=org.apache.hadoop.fs.s3a.S3AFileSystem \
    --conf spark.driver.memory=1g \
    --conf spark.executor.memory=1g \
    --conf spark.executor.cores=1 \
    -- verbose \
    --packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.5.0,io.delta:delta-spark_2.12:3.1.0,org.apache.hadoop:hadoop-aws:3.3.1,com.amazonaws:aws-java-sdk-bundle:1.11.375 \
    spark-apps/spark-best_practises_and_deploy-course-assembly-0.1.0-SNAPSHOT.jar


