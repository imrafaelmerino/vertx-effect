package vertx.effect.api.exp;

import fun.gen.Gen;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsArray;
import jsonvalues.JsStr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.JsArrayExp;
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
public class TestJsArrayExp {

    private static VertxRef vertxRef;

    @BeforeAll
    public static void prepare(final Vertx vertx, final VertxTestContext testContext
                              ) {

        vertxRef = new VertxRef(vertx);
        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS, System.out::println);
        vertxRef.deployVerticle(new RegisterJsValuesCodecs()).onComplete(event -> testContext.completeNow()).get();
    }

    @Test
    public void test_array_exp_map(VertxTestContext context) {

        JsArrayExp.par(VIO.succeed(JsStr.of("a")), VIO.succeed(JsStr.of("b"))).map(arr -> arr.mapValues(value -> JsStr.prism.modify.apply(String::toUpperCase).apply(value))).onSuccess(r -> context.verify(() -> {
            Assertions.assertEquals(JsArray.of("A", "B"), r);
            context.completeNow();
        })).get();


    }

    @Test
    public void test_array_exp_flatmap_success(VertxTestContext context) {

        JsArrayExp.seq(VIO.succeed(JsStr.of("a")), VIO.succeed(JsStr.of("b"))).then(obj -> VIO.succeed(obj.mapValues(value -> JsStr.prism.modify.apply(String::toUpperCase).apply(value)))).onSuccess(r -> context.verify(() -> {
            Assertions.assertEquals(JsArray.of("A", "B"), r);
            context.completeNow();
        })).get();


    }

    @Test
    public void test_array_exp_flatmap_failure(VertxTestContext context) {
        JsArrayExp.par(VIO.succeed(JsStr.of("a")), VIO.succeed(JsStr.of("b"))).then(s -> VIO.fail(new RuntimeException())).onComplete(r -> context.verify(() -> {
            Assertions.assertTrue(r.failed());
            context.completeNow();
        })).get();

    }

    @Test
    public void test_array_exp_retry_with_delay(VertxTestContext context) {
        int ATTEMPTS = 3;

        long start = System.nanoTime();
        StubBuilder<String> a =
                StubBuilder.ofGen(Gen.seq(counter ->
                                                  counter < 3 ? VIO.fail(new RuntimeException("counter: " + counter)) :
                                                          VIO.succeed("a")
                                         )
                                 );

        StubBuilder<String> b =
                StubBuilder.ofGen(Gen.seq(counter ->
                                                  counter < 3 ? VIO.fail(new RuntimeException("counter: " + counter)) :
                                                          VIO.succeed("b")
                                         )
                                 );
        JsArrayExp.par(a.build().map(JsStr::of), b.build().map(JsStr::of)).retryEach(limitRetries(ATTEMPTS).append(constantDelay(vertxRef.delay(Duration.ofMillis(100))))).get().onComplete(r -> context.verify(() -> {
            Assertions.assertEquals(JsArray.of("a", "b"), r.result());
            Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= ATTEMPTS);
            context.completeNow();

        }));

    }

    @Test
    public void test_array_exp_retry_if_with_delay_success(VertxTestContext context) {
        int ATTEMPTS = 3;

        long start = System.nanoTime();
        StubBuilder<JsStr> a =
                StubBuilder.ofGen(Gen.seq(counter ->
                                                  counter < 3 ? VIO.fail(new RuntimeException("counter: " + counter)) :
                                                          VIO.succeed(JsStr.of("a"))
                                         )
                                 );

        StubBuilder<JsStr> b =
                StubBuilder.ofGen(Gen.seq(counter ->
                                                  counter < 3 ? VIO.fail(new RuntimeException("counter: " + counter)) :
                                                          VIO.succeed(JsStr.of("b"))
                                         )
                                 );
        JsArrayExp.seq(a.build(), b.build()).retryEach(e -> e instanceof RuntimeException, limitRetries(ATTEMPTS).append(constantDelay(vertxRef.delay(Duration.ofMillis(100))))).get().onComplete(r -> context.verify(() -> {
            Assertions.assertEquals(JsArray.of("a", "b"), r.result());
            Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= ATTEMPTS);
            context.completeNow();

        }));

    }

    @Test
    public void test_array_exp_retry_with_delay_success(VertxTestContext context) {
        int ATTEMPTS = 3;

        long start = System.nanoTime();
        StubBuilder<JsStr> a =
                StubBuilder.ofGen(Gen.seq(counter ->
                                                  counter < 3 ?
                                                          VIO.fail(new RuntimeException("counter: " + counter)) :
                                                          VIO.succeed(JsStr.of("a"))
                                         )
                                 );

        StubBuilder<JsStr> b =
                StubBuilder.ofGen(Gen.seq(counter ->
                                                  counter < 3 ?
                                                          VIO.fail(new RuntimeException("counter: " + counter)) :
                                                          VIO.succeed(JsStr.of("b"))
                                         )
                                 );
        JsArrayExp.seq(a.build(), b.build()).retryEach(limitRetries(ATTEMPTS).append(constantDelay(vertxRef.delay(Duration.ofMillis(100))))).get().onComplete(r -> context.verify(() -> {
            Assertions.assertEquals(JsArray.of("a", "b"), r.result());
            Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= ATTEMPTS);
            context.completeNow();

        }));

    }

    @Test
    public void test_array_exp_retry_if_success(VertxTestContext context) {
        int ATTEMPTS = 3;

        StubBuilder<JsStr> a =
                StubBuilder.ofGen(Gen.seq(counter ->
                                                  counter < 3 ? VIO.fail(new RuntimeException("counter: " + counter)) :
                                                          VIO.succeed(JsStr.of("a"))
                                         )
                                 );

        StubBuilder<JsStr> b =
                StubBuilder.ofGen(Gen.seq(counter ->
                                                  counter < 3 ? VIO.fail(new RuntimeException("counter: " + counter)) :
                                                          VIO.succeed(JsStr.of("b"))
                                         )
                                 );
        JsArrayExp.seq(a.build(), b.build()).retryEach(e -> e instanceof RuntimeException, limitRetries(ATTEMPTS)).onComplete(r -> context.verify(() -> {
            Assertions.assertEquals(JsArray.of("a", "b"), r.result());
            context.completeNow();

        })).get();

    }

    @Test
    public void test_array_exp_retryEach_with_delay_failure(VertxTestContext context) {
        int ATTEMPTS = 3;

        long start = System.nanoTime();
        StubBuilder<JsStr> a =
                StubBuilder.ofGen(Gen.seq(counter ->
                                                  counter <= ATTEMPTS ? VIO.fail(new RuntimeException("counter: " + counter)) :
                                                          VIO.succeed(JsStr.of("a"))
                                         )
                                 );

        StubBuilder<JsStr> b =
                StubBuilder.ofGen(Gen.seq(counter ->
                                                  counter < 3 ? VIO.fail(new RuntimeException("counter: " + counter)) :
                                                          VIO.succeed(JsStr.of("b"))
                                         )
                                 );
        JsArrayExp.par(a.build(), b.build()).retryEach(limitRetries(ATTEMPTS - 1).append(constantDelay(vertxRef.delay(Duration.ofMillis(100))))).onComplete(r -> context.verify(() -> {
            Assertions.assertTrue(r.cause() instanceof RuntimeException);
            Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= ATTEMPTS - 1);
            context.completeNow();
        })).get();

    }

    @Test
    public void test_array_exp_retry_with_delay_failure(VertxTestContext context) {
        int ATTEMPTS = 3;

        long start = System.nanoTime();
        StubBuilder<JsStr> a =
                StubBuilder.ofGen(Gen.seq(counter ->
                                                  counter <= ATTEMPTS ? VIO.fail(new RuntimeException("counter: " + counter)) :
                                                          VIO.succeed(JsStr.of("a"))
                                         )
                                 );

        StubBuilder<JsStr> b =
                StubBuilder.ofGen(Gen.seq(counter ->
                                                  counter <= ATTEMPTS ? VIO.fail(new RuntimeException("counter: " + counter)) :
                                                          VIO.succeed(JsStr.of("b"))
                                         )
                                 );
        JsArrayExp.par(a.build(), b.build())
                  .retry(limitRetries(2).append(constantDelay(vertxRef.delay(Duration.ofMillis(100)))))
                  .onComplete(r -> context.verify(() -> {
            Assertions.assertTrue(r.cause() instanceof RuntimeException);
            Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= 2);
            context.completeNow();
        })).get();

    }

    @Test
    public void test_array_exp_retry_if_with_delay_failure(VertxTestContext context) {
        int ATTEMPTS = 3;

        long start = System.nanoTime();
        StubBuilder<JsStr> a =
                StubBuilder.ofGen(Gen.seq(counter ->
                                                  counter <= ATTEMPTS ? VIO.fail(new RuntimeException("counter: " + counter)) :
                                                          VIO.succeed(JsStr.of("a"))
                                         )
                                 );

        StubBuilder<JsStr> b =
                StubBuilder.ofGen(Gen.seq(counter ->
                                                  counter <= ATTEMPTS ? VIO.fail(new RuntimeException("counter: " + counter)) :
                                                          VIO.succeed(JsStr.of("b"))
                                         )
                                 );
        JsArrayExp.par(a.build(), b.build())
                  .retryEach(e -> e instanceof RuntimeException,
                             limitRetries(ATTEMPTS - 1).append(constantDelay(vertxRef.delay(Duration.ofMillis(100)))))
                  .get()
                  .onComplete(r -> context.verify(() -> {
                      Assertions.assertTrue(r.cause() instanceof RuntimeException);
                      Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= ATTEMPTS - 1);
                      context.completeNow();

                  }));

    }


}
