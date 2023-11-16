package vertx.effect.api.exp;

import fun.gen.Gen;
import fun.tuple.Pair;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.Failures;
import vertx.effect.PairExp;
import vertx.effect.VIO;
import vertx.effect.VertxRef;
import vertx.effect.stub.StubBuilder;
import vertx.values.codecs.RegisterJsValuesCodecs;

import java.time.Duration;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static vertx.effect.RetryPolicies.constantDelay;
import static vertx.effect.RetryPolicies.limitRetries;

@SuppressWarnings("ReturnValueIgnored")
@ExtendWith(VertxExtension.class)
public class TestPair {


    private static final StubBuilder<String> a =
            StubBuilder.ofGen(Gen.seq(counter ->
                                              counter == 1 || counter == 2
                                                      ? VIO.fail(new RuntimeException("counter: " + counter)) :
                                                      VIO.succeed("a")
                                     )
                             );
    static VertxRef vertxRef;

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
    public void test_parallel_retry_if_success_with_delay(final VertxTestContext context) {


        int ATTEMPTS = 3;


        StubBuilder<Integer> one =
                StubBuilder.ofGen(Gen.seq(counter ->
                                                  counter <= ATTEMPTS
                                                          ? VIO.fail(new IllegalArgumentException()) :
                                                          VIO.succeed(1)
                                         )
                                 );
        PairExp.par(one.build(),
                    one.build()
                   )
               .retryEach(e -> e instanceof IllegalArgumentException,
                          limitRetries(ATTEMPTS).append(constantDelay(vertxRef.delay(Duration.ofMillis(100))))
                         )
               .onSuccess(it -> {
                   context.verify(() -> {
                       Assertions.assertEquals(Pair.of(1, 1), it);
                       context.completeNow();
                   });
               })
               .get();
    }

    @Test
    public void test_sequential_retry_if_success_with_delay(final VertxTestContext context) {


        int ATTEMPTS = 3;
        StubBuilder<Integer> one =
                StubBuilder.ofGen(Gen.seq(counter ->
                                                  counter <= ATTEMPTS
                                                          ? VIO.fail(new IllegalArgumentException()) :
                                                          VIO.succeed(1)
                                         )
                                 );
        PairExp.seq(one.build(),
                    one.build()
                   )
               .retryEach(e -> e instanceof IllegalArgumentException,
                          limitRetries(ATTEMPTS)
                                  .append(constantDelay(vertxRef.delay(Duration.ofMillis(100))))
                         )
               .onSuccess(it -> {
                   context.verify(() -> {
                       Assertions.assertEquals(Pair.of(1,
                                                       1
                                                      ),
                                               it
                                              );
                       context.completeNow();
                   });
               })
               .get();
    }

    @Test
    public void test_parallel_retries(VertxTestContext context) {

        PairExp.par(a.build(),
                    a.build()
                   )
               .retryEach(limitRetries(2))
               .get()
               .onComplete(it -> {
                   context.verify(() -> Assertions.assertEquals(Pair.of("a",
                                                                        "a"
                                                                       ),
                                                                it.result()
                                                               )
                                 );
                   context.completeNow();
               });


    }

    @Test
    public void test_sequential_retries(VertxTestContext context) {

        PairExp.seq(a.build(),
                    a.build()
                   )
               .retryEach(limitRetries(2))
               .get()
               .onComplete(it -> {
                   context.verify(() -> Assertions.assertEquals(Pair.of("a",
                                                                        "a"
                                                                       ),
                                                                it.result()
                                                               )
                                 );
                   context.completeNow();
               });


    }

    @Test
    public void test_parallel_retries_if_Success(VertxTestContext context) {


        StubBuilder<String> val =
                StubBuilder.ofGen(Gen.seq(counter ->
                                                  counter == 1 || counter == 2
                                                          ? VIO.fail(Failures.GET_BAD_MESSAGE_EXCEPTION.apply("counter " + counter)) :
                                                          VIO.succeed("a")
                                         )
                                 );

        PairExp.par(val.build(),
                    val.build()
                   )
               .retryEach(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                          limitRetries(2)
                         )
               .get()
               .onComplete(it -> {
                   context.verify(() -> Assertions.assertEquals(Pair.of("a",
                                                                        "a"
                                                                       ),
                                                                it.result()
                                                               )
                                 );
                   context.completeNow();
               });

    }

