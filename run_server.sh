#!/bin/bash
set -eux

mvn compile exec:java -Dexec.mainClass="com.malcolmcrum.typescriptapigenerator.demo.App"