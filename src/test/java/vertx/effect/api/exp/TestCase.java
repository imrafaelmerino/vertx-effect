package vertx.effect.api.exp;

import fun.gen.Gen;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.*;
import vertx.effect.stub.StubBuilder;
import vertx.values.codecs.RegisterJsValuesCodecs;

import java.time.Duration;
import java.util.Arrays;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static vertx.effect.RetryPolicies.constantDelay;
import static vertx.effect.RetryPolicies.limitRetries;
@SuppressWarnings("ReturnValueIgnored")
@ExtendWith(VertxExtension.class)
public class TestCase {
    private static VertxRef vertxRef;

    private static int ATTEMPTS = 2;

  
    
    static StubBuilder<String> a = StubBuilder.ofGen(Gen.seq(counter -> counter <= ATTEMPTS ?
                                            VIO.fail(new RuntimeException("counter: " + counter)) :
                                            VIO.succeed("a")
                                                     )
                                           );

    static StubBuilder<String> b = StubBuilder.ofGen(Gen.seq(counter -> counter <= ATTEMPTS ?
                                                             VIO.fail(new RuntimeException("counter: " + counter)) :
                                                             VIO.succeed("b")
                                                            )
                                                    );

   

    @BeforeAll
    public static void prepare(final Vertx vertx, final VertxTestContext testContext
                              ) {
        vertxRef = new VertxRef(vertx);

        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS, System.out::println);
        vertxRef.deployVerticle(new RegisterJsValuesCodecs()).onComplete(event -> testContext.completeNow()).get();

    }


    @Test
    public void test_case_exp_succeeds_two_branches_otherwise(VertxTestContext context) {

        SwitchExp.<String, String>eval(VIO.succeed("c")).match("a", VIO::succeed, "b", VIO::succeed, VIO::succeed).onSuccess(r -> context.verify(() -> {
            Assertions.assertEquals("c", r);
            context.completeNow();
        })).get();
    }


    @Test
    public void test_case_exp_succeeds_two_list_branches_otherwise(VertxTestContext context) {
        SwitchExp.<String, Integer>eval(VIO.succeed("cc")).match(Arrays.asList("a", "b"), l -> VIO.succeed(1), Arrays.asList("aa", "bb"), l -> VIO.succeed(2), l -> VIO.succeed(-1)).onSuccess(r -> context.verify(() -> {
            Assertions.assertEquals(-1, r);
            context.completeNow();
        })).get();
    }


    @Test
    public void test_case_exp_succeeds_three_list_branches_otherwise(VertxTestContext context) {
        SwitchExp.<String, Integer>eval(VIO.succeed("ccc")).match(Arrays.asList("a", "b"), i -> VIO.succeed(1), Arrays.asList("aa", "bb"), i -> VIO.succeed(2), Arrays.asList("aaa", "bbb"), i -> VIO.succeed(3), i -> VIO.succeed(-1)).onSuccess(r -> context.verify(() -> {
            Assertions.assertEquals(-1, r);
            context.completeNow();
        })).get();
    }


    @Test
    public void test_case_exp_succeeds_four_list_branches_otherwise(VertxTestContext context) {
        SwitchExp.<String, Integer>eval(VIO.succeed("cccc")).match(Arrays.asList("a", "b"), i -> VIO.succeed(1), Arrays.asList("aa", "bb"), i -> VIO.succeed(2), Arrays.asList("aaa", "bbb"), i -> VIO.succeed(3), Arrays.asList("aaaa", "bbbb"), i -> VIO.succeed(4), i -> VIO.succeed(-1)).onSuccess(r -> context.verify(() -> {
            Assertions.assertEquals(-1, r);
            context.completeNow();
        })).get();
    }


    @Test
    public void test_case_exp_succeeds_five_list_branches_otherwise(VertxTestContext context) {
        SwitchExp.<String, Integer>eval(VIO.succeed("ccccc")).match(Arrays.asList("a", "b"), i -> VIO.succeed(1), Arrays.asList("aa", "bb"), i -> VIO.succeed(2), Arrays.asList("aaa", "bbb"), i -> VIO.succeed(3), Arrays.asList("aaaa", "bbbb"), i -> VIO.succeed(4), Arrays.asList("aaaaa", "bbbbb"), i -> VIO.succeed(5), i -> VIO.succeed(-1)).onSuccess(r -> context.verify(() -> {
            Assertions.assertEquals(-1, r);
            context.completeNow();
        })).get();
    }


    @Test
    public void test_case_exp_succeeds_six_list_branches_otherwise(VertxTestContext context) {
        SwitchExp.<String, Integer>eval(VIO.succeed("ccccccc")).match(Arrays.asList("a", "b"), i -> VIO.succeed(1), Arrays.asList("aa", "bb"), i -> VIO.succeed(2), Arrays.asList("aaa", "bbb"), i -> VIO.succeed(3), Arrays.asList("aaaa", "bbbb"), i -> VIO.succeed(4), Arrays.asList("aaaaa", "bbbbb"), i -> VIO.succeed(5), Arrays.asList("aaaaaa", "bbbbbb"), i -> VIO.succeed(6), i -> VIO.succeed(-1)).onSuccess(r -> context.verify(() -> {
            Assertions.assertEquals(-1, r);
            context.completeNow();
        })).get();
    }


    @Test
    public void test_case_exp_succeeds_three_branches_otherwise(VertxTestContext context) {

        SwitchExp.<String, Integer>eval(VIO.succeed("d")).match("a", i -> VIO.succeed(1), "b", i -> VIO.succeed(2), "c", i -> VIO.succeed(3), i -> VIO.succeed(-1)).onSuccess(r -> context.verify(() -> {
            Assertions.assertEquals(-1, r);
            context.completeNow();
        })).get();
    }


    @Test
    public void test_case_exp_succeeds_four_branches_otherwise(VertxTestContext context) {

        SwitchExp.<String, Integer>eval(VIO.succeed("e")).match("a", i -> VIO.succeed(1), "b", i -> VIO.succeed(2), "c", i -> VIO.succeed(3), "d", i -> VIO.succeed(4), i -> VIO.succeed(-1)).onSuccess(r -> context.verify(() -> {
            Assertions.assertEquals(-1, r);
            context.completeNow();
        })).get();
    }


    @Test
    public void test_case_exp_succeeds_five_branches_otherwise(VertxTestContext context) {

        SwitchExp.<String, Integer>eval(VIO.succeed("f")).match("a", i -> VIO.succeed(1), "b", i -> VIO.succeed(2), "c", i -> VIO.succeed(3), "d", i -> VIO.succeed(4), "e", i -> VIO.succeed(5), i -> VIO.succeed(-1)).onSuccess(r -> context.verify(() -> {
            Assertions.assertEquals(-1, r);
            context.completeNow();
        })).get();
    }


    @Test
    public void test_case_exp_succeeds_six_branches_otherwise(VertxTestContext context) {

        SwitchExp.<String, Integer>eval(VIO.succeed("g")).match("a", i -> VIO.succeed(1), "b", i -> VIO.succeed(2), "c", i -> VIO.succeed(3), "d", i -> VIO.succeed(4), "e", i -> VIO.succeed(5), "f", i -> VIO.succeed(6), i -> VIO.succeed(-1)).onSuccess(r -> context.verify(() -> {
            Assertions.assertEquals(-1, r);
            context.completeNow();
        })).get();
    }


    @Test
    public void test_case_exp_flatmap_failure(VertxTestContext context) {

        SwitchExp.<Integer, String>eval(VIO.succeed(5)).match(1, i -> VIO.succeed("a"), 5, i -> VIO.succeed("bcd"), it -> VIO.fail(new IllegalArgumentException("not ma"))).then(s -> VIO.fail(new RuntimeException())).onComplete(r -> context.verify(() -> {
            Assertions.assertTrue(r.failed());
            context.completeNow();
        })).get();

    }


    @Test
    public void test_case_exp_succeeds_after_two_retries(VertxTestContext context) {

        VIO<String> A = a.build();
        VIO<String> B = b.build();
        SwitchExp.<String, String>eval(VIO.succeed("a")).match("a", i -> A, "c", i -> B, it -> VIO.fail(new IllegalArgumentException("not ma"))).retryEach(limitRetries(2)).onSuccess(r -> context.verify(() -> {
            Assertions.assertEquals("a", r);
            context.completeNow();
        })).get();

    }

    @Test
    public void test_retries_never_happens_because_error_is_texted_false_on_the_predicate(VertxTestContext context) {
        VIO<String> A = a.build();
        VIO<String> B = b.build();
        SwitchExp.<String, String>eval(VIO.succeed("a")).match("a", i -> A, "c", i -> B,
                                                               it -> VIO.fail(new IllegalArgumentException("not ma"))
                                                              ).retryEach(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE), limitRetries(2)).onComplete(r -> context.verify(() -> {
            Assertions.assertTrue(r.failed());
            context.completeNow();
        })).get();

    }

    @Test
    public void test_retries_if_with_delay(VertxTestContext context) {
        VIO<String> A = a.build();
        VIO<String> B = b.build();
        SwitchExp.<String, String>eval(VIO.succeed("a")).match("a", i -> A, "c", i -> B, it -> VIO.fail(new IllegalArgumentException("not ma"))).retryEach(e -> e instanceof RuntimeException, limitRetries(3)).onComplete(r -> context.verify(() -> {
            Assertions.assertTrue(r.succeeded());
            context.completeNow();
        })).get();

    }


    @Test
    public void test_retries_happens_because_error_is_texted_true_on_the_predicate(VertxTestContext context) {
        VIO<String> A = a.build();
        VIO<String> B = b.build();

        SwitchExp.<String, String>eval(VIO.succeed("a")).match("a", i -> A, "c", i -> B, it -> VIO.fail(new IllegalArgumentException("not ma"))).retryEach(e -> e instanceof RuntimeException, limitRetries(2)).onSuccess(r -> context.verify(() -> {
            Assertions.assertEquals("a", r);
            context.completeNow();
        })).get();

    }

    @Test
    public void test_case_exp_succeeds_after_two_retries_waiting_1sec_before_retries(VertxTestContext context) {
        VIO<String> A = a.build();
        VIO<String> B = b.build();

        int attempts = 2;
        long start = System.nanoTime();
        SwitchExp.<String, String>eval(VIO.succeed("a")).match("a", i -> A, "c", i -> B, it -> VIO.fail(new IllegalArgumentException("not ma"))).retryEach(limitRetries(ATTEMPTS).append(RetryPolicies.constantDelay(vertxRef.delay(Duration.ofMillis(100))))).onSuccess(r -> context.verify(() -> {
            Assertions.assertEquals("a", r);
            Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= attempts);
            context.completeNow();
        })).get();

    }

    @Test
    public void test_case_exp_recover(VertxTestContext context) {
        VIO<String> B = b.build();

        SwitchExp.<String, String>eval(VIO.succeed("a")).match("a", i -> VIO.fail(new RuntimeException()), "c", i -> B, it -> VIO.fail(new IllegalArgumentException("not ma"))).recover(e -> e instanceof RuntimeException ? "hi!" : "bye!").onSuccess(str -> context.verify(() -> {
            Assertions.assertEquals("hi!", str);
            context.completeNow();
        })).get();


    }


    @Test
    public void test_case_exp_fails_and_recover_with_success(VertxTestContext context) {
        VIO<String> B = b.build();

        SwitchExp.<String, String>eval(VIO.succeed("a")).match("a", i -> VIO.fail(new RuntimeException()), "c", i -> B, it -> VIO.fail(new IllegalArgumentException("not ma"))).recoverWith(e -> e instanceof RuntimeException ? VIO.succeed("hi!") : VIO.succeed("bye!")).onSuccess(str -> context.verify(() -> {
            Assertions.assertEquals("hi!", str);
            context.completeNow();
        })).get();
    }

    @Test
    public void test_case_exp_fails_and_recover_with_failure(VertxTestContext context) {
        VIO<String> B = b.build();
        SwitchExp.<String, String>eval(VIO.succeed("a")).match("a", i -> VIO.fail(new RuntimeException()), "c", i -> B, it -> VIO.fail(new IllegalArgumentException("not ma"))).recoverWith(e -> e instanceof RuntimeException ? VIO.fail(new IllegalArgumentException()) : VIO.succeed("bye!")).onComplete(r -> context.verify(() -> {
            Assertions.assertTrue(r.failed());
            Assertions.assertTrue(r.cause() instanceof IllegalArgumentException);
            context.completeNow();
        })).get();
    }

    @Test
    public void test_case_exp_recover_with_success(VertxTestContext context) {
        VIO<String> A = a.build();
        VIO<String> B = b.build();
        SwitchExp.<String, String>eval(VIO.succeed("a")).match("a", i -> A, "c", i -> B, it -> VIO.fail(new IllegalArgumentException("not ma"))).retryEach(limitRetries(2)).recoverWith(e -> e instanceof RuntimeException ? VIO.succeed("hi!") : VIO.succeed("bye!")).onSuccess(str -> context.verify(() -> {
            Assertions.assertEquals("a", str);
            context.completeNow();
        })).get();
    }


    @Test
    public void test_case_exp_fails_and_fallbackto_success(VertxTestContext context) {
        VIO<String> B = b.build();
        SwitchExp.<String, String>eval(VIO.succeed("a")).match("a", i -> VIO.fail(new RuntimeException()), "c", i -> B,
                                                               it -> VIO.fail(new IllegalArgumentException("not ma"))
                                                              ).fallbackTo(e -> e instanceof RuntimeException ? VIO.succeed("hi!") : VIO.succeed("bye!")).onSuccess(str -> context.verify(() -> {
            Assertions.assertEquals("hi!", str);
            context.completeNow();
        })).get();
    }

    @Test
    public void test_case_exp_fails_and_fallbackto_fails(VertxTestContext context) {
        SwitchExp.<String, String>eval(VIO.succeed("a")).match("a", i -> VIO.fail(new RuntimeException()), "c", i -> b.build(), it -> VIO.fail(new IllegalArgumentException("not ma"))).fallbackTo(e -> e instanceof RuntimeException ? VIO.fail(new IllegalArgumentException()) : VIO.succeed("bye!")).onComplete(r -> context.verify(() -> {
            Assertions.assertTrue(r.failed());
            Assertions.assertTrue(r.cause() instanceof RuntimeException);
            context.completeNow();
        })).get();
    }

    @Test
    public void test_case_exp_success_and_fallback_is_not_applied(VertxTestContext context) {
        VIO<String> A = a.build();
        VIO<String> B = b.build();
        SwitchExp.<String, String>eval(VIO.succeed("a")).match("a", i -> A, "c", i -> B,
                                                               it -> VIO.fail(new IllegalArgumentException("not ma"))
                                                              ).retryEach(limitRetries(2)).fallbackTo(e -> e instanceof RuntimeException ? VIO.succeed("hi!") : VIO.succeed("bye!")).onSuccess(str -> context.verify(() -> {
            Assertions.assertEquals("a", str);
            context.completeNow();
        })).get();
    }

    @Test
    public void test_retry_with_delay(VertxTestContext context) {
        VIO<String> A = a.build();
        VIO<String> B = b.build();
        long start = System.nanoTime();

        SwitchExp.<String, String>eval(VIO.succeed("a"))
                 .match("a", i -> A, "c", i -> B, it -> VIO.fail(new IllegalArgumentException("not ma"))).retryEach(limitRetries(ATTEMPTS).append(constantDelay(vertxRef.delay(Duration.ofMillis(100)))))
                 .get()
                 .onComplete(r -> context.verify(() -> {
                     Assertions.assertEquals("a", r.result());
                     Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= ATTEMPTS);
                     context.completeNow();
                 }));

    }

}
