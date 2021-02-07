mvn clean package
java -Xms256m -jar -D"vertx.effect.enable.log.events"=false target/benchmark.jar -o results/json_values_jackson.txt \
vertx\.effect\.performance\.benchmarks\.JacksonVsJsValues

