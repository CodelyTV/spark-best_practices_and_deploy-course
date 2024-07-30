#!/bin/bash

# Detach policies from roles if they exist
detach_role_policy() {
  local role_name=$1
  local policy_arn=$2
  if aws iam get-role --role-name "$role_name" --profile emr-user > /dev/null 2>&1; then
    echo "Detaching policy $policy_arn from role $role_name"
    aws iam detach-role-policy --role-name "$role_name" --policy-arn "$policy_arn" --profile emr-user
  fi
}

detach_user_policy() {
  local user_name=$1
  local policy_arn=$2
  if aws iam get-user --user-name "$user_name" --profile emr-user > /dev/null 2>&1; then
    echo "Detaching policy $policy_arn from user $user_name"
    aws iam detach-user-policy --user-name "$user_name" --policy-arn "$policy_arn" --profile emr-user
  fi
}

# Detach policies from roles
detach_role_policy EMR_DefaultRole arn:aws:iam::010928190667:policy/EMR_S3_FullAccessPolicy
detach_role_policy EMR_EC2_DefaultRole arn:aws:iam::aws:policy/service-role/AmazonElasticMapReduceforEC2Role
detach_role_policy EMR_EC2_DefaultRole arn:aws:iam::aws:policy/AmazonS3FullAccess

# Detach policies from user
detach_user_policy emr-user arn:aws:iam::010928190667:policy/EMR_RunJobFlow_Policy
detach_user_policy emr-user arn:aws:iam::010928190667:policy/EMR_FullAccess_Policy

# Detach policies from Step Functions role
detach_role_policy EMR_StepFunctions_Role arn:aws:iam::010928190667:policy/EMR_StepFunctions_Policy

# Delete policies if they exist
delete_policy() {
  local policy_arn=$1
  if aws iam get-policy --policy-arn "$policy_arn" --profile emr-user > /dev/null 2>&1; then
    echo "Deleting policy $policy_arn"
    aws iam delete-policy --policy-arn "$policy_arn" --profile emr-user
  fi
}

delete_policy arn:aws:iam::010928190667:policy/EMR_RunJobFlow_Policy
delete_policy arn:aws:iam::010928190667:policy/EMR_FullAccess_Policy
delete_policy arn:aws:iam::010928190667:policy/EMR_StepFunctions_Policy

# Delete roles if they exist
delete_role() {
  local role_name=$1
  if aws iam get-role --role-name "$role_name" --profile emr-user > /dev/null 2>&1; then
    echo "Deleting role $role_name"
    aws iam delete-role --role-name "$role_name" --profile emr-user
  fi
}

delete_role EMR_DefaultRole
delete_role EMR_EC2_DefaultRole
delete_role EMR_StepFunctions_Role

# Delete instance profile if it exists
delete_instance_profile() {
  local profile_name=$1
  if aws iam get-instance-profile --instance-profile-name "$profile_name" --profile emr-user > /dev/null 2>&1; then
    echo "Deleting instance profile $profile_name"
    aws iam remove-role-from-instance-profile --instance-profile-name "$profile_name" --role-name EMR_EC2_DefaultRole --profile emr-user
    aws iam delete-instance-profile --instance-profile-name "$profile_name" --profile emr-user
  fi
}

delete_instance_profile EMR_EC2_DefaultRole

# Delete service-linked role for EMR if it exists
if aws iam get-role --role-name AWSServiceRoleForEMRCleanup --profile emr-user > /dev/null 2>&1; then
  echo "Deleting service-linked role AWSServiceRoleForEMRCleanup"
  aws iam delete-service-linked-role --role-name AWSServiceRoleForEMRCleanup --profile emr-user
fi

# Delete S3 bucket if it exists
if aws s3 ls s3://my-bucket-codely --profile emr-user > /dev/null 2>&1; then
  echo "Deleting S3 bucket my-bucket-codely"
  aws s3 rb s3://my-bucket-codely --force --profile emr-user
fi
