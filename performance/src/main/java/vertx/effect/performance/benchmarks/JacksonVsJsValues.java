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
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.SECONDS;
import static vertx.effect.performance.Functions.awaitForEnding;

public class JacksonVsJsValuesBenchmark {
    static JsonObject jacksonObj;
    static JsObj      jsonValuesObj;
    static {
        String     json          = "{\"firstName\": \"John\",  \"lastName\": \"Smith\",  \"isAlive\": true,  \"age\": 27,  \"address\": {    \"streetAddress\": \"21 2nd Street\",    \"city\": \"New York\",    \"state\": \"NY\",    \"postalCode\": \"10021-3100\"  },  \"phoneNumbers\": [    {      \"type\": \"home\",      \"number\": \"212 555-1234\"    },    {      \"type\": \"office\",      \"number\": \"646 555-4567\"    }  ],  \"children\": [],  \"spouse\": null}";
         jacksonObj    = new JsonObject(json);
              jsonValuesObj = JsObj.parse(json);
        VertxRef   vertxRef      = new VertxRef(Vertx.vertx());
        awaitForEnding(Pair.sequential(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                                       vertxRef.deployVerticle(new MyModule())
                                      ),
                       2,
                       SECONDS
                      );
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(1)
    public void jsonValues(Blackhole blackhole) {
        awaitForEnding(MyModule.id.apply(jsonValuesObj),3,SECONDS,blackhole);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(1)
    public void jackson(Blackhole blackhole) {
        awaitForEnding(MyModule.jacksonId.apply(jacksonObj),3,SECONDS,blackhole);

    }


}
