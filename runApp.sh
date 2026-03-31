#!/bin/bash

mkdir -p bin

javac -cp "lib/gson-2.10.1.jar" -d bin src/main/*.java

if [ $? -ne 0 ]; then
  echo "ERROR: Unable to compile Java"
  exit 1
fi

java -cp "bin:lib/gson-2.10.1.jar" main.MainMenu