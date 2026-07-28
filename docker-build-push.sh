#!/bin/bash
set -e

IMAGE_NAME="kishorek12/iyarkaivanam-backend"

if [ -z "$1" ]; then
  read -p "Enter image tag: " TAG
else
  TAG="$1"
fi

FULL_IMAGE="$IMAGE_NAME:$TAG"

echo "Building $FULL_IMAGE ..."
docker build -f Dockerfile -t "$FULL_IMAGE" .

echo "Pushing $FULL_IMAGE ..."
docker push "$FULL_IMAGE"

echo "Done: $FULL_IMAGE"
