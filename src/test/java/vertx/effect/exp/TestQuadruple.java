package vertx.effect.exp;

import io.vavr.Tuple4;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.*;
import vertxeffect.*;

import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

@ExtendWith(VertxExtension.class)
public class TestQuadruple {

    final Supplier<Val<String>> a =
            new ErrorWhile<>(counter -> counter == 1 || counter == 2,
                             counter -> new RuntimeException("counter: " + counter),
                             "a"
            );

    final Supplier<Val<String>> b =
            new ErrorWhile<>(counter -> counter == 1 || counter == 2,
                             counter -> new RuntimeException("counter: " + counter),
                             "b"
            );

    private static VertxRef vertxRef;

    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext testContext
                              ) {

        vertxRef = new VertxRef(vertx);
        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println);
        vertxRef.deploy(new RegisterJsValuesCodecs())
                .onComplete(event -> testContext.completeNow())
                .get();
    }

    @Test
    public void test_retries(VertxTestContext context) {

        final Supplier<Val<String>> a =
                new ErrorWhile<>(counter -> counter == 1 || counter == 2,
                                 counter -> new RuntimeException("counter: " + counter),
                                 "a"
                );

        Val<Tuple4<String, String, String, String>> val =
                Quadruple.of(a.get(),
                             a.get(),
                             a.get(),
                             a.get()
                            )
                         .retry(2);

        Verifiers.<Tuple4<String, String, String, String>>verifySuccess(tuple -> tuple.equals(new Tuple4<>("a",
                                                                                                           "a",
                                                                                                           "a",
                                                                                                           "a"
        ))).accept(val,
                   context
                  );


    }


    @Test
    public void test_retries_if_Success(VertxTestContext context) {

        final Supplier<Val<String>> val =
                new ErrorWhile<>(counter -> counter == 1 || counter == 2,
                                 counter -> Failures.GET_BAD_MESSAGE_EXCEPTION.apply("counter " + counter),
                                 "a"
                );

        Quadruple.of(val.get(),
                     val.get(),
                     val.get(),
                     val.get()
                    )
                 .retryIf(Failures.prism.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                          2
                         )
                 .get()
                 .onComplete(it -> {
                     context.verify(() -> Assertions.assertEquals(new Tuple4<>("a",
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
    public void test_retries_if_failure(VertxTestContext context) {

        final Supplier<Val<String>> val =
                new ErrorWhile<>(counter -> counter == 1 || counter == 2,
                                 counter -> new RuntimeException("counter " + counter),
                                 "a"
                );

        Quadruple.of(val.get(),
                     val.get(),
                     val.get(),
                     val.get()
                    )
                 .retryIf(Failures.prism.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
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
    public void test_quadruple_exp_map(VertxTestContext context) {

        Quadruple.of(Cons.success("a"),
                     Cons.success("ab"),
                     Cons.success("abc"),
                     Cons.success("abcd")
                    )
                 .map(pair -> pair.map((a, b, c, d) -> new Tuple4<>(a.length(),
                                                                    b.length(),
                                                                    c.length(),
                                                                    d.length()
                 )))
                 .onSuccess(r -> context.verify(() -> {
                     Assertions.assertEquals(new Tuple4<>(1,
                                                          2,
                                                          3,
                                                          4
                                             ),
                                             r
                                            );
                     context.completeNow();
                 }))
                 .get();

    }

    @Test
    public void test_quadruple_exp_flatmap_success(VertxTestContext context) {

        Quadruple.of(Cons.success("a"),
                     Cons.success("b"),
                     Cons.success("c"),
                     Cons.success("d")
                    )
                 .flatMap(pair -> Cons.success(pair.map((a, b, c, d) -> new Tuple4<>(a.toUpperCase(),
                                                                                     b.toUpperCase(),
                                                                                     c.toUpperCase(),
                                                                                     d.toUpperCase()
                                                        )
                                                       )))
                 .onSuccess(r -> context.verify(() -> {
                     Assertions.assertEquals(new Tuple4<>("A",
                                                          "B",
                                                          "C",
                                                          "D"
                                             ),
                                             r
                                            );
                     context.completeNow();
                 }))
                 .get();
    }

    @Test
    public void test_quadruple_exp_flatmap_failure(VertxTestContext context) {

        Quadruple.of(Cons.success("a"),
                     Cons.success("ab"),
                     Cons.success("abc"),
                     Cons.success("abcd")
                    )
                 .flatMap(s -> Cons.failure(new RuntimeException()))
                 .onComplete(r -> context.verify(() -> {
                     Assertions.assertTrue(r.failed());
                     context.completeNow();
                 }))
                 .get();

    }


    @Test
    public void test_quadruple_exp_fails_and_recover_with_success(VertxTestContext context) {

        Quadruple.of(a.get(),
                     b.get(),
                     b.get(),
                     a.get()
                    )
                 .recoverWith(e -> Cons.success(new Tuple4<>("",
                                                             "",
                                                             "",
                                                             ""
                              ))
                             )
                 .onSuccess(map -> context.verify(() -> {
                     Assertions.assertEquals(new Tuple4<>("",
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
    public void test_quadruple_exp_fails_and_recover_with_failure(VertxTestContext context) {

        Quadruple.of(a.get(),
                     b.get(),
                     b.get(),
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
    public void test_quadruple_exp_recover_with_success(VertxTestContext context) {
        Quadruple.of(a.get(),
                     b.get(),
                     b.get(),
                     a.get()
                    )
                 .retry(2)
                 .recoverWith(e -> Cons.failure(new IllegalArgumentException()))
                 .onSuccess(map -> context.verify(() -> {
                     Assertions.assertEquals(new Tuple4<>("a",
                                                          "b",
                                                          "b",
                                                          "a"
                                             ),
                                             map
                                            );
                     context.completeNow();
                 }))
                 .get();
    }

    @Test
    public void test_retry_with_delay(VertxTestContext context) {
        int ATTEMPTS = 3;

        long start = System.nanoTime();
        ErrorWhile<String> a = new ErrorWhile<>(counter -> counter <= ATTEMPTS,
                                                counter -> new RuntimeException("counter: " + counter),
                                                "a"
        );
        Quadruple.of(a.get(),
                     a.get(),
                     a.get(),
                     a.get()
                    )
                 .retry(ATTEMPTS,
                        (error, n) -> vertxRef.timer(1,
                                                     SECONDS,
                                                     "one sec"
                                                    )
                       )
                 .get()
                 .onComplete(r -> context.verify(() -> {
                     Assertions.assertEquals(new Tuple4<>("a",
                                                          "a",
                                                          "a",
                                                          "a"
                                             ),
                                             r.result()
                                            );
                     Assertions.assertTrue(NANOSECONDS.toSeconds(System.nanoTime() - start) >= ATTEMPTS);
                     context.completeNow();

                 }));

    }
}
