package vertx.effect.exp;

import io.vavr.Tuple2;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.Failures;
import vertx.effect.RegisterJsValuesCodecs;
import vertx.effect.Val;
import vertx.effect.VertxRef;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

@ExtendWith(VertxExtension.class)
public class TestPair {


    private static final Supplier<Val<String>> a =
            new ErrorWhile<>(counter -> counter == 1 || counter == 2,
                             counter -> new RuntimeException("counter: " + counter),
                             "a"
            );
    static VertxRef vertxRef;
    private static Val<Void> oneSec;

    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext testContext
                              ) {

        vertxRef = new VertxRef(vertx);
        oneSec = vertxRef.timer(1,
                                TimeUnit.SECONDS,
                                "one sec"
                               );
        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );
        vertxRef.deploy(new RegisterJsValuesCodecs())
                .onComplete(event -> testContext.completeNow())
                .get();
    }

    @Test
    public void test_retry_if_success_with_delay(final VertxTestContext context) {


        int ATTEMPTS = 3;
        ErrorWhile<Integer> one = new ErrorWhile<>(ATTEMPTS,
                                                   i -> new IllegalArgumentException(),
                                                   1
        );
        Pair.parallel(one.get(),
                      one.get()
                     )
            .retryIf(it -> it instanceof IllegalArgumentException,
                     ATTEMPTS,
                     (e, i) -> vertxRef.timer(1,
                                              SECONDS,
                                              "1 sec"
                                             )
                    )
            .onSuccess(it -> {
                context.verify(() -> {
                    Assertions.assertEquals(new Tuple2<>(1,
                                                         1
                                            ),
                                            it
                                           );
                    context.completeNow();
                });
            })
            .get();
    }

    @Test
    public void test_retries(VertxTestContext context) {

        ParallelPair.of(a.get(),
                        a.get()
                       )
                    .retry(2)
                    .get()
                    .onComplete(it -> {
                        context.verify(() -> Assertions.assertEquals(new Tuple2<>("a",
                                                                                  "a"
                                                                     ),
                                                                     it.result()
                                                                    )
                                      );
                        context.completeNow();
                    });


    }

    @Test
    public void test_retries_if_Success(VertxTestContext context) {

        final Supplier<Val<String>> val =
                new ErrorWhile<>(counter -> counter == 1 || counter == 2,
                                 counter -> Failures.GET_BAD_MESSAGE_EXCEPTION.apply("counter " + counter),
                                 "a"
                );

        ParallelPair.of(val.get(),
                        val.get()
                       )
                    .retryIf(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                             2
                            )
                    .get()
                    .onComplete(it -> {
                        context.verify(() -> Assertions.assertEquals(new Tuple2<>("a",
                                                                                  "a"
                                                                     ),
                                                                     it.result()
                                                                    )
                                      );
                        context.completeNow();
                    });

    }

    @Test
    public void test_retries_if_failure(VertxTestContext context) {

        final Supplier<Val<String>> val =
                new ErrorWhile<>(counter -> counter == 1 || counter == 2,
                                 counter -> new RuntimeException("counter " + counter),
                                 "a"
                );

        ParallelPair.of(val.get(),
                        val.get()
                       )
                    .retryIf(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                             2
                            )
                    .get()
                    .onComplete(it -> {
                        context.verify(() -> Assertions.assertTrue(it.failed())
                                      );
                        context.completeNow();
                    });

    }

    @Test
    public void test_retry_with_delay(VertxTestContext context) {
        int ATTEMPTS = 2;

        long start = System.nanoTime();

        ParallelPair.of(a.get(),
                        a.get()
                       )
                    .retry(ATTEMPTS,
                           (error, n) -> vertxRef.timer(1,
                                                        SECONDS,
                                                        "next attempt"
                                                       )
                          )
                    .get()
                    .onComplete(r -> context.verify(() -> {
                        Assertions.assertEquals(new Tuple2<>("a",
                                                             "a"
                                                ),
                                                r.result()
                                               );
                        Assertions.assertTrue(NANOSECONDS.toSeconds(System.nanoTime() - start) >= ATTEMPTS);
                        context.completeNow();

                    }));

    }

    @Test
    public void test_pair_exp_map(VertxTestContext context) {

        ParallelPair.of(Cons.success("a"),
                        Cons.success("ab")
                       )
                    .map(pair -> pair.map((a, b) -> new Tuple2<>(a.length(),
                                                                 b.length()
                    )))
                    .onSuccess(r -> context.verify(() -> {
                        Assertions.assertEquals(new Tuple2<>(1,
                                                             2
                                                ),
                                                r
                                               );
                        context.completeNow();
                    }))
                    .get();

    }

    @Test
    public void test_pair_exp_flatmap_success(VertxTestContext context) {


        Pair.parallel(Cons.success("a"),
                      Cons.success("b")
                     )
            .flatMap(pair -> Cons.success(pair.map((a, b) -> new Tuple2<>(a.toUpperCase(),
                                                                          b.toUpperCase()
                                                   )
                                                  )))
            .onSuccess(r -> context.verify(() -> {
                Assertions.assertEquals(new Tuple2<>("A",
                                                     "B"
                                        ),
                                        r
                                       );
                context.completeNow();
            }))
            .get();
    }

    @Test
    public void test_pair_exp_flatmap_failure(VertxTestContext context) {


        Pair.parallel(Cons.success("a"),
                      Cons.success("ab")
                     )
            .flatMap(s -> Cons.failure(new RuntimeException()))
            .onComplete(r -> context.verify(() -> {
                Assertions.assertTrue(r.failed());
                context.completeNow();
            }))
            .get();

    }

    @Test
    public void test_pair_exp_fails_and_recover_with_success(VertxTestContext context) {

        Pair.parallel(a.get(),
                      a.get()
                     )
            .recoverWith(e -> Cons.success(new Tuple2<>("",
                                                        ""
            )))
            .onSuccess(map -> context.verify(() -> {
                Assertions.assertEquals(new Tuple2<>("",
                                                     ""
                                        ),
                                        map
                                       );
                context.completeNow();
            }))
            .get();
    }

    @Test
    public void test_pair_exp_fails_and_recover_with_failure(VertxTestContext context) {

        Pair.parallel(a.get(),
                      a.get()
                     )
            .recoverWith(e -> Cons.failure(new IllegalArgumentException()))
            .onComplete(r -> context.verify(() -> {
                Assertions.assertTrue(r.failed());
                Assertions.assertTrue(r.cause() instanceof IllegalArgumentException);
                context.completeNow();
            }))
            .get();
    }

    @Test
    public void test_pair_exp_recover_with_success(VertxTestContext context) {
        Pair.parallel(a.get(),
                      a.get()
                     )
            .retry(2)
            .recoverWith(e -> Cons.failure(new IllegalArgumentException()))
            .onSuccess(map -> context.verify(() -> {
                Assertions.assertEquals(new Tuple2<>("a",
                                                     "a"
                                        ),
                                        map
                                       );
                context.completeNow();
            }))
            .get();
    }
}
