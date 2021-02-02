package vertx.effect.exp;

import io.vavr.Tuple5;
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

import static java.util.concurrent.TimeUnit.*;
import static vertx.effect.RetryPolicies.limitRetries;

@ExtendWith(VertxExtension.class)
public class TestQuintuple {
    final Supplier<Val<String>> a =
            new ValOrErrorMock<>(counter -> counter == 1 || counter == 2,
                             counter -> new RuntimeException("counter: " + counter),
                                 "a"
            );


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
    public void test_parallel_retries(VertxTestContext context) {

        final Supplier<Val<String>> val =
                new ValOrErrorMock<>(counter -> counter == 1 || counter == 2,
                                 counter -> new RuntimeException("counter: " + counter),
                                     "a"
                );

        Quintuple.parallel(val.get(),
                           val.get(),
                           val.get(),
                           val.get(),
                           val.get()
                          )
                 .retry(limitRetries(2))
                 .get()
                 .onComplete(it -> {
                     context.verify(() -> it.result()
                                            .equals(new Tuple5<>("a",
                                                                 "a",
                                                                 "a",
                                                                 "a",
                                                                 "a"
                                                    )
                                                   )
                                   );
                     context.completeNow();
                 });

    }

    @Test
    public void test_sequential_retries(VertxTestContext context) {

        final Supplier<Val<String>> val =
                new ValOrErrorMock<>(counter -> counter == 1 || counter == 2,
                                 counter -> new RuntimeException("counter: " + counter),
                                     "a"
                );

        Quintuple.sequential(val.get(),
                             val.get(),
                             val.get(),
                             val.get(),
                             val.get()
                            )
                 .retry(limitRetries(2))
                 .get()
                 .onComplete(it -> {
                     context.verify(() -> it.result()
                                            .equals(new Tuple5<>("a",
                                                                 "a",
                                                                 "a",
                                                                 "a",
                                                                 "a"
                                                    )
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

        Quintuple.parallel(val.get(),
                           val.get(),
                           val.get(),
                           val.get(),
                           val.get()
                          )
                 .retry(limitRetries(2)
                                .join(RetryPolicies.retryIf(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE))
                                     )
                       )
                 .get()
                 .onComplete(it -> {
                     context.verify(() -> Assertions.assertEquals(new Tuple5<>("a",
                                                                               "a",
                                                                               "a",
                                                                               "a",
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

        Quintuple.sequential(val.get(),
                             val.get(),
                             val.get(),
                             val.get(),
                             val.get()
                            )
                 .retry(limitRetries(2)
                                .join(RetryPolicies.retryIf(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE))
                                     )
                       )
                 .get()
                 .onComplete(it -> {
                     context.verify(() -> Assertions.assertEquals(new Tuple5<>("a",
                                                                               "a",
                                                                               "a",
                                                                               "a",
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

        Quintuple.parallel(val.get(),
                           val.get(),
                           val.get(),
                           val.get(),
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

        Quintuple.sequential(val.get(),
                             val.get(),
                             val.get(),
                             val.get(),
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
    public void test_parallel_quintuple_exp_map(VertxTestContext context) {

        Quintuple.parallel(Cons.success("a"),
                           Cons.success("b"),
                           Cons.success("c"),
                           Cons.success("d"),
                           Cons.success("e")
                          )
                 .map(pair -> pair.map((a, b, c, d, e) -> new Tuple5<>(a.toUpperCase(),
                                                                       b.toUpperCase(),
                                                                       c.toUpperCase(),
                                                                       d.toUpperCase(),
                                                                       e.toUpperCase()
                 )))
                 .onSuccess(r -> context.verify(() -> {
                     Assertions.assertEquals(new Tuple5<>("A",
                                                          "B",
                                                          "C",
                                                          "D",
                                                          "E"
                                             ),
                                             r
                                            );
                     context.completeNow();
                 }))
                 .get();
    }

    @Test
    public void test_sequential_quintuple_exp_map(VertxTestContext context) {

        Quintuple.sequential(Cons.success("a"),
                             Cons.success("b"),
                             Cons.success("c"),
                             Cons.success("d"),
                             Cons.success("e")
                            )
                 .map(pair -> pair.map((a, b, c, d, e) -> new Tuple5<>(a.toUpperCase(),
                                                                       b.toUpperCase(),
                                                                       c.toUpperCase(),
                                                                       d.toUpperCase(),
                                                                       e.toUpperCase()
                 )))
                 .onSuccess(r -> context.verify(() -> {
                     Assertions.assertEquals(new Tuple5<>("A",
                                                          "B",
                                                          "C",
                                                          "D",
                                                          "E"
                                             ),
                                             r
                                            );
                     context.completeNow();
                 }))
                 .get();
    }

    @Test
    public void test_parallel_quintuple_exp_flatmap_success(VertxTestContext context) {
        Quintuple.parallel(Cons.success("a"),
                           Cons.success("ab"),
                           Cons.success("abc"),
                           Cons.success("abcd"),
                           Cons.success("abcde")
                          )
                 .flatMap(pair -> Cons.success(pair.map((a, b, c, d, e) -> new Tuple5<>(a.length(),
                                                                                        b.length(),
                                                                                        c.length(),
                                                                                        d.length(),
                                                                                        e.length()
                                                        )
                                                       )))
                 .onSuccess(r -> context.verify(() -> {
                     Assertions.assertEquals(new Tuple5<>(1,
                                                          2,
                                                          3,
                                                          4,
                                                          5
                                             ),
                                             r
                                            );
                     context.completeNow();
                 }))
                 .get();


    }

    @Test
    public void test_sequential_quintuple_exp_flatmap_success(VertxTestContext context) {
        Quintuple.sequential(Cons.success("a"),
                             Cons.success("ab"),
                             Cons.success("abc"),
                             Cons.success("abcd"),
                             Cons.success("abcde")
                            )
                 .flatMap(pair -> Cons.success(pair.map((a, b, c, d, e) -> new Tuple5<>(a.length(),
                                                                                        b.length(),
                                                                                        c.length(),
                                                                                        d.length(),
                                                                                        e.length()
                                                        )
                                                       )))
                 .onSuccess(r -> context.verify(() -> {
                     Assertions.assertEquals(new Tuple5<>(1,
                                                          2,
                                                          3,
                                                          4,
                                                          5
                                             ),
                                             r
                                            );
                     context.completeNow();
                 }))
                 .get();


    }

    @Test
    public void test_parallel_quintuple_exp_flatmap_failure(VertxTestContext context) {

        Quintuple.parallel(Cons.success("a"),
                           Cons.success("ab"),
                           Cons.success("abc"),
                           Cons.success("abcd"),
                           Cons.success("abcde")
                          )
                 .flatMap(s -> Cons.failure(new RuntimeException()))
                 .onComplete(r -> context.verify(() -> {
                     Assertions.assertTrue(r.failed());
                     context.completeNow();
                 }))
                 .get();

    }

    @Test
    public void test_sequential_quintuple_exp_flatmap_failure(VertxTestContext context) {

        Quintuple.sequential(Cons.success("a"),
                             Cons.success("ab"),
                             Cons.success("abc"),
                             Cons.success("abcd"),
                             Cons.success("abcde")
                            )
                 .flatMap(s -> Cons.failure(new RuntimeException()))
                 .onComplete(r -> context.verify(() -> {
                     Assertions.assertTrue(r.failed());
                     context.completeNow();
                 }))
                 .get();

    }

    @Test
    public void test_parallel_quintuple_exp_fails_and_recover_with_success(VertxTestContext context) {

        Quintuple.parallel(a.get(),
                           a.get(),
                           a.get(),
                           a.get(),
                           a.get()
                          )
                 .recoverWith(e -> Cons.success(new Tuple5<>("",
                                                             "",
                                                             "",
                                                             "",
                                                             ""
                              ))
                             )
                 .onSuccess(map -> context.verify(() -> {
                     Assertions.assertEquals(new Tuple5<>("",
                                                          "",
                                                          "",
                                                          "",
                                                          ""
                                             ),
                                             map
                                            );
                     context.completeNow();
                 }))
                 .get();
    }

    @Test
    public void test_sequential_quintuple_exp_fails_and_recover_with_success(VertxTestContext context) {

        Quintuple.sequential(a.get(),
                             a.get(),
                             a.get(),
                             a.get(),
                             a.get()
                            )
                 .recoverWith(e -> Cons.success(new Tuple5<>("",
                                                             "",
                                                             "",
                                                             "",
                                                             ""
                              ))
                             )
                 .onSuccess(map -> context.verify(() -> {
                     Assertions.assertEquals(new Tuple5<>("",
                                                          "",
                                                          "",
                                                          "",
                                                          ""
                                             ),
                                             map
                                            );
                     context.completeNow();
                 }))
                 .get();
    }

    @Test
    public void test_parallel_quintuple_exp_fails_and_recover_with_failure(VertxTestContext context) {

        Quintuple.parallel(a.get(),
                           a.get(),
                           a.get(),
                           a.get(),
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
    public void test_sequential_quintuple_exp_fails_and_recover_with_failure(VertxTestContext context) {

        Quintuple.sequential(a.get(),
                             a.get(),
                             a.get(),
                             a.get(),
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
    public void test_parallel_quintuple_exp_recover_with_success(VertxTestContext context) {
        Quintuple.parallel(a.get(),
                           a.get(),
                           a.get(),
                           a.get(),
                           a.get()
                          )
                 .retry(limitRetries(2))
                 .recoverWith(e -> Cons.failure(new IllegalArgumentException()))
                 .onSuccess(map -> context.verify(() -> {
                     Assertions.assertEquals(new Tuple5<>("a",
                                                          "a",
                                                          "a",
                                                          "a",
                                                          "a"
                                             ),
                                             map
                                            );
                     context.completeNow();
                 }))
                 .get();
    }

    @Test
    public void test_sequential_quintuple_exp_recover_with_success(VertxTestContext context) {
        Quintuple.sequential(a.get(),
                             a.get(),
                             a.get(),
                             a.get(),
                             a.get()
                            )
                 .retry(limitRetries(2))
                 .recoverWith(e -> Cons.failure(new IllegalArgumentException()))
                 .onSuccess(map -> context.verify(() -> {
                     Assertions.assertEquals(new Tuple5<>("a",
                                                          "a",
                                                          "a",
                                                          "a",
                                                          "a"
                                             ),
                                             map
                                            );
                     context.completeNow();
                 }))
                 .get();
    }

    @Test
    public void test_parallel_retry_with_delay(VertxTestContext context) {
        int ATTEMPTS = 2;

        long start = System.nanoTime();

        Quintuple.parallel(a.get(),
                           a.get(),
                           a.get(),
                           a.get(),
                           a.get()
                          )
                 .retry(limitRetries(ATTEMPTS)
                                .join(RetryPolicies.constantDelay(vertxRef.delay(100,
                                                                                 MILLISECONDS
                                                                                )))
                       )
                 .get()
                 .onComplete(r -> context.verify(() -> {
                     Assertions.assertEquals(new Tuple5<>("a",
                                                          "a",
                                                          "a",
                                                          "a",
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

        Quintuple.sequential(a.get(),
                             a.get(),
                             a.get(),
                             a.get(),
                             a.get()
                            )
                 .retry(limitRetries(ATTEMPTS)
                                .join(RetryPolicies.constantDelay(vertxRef.delay(100,
                                                                                 MILLISECONDS
                                                                                )))
                       )
                 .get()
                 .onComplete(r -> context.verify(() -> {
                     Assertions.assertEquals(new Tuple5<>("a",
                                                          "a",
                                                          "a",
                                                          "a",
                                                          "a"
                                             ),
                                             r.result()
                                            );
                     Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= ATTEMPTS);
                     context.completeNow();

                 }));

    }
}
