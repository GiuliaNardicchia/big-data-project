#!/usr/bin/env bash

AWS_PROFILE="giulia"

echo "Configure profile"
aws configure --profile "$AWS_PROFILE"
# AWS Access Key ID: AWS details > AWS CLI > aws_access_key_id
# AWS Secret Access Key: AWS details > AWS CLI > aws_secret_access_key
# Default region name: us-east-1
# Default output format: json

read -p "Enter AWS Session Token: " AWS_SESSION_TOKEN
aws configure set aws_session_token "$AWS_SESSION_TOKEN" --profile "$AWS_PROFILE"
# AWS Session Token: AWS details > AWS CLI > aws_session_token