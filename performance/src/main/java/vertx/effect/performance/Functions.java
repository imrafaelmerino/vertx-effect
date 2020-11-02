package vertx.effect.performance;

import jsonvalues.*;
import org.openjdk.jmh.infra.Blackhole;
import vertx.effect.Val;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

public class Functions {

    private Functions() {
    }

    private static final int A = 97;
    private static final int Z = 122;
    public static final int TIME_WAITING_MS = 100;


    public static Function<Random, Supplier<JsObj>> generator = random -> () ->
    {

        final Supplier<String> strGen = randomStr().apply(random)
                                                   .apply(10);
        return JsObj.of("a",
                        JsStr.of(strGen.get()),
                        "b",
                        JsStr.of(strGen.get()),
                        "c",
                        JsStr.of(strGen.get()),
                        "d",
                        JsInt.of(random.nextInt()),
                        "e",
                        JsObj.of("f",
                                 JsStr.of(strGen.get()),
                                 "g",
                                 JsStr.of(strGen.get()),
                                 "h",
                                 JsStr.of(strGen.get()),
                                 "i",
                                 JsBool.TRUE,
                                 "j",
                                 JsArray.of("apple",
                                            "banana",
                                            "pineapple",
                                            "tangerine",
                                            "melon",
                                            "watermelon"
                                           )

                                )
                       );
    };

    public static <O> void awaitForEnding(Val<O> fn,
                                          int time,
                                          TimeUnit unit) {
        CountDownLatch latch = new CountDownLatch(1);

        try {
            fn.onSuccess(it -> {
                latch.countDown();
            })
              .get();
            latch.await(time,
                        unit
                       );
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static <O> void awaitForEnding(Val<O> fn,
                                          int time,
                                          TimeUnit unit,
                                          Blackhole blackhole) {
        CountDownLatch latch = new CountDownLatch(1);

        try {
            fn.onSuccess(it -> {
                latch.countDown();
                blackhole.consume(it);
            })
              .get();
            latch.await(time,
                        unit
                       );
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static Function<Random, Function<Integer, Supplier<String>>> randomStr() {
        return random -> length -> () -> random.ints(A,
                                                     Z + 1
                                                    )
                                               .limit(length)
                                               .collect(StringBuilder::new,
                                                        StringBuilder::appendCodePoint,
                                                        StringBuilder::append
                                                       )
                                               .toString();
    }
}
