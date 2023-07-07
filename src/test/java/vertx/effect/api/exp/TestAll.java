package vertx.effect.api.exp;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.AllExp;
import vertx.effect.RetryPolicies;
import vertx.effect.VIO;
import vertx.effect.VertxRef;
import vertx.effect.stub.VIOStub;
import vertx.values.codecs.RegisterJsValuesCodecs;

import java.time.Duration;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static vertx.effect.RetryPolicies.limitRetries;
@SuppressWarnings("ReturnValueIgnored")
@ExtendWith(VertxExtension.class)
public class TestAll {
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
    public void test_sequential_retryEach_returns_true(VertxTestContext context) {
        int attempts = 2;

        VIOStub<Boolean> trueVal =
                VIOStub.failThenSucceed(counter -> counter <= attempts ? new RuntimeException("counter:+" + counter) : null, true);

        AllExp.seq(trueVal.get(),
                   trueVal.get()
                  )
              .retryEach(limitRetries(attempts))
              .get()
              .onComplete(it -> {
                  context.verify(() -> Assertions.assertEquals(true,
                                                               it.result()
                                                              ));
                  context.completeNow();
              });
    }

    @Test
    public void test_sequential_retry_returns_true(VertxTestContext context) {

        VIOStub<Boolean> trueVal =
                VIOStub.failThenSucceed(counter -> counter <= 2 ? new RuntimeException("counter:+" + counter) : null,
                                        true
                                       );

        AllExp.seq(trueVal.get(),
                   trueVal.get()
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
    public void test_parallel_retryEach_returns_true(VertxTestContext context) {
        int attempts = 2;

        VIOStub<Boolean> trueVal =
                VIOStub.failThenSucceed(
                        counter -> counter <= attempts ? new RuntimeException("counter:+" + counter) : null,
                        true
                                       );

        AllExp.par(trueVal.get(), trueVal.get())
              .retryEach(limitRetries(attempts))
              .get()
              .onComplete(it -> {
                  context.verify(() -> Assertions.assertEquals(true,
                                                               it.result()
                                                              ));
                  context.completeNow();
              });
    }

    @Test
    public void test_parallel_retry_returns_true(VertxTestContext context) {

        VIOStub<Boolean> trueVal =
                VIOStub.failThenSucceed(
                        counter -> counter <= 2 ? new RuntimeException("counter:+" + counter) : null,
                        true
                                       );

        AllExp.par(trueVal.get(),
                   trueVal.get()
                  )
              .retryEach(limitRetries(4))
              .get()
              .onComplete(it -> {
                  context.verify(() -> Assertions.assertEquals(true,
                                                               it.result()
                                                              ));
                  context.completeNow();
              });
    }

    @Test
    public void test_parallel_retryEach_returns_false(VertxTestContext context) {

        int attempts = 2;
        VIOStub<Boolean> falseVal =
                VIOStub.failThenSucceed(counter -> counter <= attempts ? new RuntimeException("counter:+" + counter) : null, false);


        AllExp.par(falseVal.get(), falseVal.get())
              .retryEach(limitRetries(attempts))
              .get()
              .onComplete(it -> {
                  context.verify(() -> Assertions.assertEquals(false, it.result()));
                  context.completeNow();
              });
    }

    @Test
    public void test_parallel_retry_returns_false(VertxTestContext context) {

        VIOStub<Boolean> falseVal =
                VIOStub.failThenSucceed(counter -> counter <= 2 ? new RuntimeException("counter:+" + counter) : null, false);


        AllExp.par(falseVal.get(), falseVal.get())
              .retry(limitRetries(4))
              .get()
              .onComplete(it -> {
                  context.verify(() -> Assertions.assertEquals(false,
                                                               it.result()
                                                              ));
                  context.completeNow();
              });
    }

    @Test
    public void test_sequential_retryEach_returns_false(VertxTestContext context) {

        int attempts = 2;
        VIOStub<Boolean> falseVal =
                VIOStub.failThenSucceed(counter -> counter <= attempts ? new RuntimeException("counter:+" + counter) : null,
                                        false
                                       );


        AllExp.seq(falseVal.get(),
                   falseVal.get()
                  )
              .retryEach(limitRetries(attempts))
              .get()
              .onComplete(it -> {
                  context.verify(() -> Assertions.assertEquals(false,
                                                               it.result()
                                                              ));
                  context.completeNow();
              });
    }

    @Test
    public void test_sequential_retry_returns_false(VertxTestContext context) {

        VIOStub<Boolean> falseVal =
                VIOStub.failThenSucceed(counter -> counter <= 2 ? new RuntimeException("counter:+" + counter) : null,
                                        false
                                       );


        AllExp.seq(falseVal.get(),
                   falseVal.get()
                  )
              .retryEach(limitRetries(4))
              .get()
              .onComplete(it -> {
                  context.verify(() -> Assertions.assertEquals(false,
                                                               it.result()
                                                              ));
                  context.completeNow();
              });
    }

    @Test
    public void test_parallel_retryEach_with_delay(VertxTestContext context) {
        int attempts = 3;
        VIOStub<Boolean> trueVal =
                VIOStub.failThenSucceed(counter -> counter <= attempts ? new RuntimeException("counter:+" + counter) : null,
                                        true
                                       );

        VIOStub<Boolean> falseVal =
                VIOStub.failThenSucceed(counter -> counter <= attempts ? new RuntimeException("counter:+" + counter) : null,
                                        false
                                       );
        long start = System.nanoTime();
        AllExp.par(trueVal.get(),
                   falseVal.get()
                  )
              .retryEach(limitRetries(attempts)
                                 .append(RetryPolicies.constantDelay(vertxRef.delay(Duration.ofMillis(100))))
                        )
              .get()
              .onComplete(r -> context.verify(() -> {
                  Assertions.assertFalse(r.result());
                  Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= attempts);
                  context.completeNow();
              }));

    }

    @Test
    public void test_parallel_retry_with_delay(VertxTestContext context) {
        VIOStub<Boolean> trueVal =
                VIOStub.failThenSucceed(counter -> counter <= 3 ? new RuntimeException("counter:+" + counter) : null,
                                        true
                                       );

        VIOStub<Boolean> falseVal =
                VIOStub.failThenSucceed(counter -> counter <= 3 ? new RuntimeException("counter:+" + counter) : null,
                                        false
                                       );
        long start = System.nanoTime();
        AllExp.par(trueVal.get(),
                   falseVal.get()
                  )
              .retry(limitRetries(6)
                             .append(RetryPolicies.constantDelay(vertxRef.delay(Duration.ofMillis(100))))
                    )
              .get()
              .onComplete(r -> context.verify(() -> {
                  Assertions.assertFalse(r.result());
                  Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= 6);
                  context.completeNow();
              }));

    }

