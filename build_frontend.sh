#!/bin/bash
set -eux

# compile the code
cd frontend || exit
./node_modules/.bin/esbuild src/App.ts --bundle --outfile=src/App.js