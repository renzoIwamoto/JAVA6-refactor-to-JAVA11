#!/bin/bash
set -euo pipefail

echo "============================================================"
echo "BUILDING LEGACY ORDERS JAVA 6 APPLICATION"
echo "============================================================"

# Build Docker image with Zulu JDK 6 and Maven 3.2.5
echo "Building Docker image with Java 6 (Zulu) and Maven 3.2.5..."
docker build -f docker/Dockerfile.jdk6 -t jdk6-mvn .
echo "✓ Docker image built successfully: jdk6-mvn"

echo ""
echo "Compiling & packaging Java 6 application with Maven (using insecure HTTP mirror)..."

# Use custom settings to avoid TLS issues with Java 6
MSYS_NO_PATHCONV=1 docker run --rm \
  -v "$(pwd)":/work \
  -w /work \
  jdk6-mvn \
  bash -lc 'mvn -q clean package -DskipTests -s maven-settings.xml'

# Verificar si el JAR fue creado
JAR_PATH="target/legacy-orders-java6-1.0-SNAPSHOT.jar"
if [[ -f "$JAR_PATH" ]]; then
  echo "✓ Build successful"
  echo "✓ JAR file created: $JAR_PATH"
else
  echo "✗ Packaging failed o el JAR no fue generado: $JAR_PATH"
  echo "Mostrando archivos en target/ para diagnóstico:"
  ls -l target/ || true
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
