#!/bin/bash

# Build script for Legacy Orders Java 6 project
# This script builds the Docker image and compiles the Java 6 application

set -e

echo "============================================================"
echo "BUILDING LEGACY ORDERS JAVA 6 APPLICATION"
echo "============================================================"

# Build Docker image with JDK 6 and Maven 3.2.5
echo "Building Docker image with Java 6 and Maven 3.2.5..."
docker build -f docker/Dockerfile.jdk6 -t jdk6-mvn .

if [ $? -eq 0 ]; then
    echo "✓ Docker image built successfully: jdk6-mvn"
else
    echo "✗ Failed to build Docker image"
    exit 1
fi

# Compile the Java 6 application
echo ""
echo "Compiling Java 6 application with Maven..."
docker run --rm -v "$(pwd)":/work -w /work jdk6-mvn mvn -q clean compile

if [ $? -eq 0 ]; then
    echo "✓ Java 6 compilation successful"
else
    echo "✗ Java 6 compilation failed"
    exit 1
fi

# Package the application
echo ""
echo "Packaging Java 6 application..."
docker run --rm -v "$(pwd)":/work -w /work jdk6-mvn mvn -q -DskipTests package

if [ $? -eq 0 ]; then
    echo "✓ Java 6 packaging successful"
    echo "✓ JAR file created: target/legacy-orders-java6-1.0-SNAPSHOT.jar"
else
    echo "✗ Java 6 packaging failed"
    exit 1
fi

echo ""
echo "============================================================"
echo "BUILD COMPLETED SUCCESSFULLY!"
echo "============================================================"
echo "Next steps:"
echo "1. Run the application: ./scripts/run_java6.sh"
echo "2. Test SOAP service: ./scripts/soap_test_curl.sh"
echo "============================================================"