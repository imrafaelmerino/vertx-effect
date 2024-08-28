package vertx.effect.api.policies;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.Delay;
import vertx.effect.RetryPolicies;
import vertx.effect.RetryStatus;
import vertx.effect.VertxRef;

import java.time.Duration;
import java.util.List;

@ExtendWith(VertxExtension.class)
public class TestPolicies {

    @Test
    public void test_simulation_1(Vertx vertx) {
        VertxRef ref = new VertxRef(vertx);

        Delay base = ref.delay(Duration.ofMillis(10));
        List<RetryStatus> simulation = RetryPolicies.incrementalDelay(base)
                                                    .limitRetriesByCumulativeDelay(Duration.ofMillis(120))
                                                    .simulate(20);
        System.out.println(simulation);
        Assertions.assertEquals(5,
                                simulation.size()
                               );
        Assertions.assertEquals(new RetryStatus(4,
                                                100,
                                                40
                                ),
                                simulation.get(simulation.size() - 1)
                               );

    }

    @Test
    public void test_simulation_2(Vertx vertx) {
        VertxRef ref = new VertxRef(vertx);

        Delay base = ref.delay(Duration.ofMillis(10));
        int cap = 100;
        List<RetryStatus> simulation = RetryPolicies.incrementalDelay(base)
                                                    .capDelay(ref.delay(Duration.ofMillis(cap)))
                                                    .simulate(20);
        System.out.println(simulation);
        Assertions.assertEquals(20,
                                simulation.size()
                               );
        Assertions.assertTrue(simulation.stream()
                                        .allMatch(rs -> rs.previousDelay <= cap));

    }

    @Test
    public void test_simulation_3(Vertx vertx) {
        VertxRef ref = new VertxRef(vertx);

        Delay base = ref.delay(Duration.ofMillis(10));
        int acc = 150;
        List<RetryStatus> simulation = RetryPolicies.incrementalDelay(base)
                                                    .limitRetriesByCumulativeDelay(Duration.ofMillis(acc))
                                                    .simulate(20);
        System.out.println(simulation);
        Assertions.assertEquals(6,
                                simulation.size()
                               );
        Assertions.assertTrue(simulation.stream()
                                        .allMatch(rs -> rs.cumulativeDelay <= acc));

    }


    @Test
    public void test_simulation_4(Vertx vertx) {
        VertxRef ref = new VertxRef(vertx);

        Delay base = ref.delay(Duration.ofMillis(10));
        List<RetryStatus> simulation = RetryPolicies.incrementalDelay(base)
                                                    .limitRetriesByDelay(Duration.ofMillis(100))
                                                    .simulate(20);
        System.out.println(simulation);
        Assertions.assertEquals(9,
                                simulation.size()
                               );
        Assertions.assertTrue(simulation.stream()
                                        .allMatch(rs -> rs.previousDelay <= 100));

    }


    @Test
    public void test_simulation_5(Vertx vertx) {
        VertxRef ref = new VertxRef(vertx);

        Duration tenMillis = Duration.ofMillis(10);
        Delay base = ref.delay(tenMillis);
        List<RetryStatus> simulation = RetryPolicies.incrementalDelay(base)
                                                    .append(RetryPolicies.limitRetries(2))
                                                    .followedBy(RetryPolicies.constantDelay(base)
                                                                             .limitRetriesByCumulativeDelay(Duration.ofMillis(200)
                                                                                                           )
                                                               )
                                                            .
                                                    simulate(20);
        System.out.println(simulation);
        Assertions.assertEquals(20,
                                simulation.size()
                               );
        Assertions.assertEquals(10,
                                simulation.get(1).previousDelay
                               );
        Assertions.assertEquals(20,
                                simulation.get(2).previousDelay
                               );
        Assertions.assertEquals(200,
                                simulation.get(simulation.size() - 1).cumulativeDelay
                               );
        Assertions.assertTrue(simulation.subList(3,
                                                 simulation.size()
                                                )
                                        .stream()
                                        .allMatch(rs -> rs.previousDelay == 10));

    }

    @Test
    public void test_simulation_6(Vertx vertx) {
        VertxRef ref = new VertxRef(vertx);

        Duration tenMillis = Duration.ofMillis(10);

        Delay cap = ref.delay(Duration.ofMillis(400));


        List<RetryStatus> simulation = RetryPolicies.exponentialBackoffDelay(tenMillis,
                                                                             ref::delay
                                                                            )
                                                    .capDelay(cap)
                                                    .simulate(10);

        System.out.println(simulation);

    }

    @Test
    public void test_simulation_7(Vertx vertx) {
        VertxRef ref = new VertxRef(vertx);

        Duration tenMillis = Duration.ofMillis(10);
        Duration cap = Duration.ofMillis(1000);


        List<RetryStatus> simulation = RetryPolicies.fullJitter(tenMillis,
                                                                cap,
                                                                ref::delay
                                                               )
                                                    .simulate(20);

        System.out.println(simulation);

    }

    @Test
    public void test_simulation_8(Vertx vertx) {
        VertxRef ref = new VertxRef(vertx);

        Duration tenMillis = Duration.ofMillis(10);
        Duration cap = Duration.ofMillis(1000);


        List<RetryStatus> simulation = RetryPolicies.equalJitter(tenMillis,
                                                                 cap,
                                                                 ref::delay
                                                                )
                                                    .simulate(20);

        System.out.println(simulation);

    }

    @Test
    public void test_simulation_9(Vertx vertx) {
        VertxRef ref = new VertxRef(vertx);

        Duration tenMillis = Duration.ofMillis(10);
        Duration cap = Duration.ofMillis(1000);


        List<RetryStatus> simulation = RetryPolicies.decorrelatedJitter(tenMillis,
                                                                        cap,
                                                                        ref::delay
                                                                       )
                                                    .simulate(23);

        System.out.println(simulation);

    }
}
