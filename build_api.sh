#!/bin/bash
set -eux

mvn package
mkdir -p frontend/src/api/
cp target/ts/* frontend/src/api