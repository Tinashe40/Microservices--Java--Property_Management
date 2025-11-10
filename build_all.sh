#!/bin/bash
for d in */ ; do
  if [ -f "$d/pom.xml" ]; then
    echo "Building $d"
    (cd "$d" && mvn clean install)
  fi

done
