package vertx.effect.exp;

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
import vertx.effect.mock.ValOrErrorMock;

import java.time.Duration;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static vertx.effect.RetryPolicies.constantDelay;
import static vertx.effect.RetryPolicies.limitRetries;
import static vertx.effect.Val.FALSE;
import static vertx.effect.Val.TRUE;

@ExtendWith(VertxExtension.class)
public class TestIfElse {

    static final Supplier<Val<Boolean>> trueVal =
            new ValOrErrorMock<>(counter -> counter == 1 || counter == 2,
                                 counter -> new RuntimeException("counter:+" + counter),
                                 true
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
    public void testRetryIfElsePredicate(final VertxTestContext context) {
        IfElse.predicate(trueVal.get())
              .consequence(Val.succeed("consequence"))
              .alternative(Val.succeed("alternative"))
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
        final Supplier<Val<Boolean>> trueVal =
                new ValOrErrorMock<>(counter -> counter == 1 || counter == 2,
                                     counter -> Failures.GET_BAD_MESSAGE_EXCEPTION.apply("bad message"),
                                     true
                );
        IfElse.predicate(trueVal.get())
              .consequence(Val.succeed("consequence"))
              .alternative(Val.succeed("alternative"))
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
        final Supplier<Val<String>> consequence =
                new ValOrErrorMock<>(counter -> counter == 1 || counter == 2,
                                     counter -> Failures.GET_BAD_MESSAGE_EXCEPTION.apply("bad message"),
                                     "consequence"
                );


        IfElse.<String>predicate(Val.TRUE)
                .consequence(consequence.get())
                .alternative(Val.succeed("alternative"))
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
        final Supplier<Val<String>> consequence =
                new ValOrErrorMock<>(counter -> counter == 1 || counter == 2,
                                     counter -> Failures.GET_BAD_MESSAGE_EXCEPTION.apply("bad message"),
                                     "consequence"
                );


        IfElse.<String>predicate(Val.TRUE)
                .consequence(consequence.get())
                .alternative(Val.succeed("alternative"))
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

        final Supplier<Val<String>> alternative =
                new ValOrErrorMock<>(counter -> counter == 1 || counter == 2,
                                     counter -> Failures.GET_BAD_MESSAGE_EXCEPTION.apply("bad message"),
                                     "alternative"
                );


        IfElse.<String>predicate(Val.succeed(false))
                .consequence(Val.succeed("consequence"))
                .alternative(alternative.get())
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
        final Supplier<Val<String>> consequence =
                new ValOrErrorMock<>(counter -> counter == 1 || counter == 2,
                                     counter -> new RuntimeException("counter: " + counter),
                                     "consequence"
                );


        IfElse.<String>predicate(Val.succeed(true))
                .consequence(consequence.get())
                .alternative(Val.succeed("alternative"))
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

        final Supplier<Val<String>> alternative =
                new ValOrErrorMock<>(counter -> counter == 1 || counter == 2,
                                     counter -> new RuntimeException("counter: " + counter),
                                     "alternative"
                );


        IfElse.<String>predicate(Val.succeed(false))
                .consequence(Val.succeed("consequence"))
                .alternative(alternative.get())
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

        Val<Boolean> a = IfElse.<Boolean>predicate(Val.TRUE)
                .consequence(FALSE)
                .alternative(TRUE);

        IfElse<Integer> b = IfElse.<Integer>predicate(Val.TRUE)
                .consequence(Val.succeed(1))
                .alternative(Val.succeed(2));

        IfElse<Integer> c = IfElse.<Integer>predicate(TRUE)
                .consequence(Val.succeed(89))
                .alternative(Val.succeed(99));

        IfElse<Integer> d = IfElse.<Integer>predicate(a).consequence(b)
                                                        .alternative(c);


        JsObjExp of = JsObjExp.sequential("a",
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

        IfElse.<String>predicate(TRUE)
                .consequence(Val.succeed("a"))
                .alternative(Val.succeed("bcd"))
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

        IfElse.<String>predicate(FALSE)
                .consequence(Val.succeed("a"))
                .alternative(Val.succeed("bcd"))
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

        IfElse.<String>predicate(TRUE)
                .consequence(Val.succeed("a"))
                .alternative(Val.succeed("bcd"))
                .flatMap(str -> Val.succeed(str.toUpperCase()))
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

        IfElse.<String>predicate(FALSE)
                .consequence(Val.succeed("a"))
                .alternative(Val.succeed("bcd"))
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
    public void test_ifelse_exp_flatmap_failure(VertxTestContext context) {


        IfElse.<String>predicate(FALSE)
                .consequence(Val.succeed("a"))
                .alternative(Val.succeed("bcd"))
                .flatMap(s -> Val.fail(new RuntimeException()))
                .onComplete(r -> context.verify(() -> {
                    Assertions.assertTrue(r.failed());
                    context.completeNow();
                }))
                .get();
    }


    @Test
    public void test_ifelse_exp_fails_and_recover_with_success(VertxTestContext context) {

        Val<Object> val = IfElse.predicate(trueVal.get())
                                .consequence(Val.fail(new RuntimeException()))
                                .alternative(Val.succeed("a"))
                                .recoverWith(e -> Val.succeed(""));
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
        Val<Object> val = IfElse.predicate(trueVal.get())
                                .consequence(Val.fail(new RuntimeException()))
                                .alternative(Val.succeed("a"))
                                .recoverWith(e -> Val.fail(new IllegalArgumentException()));

        Verifiers.verifyFailure(e -> e instanceof IllegalArgumentException)
                 .accept(val,
                         context
                        );

    }

    @Test
    public void test_ifelse_exp_recover_with_success(VertxTestContext context) {

        IfElse.predicate(trueVal.get())
              .consequence(Val.succeed("b"))
              .alternative(Val.succeed("a"))
              .retryEach(limitRetries(2))
              .recoverWith(e -> Val.fail(new IllegalArgumentException()))
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
        ValOrErrorMock<Boolean> True = new ValOrErrorMock<>(ATTEMPTS,
                                                            counter -> new RuntimeException("counter: " + counter),
                                                            true
        );

        IfElse.predicate(True.get())
              .consequence(Val.succeed("b"))
              .alternative(Val.succeed("a"))
              .retryEach(limitRetries(ATTEMPTS)
                             .append(constantDelay(vertxRef.sleep(Duration.ofMillis(100))))
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


        ValOrErrorMock<String> str = new ValOrErrorMock<>(3,
                                                          i -> new IllegalArgumentException(),
                                                          "hi"
        );
        IfElse.<String>predicate(TRUE)
                .consequence(str.get())
                .alternative(Val.succeed("bye"))
                .retryEach(e -> e instanceof IllegalArgumentException,
                       limitRetries(3)
                               .append(constantDelay(vertxRef.sleep(Duration.ofMillis(100))))
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


        ValOrErrorMock<String> str = new ValOrErrorMock<>(3,
                                                          i -> new IllegalArgumentException(),
                                                          "hi"
        );
        IfElse.<String>predicate(TRUE)
                .consequence(str.get())
                .alternative(Val.succeed("bye"))
                .retryEach(e -> e instanceof IllegalArgumentException,
                        limitRetries(2)
                                .append(constantDelay(vertxRef.sleep(Duration.ofMillis(100)))))
                .onComplete(it -> context.verify(() -> {
                    Assertions.assertTrue(it.cause() instanceof IllegalArgumentException);
                    context.completeNow();
                }))
                .get();
    }


}
