# Repository Guidelines

## Project Structure & Module Organization
- `src/main/java`: Application code (entrypoint: `com.example.legacy.app.App`).
- `src/main/resources/wsdl`: SOAP/WSDL assets.
- `security/`: Java security policy (`legacy.policy`).
- `scripts/`: Helper scripts to build/run with Docker JDK6.
- `docker/`: Dockerfiles (e.g., `Dockerfile.jdk6`).
- `target/`: Maven build outputs (e.g., `legacy-orders-java6.jar`).

## Build, Test, and Development Commands
- `mvn -q -DskipTests clean package`: Build a jar for Java 6.
- `java -Djava.security.manager -Djava.security.policy=security/legacy.policy -jar target/legacy-orders-java6.jar`: Run locally.
- `scripts/build_java6.sh`: Build using Dockerized JDK6 + Maven.
- `scripts/run_java6.sh`: Run the built jar inside Docker; exposes `:8080`.
- Example SOAP check: `scripts/soap_test_curl.sh` (posts a sample request to `http://localhost:8080/legacy`).

## Coding Style & Naming Conventions
- Indentation: 4 spaces, no tabs.
- Java 6 compatibility: avoid Java 7+ APIs and language features.
- Packages: `com.example.legacy...`; classes `CamelCase`; methods/fields `lowerCamelCase`.
- One public top-level class per file; keep classes < 300 lines where practical.

## Testing Guidelines
- Framework: Maven Surefire is configured; add tests under `src/test/java`.
- Naming: `*Test.java` (e.g., `OrderServiceTest.java`).
- Run: `mvn test` (add JUnit 4 for Java 6; consider JUnit 5 after migration to Java 11).
- Target: Prefer fast, deterministic unit tests; add simple integration checks for SOAP endpoints where feasible.

## Commit & Pull Request Guidelines
- Commits: Use Conventional Commits (e.g., `feat:`, `fix:`, `chore:`). Keep messages imperative and scoped.
- PRs: Include a clear description, linked issue, scope of changes, and how to validate (commands or curl example). Add screenshots/log excerpts when relevant.
- CI/readiness: Ensure project builds (`mvn clean package`) and tests pass before requesting review.

## Security & Configuration Tips
- Security Manager: When running locally, pass `-Djava.security.manager -Djava.security.policy=security/legacy.policy`.
- Ports: Default HTTP port `8080` (see `scripts/run_java6.sh`).
