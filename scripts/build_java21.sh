
#!/usr/bin/env bash
set -e
pushd baseline-java6 >/dev/null
mvn -q -DskipTests clean package
popd >/dev/null
