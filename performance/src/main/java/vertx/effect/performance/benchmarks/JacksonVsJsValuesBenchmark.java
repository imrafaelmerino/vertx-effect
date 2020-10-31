package vertx.effect.performance.benchmarks;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import jsonvalues.JsObj;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.infra.Blackhole;

import vertx.effect.RegisterJsValuesCodecs;
import vertx.effect.VertxRef;
import vertx.effect.exp.Pair;
import vertx.effect.performance.Functions;
import vertx.effect.performance.MyModule;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;


import static vertx.effect.performance.Functions.awaitForEnding;
import static vertx.effect.performance.MyModule.*;

public class JacksonVsJsValuesBenchmark {
    private static final Supplier<JsObj> objGen = Functions.generator.apply(new Random());

    private static final Supplier<String> strGen = () -> objGen.get()
                                                               .toString();

    static {
        VertxRef vertxRef = new VertxRef(Vertx.vertx());
        awaitForEnding(Pair.sequential(vertxRef.deploy(new RegisterJsValuesCodecs()),
                                   vertxRef.deploy(new MyModule())
                                      ),
                       2,
                       TimeUnit.SECONDS
                          );
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(1)
    public void parsing_with_JsValues(Blackhole blackhole)  {
        blackhole.consume(JsObj.parse(strGen.get()));
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(1)
    public void parsing_with_Jackson(Blackhole blackhole)  {
        blackhole.consume(new JsonObject(strGen.get()));
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(1)
    public void parsing_with_JsValues_and_sending_to_event_bus() {
        awaitForEnding(parser.apply(strGen.get())
                                 .flatMap(id),
                       10,
                       TimeUnit.SECONDS
                      );
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(1)
    public void parsing_with_Jackson_and_sending_to_event_bus() {
        awaitForEnding(jacksonParser.apply(strGen.get())
                                        .flatMap(jacksonId),
                       10,
                       TimeUnit.SECONDS
                          );
    }


   /* @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(1)
    public void test_2_steps_JsValues() {
        await5segForEnding(parser.apply(strGen.get())
                                 .flatMap(id)
                                 .flatMap(id));
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(1)
    public void test_3_steps_JsValues() {

        await5segForEnding(parser.apply(strGen.get())
                                 .flatMap(id)
                                 .flatMap(id)
                                 .flatMap(id)
                          );

    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(1)
    public void test_4_steps_JsValues() {
        await5segForEnding(parser.apply(strGen.get())
                                 .flatMap(id)
                                 .flatMap(id)
                                 .flatMap(id)
                                 .flatMap(id)
                          );
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(1)
    public void test_5_steps_JsValues() {
        await5segForEnding(parser.apply(strGen.get())
                                 .flatMap(id)
                                 .flatMap(id)
                                 .flatMap(id)
                                 .flatMap(id)
                                 .flatMap(id)
                          );
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(1)
    public void test_6_steps_JsValues() {
        await5segForEnding(parser.apply(strGen.get())
                                 .flatMap(id)
                                 .flatMap(id)
                                 .flatMap(id)
                                 .flatMap(id)
                                 .flatMap(id)
                                 .flatMap(id)
                          );
    }


    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(1)
    public void test_10_steps_JsValues() {
        await5segForEnding(parser.apply(strGen.get())
                                 .flatMap(id)
                                 .flatMap(id)
                                 .flatMap(id)
                                 .flatMap(id)
                                 .flatMap(id)
                                 .flatMap(id)
                                 .flatMap(id)
                                 .flatMap(id)
                                 .flatMap(id)
                                 .flatMap(id)
                          );
    }


    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(1)
    public void test_2_steps_Jackson() {
        await5segForEnding(jacksonParser.apply(strGen.get())
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId));
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(1)
    public void test_3_steps_Jackson() {

        await5segForEnding(jacksonParser.apply(strGen.get())
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                          );

    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(1)
    public void test_4_steps_Jackson() {
        await5segForEnding(jacksonParser.apply(strGen.get())
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                          );
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(1)
    public void test_5_steps_Jackson() {
        await5segForEnding(jacksonParser.apply(strGen.get())
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                          );
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(1)
    public void test_6_steps_Jackson() {
        await5segForEnding(jacksonParser.apply(strGen.get())
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                          );
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(1)
    public void test_10_steps_Jackson() {
        await5segForEnding(jacksonParser.apply(strGen.get())
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                                        .flatMap(jacksonId)
                          );
    }*/
}
