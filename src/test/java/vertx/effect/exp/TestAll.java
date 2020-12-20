package vertx.effect.exp;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.RegisterJsValuesCodecs;
import vertx.effect.Val;
import vertx.effect.VertxRef;
import vertx.effect.mock.ValOrErrorMock;

import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.*;

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
    public void test_sequential_retry_returns_true(VertxTestContext context) {
        int attempts = 2;

        Supplier<Val<Boolean>> trueVal =
                new ValOrErrorMock<>(attempts,
                                 counter -> new RuntimeException("counter:+" + counter),
                                     true
                );

        All.sequential(trueVal.get(),
                       trueVal.get()
                      )
           .retry(attempts)
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
        int attempts = 2;

        Supplier<Val<Boolean>> trueVal =
                new ValOrErrorMock<>(attempts,
                                 counter -> new RuntimeException("counter:+" + counter),
                                     true
                );

        All.parallel(trueVal.get(),
                     trueVal.get()
                    )
           .retry(attempts)
           .get()
           .onComplete(it -> {
               context.verify(() -> Assertions.assertEquals(true,
                                                            it.result()
                                                           ));
               context.completeNow();
           });
    }

    @Test
    public void test_parallel_retry_returns_false(VertxTestContext context) {

        int attempts = 2;
        Supplier<Val<Boolean>> falseVal =
                new ValOrErrorMock<>(attempts,
                                 counter -> new RuntimeException("counter:+" + counter),
                                     false
                );


        All.parallel(falseVal.get(),
                     falseVal.get()
                    )
           .retry(attempts)
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

        int attempts = 2;
        Supplier<Val<Boolean>> falseVal =
                new ValOrErrorMock<>(attempts,
                                 counter -> new RuntimeException("counter:+" + counter),
                                     false
                );


        All.sequential(falseVal.get(),
                       falseVal.get()
                      )
           .retry(attempts)
           .get()
           .onComplete(it -> {
               context.verify(() -> Assertions.assertEquals(false,
                                                            it.result()
                                                           ));
               context.completeNow();
           });
    }

    @Test
    public void test_parallel_retry_with_delay(VertxTestContext context) {
        int attempts = 3;
        Supplier<Val<Boolean>> trueVal =
                new ValOrErrorMock<>(attempts,
                                 counter -> new RuntimeException("counter:+" + counter),
                                     true
                );

        Supplier<Val<Boolean>> falseVal =
                new ValOrErrorMock<>(attempts,
                                 counter -> new RuntimeException("counter:+" + counter),
                                     false
                );
        long start = System.nanoTime();
        All.parallel(trueVal.get(),
                     falseVal.get()
                    )
           .retry(attempts,
                  (error, n) -> vertxRef.delay(100,
                                               MILLISECONDS
                                              )
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
        int attempts = 3;
        Supplier<Val<Boolean>> trueVal =
                new ValOrErrorMock<>(attempts,
                                 counter -> new RuntimeException("counter:+" + counter),
                                     true
                );

        Supplier<Val<Boolean>> falseVal =
                new ValOrErrorMock<>(attempts,
                                 counter -> new RuntimeException("counter:+" + counter),
                                     false
                );
        long start = System.nanoTime();
        All.sequential(trueVal.get(),
                       falseVal.get()
                      )
           .retry(attempts,
                  (error, n) -> vertxRef.delay(100,
                                               MILLISECONDS
                                              )
                 )
           .get()
           .onComplete(r -> context.verify(() -> {
               Assertions.assertFalse(r.result());
               Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= attempts);
               context.completeNow();
           }));

    }

    @Test
    public void test_sequential_map(final VertxTestContext context,
                                    final Vertx vertx) {

        All.sequential(Cons.success(true),
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
    public void test_parallel_map(final VertxTestContext context,
                                  final Vertx vertx) {

        All.parallel(Cons.success(true),
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
    public void test_parallel_retry_if_success(final VertxTestContext context) {


        ValOrErrorMock<Boolean> True = new ValOrErrorMock<>(3,
                                                    i -> new IllegalArgumentException(),
                                                            true
        );
        All.parallel(True.get(),
                     True.get()
                    )
           .retry(it -> it instanceof IllegalArgumentException,
                  3
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
    public void test_sequential_retry_if_success(final VertxTestContext context) {


        ValOrErrorMock<Boolean> True = new ValOrErrorMock<>(3,
                                                    i -> new IllegalArgumentException(),
                                                            true
        );
        All.sequential(True.get(),
                       True.get()
                      )
           .retry(it -> it instanceof IllegalArgumentException,
                  3
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
    public void test_parallel_retry_if_success_with_delay(final VertxTestContext context) {


        ValOrErrorMock<Boolean> True = new ValOrErrorMock<>(3,
                                                    i -> new IllegalArgumentException(),
                                                            true
        );
        All.parallel(True.get(),
                     True.get()
                    )
           .retry(it -> it instanceof IllegalArgumentException,
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


        ValOrErrorMock<Boolean> True = new ValOrErrorMock<>(3,
                                                    i -> new IllegalArgumentException(),
                                                            true
        );
        All.sequential(True.get(),
                       True.get()
                      )
           .retry(it -> it instanceof IllegalArgumentException,
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
    public void test_sequential_retry_if_failure(final VertxTestContext context) {


        ValOrErrorMock<Boolean> True = new ValOrErrorMock<>(3,
                                                    i -> new IllegalArgumentException(),
                                                            true
        );
        All.sequential(True.get(),
                       True.get()
                      )
           .retry(it -> it instanceof IllegalArgumentException,
                  2
                 )
           .onComplete(it -> {
               context.verify(() -> {
                   Assertions.assertTrue(it.cause() instanceof IllegalArgumentException);
                   context.completeNow();
               });
           })
           .get();
    }

    @Test
    public void test_parallel_retry_if_failure(final VertxTestContext context) {


        ValOrErrorMock<Boolean> True = new ValOrErrorMock<>(3,
                                                    i -> new IllegalArgumentException(),
                                                            true
        );
        All.parallel(True.get(),
                     True.get()
                    )
           .retry(it -> it instanceof IllegalArgumentException,
                  2
                 )
           .onComplete(it -> {
               context.verify(() -> {
                   Assertions.assertTrue(it.cause() instanceof IllegalArgumentException);
                   context.completeNow();
               });
           })
           .get();
    }
}
