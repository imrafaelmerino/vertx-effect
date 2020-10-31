java -Xms256m -jar -Dpublish.events=false target/benchmark.jar -o results/24-Oct-2020/count_strings_multiple_verticles.txt \
vertx\.effect\.performance\.benchmarks\.MyBenchmark\.testCountStringMultiVerticle

java -Xms256m  -jar -Dpublish.events=false target/benchmark.jar -o results/24-Oct-2020/count_strings_processes.txt \
vertx\.effect\.performance\.benchmarks\.MyBenchmark\.testCountStringProcesses
