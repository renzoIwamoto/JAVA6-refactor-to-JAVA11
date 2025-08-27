# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a **Legacy Java 6 Migration Demonstration Project** that showcases a working Java 6 application with dependencies and features that break in modern Java versions (11/17/21). The project serves as a realistic example of legacy enterprise applications requiring careful migration planning.

**Core Purpose**: Demonstrate Java 6 code that compiles and runs perfectly in Java 6 but fails in modern JDK versions due to removed APIs, deprecated features, and architectural changes.

## Build Commands

### Docker-based Development (Required for Java 6)
```bash
# Build Docker image and compile application
./scripts/build_java6.sh

# Run application with legacy JVM flags
./scripts/run_java6.sh

# Test SOAP endpoints
./scripts/soap_test_curl.sh
```

### Manual Docker Commands (if scripts fail)
```bash
# Build Docker image with JDK 6 + Maven 3.2.5
docker build -f docker/Dockerfile.jdk6 -t jdk6-mvn .

# Compile and package
docker run --rm -v "$(pwd)":/work -w /work jdk6-mvn mvn clean compile package -DskipTests

# Run with legacy flags
docker run --rm -p 8080:8080 -v "$(pwd)":/work -w /work jdk6-mvn java \
  -XX:PermSize=64m -XX:MaxPermSize=128m \
  -Djava.security.manager -Djava.security.policy=security/legacy.policy \
  -jar target/legacy-orders-java6-1.0-SNAPSHOT.jar
```

### Maven Commands (inside Docker container)
```bash
# Compilation
mvn clean compile

# Package (creates JAR with main class manifest)
mvn clean package -DskipTests

# Run locally (inside Java 6 environment only)
mvn exec:java -Dexec.mainClass="com.example.legacy.app.App"
```

## Architecture Overview

### Package Structure and Responsibilities

- **`com.example.legacy.app`**: Main application entry point and demonstration orchestration
- **`com.example.legacy.model`**: Domain objects (Customer, Order) with JAXB annotations
- **`com.example.legacy.repository`**: In-memory data persistence using Collections.synchronizedList
- **`com.example.legacy.service`**: Business logic layer for order management
- **`com.example.legacy.legacy`**: Contains Java 6 code that breaks in modern versions:
  - `XmlMarshaller`: Uses `javax.xml.bind` (JAXB) - removed in Java 11+
  - `LegacyBase64`: Uses `sun.misc.BASE64*` classes - encapsulated in Java 9+
  - `LegacySoapService`/`LegacySoapEndpoint`: Uses `javax.xml.ws` (JAX-WS) - removed in Java 11+
- **`com.example.legacy.security`**: SecurityManager setup using legacy security model

### Key Breaking Points

1. **JAXB (`javax.xml.bind`)**: XML marshalling/unmarshalling functionality built into Java 6 JDK but removed in Java 11+
2. **JAX-WS (`javax.xml.ws`)**: SOAP web services support built into Java 6 JDK but removed in Java 11+  
3. **sun.misc Classes**: Internal Base64 encoder/decoder accessible in Java 6 but encapsulated/removed in Java 9+
4. **SecurityManager**: Fully functional in Java 6, deprecated in Java 17, disabled by default in Java 21
5. **PermGen JVM Flags**: `-XX:PermSize`/`-XX:MaxPermSize` valid in Java 6/7, unrecognized in Java 8+

### Application Flow

1. **Security Bootstrap**: Installs SecurityManager with custom policy (`security/legacy.policy`)
2. **Order Management Demo**: Creates sample orders using layered architecture with legacy date handling
3. **JAXB XML Demo**: Marshals/unmarshals Order objects to/from XML using built-in JAXB
4. **Legacy Base64 Demo**: Encodes/decodes data using deprecated sun.misc classes
5. **SOAP Service**: Publishes JAX-WS endpoint at `http://localhost:8080/legacy` with WSDL

## Development Context

### This is NOT a Refactoring Project
- The Java 6 code is intentionally left as-is to demonstrate compatibility issues
- Do not modernize the legacy code unless explicitly requested for migration planning
- The goal is to preserve authentic Java 6 patterns that break in modern versions

### Testing the Migration Failures
- Successfully runs in Docker with OpenJDK 6
- Compilation fails in Java 11+ due to missing `javax.xml.bind` and `javax.xml.ws` packages
- Runtime fails due to unrecognized JVM flags and missing sun.misc classes
- SecurityManager warnings/failures in Java 17+

### Migration Planning Tasks
When working on Java 11+ migration:

1. **Replace JAXB**: Add `jakarta.xml.bind-api` + `jaxb-runtime` dependencies, update imports from `javax.*` to `jakarta.*`
2. **Replace JAX-WS**: Add `jaxws-rt` dependency or migrate to Spring Boot REST endpoints  
3. **Replace Base64**: Use `java.util.Base64` instead of `sun.misc.BASE64*`
4. **Remove SecurityManager**: Replace with modern security patterns (Spring Security, container-level controls)
5. **Update JVM Flags**: Remove PermGen flags, optionally add Metaspace settings
6. **Modernize Date Handling**: Replace `java.util.Date` with `java.time.*` APIs
7. **Update Collections**: Replace `Collections.synchronizedList` with `java.util.concurrent` alternatives

### Service Endpoints (when running)
- **SOAP Service**: `http://localhost:8080/legacy`  
- **WSDL**: `http://localhost:8080/legacy?wsdl`
- **Operations**: `createOrder`, `getOrder`, `getServerInfo`

## Important Files

- **`pom.xml`**: Maven 3.2.5 compatible configuration, Java 6 source/target, no external dependencies (demonstrating JDK-included APIs)
- **`docker/Dockerfile.jdk6`**: OpenJDK 6 + Maven 3.2.5 environment setup
- **`security/legacy.policy`**: SecurityManager policy file with Java 6 permissions
- **`src/main/java/com/example/legacy/app/App.java`**: Main application demonstrating all legacy features

This codebase serves as a comprehensive example of Java 6 enterprise patterns that require careful planning and testing when migrating to modern Java versions.