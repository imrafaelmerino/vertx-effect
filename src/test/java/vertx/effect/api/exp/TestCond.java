package vertx.effect.api.exp;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.*;
import vertx.effect.stub.VIOStub;
import vertx.values.codecs.RegisterJsValuesCodecs;

import java.time.Duration;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static vertx.effect.RetryPolicies.limitRetries;
import static vertx.effect.VIO.FALSE;
import static vertx.effect.VIO.TRUE;
@SuppressWarnings("ReturnValueIgnored")
@ExtendWith(VertxExtension.class)
public class TestCond {

    private static VertxRef vertxRef;
    private static final int ATTEMPTS = 2;
    private static Supplier<VIO<String>> a =
            VIOStub.failThenSucceed(counter -> counter < ATTEMPTS ? new RuntimeException("counter: " + counter) : null, "a");
    private static Supplier<VIO<String>> b =
            VIOStub.failThenSucceed(counter -> counter < ATTEMPTS ? new RuntimeException("counter: " + counter) : null, "b");


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
    public void test_cond_exp_returns_the_first_branch(VertxTestContext context) {

        CondExp.seq(TRUE,
                    () -> VIO.succeed("hi"),
                    FALSE,
                    () -> VIO.succeed("bye")
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

        CondExp.seq(FALSE,
                    () -> VIO.succeed("hi"),
                    FALSE,
                    () -> VIO.succeed("bye"),
                    () -> VIO.succeed("otherwise")
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
    public void test_cond_exp_returns_the_first_branch_with_retryEach(VertxTestContext context) {
        VIOStub<Boolean> valSupplier =
                VIOStub.failThenSucceed(
                        counter -> counter == 1 || counter == 2 ? new RuntimeException("counter:+" + counter) : null,
                        true
                                       );
        CondExp.seq(valSupplier.get(),
                    () -> VIO.succeed("hi"),
                    FALSE,
                    () -> VIO.succeed("bye")
                   )
               .retryEach(limitRetries(2))
               .onSuccess(it -> context.verify(() -> {
                   Assertions.assertEquals("hi",
                                           it
                                          );
                   context.completeNow();
               }))
               .get();

    }

    @Test
    public void test_cond_exp_returns_the_first_branch_with_retry(VertxTestContext context) {
        VIOStub<Boolean> valSupplier =
                VIOStub.failThenSucceed(
                        counter -> counter == 1 || counter == 2 ? new RuntimeException("counter:+" + counter) : null,
                        true
                                       );
        CondExp.seq(valSupplier.get(),
                    () -> VIO.succeed("hi"),
                    FALSE,
                    () -> VIO.succeed("bye")
                   )
               .retry(limitRetries(4))
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
        VIOStub<Boolean> valSupplier =
                VIOStub.failThenSucceed(
                        counter -> counter == 1 || counter == 2 ? new RuntimeException("counter:+" + counter) : null,
                        false
                                       );
        CondExp.seq(valSupplier.get(),
                    () -> VIO.succeed("hi"),
                    FALSE,
                    () -> VIO.succeed("bye"),
                    () -> VIO.succeed("otherwise")
                   )
               .retryEach(limitRetries(2))
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

        CondExp.seq(FALSE,
                    () -> VIO.succeed("hi"),
                    TRUE,
                    () -> VIO.succeed("bye")
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

        CondExp.seq(FALSE,
                    () -> VIO.succeed("hi"),
                    FALSE,
                    () -> VIO.succeed("bye"),
                    () -> VIO.succeed("otherwise")
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
        VIOStub<Boolean> falseSupplier =
                VIOStub.failThenSucceed(counter -> counter == 1 || counter == 2 ? new RuntimeException("counter:+" + counter) : null,
                                        false
                                       );
        VIOStub<Boolean> trueSupplier =
                VIOStub.failThenSucceed(counter -> counter == 1 || counter == 2 ? new RuntimeException("counter:+" + counter) : null,
                                        false
                                       );

        CondExp.seq(falseSupplier.get(),
                    () -> VIO.succeed("hi"),
                    trueSupplier.get(),
                    () -> VIO.succeed("bye"),
                    () -> VIO.succeed("otherwise")
                   )
               .retryEach(limitRetries(2))
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

        CondExp.seq(TRUE,
                    () -> VIO.succeed("a"),
                    FALSE,
                    () -> VIO.succeed("bcd")
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

        CondExp.seq(FALSE,
                    () -> VIO.succeed("a"),
                    FALSE,
                    () -> VIO.succeed("bcd"),
                    () -> VIO.succeed("otherwise")
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
        CondExp.seq(FALSE,
                    () -> VIO.succeed("a"),
                    TRUE,
                    () -> VIO.succeed("bcd")
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
        CondExp.seq(FALSE,
                    () -> VIO.succeed("a"),
                    FALSE,
                    () -> VIO.succeed("bcd"),
                    () -> VIO.succeed("otherwise")
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

        CondExp.seq(FALSE,
                    () -> VIO.succeed("a"),
                    TRUE,
                    () -> VIO.succeed("bcd")
                   )
               .then(s -> VIO.fail(new RuntimeException()))
               .onComplete(r -> context.verify(() -> {
                   Assertions.assertTrue(r.failed());
                   context.completeNow();
               }))
               .get();


    }


    @Test
    public void test_retries_never_happens_because_error_is_texted_false_on_the_predicate(VertxTestContext context) {

        CondExp.seq(TRUE,
                    a,
                    FALSE,
                    b
                   )
               .retryEach(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                          limitRetries(ATTEMPTS)
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

        CondExp.seq(TRUE,
                    a,
                    FALSE,
                    b
                   )
               .retryEach(e -> e instanceof RuntimeException,
                          limitRetries(ATTEMPTS)
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

        long start = System.nanoTime();
        CondExp.seq(TRUE,
                    a,
                    FALSE,
                    b
                   )
               .retryEach(limitRetries(ATTEMPTS)
                                  .append(RetryPolicies.constantDelay(vertxRef.delay(Duration.ofMillis(100))))
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
        CondExp.seq(TRUE,
                    () -> VIO.fail(new RuntimeException()),
                    FALSE,
                    b
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

        CondExp.seq(TRUE,
                    () -> VIO.fail(new RuntimeException()),
                    FALSE,
                    b
                   )
               .recoverWith(e -> e instanceof RuntimeException ?
                       VIO.fail(new IllegalArgumentException()) :
                       VIO.succeed("bye!"))
               .onComplete(r -> context.verify(() -> {
                   Assertions.assertTrue(r.failed());
                   Assertions.assertTrue(r.cause() instanceof IllegalArgumentException);
                   context.completeNow();
               }))
               .get();

    }

    @Test
    public void test_cond_exp_fails_and_recover_with_success(VertxTestContext context) {
        CondExp.seq(TRUE,
                    () -> VIO.fail(new RuntimeException()),
                    FALSE,
                    b
                   )
               .recoverWith(e -> e instanceof RuntimeException ? VIO.succeed("hi!") : VIO.succeed("bye!"))
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
        CondExp.seq(TRUE,
                    () -> VIO.fail(new RuntimeException()),
                    FALSE,
                    b
                   )
               .fallbackTo(e -> e instanceof RuntimeException ? VIO.succeed("hi!") : VIO.succeed("bye!"))
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
        CondExp.seq(TRUE,
                    () -> VIO.fail(new RuntimeException()),
                    FALSE,
                    b
                   )
               .fallbackTo(e -> e instanceof RuntimeException ? VIO.fail(new IllegalArgumentException()) : VIO.succeed("bye!"))
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
        VIOStub<Boolean> True = VIOStub.failThenSucceed(
                counter -> counter <= ATTEMPTS ? new RuntimeException("counter: " + counter) : null,
                true
                                                       );

        CondExp.seq(True.get(),
                    () -> VIO.succeed("a"),
                    FALSE,
                    b
                   )
               .retryEach(limitRetries(ATTEMPTS)
                                  .append(RetryPolicies.constantDelay(vertxRef.delay(Duration.ofMillis(100))))
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
