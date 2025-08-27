#!/usr/bin/env bash
set -e
docker run --rm -p 8080:8080 -v "$PWD":/work -w /work jdk6-mvn java   -XX:PermSize=64m -XX:MaxPermSize=128m   -Djava.security.manager   -Djava.security.policy=security/legacy.policy   -jar target/legacy-orders-java6.jar
