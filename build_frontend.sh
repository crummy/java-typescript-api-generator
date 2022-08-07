#!/bin/bash
set -eux

# compile the code
cd frontend || exit
./node_modules/.bin/tsc