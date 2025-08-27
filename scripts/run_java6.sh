#!/bin/bash

# Run script for Legacy Orders Java 6 project
# Uses legacy JVM flags that will fail in modern Java versions

set -e

echo "============================================================"
echo "RUNNING LEGACY ORDERS JAVA 6 APPLICATION"
echo "============================================================"

# Check if JAR file exists
JAR_FILE="target/legacy-orders-java6-1.0-SNAPSHOT.jar"
if [ ! -f "$JAR_FILE" ]; then
    echo "✗ JAR file not found: $JAR_FILE"
    echo "Run ./scripts/build_java6.sh first"
    exit 1
fi

echo "Starting Java 6 application with legacy JVM flags..."
echo ""
echo "⚠️  USING LEGACY JVM FLAGS:"
echo "   -XX:PermSize=64m          (PermGen sizing - removed in Java 8+)"
echo "   -XX:MaxPermSize=128m      (PermGen sizing - removed in Java 8+)"
echo "   -Djava.security.manager   (SecurityManager - deprecated in Java 17+)"
echo "   -Djava.security.policy    (Policy file - legacy security model)"
echo ""
echo "These flags will FAIL in modern Java versions!"
echo ""

# Run with legacy flags that demonstrate incompatibility
docker run --rm \
  -p 8080:8080 \
  -v "$(pwd)":/work \
  -w /work \
  jdk6-mvn \
  java \
  -XX:PermSize=64m \
  -XX:MaxPermSize=128m \
  -Djava.security.manager \
  -Djava.security.policy=security/legacy.policy \
  -Dfile.encoding=UTF-8 \
  -Djava.awt.headless=true \
  -jar "$JAR_FILE"

echo ""
echo "============================================================"
echo "APPLICATION STOPPED"
echo "============================================================"