    @Test
    public void test_sequential_retries_if_Success(VertxTestContext context) {


        StubBuilder<String> val =
                StubBuilder.ofGen(Gen.seq(counter ->
                                                  counter == 1 || counter == 2
                                                          ? VIO.fail(Failures.GET_BAD_MESSAGE_EXCEPTION.apply("counter " + counter)) :
                                                          VIO.succeed("a")
                                         )
                                 );

        PairExp.seq(val.build(),
                    val.build()
                   )
               .retryEach(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                          limitRetries(2)
                         )
               .get()
               .onComplete(it -> {
                   context.verify(() -> Assertions.assertEquals(Pair.of("a",
                                                                        "a"
                                                                       ),
                                                                it.result()
                                                               )
                                 );
                   context.completeNow();
               });

    }

    @Test
    public void test_parallel_retries_if_failure(VertxTestContext context) {

        StubBuilder<String> val =
                StubBuilder.ofGen(Gen.seq(counter ->
                                                  counter <= 3
                                                          ? VIO.fail(Failures.GET_BAD_MESSAGE_EXCEPTION.apply("counter " + counter)) :
                                                          VIO.succeed("a")
                                         )
                                 );

        PairExp.par(val.build(),
                    val.build()
                   )
               .retryEach(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                          limitRetries(2)
                         )
               .get()
               .onComplete(it -> {
                   context.verify(() -> Assertions.assertTrue(it.failed())
                                 );
                   context.completeNow();
               });

    }

    @Test
    public void test_sequential_retries_if_failure(VertxTestContext context) {

        StubBuilder<String> val =
                StubBuilder.ofGen(Gen.seq(counter ->
                                                  counter <=3
                                                          ? VIO.fail(Failures.GET_BAD_MESSAGE_EXCEPTION.apply("counter " + counter)) :
                                                          VIO.succeed("a")
                                         )
                                 );

        PairExp.seq(val.build(),
                    val.build()
                   )
               .retryEach(Failures.REPLY_EXCEPTION_PRISM.exists.apply(v -> v.failureCode() == Failures.BAD_MESSAGE_CODE),
                          limitRetries(2)
                         )
               .get()
               .onComplete(it -> {
                   context.verify(() -> Assertions.assertTrue(it.failed())
                                 );
                   context.completeNow();
               });

    }

