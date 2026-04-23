#!/bin/bash

mkdir -p bin

javac -cp "lib/gson-2.10.1.jar:test-lib/junit-platform-console-standalone-1.13.0-M3.jar" -d bin src/main/*.java src/test/*.java

if [ $? -ne 0 ]; then
	echo "ERROR: Unable to compile Java"
	exit 1
fi

java -jar "test-lib/junit-platform-console-standalone-1.13.0-M3.jar" --class-path "bin:lib/gson-2.10.1.jar" --scan-class-path
