#!/bin/bash

# Define the profile to be used for AWS CLI commands
AWS_PROFILE="emr-user"

# Define the bucket name
S3_BUCKET_NAME="my-bucket-codely"

# Define IAM policies and roles names
EMR_FULL_ACCESS_POLICY="EMR_FullAccessPolicy"
EMR_DEFAULT_ROLE="EMR_DefaultRole"
EMR_EC2_DEFAULT_ROLE="EMR_EC2_DefaultRole"
EMR_STEP_FUNCTIONS_POLICY="EMR_StepFunctions_Policy"
EMR_STEP_FUNCTIONS_ROLE="EMR_StepFunctions_Role"

# Define AWS Account ID
AWS_ACCOUNT_ID="010928190667"

# Create S3 bucket
echo "Creating S3 bucket: $S3_BUCKET_NAME"
aws s3 mb s3://$S3_BUCKET_NAME --profile $AWS_PROFILE

# Create EMR-FullAccessPolicy
echo "Creating IAM policy: $EMR_FULL_ACCESS_POLICY"
aws iam create-policy --policy-name $EMR_FULL_ACCESS_POLICY --policy-document file://policies/EMR_FullAccessPolicy.json --profile $AWS_PROFILE

# Create EMR_DefaultRole
echo "Creating IAM role: $EMR_DEFAULT_ROLE"
aws iam create-role --role-name $EMR_DEFAULT_ROLE --assume-role-policy-document file://policies/EMR_DefaultRoleTrustPolicy.json --profile $AWS_PROFILE

# Attach EMR_FullAccessPolicy to EMR_DefaultRole
echo "Attaching policy $EMR_FULL_ACCESS_POLICY to role $EMR_DEFAULT_ROLE"
aws iam attach-role-policy --role-name $EMR_DEFAULT_ROLE --policy-arn arn:aws:iam::$AWS_ACCOUNT_ID:policy/$EMR_FULL_ACCESS_POLICY --profile $AWS_PROFILE

# Create EMR_EC2_DefaultRole
echo "Creating IAM role: $EMR_EC2_DEFAULT_ROLE"
aws iam create-role --role-name $EMR_EC2_DEFAULT_ROLE --assume-role-policy-document file://policies/EMR_EC2_DefaultRoleTrustPolicy.json --profile $AWS_PROFILE

# Create instance profile and add role
echo "Creating instance profile and adding role: $EMR_EC2_DEFAULT_ROLE"
aws iam create-instance-profile --instance-profile-name $EMR_EC2_DEFAULT_ROLE --profile $AWS_PROFILE
aws iam add-role-to-instance-profile --instance-profile-name $EMR_EC2_DEFAULT_ROLE --role-name $EMR_EC2_DEFAULT_ROLE --profile $AWS_PROFILE

# Attach policies to EMR_EC2_DefaultRole
echo "Attaching policies to role $EMR_EC2_DEFAULT_ROLE"
aws iam attach-role-policy --role-name $EMR_EC2_DEFAULT_ROLE --policy-arn arn:aws:iam::aws:policy/service-role/AmazonElasticMapReduceforEC2Role --profile $AWS_PROFILE
aws iam attach-role-policy --role-name $EMR_EC2_DEFAULT_ROLE --policy-arn arn:aws:iam::aws:policy/AmazonS3FullAccess --profile $AWS_PROFILE

# Create Step Functions policy
echo "Creating IAM policy: $EMR_STEP_FUNCTIONS_POLICY"
aws iam create-policy --policy-name $EMR_STEP_FUNCTIONS_POLICY --policy-document file://policies/EMR_StepFunctions_Policy.json --profile $AWS_PROFILE

# Create Step Functions role and attach policy
echo "Creating IAM role: $EMR_STEP_FUNCTIONS_ROLE"
aws iam create-role --role-name $EMR_STEP_FUNCTIONS_ROLE --assume-role-policy-document file://policies/StepFunctionsTrustPolicy.json --profile $AWS_PROFILE
aws iam attach-role-policy --role-name $EMR_STEP_FUNCTIONS_ROLE --policy-arn arn:aws:iam::$AWS_ACCOUNT_ID:policy/$EMR_STEP_FUNCTIONS_POLICY --profile $AWS_PROFILE

# Create service-linked role for EMR
echo "Creating service-linked role for EMR"
aws iam create-service-linked-role --aws-service-name elasticmapreduce.amazonaws.com --description "Role for EMR cleanup tasks" --profile $AWS_PROFILE

# Create EMR cluster using Step Functions
echo "Creating Step Functions state machine for EMR cluster"
aws stepfunctions create-state-machine --name "EMR_StepFunctions_Machine" --definition file://policies/state_machine_definition.json --role-arn arn:aws:iam::$AWS_ACCOUNT_ID:role/$EMR_STEP_FUNCTIONS_ROLE --profile $AWS_PROFILE

echo "Setup complete."
