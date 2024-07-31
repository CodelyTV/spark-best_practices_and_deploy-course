  spark/bin/spark-submit \
    --class com.codely.lesson_04_how_to_deploy_spark.video_01__deploy_application.DeploySparkApp \
    --deploy-mode client \
    --master spark://spark-master:7077 \
    --executor-memory 1G \
    --driver-memory 1G \
    --total-executor-cores 2 \
    --conf spark.sql.shuffle.partitions=2 \
    --packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.5.0 \
    --verbose \
    spark-apps/spark-best_practises_and_deploy-course-assembly-0.1.0-SNAPSHOT.jar