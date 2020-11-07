package vertx.effect.performance.benchmarks;

import io.vertx.core.Vertx;
import jsonvalues.JsObj;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import vertx.effect.VertxRef;
import vertx.effect.λ;

import java.util.concurrent.CountDownLatch;

import static java.util.concurrent.TimeUnit.MINUTES;

public class Processes {

    private static final int TIME_WAITING = 60;

    private static VertxRef vertxRef;
    private static int processes;

    static {
        vertxRef = new VertxRef(Vertx.vertx());
        String strProcesses = System.getProperty("processes",
                                                 null
                                                );
        if (strProcesses == null) throw new RuntimeException("specify number of processes with -Dprocesses");
        System.out.println("processes " + strProcesses);
        processes = Integer.parseInt(strProcesses);

    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Fork(2)
    public void deploy_undeploy() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(processes);

        for (int i = 0; i < processes; i++) {
            vertxRef.deploy("id" + i,
                            λ.<JsObj>identity()
                           )
                    .onComplete(vr -> {
                        vr.result()
                          .undeploy()
                          .onSuccess(it -> latch.countDown());
                    })
                    .get();
        }

        latch.await(1,
                    MINUTES
                   );

    }


}