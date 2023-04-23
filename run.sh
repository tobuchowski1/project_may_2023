#!/bin/bash

mvn -f $( dirname -- "$0"; )/pom.xml exec:java
