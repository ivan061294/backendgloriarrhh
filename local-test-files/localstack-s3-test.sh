#!/bin/bash

S3_BUCKET="filesbucket"
S3_TRIGGER_CONFIG="$(pwd)/local-test-files/s3-trigger-config.json"
LAMBDA_JAR="$(pwd)/target/human-management-api-1.0.0.jar"
EXAMPLE_FILE_PATH="$(pwd)/local-test-files/s3-event-body.json"

# Deploy localstack
docker-compose -f src/main/docker/localstack.yml up -d

# Compile your code, use -Pprod for production
mvn clean package

# Create a function
aws lambda create-function \
  --endpoint-url http://localhost:4566 \
  --function-name LoadData \
  --runtime java11 \
  --handler pe.com.centro.modules.load.LoadData \
  --region us-east-2 \
  --zip-file "fileb://${LAMBDA_JAR}" \
  --role arn:aws:iam::12345:role/ignoreme

# Create a S3 bucket
aws --endpoint-url http://localhost:4566 s3 mb "s3://${S3_BUCKET}"

# Create a S3 put event trigger
aws s3api --endpoint-url http://localhost:4566 \
  put-bucket-notification-configuration \
  --bucket $S3_BUCKET \
  --notification-configuration "file://${S3_TRIGGER_CONFIG}"

# Upload a file
aws --endpoint-url http://localhost:4566 s3 \
  cp "$EXAMPLE_FILE_PATH" s3://filesbucket

# Delete a function
#aws lambda delete-function \
#  --endpoint-url http://localhost:4566 \
#  --function-name LoadData \
#  --region us-east-2

# To test using SAM with a template `s3-event-body.json`
sam deployer invoke LoadData -e "$EXAMPLE_FILE_PATH"
