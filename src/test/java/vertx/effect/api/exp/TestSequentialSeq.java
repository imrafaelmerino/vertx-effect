package vertx.effect.api.exp;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.*;
import vertx.effect.api.Verifiers;
import vertx.effect.stub.VIOStub;
import vertx.values.codecs.RegisterJsValuesCodecs;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static vertx.effect.RetryPolicies.limitRetries;
@SuppressWarnings("ReturnValueIgnored")
@ExtendWith(VertxExtension.class)
public class TestSequentialSeq {

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
    public void test_head_returns_the_first_element(VertxTestContext context) {


        VIO<String> val = ListExp.<String>seq()
                                 .append(VIO.succeed("a"))
                                 .append(VIO.succeed("b"))
                                 .head();

        Verifiers.<String>verifySuccess(head -> Objects.equals(head, "a"))
                 .accept(val,
                         context
                        );
    }

    @Test
    public void test_head_returns_the_tail(VertxTestContext context) {

        List<String> expected = new ArrayList<>();
        expected.add("b");
        expected.add("c");
        ListExp<String> val = ListExp.<String>seq()
                                     .append(VIO.succeed("a"))
                                     .append(VIO.succeed("b"))
                                     .append(VIO.succeed("c"))
                                     .tail();


        Verifiers.<List<String>>verifySuccess(tail -> Objects.equals(tail,
                                                                     expected
                                                                    )
                                             ).accept(val,
                                                      context
                                                     );


    }

    @Test
    public void test_retries(VertxTestContext context) {
        List<String> expected = new ArrayList<>();
        expected.add("a");
        expected.add("b");
        int ATTEMPTS = 3;

        Supplier<VIO<String>> a =
                VIOStub.failThenSucceed(counter ->
                                                counter <= ATTEMPTS ?
                                                        new RuntimeException("counter: " + counter) : null,
                                        "a"
                                       );

        Supplier<VIO<String>> b =
                VIOStub.failThenSucceed(counter ->
                                                counter <= ATTEMPTS ?
                                                        new RuntimeException("counter: " + counter) : null,
                                        "b"
                                       );

        VIO<List<String>> val = ListExp.<String>seq()
                                       .append(b.get())
                                       .prepend(a.get())
                                       .retryEach(limitRetries(ATTEMPTS));


        Verifiers.<List<String>>verifySuccess(list -> Objects.equals(list,
                                                                     expected
                                                                    ))
                 .accept(val,
                         context
                        );

    }

    @Test
    public void test_retry_if(VertxTestContext context) {
        List<String> expected = new ArrayList<>();
        expected.add("hi");
        expected.add("hi");
        VIOStub<String> hi = VIOStub.failThenSucceed(counter ->
                                                             counter <= 3 ?
                                                                     new IllegalArgumentException("counter: " + counter) : null,
                                                     "hi"
                                                    );
        ListExp.seq(hi.get(),
                    hi.get()
                   )
               .retryEach(e -> e instanceof IllegalArgumentException,
                          limitRetries(3)
                         )
               .onSuccess(it -> context.verify(() -> {
                   Assertions.assertEquals(expected,
                                           it
                                          );
                   context.completeNow();
               }))
               .get();
    }


    @Test
    public void test_append_all(VertxTestContext context) {
        List<String> expected = new ArrayList<>();
        expected.add("a");
        expected.add("b");
        expected.add("c");
        expected.add("d");
        ListExp<String> a = ListExp.seq(VIO.succeed("a"),
                                        VIO.succeed("b")
                                       );

        ListExp<String> b = ListExp.seq(VIO.succeed("c"),
                                        VIO.succeed("d")
                                       );

        a.appendAll(b)
         .onSuccess(list -> context.verify(() -> {
             Assertions.assertEquals(expected,
                                     list
                                    );
             context.completeNow();
         }))
         .get();

    }

