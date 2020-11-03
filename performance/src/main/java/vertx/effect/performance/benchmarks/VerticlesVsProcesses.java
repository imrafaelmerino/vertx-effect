package vertx.effect.performance.benchmarks;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import vertx.effect.RegisterJsValuesCodecs;
import vertx.effect.VertxRef;
import vertx.effect.exp.Pair;
import vertx.effect.performance.MyModule;

import static java.util.concurrent.TimeUnit.SECONDS;
import static vertx.effect.performance.Functions.awaitForEnding;
import static vertx.effect.performance.MyModule.countStringsLengthMultiProcesses;
import static vertx.effect.performance.MyModule.countStringsLengthMultiVerticles;

public class MyBenchmark {

    static {
        VertxRef vertxRef = new VertxRef(Vertx.vertx(new VertxOptions().setWorkerPoolSize(100)));


        awaitForEnding(Pair.sequential(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                                       vertxRef.deployVerticle(new MyModule())
                                      ),
                       2,
                       SECONDS
                      );
    }

    private static final int TIMES = 100;
    private  static final int TIME_WAITING = 200;


    @Benchmark
    @BenchmarkMode(Mode.All)
    @Fork(1)
    public void testCountStringMultiVerticle()  {

        awaitForEnding(countStringsLengthMultiVerticles.apply(TIMES),
                       TIME_WAITING,
                       SECONDS
                      );

    }

    @Benchmark
    @BenchmarkMode(Mode.All)
    @Fork(1)
    public void testCountStringProcesses()  {

        awaitForEnding(countStringsLengthMultiProcesses.apply(TIMES),
                       TIME_WAITING,
                       SECONDS
                      );


    }

}
