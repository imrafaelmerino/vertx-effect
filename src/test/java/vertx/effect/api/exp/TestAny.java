package vertx.effect.api.exp;

import fun.gen.Gen;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.*;
import vertx.effect.stub.StubBuilder;
import vertx.values.codecs.RegisterJsValuesCodecs;

import java.time.Duration;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static vertx.effect.RetryPolicies.limitRetries;

@SuppressWarnings("ReturnValueIgnored")
@ExtendWith(VertxExtension.class)
public class TestAny {
    static final int ATTEMPTS = 2;
    static final StubBuilder<Boolean> TRUE =
            StubBuilder.ofGen(Gen.seq(counter -> counter <= ATTEMPTS ?
                                      VIO.fail(Failures.GET_BAD_MESSAGE_EXCEPTION.apply("counter " + counter)) :
                                      VIO.TRUE
                                     )
                             );


    static StubBuilder<Boolean> FALSE = StubBuilder.ofGen(Gen.seq(counter -> counter <= ATTEMPTS ?
                                                                  VIO.fail(Failures.GET_BAD_MESSAGE_EXCEPTION.apply("counter " + counter)) :
                                                                  VIO.FALSE
                                                                 )
                                                         );
    static VertxRef vertxRef;

    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext testContext
                              ) {

        vertxRef = new VertxRef(vertx);
        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS, System.out::println);
        vertxRef.deployVerticle(new RegisterJsValuesCodecs())
                .onComplete(event -> testContext.completeNow())
                .get();
    }


    @Test
    public void test_parallel_retriesEach_two_times_returns_true(VertxTestContext context) {

        AnyExp.par(TRUE.build(),
                   TRUE.build()
                  )
              .retryEach(limitRetries(2))
              .get()
              .onComplete(it -> {
                  context.verify(() -> Assertions.assertEquals(true,
                                                               it.result()
                                                              ));
                  context.completeNow();
              });
    }

    @Test
    public void test_parallel_retries_two_times_returns_true(VertxTestContext context) {

        AnyExp.par(TRUE.build(),
                   TRUE.build()
                  )
              .retry(limitRetries(4))
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

        AnyExp.seq(TRUE.build(),
                   TRUE.build()
                  )
              .retryEach(limitRetries(2))
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


        AnyExp.par(FALSE.build(),
                   FALSE.build()
                  )
              .retryEach(limitRetries(2))
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


        AnyExp.seq(FALSE.build(),
                   FALSE.build()
                  )
              .retryEach(limitRetries(2))
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


        AnyExp.par(TRUE.build(),
                   FALSE.build()
                  )
              .retryEach(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                         limitRetries(2)
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


        AnyExp.seq(TRUE.build(),
                   FALSE.build()
                  )
              .retryEach(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                         limitRetries(2)
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
    public void test_parallel_map(
            final VertxTestContext context,
            final Vertx vertx
                                 ) {

        AnyExp.par(VIO.succeed(true),
                   VIO.succeed(true)
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
    public void test_sequential_map(
            final VertxTestContext context,
            final Vertx vertx
                                   ) {

        AnyExp.seq(VIO.succeed(true),
                   VIO.succeed(true)
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


        AnyExp.par(TRUE.build(),
                   FALSE.build()
                  )
              .retryEach(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                         limitRetries(ATTEMPTS - 1)
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


        AnyExp.seq(TRUE.build(), FALSE.build())
              .retryEach(
                      Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                      limitRetries(ATTEMPTS - 1)
                        )
              .get()
              .onComplete(it -> {
                  context.verify(() -> Assertions.assertTrue(it.failed()));
                  context.completeNow();
              });

    }

    @Test
    public void test_parallel_retry_if_success_with_delay(final VertxTestContext context) {


        StubBuilder<Boolean> True = StubBuilder.ofGen(Gen.seq(counter -> counter <= 3 ?
                                                              VIO.fail(new IllegalArgumentException()) :
                                                              VIO.TRUE
                                                             )
                                                     );

        AnyExp.par(True.build(),
                   True.build()
                  )
              .retryEach(it -> it instanceof IllegalArgumentException,
                         limitRetries(3)
                                 .append(RetryPolicies.constantDelay(vertxRef.delay(Duration.ofMillis(100))))
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


        StubBuilder<Boolean> True = StubBuilder.ofGen(Gen.seq(counter -> counter <= 3 ?
                                                              VIO.fail(new IllegalArgumentException()) :
                                                              VIO.TRUE
                                                             )
                                                     );
        AnyExp.seq(True.build(),
                   True.build()
                  )
              .retryEach(it -> it instanceof IllegalArgumentException,
                         limitRetries(3)
                                 .append(RetryPolicies.constantDelay(vertxRef.delay(Duration.ofMillis(100))))
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
        AnyExp.par(TRUE.build(),
                   FALSE.build()
                  )
              .retryEach(limitRetries(ATTEMPTS)
                                 .append(RetryPolicies.constantDelay(vertxRef.delay(Duration.ofMillis(100))))
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
        AnyExp.seq(TRUE.build(),
                   FALSE.build()
                  )
              .retryEach(limitRetries(ATTEMPTS)
                                 .append(RetryPolicies.constantDelay(vertxRef.delay(Duration.ofMillis(100))))
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


}
