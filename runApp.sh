#!/bin/bash

javac src/main/*.java

if [ $? -ne 0 ]; then
  echo "ERROR: Unable to compile Java"
  exit 1
fi

java -cp src main.MainMenu