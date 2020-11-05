mvn clean package

java -Xms256m -jar -Dpublish.events=false target/benchmark.jar -o results/count_strings_multiple_verticles.txt \
vertx\.effect\.performance\.benchmarks\.VerticlesVsProcesses\.test_count_string_multi_verticles

java -Xms256m  -jar -Dpublish.events=false target/benchmark.jar -o results/count_strings_processes.txt \
vertx\.effect\.performance\.benchmarks\.VerticlesVsProcesses\.test_count_string_processes
