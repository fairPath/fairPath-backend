#!/usr/bin/env bash
set -euo pipefail

export AWS_PROFILE="fairpath"
export AWS_REGION="us-east-2"
export AWS_S3_RESUME_BUCKET="fairpath-resumes-dev"

./gradlew bootRun