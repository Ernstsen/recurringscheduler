#!/bin/bash

source version.properties

if [ -z "${version}" ]; then
  echo "Version is not set in version.properties"
  exit 1
fi

if [[ $version == *"SNAPSHOT"* ]]; then
  echo "Unable to release a snapshot version!"
  exit 2
fi

bin/buildApplication.sh

docker tag ernstsen/recurringscheduler-backend:"${version}" ernstsen/recurringscheduler-backend:latest
docker tag ernstsen/recurringscheduler-frontend:"${version}" ernstsen/recurringscheduler-frontend:latest

docker push ernstsen/recurringscheduler-backend:"${version}"
docker push ernstsen/recurringscheduler-backend:latest
docker push ernstsen/recurringscheduler-frontend:"${version}"
docker push ernstsen/recurringscheduler-frontend:latest

git tag -a "${version}" -m "Release version ${version}"
git push && git push origin "${version}"
