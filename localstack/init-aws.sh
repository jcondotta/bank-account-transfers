#!/bin/bash
set -e

echo "🚀 Creating Kinesis stream..."

awslocal kinesis create-stream \
  --stream-name request-internal-transfers \
  --shard-count 1 &

echo "✅ Kinesis stream creation requested."
