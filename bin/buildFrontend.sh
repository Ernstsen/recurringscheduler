#!/bin/bash

source version.properties

if [ -z "${version}" ]; then
  echo "Version is not set in version.properties"
  exit 1
fi

# shellcheck disable=SC2154
echo "Building frontend with version: ${version}"

cd webapp/ui/admin-ui || exit
npm ci
npm run build
cd ../../../

cd webapp/ui/collector-ui || exit
npm ci
npm run build
cd ../../../

cd webapp/ui || exit
docker build -t ernstsen/recurringscheduler-frontend:"${version}" .
