package vertx.effect.performance.benchmarks;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import vertx.effect.RegisterJsValuesCodecs;
import vertx.effect.VertxRef;
import vertx.effect.exp.Pair;
import vertx.effect.performance.MyModule;

import static java.util.concurrent.TimeUnit.SECONDS;
import static vertx.effect.performance.Functions.awaitForEnding;
import static vertx.effect.performance.MyModule.countStringsLengthMultiProcesses;
import static vertx.effect.performance.MyModule.countStringsLengthMultiVerticles;
import static vertx.effect.performance.benchmarks.Inputs.TIMES;

public class VerticlesVsProcesses {
    private  static final int TIME_WAITING = 60;

    static {
        VertxRef vertxRef = new VertxRef(Vertx.vertx(new VertxOptions().setWorkerPoolSize(Inputs.WORKERS)));


        awaitForEnding(Pair.sequential(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                                       vertxRef.deployVerticle(new MyModule())
                                      ),
                       2,
                       SECONDS
                      );
    }



    @Benchmark
    @BenchmarkMode(Mode.All)
    public void count_string_verticles()  {

        awaitForEnding(countStringsLengthMultiVerticles.apply(TIMES),
                       TIME_WAITING,
                       SECONDS
                      );

    }

    @Benchmark
    @BenchmarkMode(Mode.All)
    public void count_string_processes()  {

        awaitForEnding(countStringsLengthMultiProcesses.apply(TIMES),
                       TIME_WAITING,
                       SECONDS
                      );


    }

}
