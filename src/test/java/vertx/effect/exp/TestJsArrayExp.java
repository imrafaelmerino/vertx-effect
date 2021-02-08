package vertx.effect.exp;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsArray;
import jsonvalues.JsStr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.RegisterJsValuesCodecs;
import vertx.effect.RetryPolicies;
import vertx.effect.Val;
import vertx.effect.VertxRef;
import vertx.effect.mock.ValOrErrorMock;

import java.time.Duration;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static vertx.effect.RetryPolicies.limitRetries;

@ExtendWith(VertxExtension.class)
public class TestJsArrayExp {

    private static VertxRef vertxRef;

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
    public void test_array_exp_map(VertxTestContext context) {

        JsArrayExp.parallel(Val.succeed(JsStr.of("a")),
                            Val.succeed(JsStr.of("b"))
                           )
                  .map(arr -> arr.mapValues(value -> JsStr.prism.modify.apply(String::toUpperCase)
                                                                       .apply(value))
                      )
                  .onSuccess(r -> context.verify(() -> {
                      Assertions.assertEquals(JsArray.of("A",
                                                         "B"
                                                        ),
                                              r
                                             );
                      context.completeNow();
                  }))
                  .get();


    }

    @Test
    public void test_array_exp_flatmap_success(VertxTestContext context) {

        JsArrayExp.sequential(Val.succeed(JsStr.of("a")),
                              Val.succeed(JsStr.of("b"))
                             )
                  .flatMap(obj -> Val.succeed(obj.mapValues(value -> JsStr.prism.modify.apply(String::toUpperCase)
                                                                                       .apply(value)
                                                           ))
                          )
                  .onSuccess(r -> context.verify(() -> {
                      Assertions.assertEquals(JsArray.of("A",
                                                         "B"
                                                        ),
                                              r
                                             );
                      context.completeNow();
                  }))
                  .get();


    }

    @Test
    public void test_array_exp_flatmap_failure(VertxTestContext context) {
        JsArrayExp.parallel(Val.succeed(JsStr.of("a")),
                            Val.succeed(JsStr.of("b"))
                           )
                  .flatMap(s -> Val.fail(new RuntimeException()))
                  .onComplete(r -> context.verify(() -> {
                      Assertions.assertTrue(r.failed());
                      context.completeNow();
                  }))
                  .get();

    }