    @Test
    public void test_retry_if_with_action(VertxTestContext context) {
        List<String> expected = new ArrayList<>();
        expected.add("hi");
        expected.add("hi");
        VIOStub<String> hi = VIOStub.failThenSucceed(counter ->
                                                             counter <= 3 ?
                                                                     new IllegalArgumentException("counter: " + counter) : null,
                                                     "hi"
                                                    );
        ListExp.seq(hi.get(),
                    hi.get()
                   )
               .retryEach(it -> it instanceof IllegalArgumentException,
                          limitRetries(3)
                                  .append(RetryPolicies.constantDelay(vertxRef.delay(Duration.ofMillis(100))))
                         )
               .onSuccess(it -> context.verify(() -> {
                   Assertions.assertEquals(expected,
                                           it
                                          );
                   context.completeNow();
               }))
               .get();
    }

    @Test
    public void test_retry_with_delay(VertxTestContext context) {
        List<String> expected = new ArrayList<>();
        expected.add("a");
        expected.add("b");
        int ATTEMPTS = 3;
        Supplier<VIO<String>> a =
                VIOStub.failThenSucceed(counter ->
                                                counter <= ATTEMPTS ?
                                                        new RuntimeException("counter: " + counter) : null,
                                        "a"
                                       );

        Supplier<VIO<String>> b =
                VIOStub.failThenSucceed(counter ->
                                                counter <= ATTEMPTS ?
                                                        new RuntimeException("counter: " + counter) : null,
                                        "b"
                                       );


        long start = System.nanoTime();

        VIO<List<String>> val = ListExp.<String>seq()
                                       .append(b.get())
                                       .prepend(a.get())
                                       .retryEach(limitRetries(ATTEMPTS)
                                                          .append(RetryPolicies.constantDelay(vertxRef.delay(Duration.ofMillis(100))))
                                                 );


        Verifiers.<List<String>>verifySuccess(list -> Objects.equals(list,
                                                                     expected
                                                                    )
                         && NANOSECONDS.toMillis(System.nanoTime() - start) >= ATTEMPTS)
                 .accept(val,
                         context
                        );

    }

    @Test
    public void test_map(VertxTestContext context) {
        List<String> expected = new ArrayList<>();
        expected.add("A");
        expected.add("B");
        VIO<List<String>> val = ListExp.<String>seq()
                                       .append(VIO.succeed("a"))
                                       .append(VIO.succeed("b"))
                                       .map(it -> it.stream()
                                                    .map(String::toUpperCase)
                                                    .collect(Collectors.toList()));


        Verifiers.<List<String>>verifySuccess(list -> Objects.equals(list,
                                                                     expected
                                                                    )
                                             )
                 .accept(val,
                         context
                        );

    }

    @Test
    public void test_seqval_exp_flatmap_failure(VertxTestContext context) {

        VIO<List<String>> val = ListExp.<String>seq()
                                       .append(VIO.succeed("a"))
                                       .append(VIO.succeed("b"))
                                       .then(s -> VIO.fail(new RuntimeException()));


        Verifiers.<List<String>>verifyFailure()
                 .accept(val,
                         context
                        );


    }

    @Test
    public void test_quintuple_exp_flatmap_success(VertxTestContext context) {
        List<String> expected = new ArrayList<>();
        expected.add("A");
        expected.add("B");
        VIO<List<String>> val = ListExp.<String>seq()
                                       .append(VIO.succeed("a"))
                                       .append(VIO.succeed("b"))
                                       .then(list -> VIO.succeed(list.stream()
                                                                     .map(String::toUpperCase)
                                                                     .collect(Collectors.toList())));

        Verifiers.<List<String>>verifySuccess(list -> Objects.equals(list,
                                                                     expected
                                                                    ))
                 .accept(val,
                         context
                        );


    }

    @Test
    public void test_size(VertxTestContext context) {
        Assertions.assertEquals(3,
                                ListExp.seq(VIO.succeed(1),
                                            VIO.succeed(2),
                                            VIO.succeed(3)
                                           )
                                       .size()
                               );
        context.completeNow();
    }

    @Test
    public void test_is_empty(VertxTestContext context) {

        Assertions.assertTrue(ListExp.seq()
                                     .isEmpty());
        context.completeNow();
    }
}
