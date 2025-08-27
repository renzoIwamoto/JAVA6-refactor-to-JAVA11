#!/usr/bin/env bash
set -e
docker build -t jdk6-mvn -f docker/Dockerfile.jdk6 .
docker run --rm -v "$PWD":/work -w /work jdk6-mvn mvn -q -DskipTests clean package
