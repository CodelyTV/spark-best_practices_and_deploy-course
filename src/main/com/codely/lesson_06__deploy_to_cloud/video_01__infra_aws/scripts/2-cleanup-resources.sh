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
AWS_ACCOUNT_ID="CHANGE_FOR_YOUR_AWS_ACCOUNT_ID"

# Function to terminate active EMR clusters
function terminate_active_emr_clusters() {
    echo "Listing active EMR clusters"
    local cluster_ids=$(aws emr list-clusters --active --query 'Clusters[*].Id' --output text --profile $AWS_PROFILE)

    if [ -n "$cluster_ids" ]; then
        echo "Terminating active EMR clusters: $cluster_ids"
        aws emr terminate-clusters --cluster-ids $cluster_ids --profile $AWS_PROFILE
    else
        echo "No active EMR clusters found."
    fi
}

# Function to detach and delete IAM policies
function detach_and_delete_policy() {
    local role_name=$1
    local policy_name=$2

    echo "Detaching policy $policy_name from role $role_name"
    aws iam detach-role-policy --role-name $role_name --policy-arn arn:aws:iam::$AWS_ACCOUNT_ID:policy/$policy_name --profile $AWS_PROFILE

    echo "Deleting IAM policy: $policy_name"
    aws iam delete-policy --policy-arn arn:aws:iam::$AWS_ACCOUNT_ID:policy/$policy_name --profile $AWS_PROFILE
}

# Call the function to terminate active EMR clusters
terminate_active_emr_clusters

# Delete EMR Step Functions State Machine
echo "Deleting Step Functions state machine"
STATE_MACHINE_ARN=$(aws stepfunctions list-state-machines --profile $AWS_PROFILE --query "stateMachines[?name=='EMR_StepFunctions_Machine'].stateMachineArn" --output text)
if [ -n "$STATE_MACHINE_ARN" ]; then
    aws stepfunctions delete-state-machine --state-machine-arn $STATE_MACHINE_ARN --profile $AWS_PROFILE
else
    echo "State Machine not found."
fi

# Detach and delete Step Functions role and policy
detach_and_delete_policy $EMR_STEP_FUNCTIONS_ROLE $EMR_STEP_FUNCTIONS_POLICY
echo "Deleting IAM role: $EMR_STEP_FUNCTIONS_ROLE"
aws iam delete-role --role-name $EMR_STEP_FUNCTIONS_ROLE --profile $AWS_PROFILE

# Delete service-linked role for EMR
echo "Deleting service-linked role for EMR"
aws iam delete-service-linked-role --role-name AWSServiceRoleForEMRCleanup --profile $AWS_PROFILE

# Detach policies from EMR_EC2_DefaultRole
echo "Detaching policies from role $EMR_EC2_DEFAULT_ROLE"
aws iam detach-role-policy --role-name $EMR_EC2_DEFAULT_ROLE --policy-arn arn:aws:iam::aws:policy/service-role/AmazonElasticMapReduceforEC2Role --profile $AWS_PROFILE
aws iam detach-role-policy --role-name $EMR_EC2_DEFAULT_ROLE --policy-arn arn:aws:iam::aws:policy/AmazonS3FullAccess --profile $AWS_PROFILE

# Delete instance profile and role for EMR_EC2_DefaultRole
echo "Removing role from instance profile: $EMR_EC2_DEFAULT_ROLE"
aws iam remove-role-from-instance-profile --instance-profile-name $EMR_EC2_DEFAULT_ROLE --role-name $EMR_EC2_DEFAULT_ROLE --profile $AWS_PROFILE

echo "Deleting instance profile: $EMR_EC2_DEFAULT_ROLE"
aws iam delete-instance-profile --instance-profile-name $EMR_EC2_DEFAULT_ROLE --profile $AWS_PROFILE

echo "Deleting IAM role: $EMR_EC2_DEFAULT_ROLE"
aws iam delete-role --role-name $EMR_EC2_DEFAULT_ROLE --profile $AWS_PROFILE

# Detach and delete EMR_DefaultRole and policy
detach_and_delete_policy $EMR_DEFAULT_ROLE $EMR_FULL_ACCESS_POLICY
echo "Deleting IAM role: $EMR_DEFAULT_ROLE"
aws iam delete-role --role-name $EMR_DEFAULT_ROLE --profile $AWS_PROFILE

# Delete S3 bucket
echo "Deleting S3 bucket: $S3_BUCKET_NAME"
aws s3 rb s3://$S3_BUCKET_NAME --force --profile $AWS_PROFILE

echo "Cleanup complete."
