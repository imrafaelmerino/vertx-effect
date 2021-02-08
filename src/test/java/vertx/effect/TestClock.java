package vertx.effect;

import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.exp.Pair;

import java.util.concurrent.TimeUnit;

@ExtendWith(VertxExtension.class)
public class TestClock {



    @Test
    public void test_monotonic(VertxTestContext context){

        Val<Long> a = Clock.monotonic.apply(null);

        Val<Long> b = Clock.monotonic.apply(null);

        Pair.parallel(a,b).map(p -> p._2 - p._1 >= 0).onSuccess(r -> context.verify(()-> {
            Assertions.assertTrue(r);
            context.completeNow();
        })).get();

    }

    @Test
    public void test_monotonic_unit(VertxTestContext context) throws InterruptedException {

        Val<Long> a = Clock.monotonic(TimeUnit.SECONDS).apply(null);

        Thread.sleep(1000);

        Val<Long> b = Clock.monotonic(TimeUnit.SECONDS).apply(null);

        Pair.parallel(a,b).map(p -> p._2 - p._1 >= 1).onSuccess(r -> context.verify(()-> {
            Assertions.assertTrue(r);
            context.completeNow();
        })).get();

    }

    @Test
    public void test_realTime(VertxTestContext context){

        Val<Long> a = Clock.realTime.apply(null);

        Val<Long> b = Clock.realTime.apply(null);

        Pair.parallel(a,b).map(p -> p._2 - p._1 >= 0).onSuccess(r -> context.verify(()-> {
            Assertions.assertTrue(r);
            context.completeNow();
        })).get();

    }

    @Test
    public void test_realTime_unit(VertxTestContext context) throws InterruptedException {

        Val<Long> a = Clock.realTime(TimeUnit.SECONDS).apply(null);

        Thread.sleep(1000);

        Val<Long> b = Clock.realTime(TimeUnit.SECONDS).apply(null);

        Pair.parallel(a,b).map(p -> p._2 - p._1 >= 1).onSuccess(r -> context.verify(()-> {
            Assertions.assertTrue(r);
            context.completeNow();
        })).get();

    }
}
