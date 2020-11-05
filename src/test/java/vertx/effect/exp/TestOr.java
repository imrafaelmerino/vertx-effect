package vertx.effect.exp;

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

import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

@ExtendWith(VertxExtension.class)
public class TestOr {
    static VertxRef vertxRef;
    static final int ATTEMPTS = 2;
    static final Supplier<Val<Boolean>> TRUE =
            new ErrorWhile<>(ATTEMPTS,
                             counter ->
                                     Failures.GET_BAD_MESSAGE_EXCEPTION.apply("counter " + counter),
                             true
            );


    static final Supplier<Val<Boolean>> FALSE =
            new ErrorWhile<>(ATTEMPTS,
                             counter -> Failures.GET_BAD_MESSAGE_EXCEPTION.apply("counter " + counter),
                             false
            );

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
    public void test_parallel_retries_two_times_returns_true(VertxTestContext context) {

        Or.parallel(TRUE.get(),
                    TRUE.get()
                   )
          .retry(2)
          .get()
          .onComplete(it -> {
              context.verify(() -> Assertions.assertEquals(true,
                                                           it.result()
                                                          ));
              context.completeNow();
          });
    }

    @Test
    public void test_sequential_retries_two_times_returns_true(VertxTestContext context) {

        Or.sequential(TRUE.get(),
                      TRUE.get()
                     )
          .retry(2)
          .get()
          .onComplete(it -> {
              context.verify(() -> Assertions.assertEquals(true,
                                                           it.result()
                                                          ));
              context.completeNow();
          });
    }

    @Test
    public void test_parallel_retries_two_times_returns_false(VertxTestContext context) {


        Or.parallel(FALSE.get(),
                    FALSE.get()
                   )
          .retry(2)
          .get()
          .onComplete(it -> {
              context.verify(() -> Assertions.assertEquals(false,
                                                           it.result()
                                                          ));
              context.completeNow();
          });
    }

    @Test
    public void test_sequential_retries_two_times_returns_false(VertxTestContext context) {


        Or.sequential(FALSE.get(),
                      FALSE.get()
                     )
          .retry(2)
          .get()
          .onComplete(it -> {
              context.verify(() -> Assertions.assertEquals(false,
                                                           it.result()
                                                          ));
              context.completeNow();
          });
    }

    @Test
    public void test_parallel_retries_if_success(VertxTestContext context) {


        Or.parallel(TRUE.get(),
                    FALSE.get()
                   )
          .retryIf(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                   2
                  )
          .get()
          .onComplete(it -> {
              context.verify(() -> Assertions.assertEquals(true,
                                                           it.result()
                                                          )
                            );
              context.completeNow();
          });

    }

    @Test
    public void test_sequential_retries_if_success(VertxTestContext context) {


        Or.sequential(TRUE.get(),
                      FALSE.get()
                     )
          .retryIf(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                   2
                  )
          .get()
          .onComplete(it -> {
              context.verify(() -> Assertions.assertEquals(true,
                                                           it.result()
                                                          )
                            );
              context.completeNow();
          });

    }

    @Test
    public void test_parallel_map(final VertxTestContext context,
                                  final Vertx vertx) {

        Or.parallel(Cons.success(true),
                    Cons.success(true)
                   )
          .map(it -> !it)
          .onSuccess(result -> {
              context.verify(() -> {
                  Assertions.assertFalse(result);
                  context.completeNow();
              });
          })
          .get();
    }

    @Test
    public void test_sequential_map(final VertxTestContext context,
                                    final Vertx vertx) {

        Or.sequential(Cons.success(true),
                      Cons.success(true)
                     )
          .map(it -> !it)
          .onSuccess(result -> {
              context.verify(() -> {
                  Assertions.assertFalse(result);
                  context.completeNow();
              });
          })
          .get();
    }

    @Test
    public void test_parallel_retries_if_failure(VertxTestContext context) {


        Or.parallel(TRUE.get(),
                    FALSE.get()
                   )
          .retryIf(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.REQUEST_TIMEOUT_CODE),
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
    public void test_sequential_retries_if_failure(VertxTestContext context) {


        Or.sequential(TRUE.get(),
                      FALSE.get()
                     )
          .retryIf(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.REQUEST_TIMEOUT_CODE),
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
    public void test_parallel_retry_if_success_with_delay(final VertxTestContext context) {


        ErrorWhile<Boolean> True = new ErrorWhile<>(3,
                                                    i -> new IllegalArgumentException(),
                                                    true
        );
        Or.parallel(True.get(),
                    True.get()
                   )
          .retryIf(it -> it instanceof IllegalArgumentException,
                   3,
                   (e, i) -> vertxRef.delay(100,
                                            MILLISECONDS
                                           )
                  )
          .onSuccess(it -> {
              context.verify(() -> {
                  Assertions.assertTrue(it);
                  context.completeNow();
              });
          })
          .get();
    }

    @Test
    public void test_sequential_retry_if_success_with_delay(final VertxTestContext context) {


        ErrorWhile<Boolean> True = new ErrorWhile<>(3,
                                                    i -> new IllegalArgumentException(),
                                                    true
        );
        Or.sequential(True.get(),
                      True.get()
                     )
          .retryIf(it -> it instanceof IllegalArgumentException,
                   3,
                   (e, i) -> vertxRef.delay(100,
                                            MILLISECONDS
                                           )
                  )
          .onSuccess(it -> {
              context.verify(() -> {
                  Assertions.assertTrue(it);
                  context.completeNow();
              });
          })
          .get();
    }

    @Test
    public void test_parallel_retry_with_delay(VertxTestContext context) {
        long start = System.nanoTime();
        Or.parallel(TRUE.get(),
                    FALSE.get()
                   )
          .retry(ATTEMPTS,
                 (error, n) -> vertxRef.delay(100,
                                              MILLISECONDS
                                             )
                )
          .get()
          .onComplete(r -> context.verify(() -> {
              Assertions.assertTrue(r.result());
              long seconds = NANOSECONDS.toMillis(System.nanoTime() - start);
              Assertions.assertTrue(seconds >= ATTEMPTS);
              context.completeNow();

          }));

    }

    @Test
    public void test_sequential_retry_with_delay(VertxTestContext context) {
        long start = System.nanoTime();
        Or.sequential(TRUE.get(),
                      FALSE.get()
                     )
          .retry(ATTEMPTS,
                 (error, n) -> vertxRef.delay(100,
                                              MILLISECONDS
                                             )
                )
          .get()
          .onComplete(r -> context.verify(() -> {
                          Assertions.assertTrue(r.result());
                          long seconds = NANOSECONDS.toMillis(System.nanoTime() - start);
                          Assertions.assertTrue(seconds >= ATTEMPTS);
                          context.completeNow();
                      })
                     );

    }

    @Test
    public void test_or_booleans(VertxTestContext context) {

        Or.of(true,
              true
             )
          .onSuccess(it -> {
              context.verify(() -> {
                  Assertions.assertTrue(it);
                  context.completeNow();
              });
          })
          .get();
    }
}
