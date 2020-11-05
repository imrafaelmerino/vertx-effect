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
import vertx.effect.VertxRef;
import vertx.effect.Val;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.*;
import static vertx.effect.exp.Cons.FALSE;
import static vertx.effect.exp.Cons.TRUE;

@ExtendWith(VertxExtension.class)
public class TestCond {

    private static VertxRef vertxRef;
    private static BiFunction<Throwable, Integer, Val<Void>> delay;
    private static final int ATTEMPTS = 2;
    private static Supplier<Val<String>> a =
            new ErrorWhile<>(ATTEMPTS,
                             counter -> new RuntimeException("counter:+" + counter),
                             "a"
            );
    private static Supplier<Val<String>> b =
            new ErrorWhile<>(ATTEMPTS,
                             counter -> new RuntimeException("counter:+" + counter),
                             "b"
            );


    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext testContext
                              ) {
        vertxRef = new VertxRef(vertx);

        delay = (error, n) -> vertxRef.delay(100,
                                             MILLISECONDS
                                            );

        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println);
        vertxRef.deployVerticle(new RegisterJsValuesCodecs())
                .onComplete(event -> testContext.completeNow())
                .get();
    }


    @Test
    public void test_cond_exp_returns_the_first_branch(VertxTestContext context) {

        Cond.of(TRUE,
                Cons.success("hi"),
                FALSE,
                Cons.success("bye")
               )
            .onSuccess(it -> context.verify(() -> {
                Assertions.assertEquals("hi",
                                        it
                                       );
                context.completeNow();
            }))
            .get();

    }

    @Test
    public void test_cond_exp_returns_the_first_branch_otherwise(VertxTestContext context) {

        Cond.of(FALSE,
                Cons.success("hi"),
                FALSE,
                Cons.success("bye"),
                Cons.success("otherwise")
               )
            .onSuccess(it -> context.verify(() -> {
                Assertions.assertEquals("otherwise",
                                        it
                                       );
                context.completeNow();
            }))
            .get();

    }

    @Test
    public void test_cond_exp_returns_the_first_branch_with_retries(VertxTestContext context) {
        Supplier<Val<Boolean>> valSupplier =
                new ErrorWhile<>(counter -> counter == 1 || counter == 2,
                                 counter -> new RuntimeException("counter:+" + counter),
                                 true
                );
        Cond.of(valSupplier.get(),
                Cons.success("hi"),
                FALSE,
                Cons.success("bye")
               )
            .retry(2)
            .onSuccess(it -> context.verify(() -> {
                Assertions.assertEquals("hi",
                                        it
                                       );
                context.completeNow();
            }))
            .get();

    }

    @Test
    public void test_cond_exp_returns_the_first_branch_with_retries_otherwise(VertxTestContext context) {
        Supplier<Val<Boolean>> valSupplier =
                new ErrorWhile<>(counter -> counter == 1 || counter == 2,
                                 counter -> new RuntimeException("counter:+" + counter),
                                 false
                );
        Cond.of(valSupplier.get(),
                Cons.success("hi"),
                FALSE,
                Cons.success("bye"),
                Cons.success("otherwise")
               )
            .retry(2)
            .onSuccess(it -> context.verify(() -> {
                Assertions.assertEquals("otherwise",
                                        it
                                       );
                context.completeNow();
            }))
            .get();

    }

    @Test
    public void test_cond_exp_returns_the_second_branch(VertxTestContext context) {

        Cond.of(FALSE,
                Cons.success("hi"),
                TRUE,
                Cons.success("bye")
               )
            .onSuccess(it -> context.verify(() -> {
                Assertions.assertEquals("bye",
                                        it
                                       );
                context.completeNow();
            }))
            .get();

    }

    @Test
    public void test_cond_exp_returns_the_second_branch_otherwise(VertxTestContext context) {

        Cond.of(FALSE,
                Cons.success("hi"),
                FALSE,
                Cons.success("bye"),
                Cons.success("otherwise")
               )
            .onSuccess(it -> context.verify(() -> {
                Assertions.assertEquals("otherwise",
                                        it
                                       );
                context.completeNow();
            }))
            .get();

    }
    @Test
    public void test_cond_exp_returns_the_second_branch_with_retries(VertxTestContext context) {
        Supplier<Val<Boolean>> falseSupplier =
                new ErrorWhile<>(counter -> counter == 1 || counter == 2,
                                 counter -> new RuntimeException("counter:+" + counter),
                                 false
                );
        Supplier<Val<Boolean>> trueSupplier =
                new ErrorWhile<>(counter -> counter == 1 || counter == 2,
                                 counter -> new RuntimeException("counter:+" + counter),
                                 false
                );

        Cond.of(falseSupplier.get(),
                Cons.success("hi"),
                trueSupplier.get(),
                Cons.success("bye"),
                Cons.success("otherwise")
               )
            .retry(2)
            .onSuccess(it -> context.verify(() -> {
                Assertions.assertEquals("otherwise",
                                        it
                                       );
                context.completeNow();
            }))
            .get();

    }


    @Test
    public void test_cond_exp_map_returns_first(VertxTestContext context) {

        Cond.of(TRUE,
                Cons.success("a"),
                FALSE,
                Cons.success("bcd")
               )
            .map(String::toUpperCase)
            .onSuccess(r -> context.verify(() -> {
                Assertions.assertEquals("A",
                                        r
                                       );
                context.completeNow();
            }))
            .get();
    }

    @Test
    public void test_cond_exp_map_returns_first_otherwise(VertxTestContext context) {

        Cond.of(FALSE,
                Cons.success("a"),
                FALSE,
                Cons.success("bcd"),
                Cons.success("otherwise")
               )
            .map(String::toUpperCase)
            .onSuccess(r -> context.verify(() -> {
                Assertions.assertEquals("OTHERWISE",
                                        r
                                       );
                context.completeNow();
            }))
            .get();
    }


    @Test
    public void test_cond_exp_map_returns_second(VertxTestContext context) {
        Cond.of(FALSE,
                Cons.success("a"),
                TRUE,
                Cons.success("bcd")
               )
            .map(String::length)
            .onSuccess(r -> context.verify(() -> {
                Assertions.assertEquals(3,
                                        r
                                       );
                context.completeNow();
            }))
            .get();


    }

    @Test
    public void test_cond_exp_map_returns_second_otherwise(VertxTestContext context) {
        Cond.of(FALSE,
                Cons.success("a"),
                FALSE,
                Cons.success("bcd"),
                Cons.success("otherwise")
               )
            .map(String::length)
            .onSuccess(r -> context.verify(() -> {
                Assertions.assertEquals(9,
                                        r
                                       );
                context.completeNow();
            }))
            .get();
    }

    @Test
    public void test_case_exp_flatmap(VertxTestContext context) {

        Cond.of(FALSE,
                Cons.success("a"),
                TRUE,
                Cons.success("bcd")
               )
            .flatMap(s -> Cons.failure(new RuntimeException()))
            .onComplete(r -> context.verify(() -> {
                Assertions.assertTrue(r.failed());
                context.completeNow();
            }))
            .get();


    }


    @Test
    public void test_retries_never_happens_because_error_is_texted_false_on_the_predicate(VertxTestContext context) {

        Cond.of(TRUE,
                a.get(),
                FALSE,
                b.get()
               )
            .retryIf(
                    Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                    ATTEMPTS
                    )
            .onComplete(
                    r -> context.verify(() -> {
                        Assertions.assertTrue(r.failed());
                        context.completeNow();
                    }))
            .get();

    }

    @Test
    public void test_retries_happens_because_error_is_texted_true_on_the_predicate(VertxTestContext context) {

        Cond.of(TRUE,
                a.get(),
                FALSE,
                b.get()
               )
            .retryIf(
                    e -> e instanceof RuntimeException,
                    ATTEMPTS
                    )
            .onSuccess(r -> context.verify(() -> {
                Assertions.assertEquals("a",
                                        r
                                       );
                context.completeNow();
            }))
            .get();

    }

    @Test
    public void test_cond_exp_succeeds_after_two_retries_waiting_1sec_before_retries(VertxTestContext context) {

        long start    = System.nanoTime();
        Cond.of(TRUE,
                a.get(),
                FALSE,
                b.get()
               )
            .retry(ATTEMPTS,
                   delay
                  )
            .onSuccess(r -> context.verify(() -> {
                Assertions.assertEquals("a",
                                        r
                                       );
                Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= ATTEMPTS);
                context.completeNow();
            }))
            .get();

    }


    @Test
    public void test_cond_exp_recover_successfully(VertxTestContext context) {
        Cond.of(TRUE,
                Cons.failure(new RuntimeException()),
                FALSE,
                b.get()
               )
            .recover(e -> e instanceof RuntimeException ? "hi!" : "bye!")
            .onSuccess(str -> context.verify(() -> {
                Assertions.assertEquals("hi!",
                                        str
                                       );
                context.completeNow();
            }))
            .get();

    }

    @Test
    public void test_cond_exp_fails_and_recover_with_failure(VertxTestContext context) {

        Cond.of(TRUE,
                Cons.failure(new RuntimeException()),
                FALSE,
                b.get()
               )
            .recoverWith(e -> e instanceof RuntimeException ?
                              Cons.failure(new IllegalArgumentException()) :
                              Cons.success("bye!"))
            .onComplete(r -> context.verify(() -> {
                Assertions.assertTrue(r.failed());
                Assertions.assertTrue(r.cause() instanceof IllegalArgumentException);
                context.completeNow();
            }))
            .get();

    }

    @Test
    public void test_cond_exp_fails_and_recover_with_success(VertxTestContext context) {
        Cond.of(TRUE,
                Cons.failure(new RuntimeException()),
                FALSE,
                b.get()
               )
            .recoverWith(e -> e instanceof RuntimeException ? Cons.success("hi!") : Cons.success("bye!"))
            .onSuccess(str -> context.verify(() -> {
                Assertions.assertEquals("hi!",
                                        str
                                       );
                context.completeNow();
            }))
            .get();

    }


    @Test
    public void test_cond_exp_fallbackto_success(VertxTestContext context) {
        Cond.of(TRUE,
                Cons.failure(new RuntimeException()),
                FALSE,
                b.get()
               )
            .fallbackTo(e -> e instanceof RuntimeException ? Cons.success("hi!") : Cons.success("bye!"))
            .onSuccess(str -> context.verify(() -> {
                Assertions.assertEquals("hi!",
                                        str
                                       );
                context.completeNow();
            }))
            .get();
    }

    @Test
    public void test_cond_exp_fallbackto_failure(VertxTestContext context) {
        Cond.of(TRUE,
                Cons.failure(new RuntimeException()),
                FALSE,
                b.get()
               )
            .fallbackTo(e -> e instanceof RuntimeException ? Cons.failure(new IllegalArgumentException()) : Cons.success("bye!"))
            .onComplete(r -> context.verify(() -> {
                Assertions.assertTrue(r.failed());
                Assertions.assertTrue(r.cause() instanceof RuntimeException);
                context.completeNow();
            }))
            .get();
    }

    @Test
    public void test_retry_with_delay(VertxTestContext context) {
        int ATTEMPTS = 3;

        long start = System.nanoTime();
        ErrorWhile<Boolean> True = new ErrorWhile<>(counter -> counter <= ATTEMPTS,
                                                    counter -> new RuntimeException("counter: " + counter),
                                                    true
        );

        Cond.of(True.get(),
                Cons.success("a"),
                FALSE,
                b.get()
               )
            .retry(ATTEMPTS,
                   (error, n) -> vertxRef.delay(100,
                                                MILLISECONDS
                                               )
                  )
            .get()
            .onComplete(r -> context.verify(() -> {
                Assertions.assertEquals("a",
                                        r.result()
                                       );
                Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= ATTEMPTS);
                context.completeNow();

            }));

    }


}
