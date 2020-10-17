#java -Xms256m  -jar target/benchmark.jar -o parsing_with_JsValues.txt \
#actors\.performance\.benchmarks\.JacksonVsJsValuesBenchmark\.parsing_with_JsValues
#
#java -Xms256m  -jar target/benchmark.jar -o parsing_with_Jackson.txt \
#actors\.performance\.benchmarks\.JacksonVsJsValuesBenchmark\.parsing_with_Jackson
#
java -Xms256m  -jar target/benchmark.jar -v extra -o test_1_steps_JsValues.txt \
actors\.performance\.benchmarks\.JacksonVsJsValuesBenchmark\.test_1_steps_JsValues

java -Xms256m  -jar target/benchmark.jar -v extra  -o test_1_steps_Jackson.txt \
actors\.performance\.benchmarks\.JacksonVsJsValuesBenchmark\.test_1_steps_Jackson
#
#java -Xms256m  -jar target/benchmark.jar -o test_2_steps_JsValues.txt \
#actors\.performance\.benchmarks\.JacksonVsJsValuesBenchmark\.test_2_steps_JsValues
#
#java -Xms256m  -jar target/benchmark.jar -o test_3_steps_JsValues.txt \
#actors\.performance\.benchmarks\.JacksonVsJsValuesBenchmark\.test_3_steps_JsValues
#
#java -Xms256m  -jar target/benchmark.jar -o test_4_steps_JsValues.txt \
#actors\.performance\.benchmarks\.JacksonVsJsValuesBenchmark\.test_4_steps_JsValues
#
#java -Xms256m  -jar target/benchmark.jar -o test_5_steps_JsValues.txt \
#actors\.performance\.benchmarks\.JacksonVsJsValuesBenchmark\.test_5_steps_JsValues
#
#java -Xms256m  -jar target/benchmark.jar -o test_6_steps_JsValues.txt \
#actors\.performance\.benchmarks\.JacksonVsJsValuesBenchmark\.test_6_steps_JsValues

#java -Xms256m  -jar target/benchmark.jar -o test_10_steps_JsValues.txt \
#actors\.performance\.benchmarks\.JacksonVsJsValuesBenchmark\.test_10_steps_JsValues

#java -Xms256m  -jar target/benchmark.jar -o test_2_steps_Jackson.txt \
#actors\.performance\.benchmarks\.JacksonVsJsValuesBenchmark\.test_2_steps_Jackson
#
#java -Xms256m  -jar target/benchmark.jar -o test_3_steps_Jackson.txt \
#actors\.performance\.benchmarks\.JacksonVsJsValuesBenchmark\.test_3_steps_Jackson
#
#java -Xms256m  -jar target/benchmark.jar -o test_4_steps_Jackson.txt \
#actors\.performance\.benchmarks\.JacksonVsJsValuesBenchmark\.test_4_steps_Jackson
#
#java -Xms256m  -jar target/benchmark.jar -o test_5_steps_Jackson.txt \
#actors\.performance\.benchmarks\.JacksonVsJsValuesBenchmark\.test_5_steps_Jackson
#
#java -Xms256m  -jar target/benchmark.jar -o test_6_steps_Jackson.txt \
#actors\.performance\.benchmarks\.JacksonVsJsValuesBenchmark\.test_6_steps_Jackson

#java -Xms256m  -jar target/benchmark.jar -o test_10_steps_Jackson.txt \
#actors\.performance\.benchmarks\.JacksonVsJsValuesBenchmark\.test_10_steps_Jackson