    @Test
    public void test_sequential_retryEach_with_delay(VertxTestContext context) {
        int attempts = 3;
        VIOStub<Boolean> trueVal =
                VIOStub.failThenSucceed(counter -> counter <= attempts ? new RuntimeException("counter:+" + counter) : null, true);

        VIOStub<Boolean> falseVal =
                VIOStub.failThenSucceed(counter -> counter <= attempts ? new RuntimeException("counter:+" + counter) : null,
                                        false
                                       );
        long start = System.nanoTime();
        AllExp.seq(trueVal.get(),
                   falseVal.get()
                  )
              .retryEach(limitRetries(attempts)
                                 .append(RetryPolicies.constantDelay(vertxRef.delay(Duration.ofMillis(100))))
                        )
              .get()
              .onComplete(r -> context.verify(() -> {
                  Assertions.assertFalse(r.result());
                  Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= attempts);
                  context.completeNow();
              }));

    }

    @Test
    public void test_sequential_retry_with_delay(VertxTestContext context) {
        VIOStub<Boolean> trueVal =
                VIOStub.failThenSucceed(counter -> counter <= 3 ? new RuntimeException("counter:+" + counter) : null,
                                        true
                                       );

        VIOStub<Boolean> falseVal =
                VIOStub.failThenSucceed(counter -> counter <= 3 ? new RuntimeException("counter:+" + counter) : null,
                                        false
                                       );
        long start = System.nanoTime();
        AllExp.seq(trueVal.get(),
                   falseVal.get()
                  )
              .retry(limitRetries(6)
                             .append(RetryPolicies.constantDelay(vertxRef.delay(Duration.ofMillis(100))))
                    )
              .get()
              .onComplete(r -> context.verify(() -> {
                  Assertions.assertFalse(r.result());
                  Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= 6);
                  context.completeNow();
              }));

    }

    @Test
    public void test_sequential_map(final VertxTestContext context) {

        AllExp.seq(VIO.succeed(true),
                   VIO.succeed(true)
                  )
              .map(it -> !it)
              .onSuccess(result -> context.verify(() -> {
                  Assertions.assertFalse(result);
                  context.completeNow();
              }))
              .get();
    }

    @Test
    public void test_parallel_map(final VertxTestContext context) {

        AllExp.par(VIO.succeed(true),
                   VIO.succeed(true)
                  )
              .map(it -> !it)
              .onSuccess(result -> context.verify(() -> {
                  Assertions.assertFalse(result);
                  context.completeNow();
              }))
              .get();
    }

    @Test
    public void test_parallel_retry_if_success(final VertxTestContext context) {


        VIOStub<Boolean> True = VIOStub.failThenSucceed(
                counter -> counter <= 3 ? new IllegalArgumentException() : null,
                true
                                                       );
        AllExp.par(True.get(),
                   True.get()
                  )
              .retryEach(it -> it instanceof IllegalArgumentException,
                         limitRetries(3)
                        )
              .onSuccess(it -> context.verify(() -> {
                  Assertions.assertTrue(it);
                  context.completeNow();
              }))
              .get();
    }

    @Test
    public void test_sequential_retry_if_success(final VertxTestContext context) {


        VIOStub<Boolean> True = VIOStub.failThenSucceed(counter -> counter <= 3 ? new IllegalArgumentException() : null,
                                                        true
                                                       );
        AllExp.seq(True.get(),
                   True.get()
                  )
              .retryEach(it -> it instanceof IllegalArgumentException,
                         limitRetries(3)
                        )
              .onSuccess(it -> context.verify(() -> {
                  Assertions.assertTrue(it);
                  context.completeNow();
              }))
              .get();
    }

    @Test
    public void test_parallel_retry_if_success_with_delay(final VertxTestContext context) {


        VIOStub<Boolean> True = VIOStub.failThenSucceed(counter -> counter <= 3 ? new IllegalArgumentException() : null,
                                                        true
                                                       );
        AllExp.par(True.get(),
                   True.get()
                  )
              .retryEach(it -> it instanceof IllegalArgumentException,
                         limitRetries(3)
                                 .append(RetryPolicies.constantDelay(vertxRef.delay(Duration.ofMillis(100))))
                        )
              .onSuccess(it -> context.verify(() -> {
                  Assertions.assertTrue(it);
                  context.completeNow();
              }))
              .get();
    }

    @Test
    public void test_sequential_retry_if_success_with_delay(final VertxTestContext context) {


        VIOStub<Boolean> True = VIOStub.failThenSucceed(counter -> counter <= 3 ? new IllegalArgumentException() : null,
                                                        true
                                                       );
        AllExp.seq(True.get(),
                   True.get()
                  )
              .retryEach(it -> it instanceof IllegalArgumentException,
                         limitRetries(3)
                                 .append(RetryPolicies.constantDelay(vertxRef.delay(Duration.ofMillis(100))))
                        )
              .onSuccess(it -> context.verify(() -> {
                  Assertions.assertTrue(it);
                  context.completeNow();
              }))
              .get();
    }

    @Test
    public void test_sequential_retry_if_failure(final VertxTestContext context) {


        VIOStub<Boolean> True = VIOStub.failThenSucceed(counter -> counter <= 3 ? new IllegalArgumentException() : null,
                                                        true
                                                       );
        AllExp.seq(True.get(),
                   True.get()
                  )
              .retryEach(it -> it instanceof IllegalArgumentException,
                         limitRetries(2)
                        )
              .onComplete(it -> context.verify(() -> {
                  Assertions.assertTrue(it.cause() instanceof IllegalArgumentException);
                  context.completeNow();
              }))
              .get();
    }

    @Test
    public void test_parallel_retry_if_failure(final VertxTestContext context) {


        VIOStub<Boolean> True = VIOStub.failThenSucceed(counter -> counter <= 3 ? new IllegalArgumentException() : null,
                                                        true
                                                       );
        AllExp.par(True.get(),
                   True.get()
                  )
              .retryEach(it -> it instanceof IllegalArgumentException,
                         limitRetries(2)
                        )
              .onComplete(it -> context.verify(() -> {
                  Assertions.assertTrue(it.cause() instanceof IllegalArgumentException);
                  context.completeNow();
              }))
              .get();
    }
}
