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
import vertx.effect.Val;
import vertx.effect.VertxRef;

import static java.util.concurrent.TimeUnit.*;

@ExtendWith(VertxExtension.class)
public class TestJsArrayVal {

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

        JsArrayVal.parallel(Cons.success(JsStr.of("a")),
                            Cons.success(JsStr.of("b"))
                           )
                  .map(arr -> arr.mapValues(p -> JsStr.prism.modify.apply(String::toUpperCase)
                                                                   .apply(p.value))
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

        JsArrayVal.sequential(Cons.success(JsStr.of("a")),
                              Cons.success(JsStr.of("b"))
                             )
                  .flatMap(obj -> Cons.success(obj.mapValues(p -> JsStr.prism.modify.apply(String::toUpperCase)
                                                                                    .apply(p.value)
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
        JsArrayVal.parallel(Cons.success(JsStr.of("a")),
                            Cons.success(JsStr.of("b"))
                           )
                  .flatMap(s -> Cons.failure(new RuntimeException()))
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
        ErrorWhile<JsStr> a = new ErrorWhile<>(ATTEMPTS,
                                               counter -> new RuntimeException("counter: " + counter),
                                               JsStr.of("a")
        );
        ErrorWhile<JsStr> b = new ErrorWhile<>(ATTEMPTS,
                                               counter -> new RuntimeException("counter: " + counter),
                                               JsStr.of("b")
        );

        JsArrayVal.parallel(a.get(),
                            b.get()
                           )
                  .retry(ATTEMPTS,
                         (error, n) -> vertxRef.timer(100,
                                                      MILLISECONDS
                                                     )
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
        ErrorWhile<JsStr> a = new ErrorWhile<>(ATTEMPTS,
                                               counter -> new RuntimeException("counter: " + counter),
                                               JsStr.of("a")
        );
        ErrorWhile<JsStr> b = new ErrorWhile<>(ATTEMPTS,
                                               counter -> new RuntimeException("counter: " + counter),
                                               JsStr.of("b")
        );

        JsArrayVal.sequential(a.get(),
                              b.get()
                             )
                  .retryIf(e -> e instanceof RuntimeException,
                           ATTEMPTS,
                           (error, n) -> vertxRef.timer(100,
                                                       MILLISECONDS
                                                       )
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
        ErrorWhile<JsStr> a = new ErrorWhile<>(ATTEMPTS,
                                               counter -> new RuntimeException("counter: " + counter),
                                               JsStr.of("a")
        );
        ErrorWhile<JsStr> b = new ErrorWhile<>(ATTEMPTS,
                                               counter -> new RuntimeException("counter: " + counter),
                                               JsStr.of("b")
        );

        JsArrayVal.sequential(a.get(),
                              b.get()
                             )
                  .retry(ATTEMPTS,
                         (error, n) -> vertxRef.timer(100,
                                                      MILLISECONDS
                                                     )
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

        ErrorWhile<JsStr> a = new ErrorWhile<>(ATTEMPTS,
                                               counter -> new RuntimeException("counter: " + counter),
                                               JsStr.of("a")
        );
        ErrorWhile<JsStr> b = new ErrorWhile<>(ATTEMPTS,
                                               counter -> new RuntimeException("counter: " + counter),
                                               JsStr.of("b")
        );

        JsArrayVal.sequential(a.get(),
                              b.get()
                             )
                  .retryIf(e -> e instanceof RuntimeException,
                           ATTEMPTS
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
    public void test_array_exp_retry_with_delay_failure(VertxTestContext context) {
        int ATTEMPTS = 3;

        long start = System.nanoTime();
        ErrorWhile<JsStr> a = new ErrorWhile<>(ATTEMPTS,
                                               counter -> new RuntimeException("counter: " + counter),
                                               JsStr.of("a")
        );
        ErrorWhile<JsStr> b = new ErrorWhile<>(ATTEMPTS,
                                               counter -> new RuntimeException("counter: " + counter),
                                               JsStr.of("b")
        );

        JsArrayVal.parallel(a.get(),
                            b.get()
                           )
                  .retry(ATTEMPTS - 1,
                         (error, n) -> vertxRef.timer(100,
                                                      MILLISECONDS
                                                     )
                        )
                  .onComplete(r -> context.verify(() -> {
                      Assertions.assertTrue(r.cause() instanceof RuntimeException);
                      Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= ATTEMPTS - 1);
                      context.completeNow();
                  }))
                  .get();

    }

    @Test
    public void test_array_exp_retry_if_with_delay_failure(VertxTestContext context) {
        int ATTEMPTS = 3;

        long start = System.nanoTime();
        ErrorWhile<JsStr> a = new ErrorWhile<>(ATTEMPTS,
                                               counter -> new RuntimeException("counter: " + counter),
                                               JsStr.of("a")
        );
        ErrorWhile<JsStr> b = new ErrorWhile<>(ATTEMPTS,
                                               counter -> new RuntimeException("counter: " + counter),
                                               JsStr.of("b")
        );

        JsArrayVal.parallel(a.get(),
                            b.get()
                           )
                  .retryIf(e -> e instanceof RuntimeException,
                           ATTEMPTS - 1,
                           (error, n) -> vertxRef.timer(100,
                                                       MILLISECONDS
                                                       )
                          )
                  .get()
                  .onComplete(r -> context.verify(() -> {
                      Assertions.assertTrue(r.cause() instanceof RuntimeException);
                      Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= ATTEMPTS - 1);
                      context.completeNow();

                  }));

    }

    @Test
    public void test_race(final Vertx vertx,
                          final VertxTestContext context) {
        Val<JsStr> a = Cons.of(() -> Future.succeededFuture(JsStr.of("a")));
        Val<JsStr> b = Cons.of(() -> Future.succeededFuture(JsStr.of("b")));
        JsArrayVal.parallel()
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
