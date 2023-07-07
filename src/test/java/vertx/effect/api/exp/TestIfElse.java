package vertx.effect.api.exp;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsBool;
import jsonvalues.JsInt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.*;
import vertx.effect.api.Verifiers;
import vertx.effect.stub.VIOStub;
import vertx.values.codecs.RegisterJsValuesCodecs;

import java.time.Duration;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static vertx.effect.RetryPolicies.constantDelay;
import static vertx.effect.RetryPolicies.limitRetries;
import static vertx.effect.VIO.FALSE;
import static vertx.effect.VIO.TRUE;
@SuppressWarnings("ReturnValueIgnored")
@ExtendWith(VertxExtension.class)
public class TestIfElse {

    static final VIOStub<Boolean> trueVal =
            VIOStub.failThenSucceed(counter -> (counter == 1 || counter == 2) ? new RuntimeException("counter:+" + counter) : null, true);

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
    public void testRetryIfElsePredicate(final VertxTestContext context) {
        IfElseExp.predicate(trueVal.get())
                 .consequence(() -> VIO.succeed("consequence"))
                 .alternative(() -> VIO.succeed("alternative"))
                 .retryEach(limitRetries(2))
                 .onComplete(r -> {
                     if (r.succeeded()) {
                         context.verify(() -> {
                             Assertions.assertEquals("consequence",
                                                     r.result()
                                                    );
                             context.completeNow();

                         });
                     }
                 })
                 .get();
    }

    @Test
    public void testRetryPredicateWhenBadMessage(final VertxTestContext context) {
        final VIOStub<Boolean> trueVal =
                VIOStub.failThenSucceed(
                        counter -> (counter == 1 || counter == 2) ?
                                Failures.GET_BAD_MESSAGE_EXCEPTION.apply("bad message") : null,
                        true
                                       );
        IfElseExp.predicate(trueVal.get())
                 .consequence(() -> VIO.succeed("consequence"))
                 .alternative(() -> VIO.succeed("alternative"))
                 .retryEach(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                            limitRetries(2)
                           )
                 .onComplete(r -> {
                     if (r.succeeded()) {
                         context.verify(() -> {
                             Assertions.assertEquals("consequence",
                                                     r.result()
                                                    );
                             context.completeNow();

                         });
                     }
                 })
                 .get();
    }

    @Test
    public void testRetryEachIfIfElseConsequence(final VertxTestContext context) {
        final Supplier<VIO<String>> consequence =
                VIOStub.failThenSucceed(
                        counter -> counter == 1 || counter == 2 ? Failures.GET_BAD_MESSAGE_EXCEPTION.apply("bad message") : null,
                        "consequence"
                                       );


        IfElseExp.<String>predicate(VIO.TRUE)
                 .consequence(() -> consequence.get())
                 .alternative(() -> VIO.succeed("alternative"))
                 .retryEach(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                            limitRetries(2)
                           )
                 .onComplete(r -> {
                     if (r.succeeded()) {
                         context.verify(() -> {
                             Assertions.assertEquals("consequence",
                                                     r.result()
                                                    );
                             context.completeNow();

                         });
                     }
                 })
                 .get();
    }

    @Test
    public void testRetryIfIfElseConsequence(final VertxTestContext context) {
        final Supplier<VIO<String>> consequence =
                VIOStub.failThenSucceed(
                        counter -> counter == 1 || counter == 2 ? Failures.GET_BAD_MESSAGE_EXCEPTION.apply("bad message") : null,
                        "consequence"
                                       );


        IfElseExp.<String>predicate(VIO.TRUE)
                 .consequence(() -> consequence.get())
                 .alternative(() -> VIO.succeed("alternative"))
                 .retryEach(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                            limitRetries(4)
                           )
                 .onComplete(r -> {
                     if (r.succeeded()) {
                         context.verify(() -> {
                             Assertions.assertEquals("consequence",
                                                     r.result()
                                                    );
                             context.completeNow();

                         });
                     }
                 })
                 .get();
    }

    @Test
    public void testRetryIfIfElseAlternative(final VertxTestContext context) {

        final Supplier<VIO<String>> alternative =
                VIOStub.failThenSucceed(
                        counter -> counter == 1 || counter == 2 ? Failures.GET_BAD_MESSAGE_EXCEPTION.apply("bad message") : null,
                        "alternative"
                                       );


        IfElseExp.<String>predicate(VIO.succeed(false))
                 .consequence(() -> VIO.succeed("consequence"))
                 .alternative(alternative)
                 .retryEach(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                            limitRetries(2)
                           )
                 .onComplete(r -> {
                     if (r.succeeded()) {
                         context.verify(() -> {
                             Assertions.assertEquals("alternative",
                                                     r.result()
                                                    );
                             context.completeNow();

                         });
                     }
                 })
                 .get();
    }