    @Test
    public void test_parallel_retry_with_delay(VertxTestContext context) {
        int ATTEMPTS = 2;

        long start = System.nanoTime();

        PairExp.par(a.build(),
                    a.build()
                   )
               .retryEach(limitRetries(ATTEMPTS)
                                  .append(constantDelay(vertxRef.delay(Duration.ofMillis(100))))
                         )
               .get()
               .onComplete(r -> context.verify(() -> {
                   Assertions.assertEquals(Pair.of("a",
                                                   "a"
                                                  ),
                                           r.result()
                                          );
                   Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= ATTEMPTS);
                   context.completeNow();

               }));

    }

    @Test
    public void test_sequential_retry_with_delay(VertxTestContext context) {
        int ATTEMPTS = 2;

        long start = System.nanoTime();

        PairExp.seq(a.build(),
                    a.build()
                   )
               .retryEach(limitRetries(ATTEMPTS)
                                  .append(constantDelay(vertxRef.delay(Duration.ofMillis(100))))
                         )
               .get()
               .onComplete(r -> context.verify(() -> {
                   Assertions.assertEquals(Pair.of("a",
                                                   "a"
                                                  ),
                                           r.result()
                                          );
                   Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= ATTEMPTS);
                   context.completeNow();

               }));

    }

    @Test
    public void test_parallel_pair_exp_map(VertxTestContext context) {

        PairExp.par(VIO.succeed("a"),
                    VIO.succeed("ab")
                   )
               .map(pair -> Pair.of(pair.first().length(),
                                    pair.second().length()
                                   ))
               .onSuccess(r -> context.verify(() -> {
                   Assertions.assertEquals(Pair.of(1,
                                                   2
                                                  ),
                                           r
                                          );
                   context.completeNow();
               }))
               .get();

    }

    @Test
    public void test_sequential_pair_exp_map(VertxTestContext context) {

        PairExp.seq(VIO.succeed("a"),
                    VIO.succeed("ab")
                   )
               .map(pair -> Pair.of(pair.first().length(), pair.second().length()))
               .onSuccess(r -> context.verify(() -> {
                   Assertions.assertEquals(Pair.of(1,
                                                   2
                                                  ),
                                           r
                                          );
                   context.completeNow();
               }))
               .get();

    }

    @Test
    public void test_parallel_pair_exp_flatmap_success(VertxTestContext context) {


        PairExp.par(VIO.succeed("a"),
                    VIO.succeed("b")
                   )
               .then(pair -> VIO.succeed(Pair.of(pair.first().toUpperCase(),
                                                 pair.second().toUpperCase()
                                                )
                                        ))
               .onSuccess(r -> context.verify(() -> {
                   Assertions.assertEquals(Pair.of("A",
                                                   "B"
                                                  ),
                                           r
                                          );
                   context.completeNow();
               }))
               .get();
    }

    @Test
    public void test_sequential_pair_exp_flatmap_success(VertxTestContext context) {


        PairExp.seq(VIO.succeed("a"),
                    VIO.succeed("b")
                   )
               .then(pair -> VIO.succeed(Pair.of(pair.first().toUpperCase(),
                                                 pair.second().toUpperCase()
                                                )))
               .onSuccess(r -> context.verify(() -> {
                   Assertions.assertEquals(Pair.of("A",
                                                   "B"
                                                  ),
                                           r
                                          );
                   context.completeNow();
               }))
               .get();
    }

    @Test
    public void test_parallel_pair_exp_flatmap_failure(VertxTestContext context) {


        PairExp.par(VIO.succeed("a"),
                    VIO.succeed("ab")
                   )
               .then(s -> VIO.fail(new RuntimeException()))
               .onComplete(r -> context.verify(() -> {
                   Assertions.assertTrue(r.failed());
                   context.completeNow();
               }))
               .get();

    }

    @Test
    public void test_sequential_pair_exp_flatmap_failure(VertxTestContext context) {


        PairExp.seq(VIO.succeed("a"),
                    VIO.succeed("ab")
                   )
               .then(s -> VIO.fail(new RuntimeException()))
               .onComplete(r -> context.verify(() -> {
                   Assertions.assertTrue(r.failed());
                   context.completeNow();
               }))
               .get();

    }

    @Test
    public void test_parallel_pair_exp_fails_and_recover_with_success(VertxTestContext context) {

        PairExp.par(a.build(),
                    a.build()
                   )
               .recoverWith(e -> VIO.succeed(Pair.of("",
                                                     ""
                                                    )))
               .onSuccess(map -> context.verify(() -> {
                   Assertions.assertEquals(Pair.of("",
                                                   ""
                                                  ),
                                           map
                                          );
                   context.completeNow();
               }))
               .get();
    }

    @Test
    public void test_sequential_pair_exp_fails_and_recover_with_success(VertxTestContext context) {

        PairExp.seq(a.build(),
                    a.build()
                   )
               .recoverWith(e -> VIO.succeed(Pair.of("",
                                                     ""
                                                    )))
               .onSuccess(map -> context.verify(() -> {
                   Assertions.assertEquals(Pair.of("",
                                                   ""
                                                  ),
                                           map
                                          );
                   context.completeNow();
               }))
               .get();
    }

    @Test
    public void test_parallel_pair_exp_fails_and_recover_with_failure(VertxTestContext context) {

        PairExp.par(a.build(),
                    a.build()
                   )
               .recoverWith(e -> VIO.fail(new IllegalArgumentException()))
               .onComplete(r -> context.verify(() -> {
                   Assertions.assertTrue(r.failed());
                   Assertions.assertTrue(r.cause() instanceof IllegalArgumentException);
                   context.completeNow();
               }))
               .get();
    }

    @Test
    public void test_sequential_pair_exp_fails_and_recover_with_failure(VertxTestContext context) {

        PairExp.seq(a.build(),
                    a.build()
                   )
               .recoverWith(e -> VIO.fail(new IllegalArgumentException()))
               .onComplete(r -> context.verify(() -> {
                   Assertions.assertTrue(r.failed());
                   Assertions.assertTrue(r.cause() instanceof IllegalArgumentException);
                   context.completeNow();
               }))
               .get();
    }

    @Test
    public void test_parallel_pair_exp_recover_with_success(VertxTestContext context) {
        PairExp.par(a.build(), a.build())
               .retryEach(limitRetries(2))
               .recoverWith(e -> VIO.fail(new IllegalArgumentException()))
               .onSuccess(map -> context.verify(() -> {
                   Assertions.assertEquals(Pair.of("a", "a"), map);
                   context.completeNow();
               })).get();
    }

    @Test
    public void test_sequential_pair_exp_recover_with_success(VertxTestContext context) {
        PairExp.seq(a.build(),
                    a.build()
                   )
               .retryEach(limitRetries(2))
               .recoverWith(e -> VIO.fail(new IllegalArgumentException()))
               .onSuccess(map -> context.verify(() -> {
                   Assertions.assertEquals(Pair.of("a",
                                                   "a"
                                                  ),
                                           map
                                          );
                   context.completeNow();
               }))
               .get();
    }
}
