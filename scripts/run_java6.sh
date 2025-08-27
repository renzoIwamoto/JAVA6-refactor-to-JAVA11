
#!/usr/bin/env bash
set -e
docker run --rm -v "$PWD/baseline-java6":/work -w /work jdk6-mvn java -jar target/legacy-app.jar
