#!/bin/bash

# Start execution of Step Functions
aws stepfunctions start-execution --state-machine-arn arn:aws:states:eu-west-1:010928190667:stateMachine:EMR_StepFunctions_Machine --profile emr-user
