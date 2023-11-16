package vertx.effect.api.exp;

import fun.gen.Gen;
import fun.tuple.Triple;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.Failures;
import vertx.effect.TripleExp;
import vertx.effect.VIO;
import vertx.effect.VertxRef;
import vertx.effect.api.Verifiers;
import vertx.effect.stub.StubBuilder;
import vertx.values.codecs.RegisterJsValuesCodecs;

import java.util.Objects;

import static vertx.effect.RetryPolicies.limitRetries;

@SuppressWarnings("ReturnValueIgnored")
@ExtendWith(VertxExtension.class)
public class TestTriple {


    private static VertxRef vertxRef;
    StubBuilder<String> a =
            StubBuilder.ofGen(Gen.seq(counter -> counter == 1 || counter == 2 ?
                                      VIO.fail(new RuntimeException("counter: " + counter)) :
                                      VIO.succeed("a")
                                     )
                             );
    StubBuilder<String> b =
            StubBuilder.ofGen(Gen.seq(counter -> counter == 1 || counter == 2 ?
                                      VIO.fail(new RuntimeException("counter: " + counter)) :
                                      VIO.succeed("b")
                                     )
                             );
    StubBuilder<Boolean> True =
            StubBuilder.ofGen(Gen.seq(counter -> counter == 1 || counter == 2 ?
                                      VIO.fail(new RuntimeException("counter: " + counter)) :
                                      VIO.TRUE
                                     )
                             );