    @Test
    public void test_array_exp_retry_with_delay(VertxTestContext context) {
        int ATTEMPTS = 3;

        long start = System.nanoTime();
        ValOrErrorMock<JsStr> a = new ValOrErrorMock<>(ATTEMPTS,
                                                       counter -> new RuntimeException("counter: " + counter),
                                                       JsStr.of("a")
        );
        ValOrErrorMock<JsStr> b = new ValOrErrorMock<>(ATTEMPTS,
                                                       counter -> new RuntimeException("counter: " + counter),
                                                       JsStr.of("b")
        );

        JsArrayExp.parallel(a.get(),
                            b.get()
                           )
                  .retryEach(limitRetries(ATTEMPTS)
                                 .append(RetryPolicies.constantDelay(vertxRef.sleep(Duration.ofMillis(100))))
                        )
                  .get()
                  .onComplete(r -> context.verify(() -> {
                      Assertions.assertEquals(JsArray.of("a",
                                                         "b"
                                                        ),
                                              r.result()
                                             );
                      Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= ATTEMPTS);
                      context.completeNow();

                  }));

    }

    @Test
    public void test_array_exp_retry_if_with_delay_success(VertxTestContext context) {
        int ATTEMPTS = 3;

        long start = System.nanoTime();
        ValOrErrorMock<JsStr> a = new ValOrErrorMock<>(ATTEMPTS,
                                                       counter -> new RuntimeException("counter: " + counter),
                                                       JsStr.of("a")
        );
        ValOrErrorMock<JsStr> b = new ValOrErrorMock<>(ATTEMPTS,
                                                       counter -> new RuntimeException("counter: " + counter),
                                                       JsStr.of("b")
        );

        JsArrayExp.sequential(a.get(),
                              b.get()
                             )
                  .retryEach(e -> e instanceof RuntimeException,
                         limitRetries(ATTEMPTS)
                                 .append(RetryPolicies.constantDelay(vertxRef.sleep(Duration.ofMillis(100))))
                        )
                  .get()
                  .onComplete(r -> context.verify(() -> {
                      Assertions.assertEquals(JsArray.of("a",
                                                         "b"
                                                        ),
                                              r.result()
                                             );
                      Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= ATTEMPTS);
                      context.completeNow();

                  }));

    }

    @Test
    public void test_array_exp_retry_with_delay_success(VertxTestContext context) {
        int ATTEMPTS = 3;

        long start = System.nanoTime();
        ValOrErrorMock<JsStr> a = new ValOrErrorMock<>(ATTEMPTS,
                                                       counter -> new RuntimeException("counter: " + counter),
                                                       JsStr.of("a")
        );
        ValOrErrorMock<JsStr> b = new ValOrErrorMock<>(ATTEMPTS,
                                                       counter -> new RuntimeException("counter: " + counter),
                                                       JsStr.of("b")
        );

        JsArrayExp.sequential(a.get(),
                              b.get()
                             )
                  .retryEach(limitRetries(ATTEMPTS)
                                     .append(RetryPolicies.constantDelay(vertxRef.sleep(Duration.ofMillis(100))))
                            )
                  .get()
                  .onComplete(r -> context.verify(() -> {
                      Assertions.assertEquals(JsArray.of("a",
                                                         "b"
                                                        ),
                                              r.result()
                                             );
                      Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= ATTEMPTS);
                      context.completeNow();

                  }));

    }

    @Test
    public void test_array_exp_retry_if_success(VertxTestContext context) {
        int ATTEMPTS = 3;

        ValOrErrorMock<JsStr> a = new ValOrErrorMock<>(ATTEMPTS,
                                                       counter -> new RuntimeException("counter: " + counter),
                                                       JsStr.of("a")
        );
        ValOrErrorMock<JsStr> b = new ValOrErrorMock<>(ATTEMPTS,
                                                       counter -> new RuntimeException("counter: " + counter),
                                                       JsStr.of("b")
        );

        JsArrayExp.sequential(a.get(),
                              b.get()
                             )
                  .retryEach(e -> e instanceof RuntimeException,
                         limitRetries(ATTEMPTS)
                        )
                  .onComplete(r -> context.verify(() -> {
                      Assertions.assertEquals(JsArray.of("a",
                                                         "b"
                                                        ),
                                              r.result()
                                             );
                      context.completeNow();

                  }))
                  .get();

    }

    @Test
    public void test_array_exp_retryEach_with_delay_failure(VertxTestContext context) {
        int ATTEMPTS = 3;

        long start = System.nanoTime();
        ValOrErrorMock<JsStr> a = new ValOrErrorMock<>(ATTEMPTS,
                                                       counter -> new RuntimeException("counter: " + counter),
                                                       JsStr.of("a")
        );
        ValOrErrorMock<JsStr> b = new ValOrErrorMock<>(ATTEMPTS,
                                                       counter -> new RuntimeException("counter: " + counter),
                                                       JsStr.of("b")
        );

        JsArrayExp.parallel(a.get(),
                            b.get()
                           )
                  .retryEach(limitRetries(ATTEMPTS - 1)
                                 .append(RetryPolicies.constantDelay(vertxRef.sleep(Duration.ofMillis(100))))
                        )
                  .onComplete(r -> context.verify(() -> {
                      Assertions.assertTrue(r.cause() instanceof RuntimeException);
                      Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= ATTEMPTS - 1);
                      context.completeNow();
                  }))
                  .get();

    }

    @Test
    public void test_array_exp_retry_with_delay_failure(VertxTestContext context) {
        int ATTEMPTS = 3;

        long start = System.nanoTime();
        ValOrErrorMock<JsStr> a = new ValOrErrorMock<>(ATTEMPTS,
                                                       counter -> new RuntimeException("counter: " + counter),
                                                       JsStr.of("a")
        );
        ValOrErrorMock<JsStr> b = new ValOrErrorMock<>(ATTEMPTS,
                                                       counter -> new RuntimeException("counter: " + counter),
                                                       JsStr.of("b")
        );

        JsArrayExp.parallel(a.get(),
                            b.get()
                           )
                  .retry(limitRetries(2)
                                     .append(RetryPolicies.constantDelay(vertxRef.sleep(Duration.ofMillis(100))))
                            )
                  .onComplete(r -> context.verify(() -> {
                      Assertions.assertTrue(r.cause() instanceof RuntimeException);
                      Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= 2);
                      context.completeNow();
                  }))
                  .get();

    }

    @Test
    public void test_array_exp_retry_if_with_delay_failure(VertxTestContext context) {
        int ATTEMPTS = 3;

        long start = System.nanoTime();
        ValOrErrorMock<JsStr> a = new ValOrErrorMock<>(ATTEMPTS,
                                                       counter -> new RuntimeException("counter: " + counter),
                                                       JsStr.of("a")
        );
        ValOrErrorMock<JsStr> b = new ValOrErrorMock<>(ATTEMPTS,
                                                       counter -> new RuntimeException("counter: " + counter),
                                                       JsStr.of("b")
        );

        JsArrayExp.parallel(a.get(),
                            b.get()
                           )
                  .retryEach(e -> e instanceof RuntimeException,
                         limitRetries(ATTEMPTS - 1)
                                 .append(RetryPolicies.constantDelay(vertxRef.sleep(Duration.ofMillis(100))))
                        )
                  .get()
                  .onComplete(r -> context.verify(() -> {
                      Assertions.assertTrue(r.cause() instanceof RuntimeException);
                      Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= ATTEMPTS - 1);
                      context.completeNow();

                  }));

    }

    @Test
    public void test_race(final VertxTestContext context) {
        Val<JsStr> a = Val.succeed(JsStr.of("a"));
        Val<JsStr> b = Val.succeed(JsStr.of("b"));
        JsArrayExp.parallel()
                  .append(a)
                  .append(b)
                  .race()
                  .onSuccess(it -> context.verify(() -> {
                      Assertions.assertEquals(JsStr.of("a"),
                                              it
                                             );
                      context.completeNow();
                  }))
                  .get();
    }

}
