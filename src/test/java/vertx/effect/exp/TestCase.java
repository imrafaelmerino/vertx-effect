package vertx.effect.exp;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.*;
import vertx.effect.mock.ValOrErrorMock;

import java.time.Duration;
import java.util.Arrays;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static vertx.effect.RetryPolicies.constantDelay;
import static vertx.effect.RetryPolicies.limitRetries;

@ExtendWith(VertxExtension.class)
public class TestCase {
    private static VertxRef vertxRef;

    private static int ATTEMPTS = 2;

    private static Supplier<Val<String>> a =
            new ValOrErrorMock<>(ATTEMPTS,
                                 counter -> new RuntimeException("counter:+" + counter),
                                 "a"
            );
    private static Supplier<Val<String>> b =
            new ValOrErrorMock<>(ATTEMPTS,
                                 counter -> new RuntimeException("counter:+" + counter),
                                 "b"
            );


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
    public void test_case_exp_succeeds_two_branches(VertxTestContext context) {

        new Case<String, String>(Val.succeed("a"))
                .of("a",
                    Val.succeed("a"),
                    "b",
                    Val.succeed("b")
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

        new Case<String, String>(Val.succeed("c"))
                .of("a",
                    Val.succeed("a"),
                    "b",
                    Val.succeed("b"),
                    Val.succeed("otherwise")
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
        new Case<String, Integer>(Val.succeed("bb"))
                .of(Arrays.asList("a",
                                  "b"
                                 ),
                    Val.succeed(1),
                    Arrays.asList("aa",
                                  "bb"
                                 ),
                    Val.succeed(2)
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
        new Case<String, Integer>(Val.succeed("cc"))
                .of(Arrays.asList("a",
                                  "b"
                                 ),
                    Val.succeed(1),
                    Arrays.asList("aa",
                                  "bb"
                                 ),
                    Val.succeed(2),
                    Val.succeed(-1)
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
        new Case<String, Integer>(Val.succeed("bbb"))
                .of(Arrays.asList("a",
                                  "b"
                                 ),
                    Val.succeed(1),
                    Arrays.asList("aa",
                                  "bb"
                                 ),
                    Val.succeed(2),
                    Arrays.asList("aaa",
                                  "bbb"
                                 ),
                    Val.succeed(3)
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
        new Case<String, Integer>(Val.succeed("ccc"))
                .of(Arrays.asList("a",
                                  "b"
                                 ),
                    Val.succeed(1),
                    Arrays.asList("aa",
                                  "bb"
                                 ),
                    Val.succeed(2),
                    Arrays.asList("aaa",
                                  "bbb"
                                 ),
                    Val.succeed(3),
                    Val.succeed(-1)
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
        new Case<String, Integer>(Val.succeed("bbbb"))
                .of(Arrays.asList("a",
                                  "b"
                                 ),
                    Val.succeed(1),
                    Arrays.asList("aa",
                                  "bb"
                                 ),
                    Val.succeed(2),
                    Arrays.asList("aaa",
                                  "bbb"
                                 ),
                    Val.succeed(3),
                    Arrays.asList("aaaa",
                                  "bbbb"
                                 ),
                    Val.succeed(4)
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
        new Case<String, Integer>(Val.succeed("cccc"))
                .of(Arrays.asList("a",
                                  "b"
                                 ),
                    Val.succeed(1),
                    Arrays.asList("aa",
                                  "bb"
                                 ),
                    Val.succeed(2),
                    Arrays.asList("aaa",
                                  "bbb"
                                 ),
                    Val.succeed(3),
                    Arrays.asList("aaaa",
                                  "bbbb"
                                 ),
                    Val.succeed(4),
                    Val.succeed(-1)
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
        new Case<String, Integer>(Val.succeed("aaaaa"))
                .of(Arrays.asList("a",
                                  "b"
                                 ),
                    Val.succeed(1),
                    Arrays.asList("aa",
                                  "bb"
                                 ),
                    Val.succeed(2),
                    Arrays.asList("aaa",
                                  "bbb"
                                 ),
                    Val.succeed(3),
                    Arrays.asList("aaaa",
                                  "bbbb"
                                 ),
                    Val.succeed(4),
                    Arrays.asList("aaaaa",
                                  "bbbbb"
                                 ),
                    Val.succeed(5)
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
        new Case<String, Integer>(Val.succeed("ccccc"))
                .of(Arrays.asList("a",
                                  "b"
                                 ),
                    Val.succeed(1),
                    Arrays.asList("aa",
                                  "bb"
                                 ),
                    Val.succeed(2),
                    Arrays.asList("aaa",
                                  "bbb"
                                 ),
                    Val.succeed(3),
                    Arrays.asList("aaaa",
                                  "bbbb"
                                 ),
                    Val.succeed(4),
                    Arrays.asList("aaaaa",
                                  "bbbbb"
                                 ),
                    Val.succeed(5),
                    Val.succeed(-1)
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
        new Case<String, Integer>(Val.succeed("aaaaaa"))
                .of(Arrays.asList("a",
                                  "b"
                                 ),
                    Val.succeed(1),
                    Arrays.asList("aa",
                                  "bb"
                                 ),
                    Val.succeed(2),
                    Arrays.asList("aaa",
                                  "bbb"
                                 ),
                    Val.succeed(3),
                    Arrays.asList("aaaa",
                                  "bbbb"
                                 ),
                    Val.succeed(4),
                    Arrays.asList("aaaaa",
                                  "bbbbb"
                                 ),
                    Val.succeed(5),
                    Arrays.asList("aaaaaa",
                                  "bbbbbb"
                                 ),
                    Val.succeed(6)
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
        new Case<String, Integer>(Val.succeed("ccccccc"))
                .of(Arrays.asList("a",
                                  "b"
                                 ),
                    Val.succeed(1),
                    Arrays.asList("aa",
                                  "bb"
                                 ),
                    Val.succeed(2),
                    Arrays.asList("aaa",
                                  "bbb"
                                 ),
                    Val.succeed(3),
                    Arrays.asList("aaaa",
                                  "bbbb"
                                 ),
                    Val.succeed(4),
                    Arrays.asList("aaaaa",
                                  "bbbbb"
                                 ),
                    Val.succeed(5),
                    Arrays.asList("aaaaaa",
                                  "bbbbbb"
                                 ),
                    Val.succeed(6),
                    Val.succeed(-1)
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

        new Case<String, Integer>(Val.succeed("c"))
                .of("a",
                    Val.succeed(1),
                    "b",
                    Val.succeed(2),
                    "c",
                    Val.succeed(3)
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

        new Case<String, Integer>(Val.succeed("d"))
                .of("a",
                    Val.succeed(1),
                    "b",
                    Val.succeed(2),
                    "c",
                    Val.succeed(3),
                    Val.succeed(-1)
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

        new Case<String, Integer>(Val.succeed("d"))
                .of("a",
                    Val.succeed(1),
                    "b",
                    Val.succeed(2),
                    "c",
                    Val.succeed(3),
                    "d",
                    Val.succeed(4)
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

        new Case<String, Integer>(Val.succeed("e"))
                .of("a",
                    Val.succeed(1),
                    "b",
                    Val.succeed(2),
                    "c",
                    Val.succeed(3),
                    "d",
                    Val.succeed(4),
                    Val.succeed(-1)
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

        new Case<String, Integer>(Val.succeed("e"))
                .of("a",
                    Val.succeed(1),
                    "b",
                    Val.succeed(2),
                    "c",
                    Val.succeed(3),
                    "d",
                    Val.succeed(4),
                    "e",
                    Val.succeed(5)
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

        new Case<String, Integer>(Val.succeed("f"))
                .of("a",
                    Val.succeed(1),
                    "b",
                    Val.succeed(2),
                    "c",
                    Val.succeed(3),
                    "d",
                    Val.succeed(4),
                    "e",
                    Val.succeed(5),
                    Val.succeed(-1)
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

        new Case<String, Integer>(Val.succeed("f"))
                .of("a",
                    Val.succeed(1),
                    "b",
                    Val.succeed(2),
                    "c",
                    Val.succeed(3),
                    "d",
                    Val.succeed(4),
                    "e",
                    Val.succeed(5),
                    "f",
                    Val.succeed(6)
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

        new Case<String, Integer>(Val.succeed("g"))
                .of("a",
                    Val.succeed(1),
                    "b",
                    Val.succeed(2),
                    "c",
                    Val.succeed(3),
                    "d",
                    Val.succeed(4),
                    "e",
                    Val.succeed(5),
                    "f",
                    Val.succeed(6),
                    Val.succeed(-1)
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

        new Case<Integer, String>(Val.succeed(5))
                .of(1,
                    Val.succeed("a"),
                    5,
                    Val.succeed("bcd")
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

        new Case<Integer, String>(Val.succeed(5))
                .of(1,
                    Val.succeed("a"),
                    5,
                    Val.succeed("bcd")
                   )
                .flatMap(s -> Val.fail(new RuntimeException()))
                .onComplete(r -> context.verify(() -> {
                    Assertions.assertTrue(r.failed());
                    context.completeNow();
                }))
                .get();

    }

    @Test
    public void test_case_exp_flatmap_success(VertxTestContext context) {

        new Case<Integer, String>(Val.succeed(5))
                .of(1,
                    Val.succeed("a"),
                    5,
                    Val.succeed("bcd")
                   )
                .flatMap(s -> Val.succeed(s.length()))
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


        new Case<String, String>(Val.succeed("a"))
                .of("a",
                    a.get(),
                    "c",
                    b.get()
                   )
                .retryEach(limitRetries(2))
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

        new Case<String, String>(Val.succeed("a"))
                .of("a",
                    a.get(),
                    "c",
                    b.get()
                   )
                .retryEach(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                           limitRetries(2)
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

        new Case<String, String>(Val.succeed("a"))
                .of("a",
                    a.get(),
                    "c",
                    b.get()
                   )
                .retryEach(e -> e instanceof RuntimeException,
                           limitRetries(3)
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

        new Case<String, String>(Val.succeed("a"))
                .of("a",
                    a.get(),
                    "c",
                    b.get()
                   )
                .retryEach(e -> e instanceof RuntimeException,
                           limitRetries(2)
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
        long start    = System.nanoTime();
        new Case<String, String>(Val.succeed("a"))
                .of("a",
                    a.get(),
                    "c",
                    b.get()
                   )
                .retryEach(limitRetries(ATTEMPTS)
                               .append(RetryPolicies.constantDelay(vertxRef.sleep(Duration.ofMillis(100))))
                          )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals("a",
                                            r
                                           );
                    Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= attempts);
                    context.completeNow();
                }))
                .get();

    }

    @Test
    public void test_case_exp_recover(VertxTestContext context) {
        new Case<String, String>(Val.succeed("a"))
                .of("a",
                    Val.fail(new RuntimeException()),
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
        new Case<String, String>(Val.succeed("a"))
                .of("a",
                    Val.fail(new RuntimeException()),
                    "c",
                    b.get()
                   )
                .recoverWith(e -> e instanceof RuntimeException ? Val.succeed("hi!") : Val.succeed("bye!"))
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

        new Case<String, String>(Val.succeed("a"))
                .of("a",
                    Val.fail(new RuntimeException()),
                    "c",
                    b.get()
                   )
                .recoverWith(e -> e instanceof RuntimeException ?
                                  Val.fail(new IllegalArgumentException()) :
                                  Val.succeed("bye!")
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
        new Case<String, String>(Val.succeed("a"))
                .of("a",
                    a.get(),
                    "c",
                    b.get()
                   )
                .retryEach(limitRetries(2))
                .recoverWith(e -> e instanceof RuntimeException ? Val.succeed("hi!") : Val.succeed("bye!"))
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
        new Case<String, String>(Val.succeed("a"))
                .of("a",
                    Val.fail(new RuntimeException()),
                    "c",
                    b.get()
                   )
                .fallbackTo(e -> e instanceof RuntimeException ?
                                 Val.succeed("hi!") :
                                 Val.succeed("bye!"))
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
        new Case<String, String>(Val.succeed("a"))
                .of("a",
                    Val.fail(new RuntimeException()),
                    "c",
                    b.get()
                   )
                .fallbackTo(e -> e instanceof RuntimeException ?
                                 Val.fail(new IllegalArgumentException()) :
                                 Val.succeed("bye!"))
                .onComplete(r -> context.verify(() -> {
                    Assertions.assertTrue(r.failed());
                    Assertions.assertTrue(r.cause() instanceof RuntimeException);
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_case_exp_success_and_fallback_is_not_applied(VertxTestContext context) {
        new Case<String, String>(Val.succeed("a"))
                .of("a",
                    a.get(),
                    "c",
                    b.get()
                   )
                .retryEach(limitRetries(2))
                .fallbackTo(e -> e instanceof RuntimeException ?
                                 Val.succeed("hi!") :
                                 Val.succeed("bye!"))
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

        new Case<String, String>(Val.succeed("a"))
                .of("a",
                    a.get(),
                    "c",
                    b.get()
                   )
                .retryEach(limitRetries(ATTEMPTS)
                               .append(constantDelay(vertxRef.sleep(Duration.ofMillis(100))))
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