    @Test
    public void testRetryIfElseConsequence(final VertxTestContext context) {
        final Supplier<VIO<String>> consequence =
                VIOStub.failThenSucceed(counter -> counter == 1 || counter == 2 ? new RuntimeException("counter: " + counter) : null,
                                        "consequence"
                                       );


        IfElseExp.<String>predicate(VIO.succeed(true))
                 .consequence(() -> consequence.get())
                 .alternative(() -> VIO.succeed("alternative"))
                 .retryEach(limitRetries(2))
                 .onComplete(r -> {
                     if (r.succeeded()) {
                         context.verify(() -> {
                             Assertions.assertEquals("consequence",
                                                     r.result()
                                                    );
                             context.completeNow();

                         });
                     }
                 })
                 .get();
    }

    @Test
    public void testRetryIfElseAlternative(final VertxTestContext context) {

        final Supplier<VIO<String>> alternative =
                VIOStub.failThenSucceed(
                        counter -> counter == 1 || counter == 2 ? new RuntimeException("counter: " + counter) : null,
                        "alternative"
                                       );


        IfElseExp.<String>predicate(VIO.succeed(false))
                 .consequence(() -> VIO.succeed("consequence"))
                 .alternative(alternative)
                 .retryEach(limitRetries(2))
                 .onComplete(r -> {
                     if (r.succeeded()) {
                         context.verify(() -> {
                             Assertions.assertEquals("alternative",
                                                     r.result()
                                                    );
                             context.completeNow();

                         });
                     }
                 })
                 .get();
    }

    @Test
    public void test() {

        VIO<Boolean> a = IfElseExp.<Boolean>predicate(VIO.TRUE)
                                  .consequence(() -> FALSE)
                                  .alternative(() -> TRUE);

        IfElseExp<Integer> b = IfElseExp.<Integer>predicate(VIO.TRUE)
                                        .consequence(() -> VIO.succeed(1))
                                        .alternative(() -> VIO.succeed(2));

        IfElseExp<Integer> c = IfElseExp.<Integer>predicate(TRUE)
                                        .consequence(() -> VIO.succeed(89))
                                        .alternative(() -> VIO.succeed(99));

        IfElseExp<Integer> d = IfElseExp.<Integer>predicate(a).consequence(() -> b)
                                        .alternative(() -> c);


        JsObjExp of = JsObjExp.seq("a",
                                   a.map(JsBool::of),
                                   "b",
                                   b.map(JsInt::of),
                                   "c",
                                   c.map(JsInt::of),
                                   "d",
                                   d.map(JsInt::of)
                                  );


        jsonvalues.JsObj result = of
                .get()
                .result();

        Assertions.assertEquals(jsonvalues.JsObj.of("a",
                                                    JsBool.FALSE,
                                                    "b",
                                                    JsInt.of(1),
                                                    "c",
                                                    JsInt.of(89),
                                                    "d",
                                                    JsInt.of(89)
                                                   ),
                                result
                               );
    }


