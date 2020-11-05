mvn clean package

TIMES=10
DELAY=0
INSTANCES=1

DIR=${TIMES}/${DELAY}/${INSTANCES}

java -Xms256m -jar \
-Dpublish.events=false \
-Dtimes=${TIMES} \
-Ddelay=${DELAY} \
-Dinstances=${INSTANCES} \
target/benchmark.jar \
-o results/${DIR}/count_strings_multiple_verticles.txt \
vertx\.effect\.performance\.benchmarks\.VerticlesVsProcesses\.test_count_string_verticles

java -Xms256m -jar \
-Dpublish.events=false \
-Dtimes=${TIMES} \
-Ddelay=${DELAY} \
-Dinstances=${INSTANCES} \
-o results/${DIR}/count_strings_processes.txt \
vertx\.effect\.performance\.benchmarks\.VerticlesVsProcesses\.test_count_string_processes
