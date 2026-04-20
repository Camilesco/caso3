#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
mkdir -p out
javac -d out src/caso3/*.java
java -cp out caso3.Main "${1:-config/config_ejemplo.txt}"
