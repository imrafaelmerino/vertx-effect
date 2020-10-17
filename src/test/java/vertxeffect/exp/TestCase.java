package vertxeffect.exp;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertxeffect.RegisterJsValuesCodecs;
import vertxeffect.Val;
import vertxeffect.VertxRef;
import vertxeffect.Failures;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.*;
import static vertxeffect.Failures.BAD_MESSAGE_CODE;

@ExtendWith(VertxExtension.class)
public class TestCase {
    private static VertxRef vertxRef;

    private static BiFunction<Throwable, Integer, Val<Void>> oneSec;

    private static int ATTEMPTS = 2;

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

        oneSec = (error, n) -> vertxRef.timer(1,
                                              SECONDS,
                                              "one sec"
                                             );
        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );
        vertxRef.deploy(new RegisterJsValuesCodecs())
                .onComplete(event -> testContext.completeNow())
                .get();

    }

    @Test
    public void test_case_exp_succeeds_two_branches(VertxTestContext context) {

        new Case<String, String>(Cons.success("a"))
                .of("a",
                    Cons.success("a"),
                    "b",
                    Cons.success("b")
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
    public void test_case_exp_succeeds_two_branches_otherwise(VertxTestContext context) {

        new Case<String, String>(Cons.success("c"))
                .of("a",
                    Cons.success("a"),
                    "b",
                    Cons.success("b"),
                    Cons.success("otherwise")
                   )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals("otherwise",
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_case_exp_succeeds_two_list_branches(VertxTestContext context) {
        new Case<String, Integer>(Cons.success("bb"))
                .of(Arrays.asList("a",
                                  "b"
                                 ),
                    Cons.success(1),
                    Arrays.asList("aa",
                                  "bb"
                                 ),
                    Cons.success(2)
                   )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(2,
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_case_exp_succeeds_two_list_branches_otherwise(VertxTestContext context) {
        new Case<String, Integer>(Cons.success("cc"))
                .of(Arrays.asList("a",
                                  "b"
                                 ),
                    Cons.success(1),
                    Arrays.asList("aa",
                                  "bb"
                                 ),
                    Cons.success(2),
                    Cons.success(-1)
                   )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(-1,
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_case_exp_succeeds_three_list_branches(VertxTestContext context) {
        new Case<String, Integer>(Cons.success("bbb"))
                .of(Arrays.asList("a",
                                  "b"
                                 ),
                    Cons.success(1),
                    Arrays.asList("aa",
                                  "bb"
                                 ),
                    Cons.success(2),
                    Arrays.asList("aaa",
                                  "bbb"
                                 ),
                    Cons.success(3)
                   )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(3,
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_case_exp_succeeds_three_list_branches_otherwise(VertxTestContext context) {
        new Case<String, Integer>(Cons.success("ccc"))
                .of(Arrays.asList("a",
                                  "b"
                                 ),
                    Cons.success(1),
                    Arrays.asList("aa",
                                  "bb"
                                 ),
                    Cons.success(2),
                    Arrays.asList("aaa",
                                  "bbb"
                                 ),
                    Cons.success(3),
                    Cons.success(-1)
                   )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(-1,
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_case_exp_succeeds_four_list_branches(VertxTestContext context) {
        new Case<String, Integer>(Cons.success("bbbb"))
                .of(Arrays.asList("a",
                                  "b"
                                 ),
                    Cons.success(1),
                    Arrays.asList("aa",
                                  "bb"
                                 ),
                    Cons.success(2),
                    Arrays.asList("aaa",
                                  "bbb"
                                 ),
                    Cons.success(3),
                    Arrays.asList("aaaa",
                                  "bbbb"
                                 ),
                    Cons.success(4)
                   )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(4,
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_case_exp_succeeds_four_list_branches_otherwise(VertxTestContext context) {
        new Case<String, Integer>(Cons.success("cccc"))
                .of(Arrays.asList("a",
                                  "b"
                                 ),
                    Cons.success(1),
                    Arrays.asList("aa",
                                  "bb"
                                 ),
                    Cons.success(2),
                    Arrays.asList("aaa",
                                  "bbb"
                                 ),
                    Cons.success(3),
                    Arrays.asList("aaaa",
                                  "bbbb"
                                 ),
                    Cons.success(4),
                    Cons.success(-1)
                   )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(-1,
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_case_exp_succeeds_five_list_branches(VertxTestContext context) {
        new Case<String, Integer>(Cons.success("aaaaa"))
                .of(Arrays.asList("a",
                                  "b"
                                 ),
                    Cons.success(1),
                    Arrays.asList("aa",
                                  "bb"
                                 ),
                    Cons.success(2),
                    Arrays.asList("aaa",
                                  "bbb"
                                 ),
                    Cons.success(3),
                    Arrays.asList("aaaa",
                                  "bbbb"
                                 ),
                    Cons.success(4),
                    Arrays.asList("aaaaa",
                                  "bbbbb"
                                 ),
                    Cons.success(5)
                   )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(5,
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_case_exp_succeeds_five_list_branches_otherwise(VertxTestContext context) {
        new Case<String, Integer>(Cons.success("ccccc"))
                .of(Arrays.asList("a",
                                  "b"
                                 ),
                    Cons.success(1),
                    Arrays.asList("aa",
                                  "bb"
                                 ),
                    Cons.success(2),
                    Arrays.asList("aaa",
                                  "bbb"
                                 ),
                    Cons.success(3),
                    Arrays.asList("aaaa",
                                  "bbbb"
                                 ),
                    Cons.success(4),
                    Arrays.asList("aaaaa",
                                  "bbbbb"
                                 ),
                    Cons.success(5),
                    Cons.success(-1)
                   )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(-1,
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_case_exp_succeeds_six_list_branches(VertxTestContext context) {
        new Case<String, Integer>(Cons.success("aaaaaa"))
                .of(Arrays.asList("a",
                                  "b"
                                 ),
                    Cons.success(1),
                    Arrays.asList("aa",
                                  "bb"
                                 ),
                    Cons.success(2),
                    Arrays.asList("aaa",
                                  "bbb"
                                 ),
                    Cons.success(3),
                    Arrays.asList("aaaa",
                                  "bbbb"
                                 ),
                    Cons.success(4),
                    Arrays.asList("aaaaa",
                                  "bbbbb"
                                 ),
                    Cons.success(5),
                    Arrays.asList("aaaaaa",
                                  "bbbbbb"
                                 ),
                    Cons.success(6)
                   )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(6,
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_case_exp_succeeds_six_list_branches_otherwise(VertxTestContext context) {
        new Case<String, Integer>(Cons.success("ccccccc"))
                .of(Arrays.asList("a",
                                  "b"
                                 ),
                    Cons.success(1),
                    Arrays.asList("aa",
                                  "bb"
                                 ),
                    Cons.success(2),
                    Arrays.asList("aaa",
                                  "bbb"
                                 ),
                    Cons.success(3),
                    Arrays.asList("aaaa",
                                  "bbbb"
                                 ),
                    Cons.success(4),
                    Arrays.asList("aaaaa",
                                  "bbbbb"
                                 ),
                    Cons.success(5),
                    Arrays.asList("aaaaaa",
                                  "bbbbbb"
                                 ),
                    Cons.success(6),
                    Cons.success(-1)
                   )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(-1,
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_case_exp_succeeds_three_branches(VertxTestContext context) {

        new Case<String, Integer>(Cons.success("c"))
                .of("a",
                    Cons.success(1),
                    "b",
                    Cons.success(2),
                    "c",
                    Cons.success(3)
                   )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(3,
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_case_exp_succeeds_three_branches_otherwise(VertxTestContext context) {

        new Case<String, Integer>(Cons.success("d"))
                .of("a",
                    Cons.success(1),
                    "b",
                    Cons.success(2),
                    "c",
                    Cons.success(3),
                    Cons.success(-1)
                   )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(-1,
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_case_exp_succeeds_four_branches(VertxTestContext context) {

        new Case<String, Integer>(Cons.success("d"))
                .of("a",
                    Cons.success(1),
                    "b",
                    Cons.success(2),
                    "c",
                    Cons.success(3),
                    "d",
                    Cons.success(4)
                   )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(4,
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_case_exp_succeeds_four_branches_otherwise(VertxTestContext context) {

        new Case<String, Integer>(Cons.success("e"))
                .of("a",
                    Cons.success(1),
                    "b",
                    Cons.success(2),
                    "c",
                    Cons.success(3),
                    "d",
                    Cons.success(4),
                    Cons.success(-1)
                   )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(-1,
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_case_exp_succeeds_five_branches(VertxTestContext context) {

        new Case<String, Integer>(Cons.success("e"))
                .of("a",
                    Cons.success(1),
                    "b",
                    Cons.success(2),
                    "c",
                    Cons.success(3),
                    "d",
                    Cons.success(4),
                    "e",
                    Cons.success(5)
                   )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(5,
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_case_exp_succeeds_five_branches_otherwise(VertxTestContext context) {

        new Case<String, Integer>(Cons.success("f"))
                .of("a",
                    Cons.success(1),
                    "b",
                    Cons.success(2),
                    "c",
                    Cons.success(3),
                    "d",
                    Cons.success(4),
                    "e",
                    Cons.success(5),
                    Cons.success(-1)
                   )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(-1,
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_case_exp_succeeds_six_branches(VertxTestContext context) {

        new Case<String, Integer>(Cons.success("f"))
                .of("a",
                    Cons.success(1),
                    "b",
                    Cons.success(2),
                    "c",
                    Cons.success(3),
                    "d",
                    Cons.success(4),
                    "e",
                    Cons.success(5),
                    "f",
                    Cons.success(6)
                   )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(6,
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_case_exp_succeeds_six_branches_otherwise(VertxTestContext context) {

        new Case<String, Integer>(Cons.success("g"))
                .of("a",
                    Cons.success(1),
                    "b",
                    Cons.success(2),
                    "c",
                    Cons.success(3),
                    "d",
                    Cons.success(4),
                    "e",
                    Cons.success(5),
                    "f",
                    Cons.success(6),
                    Cons.success(-1)
                   )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(-1,
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_case_exp_map(VertxTestContext context) {

        new Case<Integer, String>(Cons.success(5))
                .of(1,
                    Cons.success("a"),
                    5,
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
    public void test_case_exp_flatmap_failure(VertxTestContext context) {

        new Case<Integer, String>(Cons.success(5))
                .of(1,
                    Cons.success("a"),
                    5,
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
    public void test_case_exp_flatmap_success(VertxTestContext context) {

        new Case<Integer, String>(Cons.success(5))
                .of(1,
                    Cons.success("a"),
                    5,
                    Cons.success("bcd")
                   )
                .flatMap(s -> Cons.success(s.length()))
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(3,
                                            r
                                           );
                    context.completeNow();
                }))
                .get();


    }

    @Test
    public void test_case_exp_succeeds_after_two_retries(VertxTestContext context) {


        new Case<String, String>(Cons.success("a"))
                .of("a",
                    a.get(),
                    "c",
                    b.get()
                   )
                .retry(2)
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals("a",
                                            r
                                           );
                    context.completeNow();
                }))
                .get();

    }

    @Test
    public void test_retries_never_happens_because_error_is_texted_false_on_the_predicate(VertxTestContext context) {

        new Case<String, String>(Cons.success("a"))
                .of("a",
                    a.get(),
                    "c",
                    b.get()
                   )
                .retryIf(
                        Failures.prism.exists.apply(v -> v.failureCode() == BAD_MESSAGE_CODE),
                        2
                        )
                .onComplete(
                        r -> context.verify(() -> {
                            Assertions.assertTrue(r.failed());
                            context.completeNow();
                        }))
                .get();

    }

    @Test
    public void test_retries_if_with_delay(VertxTestContext context) {

        new Case<String, String>(Cons.success("a"))
                .of("a",
                    a.get(),
                    "c",
                    b.get()
                   )
                .retryIf(e -> e instanceof RuntimeException,
                         3,
                         (e, n) -> vertxRef.timer(1,
                                                  SECONDS,
                                                  "1 sec"
                                                 )
                        )
                .onComplete(
                        r -> context.verify(() -> {
                            Assertions.assertTrue(r.succeeded());
                            context.completeNow();
                        }))
                .get();

    }


    @Test
    public void test_retries_happens_because_error_is_texted_true_on_the_predicate(VertxTestContext context) {

        new Case<String, String>(Cons.success("a"))
                .of("a",
                    a.get(),
                    "c",
                    b.get()
                   )
                .retryIf(
                        e -> e instanceof RuntimeException,
                        2
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
    public void test_case_exp_succeeds_after_two_retries_waiting_1sec_before_retries(VertxTestContext context) {

        int  attempts = 2;
        long start    = System.currentTimeMillis();
        new Case<String, String>(Cons.success("a"))
                .of("a",
                    a.get(),
                    "c",
                    b.get()
                   )
                .retry(attempts,
                       oneSec
                      )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals("a",
                                            r
                                           );
                    Assertions.assertTrue(MILLISECONDS.toSeconds(System.currentTimeMillis() - start) >= attempts);
                    context.completeNow();
                }))
                .get();

    }

    @Test
    public void test_case_exp_recover(VertxTestContext context) {
        new Case<String, String>(Cons.success("a"))
                .of("a",
                    Cons.failure(new RuntimeException()),
                    "c",
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
    public void test_case_exp_fails_and_recover_with_success(VertxTestContext context) {
        new Case<String, String>(Cons.success("a"))
                .of("a",
                    Cons.failure(new RuntimeException()),
                    "c",
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
    public void test_case_exp_fails_and_recover_with_failure(VertxTestContext context) {

        new Case<String, String>(Cons.success("a"))
                .of("a",
                    Cons.failure(new RuntimeException()),
                    "c",
                    b.get()
                   )
                .recoverWith(e -> e instanceof RuntimeException ?
                                  Cons.failure(new IllegalArgumentException()) :
                                  Cons.success("bye!")
                            )
                .onComplete(r -> context.verify(() -> {
                    Assertions.assertTrue(r.failed());
                    Assertions.assertTrue(r.cause() instanceof IllegalArgumentException);
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_case_exp_recover_with_success(VertxTestContext context) {
        new Case<String, String>(Cons.success("a"))
                .of("a",
                    a.get(),
                    "c",
                    b.get()
                   )
                .retry(2)
                .recoverWith(e -> e instanceof RuntimeException ? Cons.success("hi!") : Cons.success("bye!"))
                .onSuccess(str -> context.verify(() -> {
                    Assertions.assertEquals("a",
                                            str
                                           );
                    context.completeNow();
                }))
                .get();
    }


    @Test
    public void test_case_exp_fails_and_fallbackto_success(VertxTestContext context) {
        new Case<String, String>(Cons.success("a"))
                .of("a",
                    Cons.failure(new RuntimeException()),
                    "c",
                    b.get()
                   )
                .fallbackTo(e -> e instanceof RuntimeException ?
                                 Cons.success("hi!") :
                                 Cons.success("bye!"))
                .onSuccess(str -> context.verify(() -> {
                    Assertions.assertEquals("hi!",
                                            str
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_case_exp_fails_and_fallbackto_fails(VertxTestContext context) {
        new Case<String, String>(Cons.success("a"))
                .of("a",
                    Cons.failure(new RuntimeException()),
                    "c",
                    b.get()
                   )
                .fallbackTo(e -> e instanceof RuntimeException ?
                                 Cons.failure(new IllegalArgumentException()) :
                                 Cons.success("bye!"))
                .onComplete(r -> context.verify(() -> {
                    Assertions.assertTrue(r.failed());
                    Assertions.assertTrue(r.cause() instanceof RuntimeException);
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_case_exp_success_and_fallback_is_not_applied(VertxTestContext context) {
        new Case<String, String>(Cons.success("a"))
                .of("a",
                    a.get(),
                    "c",
                    b.get()
                   )
                .retry(2)
                .fallbackTo(e -> e instanceof RuntimeException ?
                                 Cons.success("hi!") :
                                 Cons.success("bye!"))
                .onSuccess(str -> context.verify(() -> {
                    Assertions.assertEquals("a",
                                            str
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_retry_with_delay(VertxTestContext context) {

        long start = System.nanoTime();

        new Case<String, String>(Cons.success("a"))
                .of("a",
                    a.get(),
                    "c",
                    b.get()
                   )
                .retry(ATTEMPTS,
                       (error, n) -> vertxRef.timer(1,
                                                    SECONDS,
                                                    "one sec"
                                                   )
                      )
                .get()
                .onComplete(r -> context.verify(() -> {
                    Assertions.assertEquals("a",
                                            r.result()
                                           );
                    Assertions.assertTrue(NANOSECONDS.toSeconds(System.nanoTime() - start) >= ATTEMPTS);
                    context.completeNow();

                }));

    }

}
