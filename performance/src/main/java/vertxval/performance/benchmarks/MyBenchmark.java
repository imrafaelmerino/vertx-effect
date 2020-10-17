package vertxval.performance.benchmarks;

import io.vertx.core.Vertx;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import vertxval.RegisterJsValuesCodecs;
import vertxval.VertxRef;
import vertxval.exp.Pair;
import vertxval.performance.Module;

import static vertxval.performance.Functions.await5segForEnding;
import static vertxval.performance.Module.*;

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
    public void testCountStringOneVerticle()  {
        await5segForEnding(countStringsOneVerticle.apply(TIMES));
    }

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
