package vertx.effect.exp;

import io.vavr.Tuple3;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.*;
import vertx.effect.mock.ValOrErrorMock;

import java.util.Objects;
import java.util.function.Supplier;

import static vertx.effect.RetryPolicies.limitRetries;

@ExtendWith(VertxExtension.class)
public class TestTriple {
    final Supplier<Val<String>> a =
            new ValOrErrorMock<>(counter -> counter == 1 || counter == 2,
                                 counter -> new RuntimeException("counter: " + counter),
                                 "a"
            );

    final Supplier<Val<String>> b =
            new ValOrErrorMock<>(counter -> counter == 1 || counter == 2,
                                 counter -> new RuntimeException("counter: " + counter),
                                 "b"
            );
    final Supplier<Val<Boolean>> True =
            new ValOrErrorMock<>(counter -> counter == 1 || counter == 2,
                                 counter -> new RuntimeException("counter: " + counter),
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
    public void test_parallel_retries(VertxTestContext context) {


        Val<Tuple3<String, String, String>> val = Triple.parallel(a.get(),
                                                                  a.get(),
                                                                  a.get()
                                                                 )
                                                        .retryEach(limitRetries(2));


        Verifiers.<Tuple3<String, String, String>>verifySuccess(
                tuple -> Objects.equals(tuple,
                                        new Tuple3<>("a",
                                                     "a",
                                                     "a"
                                        )
                                       )
                                                               )
                .accept(val,
                        context
                       );

    }

    @Test
    public void test_sequential_retries(VertxTestContext context) {


        Val<Tuple3<String, String, String>> val = Triple.sequential(a.get(),
                                                                    a.get(),
                                                                    a.get()
                                                                   )
                                                        .retryEach(limitRetries(2));


        Verifiers.<Tuple3<String, String, String>>verifySuccess(
                tuple -> Objects.equals(tuple,
                                        new Tuple3<>("a",
                                                     "a",
                                                     "a"
                                        )
                                       )
                                                               )
                .accept(val,
                        context
                       );

    }

    @Test
    public void test_parallel_retries_if_Success(VertxTestContext context) {

        final Supplier<Val<String>> a =
                new ValOrErrorMock<>(counter -> counter == 1 || counter == 2,
                                     counter -> Failures.GET_BAD_MESSAGE_EXCEPTION.apply("counter " + counter),
                                     "a"
                );

        Val<Tuple3<String, String, String>> val = Triple.parallel(a.get(),
                                                                  a.get(),
                                                                  a.get()
                                                                 )
                                                        .retryEach(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                                                               limitRetries(2)
                                                              );
        Verifiers.<Tuple3<String, String, String>>verifySuccess(
                tuple -> new Tuple3<>("a",
                                      "a",
                                      "a"
                ).equals(tuple))
                .accept(val,
                        context
                       );

    }

    @Test
    public void test_sequential_retries_if_Success(VertxTestContext context) {

        final Supplier<Val<String>> a =
                new ValOrErrorMock<>(counter -> counter == 1 || counter == 2,
                                     counter -> Failures.GET_BAD_MESSAGE_EXCEPTION.apply("counter " + counter),
                                     "a"
                );

        Val<Tuple3<String, String, String>> val = Triple.sequential(a.get(),
                                                                    a.get(),
                                                                    a.get()
                                                                   )
                                                        .retryEach(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                                                               limitRetries(2)
                                                              );
        Verifiers.<Tuple3<String, String, String>>verifySuccess(
                tuple -> new Tuple3<>("a",
                                      "a",
                                      "a"
                ).equals(tuple))
                .accept(val,
                        context
                       );

    }

    @Test
    public void test_parallel_retries_if_failure(VertxTestContext context) {


        Val<Tuple3<String, String, String>> val =
                Triple.parallel(a.get(),
                                a.get(),
                                a.get()
                               )
                      .retryEach(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                             limitRetries(2)
                            );


        Verifiers.<Tuple3<String, String, String>>verifyFailure()
                .accept(val,
                        context
                       );

    }

    @Test
    public void test_sequential_retries_if_failure(VertxTestContext context) {


        Val<Tuple3<String, String, String>> val =
                Triple.sequential(a.get(),
                                  a.get(),
                                  a.get()
                                 )
                      .retryEach(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                             limitRetries(2)
                            );


        Verifiers.<Tuple3<String, String, String>>verifyFailure()
                .accept(val,
                        context
                       );

    }

    @Test
    public void test_parallel_triple_exp_map(VertxTestContext context) {

        Val<Tuple3<Integer, Integer, Integer>> val =
                Triple.parallel(Val.succeed("a"),
                                Val.succeed("ab"),
                                Val.succeed("abc")
                               )
                      .map(pair -> pair.map((a, b, c) -> new Tuple3<>(a.length(),
                                                                      b.length(),
                                                                      c.length()
                                            )
                                           )
                          );

        Verifiers.<Tuple3<Integer, Integer, Integer>>verifySuccess(tuple -> Objects.equals(tuple,
                                                                                           new Tuple3<>(1,
                                                                                                        2,
                                                                                                        3
                                                                                           )
                                                                                          )
                                                                  ).accept(val,
                                                                           context
                                                                          );


    }

    @Test
    public void test_sequential_triple_exp_map(VertxTestContext context) {

        Val<Tuple3<Integer, Integer, Integer>> val =
                Triple.sequential(Val.succeed("a"),
                                  Val.succeed("ab"),
                                  Val.succeed("abc")
                                 )
                      .map(pair -> pair.map((a, b, c) -> new Tuple3<>(a.length(),
                                                                      b.length(),
                                                                      c.length()
                                            )
                                           )
                          );

        Verifiers.<Tuple3<Integer, Integer, Integer>>verifySuccess(tuple -> Objects.equals(tuple,
                                                                                           new Tuple3<>(1,
                                                                                                        2,
                                                                                                        3
                                                                                           )
                                                                                          )
                                                                  ).accept(val,
                                                                           context
                                                                          );


    }

    @Test
    public void test_parallel_triple_exp_flatmap_success(VertxTestContext context) {


        Val<Tuple3<String, String, String>> val =
                Triple.parallel(Val.succeed("a"),
                                Val.succeed("b"),
                                Val.succeed("c")
                               )
                      .flatMap(pair -> Val.succeed(pair.map((a, b, c) -> new Tuple3<>(a.toUpperCase(),
                                                                                      b.toUpperCase(),
                                                                                      c.toUpperCase()
                                                             )
                                                           )
                                                  )
                              );
        Verifiers.<Tuple3<String, String, String>>verifySuccess(tuple -> Objects.equals(tuple,
                                                                                        new Tuple3<>("A",
                                                                                                     "B",
                                                                                                     "C"
                                                                                        )
                                                                                       )
                                                               ).accept(val,
                                                                        context
                                                                       );
    }

    @Test
    public void test_sequential_triple_exp_flatmap_success(VertxTestContext context) {


        Val<Tuple3<String, String, String>> val =
                Triple.sequential(Val.succeed("a"),
                                  Val.succeed("b"),
                                  Val.succeed("c")
                                 )
                      .flatMap(pair -> Val.succeed(pair.map((a, b, c) -> new Tuple3<>(a.toUpperCase(),
                                                                                      b.toUpperCase(),
                                                                                      c.toUpperCase()
                                                             )
                                                           )
                                                  )
                              );
        Verifiers.<Tuple3<String, String, String>>verifySuccess(tuple -> Objects.equals(tuple,
                                                                                        new Tuple3<>("A",
                                                                                                     "B",
                                                                                                     "C"
                                                                                        )
                                                                                       )
                                                               ).accept(val,
                                                                        context
                                                                       );
    }


    @Test
    public void test_parallel_triple_exp_flatmap_success_failure(VertxTestContext context) {


        Val<String> val = Triple.parallel(Val.succeed("a"),
                                          Val.succeed("ab"),
                                          Val.succeed("abc")
                                         )
                                .flatMap(s -> Val.fail(new RuntimeException()));


        Verifiers.<String>verifyFailure()
                .accept(val,
                        context
                       );
    }

    @Test
    public void test_sequential_triple_exp_flatmap_success_failure(VertxTestContext context) {


        Val<String> val = Triple.sequential(Val.succeed("a"),
                                            Val.succeed("ab"),
                                            Val.succeed("abc")
                                           )
                                .flatMap(s -> Val.fail(new RuntimeException()));


        Verifiers.<String>verifyFailure()
                .accept(val,
                        context
                       );
    }

    @Test
    public void test_parallel_triple_exp_fails_and_recover_with_success(VertxTestContext context) {

        Val<Tuple3<String, Boolean, String>> val =
                Triple.parallel(a.get(),
                                True.get(),
                                b.get()
                               )
                      .recoverWith(e -> Val.succeed(new Tuple3<>("",
                                                                 false,
                                                                 ""
                                                     )
                                                   )
                                  );
        Verifiers.<Tuple3<String, Boolean, String>>verifySuccess(
                tuple -> Objects.equals(tuple,
                                        new Tuple3<>("",
                                                     false,
                                                     ""
                                        )
                                       ))
                .accept(val,
                        context
                       );

    }

    @Test
    public void test_sequential_triple_exp_fails_and_recover_with_success(VertxTestContext context) {

        Val<Tuple3<String, Boolean, String>> val =
                Triple.sequential(a.get(),
                                  True.get(),
                                  b.get()
                                 )
                      .recoverWith(e -> Val.succeed(new Tuple3<>("",
                                                                 false,
                                                                 ""
                                                     )
                                                   )
                                  );
        Verifiers.<Tuple3<String, Boolean, String>>verifySuccess(
                tuple -> Objects.equals(tuple,
                                        new Tuple3<>("",
                                                     false,
                                                     ""
                                        )
                                       ))
                .accept(val,
                        context
                       );

    }

    @Test
    public void test_parallel_triple_exp_fails_and_recover_with_failure(VertxTestContext context) {

        Val<Tuple3<String, Boolean, String>> val =
                Triple.parallel(a.get(),
                                True.get(),
                                b.get()
                               )
                      .recoverWith(e -> Val.fail(new IllegalArgumentException()));

        Verifiers.<Tuple3<String, Boolean, String>>verifyFailure(e -> e instanceof IllegalArgumentException)
                .accept(val,
                        context
                       );


    }

    @Test
    public void test_sequential_triple_exp_fails_and_recover_with_failure(VertxTestContext context) {

        Val<Tuple3<String, Boolean, String>> val =
                Triple.sequential(a.get(),
                                  True.get(),
                                  b.get()
                                 )
                      .recoverWith(e -> Val.fail(new IllegalArgumentException()));

        Verifiers.<Tuple3<String, Boolean, String>>verifyFailure(e -> e instanceof IllegalArgumentException)
                .accept(val,
                        context
                       );


    }

    @Test
    public void test_parallel_triple_exp_recover_with_success(VertxTestContext context) {
        Val<Tuple3<String, Boolean, String>> val =
                Triple.parallel(a.get(),
                                True.get(),
                                b.get()
                               )
                      .retryEach(limitRetries(2))
                      .recoverWith(e -> Val.fail(new IllegalArgumentException()));

        Verifiers.<Tuple3<String, Boolean, String>>verifySuccess(
                tuple -> Objects.equals(tuple,
                                        new Tuple3<>("a",
                                                     true,
                                                     "b"
                                        )
                                       )
                                                                )
                .accept(val,
                        context
                       );

    }

    @Test
    public void test_sequential_triple_exp_recover_with_success(VertxTestContext context) {
        Val<Tuple3<String, Boolean, String>> val =
                Triple.sequential(a.get(),
                                  True.get(),
                                  b.get()
                                 )
                      .retryEach(limitRetries(2))
                      .recoverWith(e -> Val.fail(new IllegalArgumentException()));

        Verifiers.<Tuple3<String, Boolean, String>>verifySuccess(
                tuple -> Objects.equals(tuple,
                                        new Tuple3<>("a",
                                                     true,
                                                     "b"
                                        )
                                       )
                                                                )
                .accept(val,
                        context
                       );

    }


}
