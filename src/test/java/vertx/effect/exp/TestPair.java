package vertx.effect.exp;

import io.vavr.Tuple2;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.*;
import vertx.effect.mock.ValOrErrorMock;

import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static vertx.effect.RetryPolicies.limitRetries;

@ExtendWith(VertxExtension.class)
public class TestPair {


    private static final Supplier<Val<String>> a =
            new ValOrErrorMock<>(counter -> counter == 1 || counter == 2,
                                 counter -> new RuntimeException("counter: " + counter),
                                 "a"
            );
    static VertxRef vertxRef;

    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext testContext
                              ) {

        vertxRef = new VertxRef(vertx);

        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );
        vertxRef.deployVerticle(new RegisterJsValuesCodecs())
                .onComplete(event -> testContext.completeNow())
                .get();
    }

    @Test
    public void test_parallel_retry_if_success_with_delay(final VertxTestContext context) {


        int ATTEMPTS = 3;
        ValOrErrorMock<Integer> one = new ValOrErrorMock<>(ATTEMPTS,
                                                           i -> new IllegalArgumentException(),
                                                           1
        );
        Pair.parallel(one.get(),
                      one.get()
                     )
            .retry(limitRetries(ATTEMPTS)
                           .join(RetryPolicies.retryIf(e -> e instanceof IllegalArgumentException)
                                )
                           .join(RetryPolicies.constantDelay(vertxRef.delay(100,
                                                                            MILLISECONDS
                                                                           )))
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
    public void test_sequential_retry_if_success_with_delay(final VertxTestContext context) {


        int ATTEMPTS = 3;
        ValOrErrorMock<Integer> one = new ValOrErrorMock<>(ATTEMPTS,
                                                           i -> new IllegalArgumentException(),
                                                           1
        );
        Pair.sequential(one.get(),
                        one.get()
                       )
            .retry(limitRetries(ATTEMPTS)
                           .join(RetryPolicies.retryIf(e -> e instanceof IllegalArgumentException)
                                )
                           .join(RetryPolicies.constantDelay(vertxRef.delay(100,
                                                                            MILLISECONDS
                                                                           )))
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
    public void test_parallel_retries(VertxTestContext context) {

        Pair.parallel(a.get(),
                      a.get()
                     )
            .retry(limitRetries(2))
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
    public void test_sequential_retries(VertxTestContext context) {

        Pair.sequential(a.get(),
                        a.get()
                       )
            .retry(limitRetries(2))
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
    public void test_parallel_retries_if_Success(VertxTestContext context) {

        final Supplier<Val<String>> val =
                new ValOrErrorMock<>(counter -> counter == 1 || counter == 2,
                                     counter -> Failures.GET_BAD_MESSAGE_EXCEPTION.apply("counter " + counter),
                                     "a"
                );

        Pair.parallel(val.get(),
                      val.get()
                     )
            .retry(limitRetries(2)
                           .join(RetryPolicies.retryIf(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE))
                                )
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
    public void test_sequential_retries_if_Success(VertxTestContext context) {

        final Supplier<Val<String>> val =
                new ValOrErrorMock<>(counter -> counter == 1 || counter == 2,
                                     counter -> Failures.GET_BAD_MESSAGE_EXCEPTION.apply("counter " + counter),
                                     "a"
                );

        Pair.sequential(val.get(),
                        val.get()
                       )
            .retry(limitRetries(2)
                           .join(RetryPolicies.retryIf(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE))
                                )
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
    public void test_parallel_retries_if_failure(VertxTestContext context) {

        final Supplier<Val<String>> val =
                new ValOrErrorMock<>(counter -> counter == 1 || counter == 2,
                                     counter -> new RuntimeException("counter " + counter),
                                     "a"
                );

        Pair.parallel(val.get(),
                      val.get()
                     )
            .retry(limitRetries(2)
                           .join(RetryPolicies.retryIf(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE))
                                )
                  )
            .get()
            .onComplete(it -> {
                context.verify(() -> Assertions.assertTrue(it.failed())
                              );
                context.completeNow();
            });

    }

    @Test
    public void test_sequential_retries_if_failure(VertxTestContext context) {

        final Supplier<Val<String>> val =
                new ValOrErrorMock<>(counter -> counter == 1 || counter == 2,
                                     counter -> new RuntimeException("counter " + counter),
                                     "a"
                );

        Pair.sequential(val.get(),
                        val.get()
                       )
            .retry(limitRetries(2)
                           .join(RetryPolicies.retryIf(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE))
                                )
                  )
            .get()
            .onComplete(it -> {
                context.verify(() -> Assertions.assertTrue(it.failed())
                              );
                context.completeNow();
            });

    }

    @Test
    public void test_parallel_retry_with_delay(VertxTestContext context) {
        int ATTEMPTS = 2;

        long start = System.nanoTime();

        Pair.parallel(a.get(),
                      a.get()
                     )
            .retry(limitRetries(ATTEMPTS)
                           .join(RetryPolicies.constantDelay(vertxRef.delay(100,
                                                                            MILLISECONDS
                                                                           )))
                  )
            .get()
            .onComplete(r -> context.verify(() -> {
                Assertions.assertEquals(new Tuple2<>("a",
                                                     "a"
                                        ),
                                        r.result()
                                       );
                Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= ATTEMPTS);
                context.completeNow();

            }));

    }

    @Test
    public void test_sequential_retry_with_delay(VertxTestContext context) {
        int ATTEMPTS = 2;

        long start = System.nanoTime();

        Pair.sequential(a.get(),
                        a.get()
                       )
            .retry(limitRetries(ATTEMPTS)
                           .join(RetryPolicies.constantDelay(vertxRef.delay(100,
                                                                            MILLISECONDS
                                                                           )))
                  )
            .get()
            .onComplete(r -> context.verify(() -> {
                Assertions.assertEquals(new Tuple2<>("a",
                                                     "a"
                                        ),
                                        r.result()
                                       );
                Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= ATTEMPTS);
                context.completeNow();

            }));

    }

    @Test
    public void test_parallel_pair_exp_map(VertxTestContext context) {

        Pair.parallel(Cons.success("a"),
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
    public void test_sequential_pair_exp_map(VertxTestContext context) {

        Pair.sequential(Cons.success("a"),
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
    public void test_parallel_pair_exp_flatmap_success(VertxTestContext context) {


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
    public void test_sequential_pair_exp_flatmap_success(VertxTestContext context) {


        Pair.sequential(Cons.success("a"),
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
    public void test_parallel_pair_exp_flatmap_failure(VertxTestContext context) {


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
    public void test_sequential_pair_exp_flatmap_failure(VertxTestContext context) {


        Pair.sequential(Cons.success("a"),
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
    public void test_parallel_pair_exp_fails_and_recover_with_success(VertxTestContext context) {

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
    public void test_sequential_pair_exp_fails_and_recover_with_success(VertxTestContext context) {

        Pair.sequential(a.get(),
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
    public void test_parallel_pair_exp_fails_and_recover_with_failure(VertxTestContext context) {

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
    public void test_sequential_pair_exp_fails_and_recover_with_failure(VertxTestContext context) {

        Pair.sequential(a.get(),
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
    public void test_parallel_pair_exp_recover_with_success(VertxTestContext context) {
        Pair.parallel(a.get(),
                      a.get()
                     )
            .retry(limitRetries(2))
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

    @Test
    public void test_sequential_pair_exp_recover_with_success(VertxTestContext context) {
        Pair.sequential(a.get(),
                        a.get()
                       )
            .retry(limitRetries(2))
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
