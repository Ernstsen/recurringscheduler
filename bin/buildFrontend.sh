#!/bin/bash

cd webapp/ui/admin-ui || exit
npm ci
npm run build
cd ../../../

cd webapp/ui/collector-ui || exit
npm ci
npm run build
cd ../../../

cd webapp/ui || exit
docker build -t tester .
