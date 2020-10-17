java -Xms256m  -jar target/benchmark.jar -o count_strings_one_verticle.txt \
actors\.performance\.benchmarks\.MyBenchmark\.testCountStringOneVerticle

java -Xms256m  -jar target/benchmark.jar -o count_strings_multiple_verticles.txt \
actors\.performance\.benchmarks\.MyBenchmark\.testCountStringMultiVerticle

java -Xms256m  -jar target/benchmark.jar -o count_strings_processes.txt \
actors\.performance\.benchmarks\.MyBenchmark\.testCountStringProcesses

