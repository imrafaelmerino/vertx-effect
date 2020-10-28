package vertx.effect.performance.benchmarks;

import io.vertx.core.Vertx;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import vertx.effect.RegisterJsValuesCodecs;
import vertx.effect.VertxRef;
import vertx.effect.exp.Pair;
import vertx.effect.Module;

import static vertx.effect.Functions.await5segForEnding;
import static vertx.effect.Module.*;

public class MyBenchmark {

    static {
        VertxRef deployer = new VertxRef(Vertx.vertx());


        await5segForEnding(Pair.of(deployer.deploy(new RegisterJsValuesCodecs()),
                                   deployer.deploy(new Module())
                                  ));
    }

    private static final int TIMES = 100;


    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(1)
    public void testCountStringMultiVerticle()  {

        await5segForEnding(countStringsMultiVerticles.apply(TIMES));

    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(1)
    public void testCountStringProcesses()  {

        await5segForEnding(countStringsMultiProcesses.apply(TIMES));


    }

}
