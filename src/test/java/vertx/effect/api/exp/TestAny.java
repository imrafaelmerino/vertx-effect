package vertx.effect.api.exp;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.*;
import vertx.effect.stub.VIOStub;
import vertx.values.codecs.RegisterJsValuesCodecs;

import java.time.Duration;


import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static vertx.effect.RetryPolicies.limitRetries;
@SuppressWarnings("ReturnValueIgnored")
@ExtendWith(VertxExtension.class)
public class TestAny {
    static VertxRef vertxRef;
    static final int ATTEMPTS = 2;
    static final VIOStub<Boolean> TRUE = VIOStub.failThenSucceed(
            counter ->
                    counter <= ATTEMPTS ?
                            Failures.GET_BAD_MESSAGE_EXCEPTION.apply("counter " + counter) :
                            null,
            true
                                                                );


    static final VIOStub<Boolean> FALSE =
            VIOStub.failThenSucceed(
                    counter ->
                            counter <= ATTEMPTS ?
                                    Failures.GET_BAD_MESSAGE_EXCEPTION.apply("counter " + counter) : null,
                    false
                                   );

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

        AnyExp.par(TRUE.get(),
                   TRUE.get()
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

        AnyExp.par(TRUE.get(),
                   TRUE.get()
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

        AnyExp.seq(TRUE.get(),
                   TRUE.get()
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


        AnyExp.par(FALSE.get(),
                   FALSE.get()
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


        AnyExp.seq(FALSE.get(),
                   FALSE.get()
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


        AnyExp.par(TRUE.get(),
                   FALSE.get()
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


        AnyExp.seq(TRUE.get(),
                   FALSE.get()
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


        AnyExp.par(TRUE.get(),
                   FALSE.get()
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


        AnyExp.seq(TRUE.get(), FALSE.get())
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


        VIOStub<Boolean> True = VIOStub.failThenSucceed(
                counter ->
                        counter <= 3 ?
                                new IllegalArgumentException() : null,
                true
                                                       );
        AnyExp.par(True.get(),
                   True.get()
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


        VIOStub<Boolean> True = VIOStub.failThenSucceed(
                counter ->
                        counter <= 3 ?
                                new IllegalArgumentException() : null,
                true
                                                       );
        AnyExp.seq(True.get(),
                   True.get()
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
        AnyExp.par(TRUE.get(),
                   FALSE.get()
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
        AnyExp.seq(TRUE.get(),
                   FALSE.get()
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
