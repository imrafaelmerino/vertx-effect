#!/bin/bash

export TIMES=$1
export DELAY=$2
export INSTANCES=$3
export WORKERS=$4

mvn \
-Dtimes=${TIMES} \
-Ddelay=${DELAY} \
-Dinstances=${INSTANCES} \
-Dworkers=${WORKERS} \
package

DIR=${TIMES}/${DELAY}/${INSTANCES}

mkdir -p results/"$DIR"

java -Xms256m -jar \
-D"vertx.effect.enable.log.events"=false \
-Dtimes=${TIMES} \
-Ddelay=${DELAY} \
-Dinstances=${INSTANCES} \
-Dworkers=${WORKERS} \
target/benchmark.jar \
-o results/${DIR}/verticlesVsProcesses.txt \
vertx\.effect\.performance\.benchmarks\.VerticlesVsProcesses\.*


