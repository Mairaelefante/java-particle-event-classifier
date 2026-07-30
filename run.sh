#!/usr/bin/env bash
set -e
mkdir -p build/classes
find src/main/java -name '*.java' > build/sources.txt
javac -d build/classes @build/sources.txt
java -cp build/classes physicsml.Main
