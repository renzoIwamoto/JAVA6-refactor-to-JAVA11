# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Java 6 legacy application designed as a migration exercise for upgrading to Java 21. The project demonstrates typical Java 6 patterns and dependencies that need modernization:

- **Legacy Base64 encoding**: Uses `sun.misc.BASE64Encoder/Decoder` (deprecated)
- **JAX-WS SOAP services**: Uses JDK 6's built-in JAX-WS implementation
- **Old XML marshalling**: Uses JAXB from JDK 6
- **Legacy Maven configuration**: Uses older plugin versions compatible with Java 6
- **Security Manager**: Uses deprecated Java Security Manager with policy files

## Architecture

The application follows a traditional layered architecture:

- **Entry Point**: `App.java` - Main class that bootstraps security, creates sample data, and publishes SOAP endpoint
- **Service Layer**: `OrderService` - Business logic for order management
- **Repository Layer**: `OrderRepository` - In-memory data storage (simple List-based)
- **Model Layer**: `Customer` and `Order` POJOs
- **Legacy Layer**: Contains Java 6-specific implementations that need modernization:
  - `LegacyBase64` - Uses sun.misc classes
  - `LegacySoapEndpoint` - JAX-WS endpoint publishing
  - `LegacySoapService` - Web service implementation
  - `XmlMarshaller` - JAXB marshalling

## Development Commands

**Build the project:**
```bash
chmod +x scripts/*.sh
./scripts/build_java6.sh
```
This builds using Docker with JDK 6 and Maven 3.2.5.

**Run the application:**
```bash
./scripts/run_java6.sh
```
Runs with Java Security Manager and specific JVM options for Java 6.

**Test SOAP endpoint:**
```bash
./scripts/soap_test_curl.sh
```
Sends a test SOAP request to the running application.

**Manual Maven commands (if needed):**
```bash
mvn clean package -DskipTests
java -jar target/legacy-orders-java6.jar
```

## Migration Context

This codebase is specifically designed for Java 6 → Java 21 migration exercises. Key modernization areas:

1. **Base64 API**: Replace sun.misc with java.util.Base64
2. **JAX-WS**: Migrate from JDK bundled JAX-WS to standalone implementation
3. **JAXB**: Update to modern JAXB runtime
4. **Security Manager**: Remove deprecated security manager usage
5. **Maven**: Update to modern plugin versions and Java 21 target
6. **Language Features**: Adopt modern Java features (var, records, streams, etc.)

## Docker Environment

The project uses Docker to maintain Java 6 compatibility during development. The `Dockerfile.jdk6` sets up OpenJDK 6 with Maven 3.2.5 for building legacy code.

## Testing

The application includes a simple SOAP endpoint test via curl. The main application publishes a web service at `http://localhost:8080/legacy` with methods like `sayHello` and order management operations.