    @Test
    public void test_ifelse_exp_map_returns_consequence(VertxTestContext context) {

        IfElseExp.<String>predicate(TRUE)
                 .consequence(() -> VIO.succeed("a"))
                 .alternative(() -> VIO.succeed("bcd"))
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
    public void test_ifelse_exp_map_returns_alternative(VertxTestContext context) {

        IfElseExp.<String>predicate(FALSE)
                 .consequence(() -> VIO.succeed("a"))
                 .alternative(() -> VIO.succeed("bcd"))
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
    public void test_ifelse_exp_flatmap_success_returns_consequence(VertxTestContext context) {

        IfElseExp.<String>predicate(TRUE)
                 .consequence(() -> VIO.succeed("a"))
                 .alternative(() -> VIO.succeed("bcd"))
                 .then(str -> VIO.succeed(str.toUpperCase()))
                 .onSuccess(r -> context.verify(() -> {
                     Assertions.assertEquals("A",
                                             r
                                            );
                     context.completeNow();
                 }))
                 .get();
    }

    @Test
    public void test_ifelse_exp_flatmap_success_returns_alternative(VertxTestContext context) {

        IfElseExp.<String>predicate(FALSE)
                 .consequence(() -> VIO.succeed("a"))
                 .alternative(() -> VIO.succeed("bcd"))
                 .then(s -> VIO.succeed(s.length()))
                 .onSuccess(r -> context.verify(() -> {
                     Assertions.assertEquals(3,
                                             r
                                            );
                     context.completeNow();
                 }))
                 .get();
    }

    @Test
    public void test_ifelse_exp_flatmap_failure(VertxTestContext context) {


        IfElseExp.<String>predicate(FALSE)
                 .consequence(() -> VIO.succeed("a"))
                 .alternative(() -> VIO.succeed("bcd"))
                 .then(s -> VIO.fail(new RuntimeException()))
                 .onComplete(r -> context.verify(() -> {
                     Assertions.assertTrue(r.failed());
                     context.completeNow();
                 }))
                 .get();
    }


    @Test
    public void test_ifelse_exp_fails_and_recover_with_success(VertxTestContext context) {

        VIO<Object> val = IfElseExp.predicate(trueVal.get())
                                   .consequence(() -> VIO.fail(new RuntimeException()))
                                   .alternative(() -> VIO.succeed("a"))
                                   .recoverWith(e -> VIO.succeed(""));
        val
                .onSuccess(it -> context.verify(() -> {
                    Assertions.assertEquals("",
                                            it
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_ifelse_exp_fails_and_recover_with_failure(VertxTestContext context) {
        VIO<Object> val = IfElseExp.predicate(trueVal.get())
                                   .consequence(() -> VIO.fail(new RuntimeException()))
                                   .alternative(() -> VIO.succeed("a"))
                                   .recoverWith(e -> VIO.fail(new IllegalArgumentException()));

        Verifiers.verifyFailure(e -> e instanceof IllegalArgumentException)
                 .accept(val,
                         context
                        );

    }

    @Test
    public void test_ifelse_exp_recover_with_success(VertxTestContext context) {

        IfElseExp.predicate(trueVal.get())
                 .consequence(() -> VIO.succeed("b"))
                 .alternative(() -> VIO.succeed("a"))
                 .retryEach(limitRetries(2))
                 .recoverWith(e -> VIO.fail(new IllegalArgumentException()))
                 .onSuccess(it -> context.verify(() -> {
                     Assertions.assertEquals("b",
                                             it
                                            );
                     context.completeNow();
                 }))
                 .get();
    }

    @Test
    public void test_retry_with_delay(VertxTestContext context) {
        int ATTEMPTS = 3;

        long start = System.nanoTime();
        VIOStub<Boolean> True = VIOStub.failThenSucceed(counter -> counter < ATTEMPTS ? new RuntimeException("counter: " + counter) : null,
                                                        true
                                                       );

        IfElseExp.predicate(True.get())
                 .consequence(() -> VIO.succeed("b"))
                 .alternative(() -> VIO.succeed("a"))
                 .retryEach(limitRetries(ATTEMPTS)
                                    .append(constantDelay(vertxRef.delay(Duration.ofMillis(100))))
                           )
                 .get()
                 .onComplete(r -> context.verify(() -> {
                     Assertions.assertEquals("b",
                                             r.result()
                                            );
                     Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= ATTEMPTS);
                     context.completeNow();

                 }));

    }


    @Test
    public void test_retry_if_success_with_delay(final VertxTestContext context) {


        VIOStub<String> str = VIOStub.failThenSucceed(
                counter -> counter < 3 ? new IllegalArgumentException() : null,
                "hi"
                                                     );
        IfElseExp.<String>predicate(TRUE)
                 .consequence(str)
                 .alternative(() -> VIO.succeed("bye"))
                 .retryEach(e -> e instanceof IllegalArgumentException,
                            limitRetries(3)
                                    .append(constantDelay(vertxRef.delay(Duration.ofMillis(100))))
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
    public void test_retry_if_failure_with_delay(final VertxTestContext context) {
        VIOStub<String> str = VIOStub.failThenSucceed(
                counter -> counter <= 3 ? new IllegalArgumentException() : null,
                "hi"
                                                     );
        IfElseExp.<String>predicate(TRUE)
                 .consequence(str)
                 .alternative(() -> VIO.succeed("bye"))
                 .retryEach(e -> e instanceof IllegalArgumentException,
                            limitRetries(2)
                                    .append(constantDelay(vertxRef.delay(Duration.ofMillis(100))))
                           )
                 .onComplete(it -> context.verify(() -> {
                     Assertions.assertTrue(it.cause() instanceof IllegalArgumentException);
                     context.completeNow();
                 }))
                 .get();
    }


}
