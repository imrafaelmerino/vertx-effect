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

import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.*;
import static vertx.effect.exp.Cons.FALSE;
import static vertx.effect.exp.Cons.TRUE;

@ExtendWith(VertxExtension.class)
public class TestIfElse {

    static final Supplier<Val<Boolean>> trueVal =
            new ErrorWhile<>(counter -> counter == 1 || counter == 2,
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
              .consequence(Cons.success("consequence"))
              .alternative(Cons.success("alternative"))
              .retry(2)
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
                new ErrorWhile<>(counter -> counter == 1 || counter == 2,
                                 counter -> Failures.GET_BAD_MESSAGE_EXCEPTION.apply("bad message"),
                                 true
                );
        IfElse.predicate(trueVal.get())
              .consequence(Cons.success("consequence"))
              .alternative(Cons.success("alternative"))
              .retryIf(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                       2
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
                new ErrorWhile<>(counter -> counter == 1 || counter == 2,
                                 counter -> Failures.GET_BAD_MESSAGE_EXCEPTION.apply("bad message"),
                                 "consequence"
                );


        IfElse.<String>predicate(Cons.TRUE)
                .consequence(consequence.get())
                .alternative(Cons.success("alternative"))
                .retryIf(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                         2
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
                new ErrorWhile<>(counter -> counter == 1 || counter == 2,
                                 counter -> Failures.GET_BAD_MESSAGE_EXCEPTION.apply("bad message"),
                                 "alternative"
                );


        IfElse.<String>predicate(Cons.success(false))
                .consequence(Cons.success("consequence"))
                .alternative(alternative.get())
                .retryIf(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                         2
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
                new ErrorWhile<>(counter -> counter == 1 || counter == 2,
                                 counter -> new RuntimeException("counter: " + counter),
                                 "consequence"
                );


        IfElse.<String>predicate(Cons.success(true))
                .consequence(consequence.get())
                .alternative(Cons.success("alternative"))
                .retry(2)
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
                new ErrorWhile<>(counter -> counter == 1 || counter == 2,
                                 counter -> new RuntimeException("counter: " + counter),
                                 "alternative"
                );


        IfElse.<String>predicate(Cons.success(false))
                .consequence(Cons.success("consequence"))
                .alternative(alternative.get())
                .retry(2)
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

        Val<Boolean> a = IfElse.<Boolean>predicate(Cons.TRUE)
                .consequence(FALSE)
                .alternative(TRUE);

        IfElse<Integer> b = IfElse.<Integer>predicate(Cons.TRUE)
                .consequence(Cons.success(1))
                .alternative(Cons.success(2));

        IfElse<Integer> c = IfElse.<Integer>predicate(TRUE)
                .consequence(Cons.success(89))
                .alternative(Cons.success(99));

        IfElse<Integer> d = IfElse.<Integer>predicate(a).consequence(b)
                                                        .alternative(c);


        JsObjVal of = JsObjVal.sequential("a",
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
                .consequence(Cons.success("a"))
                .alternative(Cons.success("bcd"))
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
                .consequence(Cons.success("a"))
                .alternative(Cons.success("bcd"))
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
                .consequence(Cons.success("a"))
                .alternative(Cons.success("bcd"))
                .flatMap(str -> Cons.success(str.toUpperCase()))
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
                .consequence(Cons.success("a"))
                .alternative(Cons.success("bcd"))
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
    public void test_ifelse_exp_flatmap_failure(VertxTestContext context) {


        IfElse.<String>predicate(FALSE)
                .consequence(Cons.success("a"))
                .alternative(Cons.success("bcd"))
                .flatMap(s -> Cons.failure(new RuntimeException()))
                .onComplete(r -> context.verify(() -> {
                    Assertions.assertTrue(r.failed());
                    context.completeNow();
                }))
                .get();
    }


    @Test
    public void test_ifelse_exp_fails_and_recover_with_success(VertxTestContext context) {

        Val<Object> val = IfElse.predicate(trueVal.get())
                                .consequence(Cons.failure(new RuntimeException()))
                                .alternative(Cons.success("a"))
                                .recoverWith(e -> Cons.success(""));
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
                                .consequence(Cons.failure(new RuntimeException()))
                                .alternative(Cons.success("a"))
                                .recoverWith(e -> Cons.failure(new IllegalArgumentException()));

        Verifiers.verifyFailure(e -> e instanceof IllegalArgumentException)
                 .accept(val,
                         context
                        );

    }

    @Test
    public void test_ifelse_exp_recover_with_success(VertxTestContext context) {

        IfElse.predicate(trueVal.get())
              .consequence(Cons.success("b"))
              .alternative(Cons.success("a"))
              .retry(2)
              .recoverWith(e -> Cons.failure(new IllegalArgumentException()))
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
        ErrorWhile<Boolean> True = new ErrorWhile<>(ATTEMPTS,
                                                    counter -> new RuntimeException("counter: " + counter),
                                                    true
        );

        IfElse.predicate(True.get())
              .consequence(Cons.success("b"))
              .alternative(Cons.success("a"))
              .retry(ATTEMPTS,
                     (error, n) -> vertxRef.delay(100,
                                                  MILLISECONDS
                                                 )
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


        ErrorWhile<String> str = new ErrorWhile<>(3,
                                                  i -> new IllegalArgumentException(),
                                                  "hi"
        );
        IfElse.<String>predicate(TRUE)
                .consequence(str.get())
                .alternative(Cons.success("bye"))
                .retryIf(it -> it instanceof IllegalArgumentException,
                         3,
                         (e, i) -> vertxRef.delay(100,
                                                  MILLISECONDS
                                                 )
                        )
                .onSuccess(it -> {
                    context.verify(() -> {
                        Assertions.assertEquals("hi",
                                                it);
                        context.completeNow();
                    });
                })
                .get();
    }

    @Test
    public void test_retry_if_failure_with_delay(final VertxTestContext context) {


        ErrorWhile<String> str = new ErrorWhile<>(3,
                                                  i -> new IllegalArgumentException(),
                                                  "hi"
        );
        IfElse.<String>predicate(TRUE)
                .consequence(str.get())
                .alternative(Cons.success("bye"))
                .retryIf(it -> it instanceof IllegalArgumentException,
                         2,
                         (e, i) -> vertxRef.delay(100,
                                                  MILLISECONDS
                                                 )
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
