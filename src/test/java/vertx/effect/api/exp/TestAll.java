package vertx.effect.api.exp;

import fun.gen.Gen;
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
import vertx.effect.stub.StubBuilder;
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

        var trueValBuilder =
                StubBuilder.ofGen(Gen.seq(counter -> counter <= attempts ?
                                          VIO.fail(new RuntimeException("counter:+" + counter)) :
                                          VIO.TRUE
                                         )
                                 );

        AllExp.seq(trueValBuilder.build(),
                   trueValBuilder.build()
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



        var trueVal = StubBuilder.ofGen(Gen.seq(counter -> counter <= 2 ?
                                  VIO.fail(new RuntimeException("counter:+" + counter)) :
                                  VIO.TRUE
                                 )
                         );

        AllExp.seq(trueVal.build(),
                   trueVal.build()
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



        var trueVal = StubBuilder.ofGen(Gen.seq(counter -> counter <= 2 ?
                                                VIO.fail(new RuntimeException("counter:+" + counter)) :
                                                VIO.TRUE
                                               )
                                       );

        AllExp.par(trueVal.build(), trueVal.build())
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



        var trueVal = StubBuilder.ofGen(Gen.seq(counter -> counter <= 2 ?
                                                VIO.fail(new RuntimeException("counter:+" + counter)) :
                                                VIO.TRUE
                                               )
                                       );

        AllExp.par(trueVal.build(),
                   trueVal.build()
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
        var falseVal = StubBuilder.ofGen(Gen.seq(counter -> counter <= attempts ?
                                                VIO.fail(new RuntimeException("counter:+" + counter)) :
                                                VIO.FALSE
                                               )
                                       );
        AllExp.par(falseVal.build(), falseVal.build())
              .retryEach(limitRetries(attempts))
              .get()
              .onComplete(it -> {
                  context.verify(() -> Assertions.assertEquals(false, it.result()));
                  context.completeNow();
              });
    }

    @Test
    public void test_parallel_retry_returns_false(VertxTestContext context) {

        var falseVal = StubBuilder.ofGen(Gen.seq(counter -> counter <= 2 ?
                                                 VIO.fail(new RuntimeException("counter:+" + counter)) :
                                                 VIO.FALSE
                                                )
                                        );
        AllExp.par(falseVal.build(), falseVal.build())
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


        var falseVal = StubBuilder.ofGen(Gen.seq(counter -> counter <= attempts ?
                                                 VIO.fail(new RuntimeException("counter:+" + counter)) :
                                                 VIO.FALSE
                                                )
                                        );


        AllExp.seq(falseVal.build(),
                   falseVal.build()
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



        var falseVal = StubBuilder.ofGen(Gen.seq(counter -> counter <= 2 ?
                                                 VIO.fail(new RuntimeException("counter:+" + counter)) :
                                                 VIO.FALSE
                                                )
                                        );
        AllExp.seq(falseVal.build(),
                   falseVal.build()
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
        var trueVal = StubBuilder.ofGen(Gen.seq(counter -> counter <= attempts ?
                                                 VIO.fail(new RuntimeException("counter:+" + counter)) :
                                                 VIO.FALSE
                                                )
                                        );
        var falseVal = StubBuilder.ofGen(Gen.seq(counter -> counter <= attempts ?
                                                 VIO.fail(new RuntimeException("counter:+" + counter)) :
                                                 VIO.FALSE
                                                )
                                        );
        long start = System.nanoTime();
        AllExp.par(trueVal.build(),
                   falseVal.build()
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
        var trueVal = StubBuilder.ofGen(Gen.seq(counter -> counter <= 3 ?
                                                VIO.fail(new RuntimeException("counter:+" + counter)) :
                                                VIO.FALSE
                                               )
                                       );
        var falseVal = StubBuilder.ofGen(Gen.seq(counter -> counter <= 3 ?
                                                 VIO.fail(new RuntimeException("counter:+" + counter)) :
                                                 VIO.FALSE
                                                )
                                        );
        long start = System.nanoTime();
        AllExp.par(trueVal.build(),
                   falseVal.build()
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
        var trueVal = StubBuilder.ofGen(Gen.seq(counter -> counter <= attempts ?
                                                VIO.fail(new RuntimeException("counter:+" + counter)) :
                                                VIO.FALSE
                                               )
                                       );
        var falseVal = StubBuilder.ofGen(Gen.seq(counter -> counter <= attempts ?
                                                 VIO.fail(new RuntimeException("counter:+" + counter)) :
                                                 VIO.FALSE
                                                )
                                        );
        long start = System.nanoTime();
        AllExp.seq(trueVal.build(),
                   falseVal.build()
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
        var trueVal = StubBuilder.ofGen(Gen.seq(counter -> counter <= 3 ?
                                                VIO.fail(new RuntimeException("counter:+" + counter)) :
                                                VIO.FALSE
                                               )
                                       );
        var falseVal = StubBuilder.ofGen(Gen.seq(counter -> counter <= 3 ?
                                                 VIO.fail(new RuntimeException("counter:+" + counter)) :
                                                 VIO.FALSE
                                                )
                                        );
        long start = System.nanoTime();
        AllExp.seq(trueVal.build(),
                   falseVal.build()
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




        var trueVal = StubBuilder.ofGen(Gen.seq(counter -> counter <= 3 ?
                                                VIO.fail(new IllegalArgumentException()) :
                                                VIO.TRUE
                                               )
                                       );

        AllExp.par(trueVal.build(),
                   trueVal.build()
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


        var trueVal = StubBuilder.ofGen(Gen.seq(counter -> counter <= 3 ?
                                                VIO.fail(new IllegalArgumentException()) :
                                                VIO.TRUE
                                               )
                                       );
        AllExp.seq(trueVal.build(),
                   trueVal.build()
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


        var trueVal = StubBuilder.ofGen(Gen.seq(counter -> counter <= 3 ?
                                                VIO.fail(new IllegalArgumentException()) :
                                                VIO.TRUE
                                               )
                                       );
        AllExp.par(trueVal.build(),
                   trueVal.build()
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

        var trueVal = StubBuilder.ofGen(Gen.seq(counter -> counter <= 3 ?
                                                VIO.fail(new IllegalArgumentException()) :
                                                VIO.TRUE
                                               )
                                       );
        AllExp.seq(trueVal.build(),
                   trueVal.build()
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


        var trueVal = StubBuilder.ofGen(Gen.seq(counter -> counter <= 3 ?
                                                VIO.fail(new IllegalArgumentException()) :
                                                VIO.TRUE
                                               )
                                       );
        AllExp.seq(trueVal.build(),
                   trueVal.build()
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


        var trueVal = StubBuilder.ofGen(Gen.seq(counter -> counter <= 3 ?
                                                VIO.fail(new IllegalArgumentException()) :
                                                VIO.TRUE
                                               )
                                       );
        AllExp.par(trueVal.build(),
                   trueVal.build()
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
