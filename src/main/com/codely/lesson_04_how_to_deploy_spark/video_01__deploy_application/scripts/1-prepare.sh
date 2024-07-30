#!/bin/bash

sbt assembly

mv target/scala-2.12/spark-best_practises_and_deploy-course-assembly-0.1.0-SNAPSHOT.jar docker/spark/apps