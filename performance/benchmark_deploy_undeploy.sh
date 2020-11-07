#!/bin/bash


export PROCESSES=$1

mvn \
-Dtimes=1 \
-Ddelay=0 \
-Dinstances=1 \
-Dworkers=50 \
-Dprocesses=${PROCESSES} \
clean package

#DIR=${TIMES}/${DELAY}/${INSTANCES}

#mkdir -p results/"$DIR"

java -Xms256m -jar \
-Dpublish.events=false \
-Dtimes=${TIMES} \
-Ddelay=${DELAY} \
-Dinstances=${INSTANCES} \
-Dworkers=${WORKERS} \
-Dprocesses=${PROCESSES} \
target/benchmark.jar \
-o results/deployUndeploy.txt \
vertx\.effect\.performance\.benchmarks\.Processes\.*


