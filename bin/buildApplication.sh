#!/bin/bash

source version.properties

if [ -z "${version}" ]; then
  echo "Version is not set in version.properties"
  exit 1
fi

bin/buildFrontend.sh

echo "Building backend"
dir=$(pwd)
cd webapp/recurringscheduler || exit
./gradlew clean build imageBuild
cd "${dir}" || exit

echo "Finished building application with version: ${version}"
