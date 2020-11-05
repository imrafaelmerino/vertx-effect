#!/bin/bash

TIMES=$1
DELAY=$2
INSTANCES=$3

mvn clean package \
-Dtimes=${TIMES} \
-Ddelay=${DELAY} \
-Dinstances=${INSTANCES} \

DIR=${TIMES}/${DELAY}/${INSTANCES}

mkdir -p results/"$DIR"

java -Xms256m -jar \
-Dpublish.events=false \
-Dtimes=${TIMES} \
-Ddelay=${DELAY} \
-Dinstances=${INSTANCES} \
target/benchmark.jar \
-o results/${DIR}/verticlesVsProcesses.txt \
vertx\.effect\.performance\.benchmarks\.VerticlesVsProcesses\.*


