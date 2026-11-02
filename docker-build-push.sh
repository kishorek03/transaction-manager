#!/bin/bash
set -e

IMAGE_NAME="kishorek12/iyarkaivanam-backend"

if [ -z "$1" ]; then
  read -p "Enter image tag: " TAG
else
  TAG="$1"
fi

FULL_IMAGE="$IMAGE_NAME:$TAG"

# If JAR does not exist in target/, package it with Maven wrapper
if [ ! -f "target/transaction-manager-0.0.1-SNAPSHOT.jar" ]; then
  echo "JAR not found in target/. Packaging with Maven..."
  ./mvnw clean package -DskipTests
fi

echo "Building $FULL_IMAGE using target/transaction-manager-0.0.1-SNAPSHOT.jar ..."
docker build -f Dockerfile.local -t "$FULL_IMAGE" .

echo "Pushing $FULL_IMAGE ..."
docker push "$FULL_IMAGE"

echo "Done: $FULL_IMAGE"
