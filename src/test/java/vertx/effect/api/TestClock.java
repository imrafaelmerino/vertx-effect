package vertx.effect.api;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import vertx.effect.Clock;
import vertx.effect.stub.ClockStub;

import java.time.Duration;
import java.time.Instant;

public class TestClock {

    @Test
    public void test_measure_time() throws InterruptedException {

        Clock monotonic = Clock.monotonic;

        long start = monotonic.get();

        Thread.sleep(10);

        long end = monotonic.get();

        long duration = Duration.ofNanos(end - start).toMillis();
        System.out.println("duration(ms): " + duration);
        Assertions.assertTrue(duration >= 10);
    }

    @Test
    public void test_real_time() {
        Clock realTime = Clock.realTime;

        Assertions.assertEquals(0,
                                Duration.between(Instant.ofEpochMilli(realTime.get()),
                                                 Instant.now()
                                                ).toDays()
                               );
        Assertions.assertEquals(0,
                                Duration.between(Instant.ofEpochMilli(realTime.get()),
                                                 Instant.now()
                                                ).toHours()
                               );


    }

    @Test
    public void testMock() throws InterruptedException {

        Instant base = Instant.parse("1982-03-13T00:00:00.000000Z");
        Clock clock = ClockStub.fromReference(base).get();

        Assertions.assertEquals(clock.get(),
                                base.toEpochMilli()
                               );

        System.out.println(base);

        Thread.sleep(1000);

        System.out.println(Instant.ofEpochMilli(clock.get())
                                  .toString());

        Thread.sleep(1000);

        System.out.println(Instant.ofEpochMilli(clock.get())
                                  .toString());


    }

}