    @BeforeAll
    public static void prepare(
            final Vertx vertx,
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


        VIO<Triple<String, String, String>> val = TripleExp.par(a.build(),
                                                                a.build(),
                                                                a.build()
                                                               )
                                                           .retryEach(limitRetries(2));


        Verifiers.<Triple<String, String, String>>verifySuccess(
                         tuple -> Objects.equals(tuple,
                                                 Triple.of("a",
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


        VIO<Triple<String, String, String>> val = TripleExp.seq(a.build(),
                                                                a.build(),
                                                                a.build()
                                                               )
                                                           .retryEach(limitRetries(2));


        Verifiers.<Triple<String, String, String>>verifySuccess(
                         tuple -> Objects.equals(tuple,
                                                 Triple.of("a",
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


        StubBuilder<String> a =
                StubBuilder.ofGen(Gen.seq(counter -> counter == 1 || counter == 2 ?
                                          VIO.fail(Failures.GET_BAD_MESSAGE_EXCEPTION.apply("counter " + counter)) :
                                          VIO.succeed("a")
                                         )
                                 );

        VIO<Triple<String, String, String>> val = TripleExp.par(a.build(),
                                                                a.build(),
                                                                a.build()
                                                               )
                                                           .retryEach(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                                                                      limitRetries(2)
                                                                     );
        Verifiers.<Triple<String, String, String>>verifySuccess(
                         tuple -> Triple.of("a",
                                            "a",
                                            "a"
                                           ).equals(tuple))
                 .accept(val,
                         context
                        );

    }

    @Test
    public void test_sequential_retries_if_Success(VertxTestContext context) {

        StubBuilder<String> a =
                StubBuilder.ofGen(Gen.seq(counter -> counter == 1 || counter == 2 ?
                                          VIO.fail(Failures.GET_BAD_MESSAGE_EXCEPTION.apply("counter " + counter)) :
                                          VIO.succeed("a")
                                         )
                                 );

        VIO<Triple<String, String, String>> val = TripleExp.seq(a.build(),
                                                                a.build(),
                                                                a.build()
                                                               )
                                                           .retryEach(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                                                                      limitRetries(2)
                                                                     );
        Verifiers.<Triple<String, String, String>>verifySuccess(
                         tuple -> Triple.of("a",
                                            "a",
                                            "a"
                                           ).equals(tuple))
                 .accept(val,
                         context
                        );

    }

    @Test
    public void test_parallel_retries_if_failure(VertxTestContext context) {


        VIO<Triple<String, String, String>> val =
                TripleExp.par(a.build(),
                              a.build(),
                              a.build()
                             )
                         .retryEach(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                                    limitRetries(2)
                                   );


        Verifiers.<Triple<String, String, String>>verifyFailure()
                 .accept(val,
                         context
                        );

    }

    @Test
    public void test_sequential_retries_if_failure(VertxTestContext context) {


        VIO<Triple<String, String, String>> val =
                TripleExp.seq(a.build(),
                              a.build(),
                              a.build()
                             )
                         .retryEach(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                                    limitRetries(2)
                                   );


        Verifiers.<Triple<String, String, String>>verifyFailure()
                 .accept(val,
                         context
                        );

    }

    @Test
    public void test_parallel_triple_exp_map(VertxTestContext context) {

        VIO<Triple<Integer, Integer, Integer>> val =
                TripleExp.par(VIO.succeed("a"),
                              VIO.succeed("ab"),
                              VIO.succeed("abc")
                             )
                         .map(t -> Triple.of(t.first().length(),
                                             t.second().length(),
                                             t.third().length()
                                            )

                             );

        Verifiers.<Triple<Integer, Integer, Integer>>verifySuccess(tuple -> Objects.equals(tuple,
                                                                                           Triple.of(1,
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

        VIO<Triple<Integer, Integer, Integer>> val =
                TripleExp.seq(VIO.succeed("a"),
                              VIO.succeed("ab"),
                              VIO.succeed("abc")
                             )
                         .map(t -> Triple.of(t.first().length(),
                                             t.second().length(),
                                             t.third().length()
                                            )
                             );

        Verifiers.<Triple<Integer, Integer, Integer>>verifySuccess(tuple -> Objects.equals(tuple,
                                                                                           Triple.of(1,
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


        VIO<Triple<String, String, String>> val =
                TripleExp.par(VIO.succeed("a"),
                              VIO.succeed("b"),
                              VIO.succeed("c")
                             )
                         .then(t -> VIO.succeed(Triple.of(t.first().toUpperCase(),
                                                          t.second().toUpperCase(),
                                                          t.third().toUpperCase()
                                                         )

                                               )
                              );
        Verifiers.<Triple<String, String, String>>verifySuccess(tuple -> Objects.equals(tuple,
                                                                                        Triple.of("A",
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


        VIO<Triple<String, String, String>> val =
                TripleExp.seq(VIO.succeed("a"),
                              VIO.succeed("b"),
                              VIO.succeed("c")
                             )
                         .then(t -> VIO.succeed(Triple.of(t.first().toUpperCase(),
                                                          t.second().toUpperCase(),
                                                          t.third().toUpperCase()

                                                         )
                                               )
                              );
        Verifiers.<Triple<String, String, String>>verifySuccess(tuple -> Objects.equals(tuple,
                                                                                        Triple.of("A",
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


        VIO<String> val = TripleExp.par(VIO.succeed("a"),
                                        VIO.succeed("ab"),
                                        VIO.succeed("abc")
                                       )
                                   .then(s -> VIO.fail(new RuntimeException()));


        Verifiers.<String>verifyFailure()
                 .accept(val,
                         context
                        );
    }

    @Test
    public void test_sequential_triple_exp_flatmap_success_failure(VertxTestContext context) {


        VIO<String> val = TripleExp.seq(VIO.succeed("a"),
                                        VIO.succeed("ab"),
                                        VIO.succeed("abc")
                                       )
                                   .then(s -> VIO.fail(new RuntimeException()));


        Verifiers.<String>verifyFailure()
                 .accept(val,
                         context
                        );
    }

    @Test
    public void test_parallel_triple_exp_fails_and_recover_with_success(VertxTestContext context) {

        VIO<Triple<String, Boolean, String>> val =
                TripleExp.par(a.build(),
                              True.build(),
                              b.build()
                             )
                         .recoverWith(e -> VIO.succeed(Triple.of("",
                                                                 false,
                                                                 ""
                                                                )
                                                      )
                                     );
        Verifiers.<Triple<String, Boolean, String>>verifySuccess(
                         tuple -> Objects.equals(tuple,
                                                 Triple.of("",
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

        VIO<Triple<String, Boolean, String>> val =
                TripleExp.seq(a.build(),
                              True.build(),
                              b.build()
                             )
                         .recoverWith(e -> VIO.succeed(Triple.of("",
                                                                 false,
                                                                 ""
                                                                )
                                                      )
                                     );
        Verifiers.<Triple<String, Boolean, String>>verifySuccess(
                         tuple -> Objects.equals(tuple,
                                                 Triple.of("",
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

        VIO<Triple<String, Boolean, String>> val =
                TripleExp.par(a.build(),
                              True.build(),
                              b.build()
                             )
                         .recoverWith(e -> VIO.fail(new IllegalArgumentException()));

        Verifiers.<Triple<String, Boolean, String>>verifyFailure(e -> e instanceof IllegalArgumentException)
                 .accept(val,
                         context
                        );


    }

    @Test
    public void test_sequential_triple_exp_fails_and_recover_with_failure(VertxTestContext context) {

        VIO<Triple<String, Boolean, String>> val =
                TripleExp.seq(a.build(),
                              True.build(),
                              b.build()
                             )
                         .recoverWith(e -> VIO.fail(new IllegalArgumentException()));

        Verifiers.<Triple<String, Boolean, String>>verifyFailure(e -> e instanceof IllegalArgumentException)
                 .accept(val,
                         context
                        );


    }

    @Test
    public void test_parallel_triple_exp_recover_with_success(VertxTestContext context) {
        VIO<Triple<String, Boolean, String>> val =
                TripleExp.par(a.build(),
                              True.build(),
                              b.build()
                             )
                         .retryEach(limitRetries(2))
                         .recoverWith(e -> VIO.fail(new IllegalArgumentException()));

        Verifiers.<Triple<String, Boolean, String>>verifySuccess(
                         tuple -> Objects.equals(tuple,
                                                 Triple.of("a",
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
        VIO<Triple<String, Boolean, String>> val =
                TripleExp.seq(a.build(),
                              True.build(),
                              b.build()
                             )
                         .retryEach(limitRetries(2))
                         .recoverWith(e -> VIO.fail(new IllegalArgumentException()));

        Verifiers.<Triple<String, Boolean, String>>verifySuccess(
                         tuple -> Objects.equals(tuple,
                                                 Triple.of("a",
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
