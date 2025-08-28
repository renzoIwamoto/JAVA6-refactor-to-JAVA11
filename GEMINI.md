# Project Overview

This is a Maven project written in Java 6 that serves as an example of an application with features and dependencies that are incompatible with modern JDKs (Java 11+). The project's primary purpose is to provide a codebase for practicing migration from Java 6 to a more recent version.

The application is a simple order management system that:
- Creates a customer and an order.
- Marshals the order to XML using JAXB.
- Encodes the XML to Base64.
- Publishes a SOAP web service using JAX-WS.
- Uses a SecurityManager with a custom policy file.

## Building and Running

The project includes shell scripts to build and run the application using Docker, which ensures a consistent environment with a Java 6 JDK.

**Building:**
```bash
./scripts/build_java6.sh
```

**Running:**
```bash
./scripts/run_java6.sh
```

## Development Conventions

The project uses a standard Maven project structure. The code is written in Java 6 and includes several legacy features that are not compatible with modern Java versions, such as:
- `javax.xml.bind` (JAXB)
- `javax.xml.ws` (JAX-WS)
- `sun.misc.BASE64Encoder` and `sun.misc.BASE64Decoder`
- `SecurityManager` with a policy file
- JVM flags like `-XX:PermSize` and `-XX:MaxPermSize`
