#!/bin/bash

# Create S3 bucket
aws s3 mb s3://my-bucket-codely --profile emr-user

# Create EMR-S3-FullAccessPolicy
aws iam create-policy --policy-name EMR_FullAccessPolicy --policy-document file://EMR_FullAccessPolicy.json --profile emr-user

# Create EMR_DefaultRole
aws iam create-role --role-name EMR_DefaultRole --assume-role-policy-document file://EMR_DefaultRoleTrustPolicy.json --profile emr-user

# Attach EMR_FullAccessPolicy to EMR_DefaultRole
aws iam attach-role-policy --role-name EMR_DefaultRole --policy-arn arn:aws:iam::010928190667:policy/EMR_FullAccessPolicy --profile emr-user

# Create EMR_EC2_DefaultRole
aws iam create-role --role-name EMR_EC2_DefaultRole --assume-role-policy-document file://EMR_EC2_DefaultRoleTrustPolicy.json --profile emr-user

# Create instance profile and add role
aws iam create-instance-profile --instance-profile-name EMR_EC2_DefaultRole --profile emr-user
aws iam add-role-to-instance-profile --instance-profile-name EMR_EC2_DefaultRole --role-name EMR_EC2_DefaultRole --profile emr-user

# Attach policies to EMR_EC2_DefaultRole
aws iam attach-role-policy --role-name EMR_EC2_DefaultRole --policy-arn arn:aws:iam::aws:policy/service-role/AmazonElasticMapReduceforEC2Role --profile emr-user
aws iam attach-role-policy --role-name EMR_EC2_DefaultRole --policy-arn arn:aws:iam::aws:policy/AmazonS3FullAccess --profile emr-user

# Create Step Functions policy
aws iam create-policy --policy-name EMR_StepFunctions_Policy --policy-document file://EMR_StepFunctions_Policy.json --profile emr-user

# Create Step Functions role and attach policy
aws iam create-role --role-name EMR_StepFunctions_Role --assume-role-policy-document file://StepFunctionsTrustPolicy.json --profile emr-user
aws iam attach-role-policy --role-name EMR_StepFunctions_Role --policy-arn arn:aws:iam::010928190667:policy/EMR_StepFunctions_Policy --profile emr-user

# Create service-linked role for EMR
aws iam create-service-linked-role --aws-service-name elasticmapreduce.amazonaws.com --description "Role for EMR cleanup tasks" --profile emr-user

# Create EMR cluster using Step Functions
aws stepfunctions create-state-machine --name "EMR_StepFunctions_Machine" --definition file://state_machine_definition.json --role-arn arn:aws:iam::010928190667:role/EMR_StepFunctions_Role --profile emr-user

# Start execution of Step Functions
# aws stepfunctions start-execution --state-machine-arn arn:aws:states:eu-west-1:010928190667:stateMachine:EMR_StepFunctions_Machine --profile emr-user
