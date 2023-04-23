#!/bin/bash

mvn -f $( dirname -- "$0"; )/pom.xml clean
mvn -f $( dirname -- "$0"; )/pom.xml package