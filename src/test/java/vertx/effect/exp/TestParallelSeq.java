package vertx.effect.exp;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.*;
import vertx.effect.mock.ValOrErrorMock;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static vertx.effect.RetryPolicies.limitRetries;

@ExtendWith(VertxExtension.class)
public class TestParallelSeq {

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


        Val<String> val = ListExp.<String>parallel()
                .append(Val.succeed("a"))
                .append(Val.succeed("b"))
                .head();

        Verifiers.<String>verifySuccess(head -> Objects.equals(head,
                                                               "a"
                                                              )
                                       )
                .accept(val,
                        context
                       );
    }

    @Test
    public void test_head_returns_the_tail(VertxTestContext context) {


        ListExp<String> val = ListExp.<String>parallel()
                .append(Val.succeed("a"))
                .append(Val.succeed("b"))
                .append(Val.succeed("c"))
                .tail();

        List<String> expected = new ArrayList<>();
        expected.add("b");
        expected.add("c");
        Verifiers.<List<String>>verifySuccess(tail -> Objects.equals(tail,
                                                                     expected
                                                                    )
                                             ).accept(val,
                                                      context
                                                     );


    }

    @Test
    public void test_retries(VertxTestContext context) {
        int ATTEMPTS = 3;

        Supplier<Val<String>> a =
                new ValOrErrorMock<>(counter -> counter <= ATTEMPTS,
                                     counter -> new RuntimeException("counter:+" + counter),
                                     "a"
                );

        Supplier<Val<String>> b =
                new ValOrErrorMock<>(counter -> counter <= ATTEMPTS,
                                     counter -> new RuntimeException("counter:+" + counter),
                                     "b"
                );

        Val<List<String>> val = ListExp.<String>parallel()
                .append(b.get())
                .prepend(a.get())
                .retryEach(limitRetries(ATTEMPTS));
        List<String> expected = new ArrayList<>();
        expected.add("a");
        expected.add("b");

        Verifiers.<List<String>>verifySuccess(list -> Objects.equals(list,
                                                                     expected
                                                                    ))
                .accept(val,
                        context
                       );

    }

    @Test
    public void test_retry_with_delay(VertxTestContext context) {
        int ATTEMPTS = 3;
        Supplier<Val<String>> a =
                new ValOrErrorMock<>(counter -> counter <= ATTEMPTS,
                                     counter -> new RuntimeException("counter:+" + counter),
                                     "a"
                );

        Supplier<Val<String>> b =
                new ValOrErrorMock<>(counter -> counter <= ATTEMPTS,
                                     counter -> new RuntimeException("counter:+" + counter),
                                     "b"
                );

        long start = System.nanoTime();

        Val<List<String>> val = ListExp.<String>parallel()
                .append(b.get())
                .prepend(a.get())
                .retryEach(limitRetries(ATTEMPTS)
                               .append(RetryPolicies.constantDelay(vertxRef.sleep(Duration.ofMillis(100))))
                      );

        List<String> expected = new ArrayList<>();
        expected.add("a");
        expected.add("b");
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

        Val<List<String>> val = ListExp.<String>parallel()
                .append(Val.succeed("a"))
                .append(Val.succeed("b"))
                .map(it -> it.stream()
                             .map(String::toUpperCase)
                             .collect(Collectors.toList()));

        List<String> expected = new ArrayList<>();
        expected.add("A");
        expected.add("B");
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

        Val<List<String>> val = ListExp.<String>parallel()
                .append(Val.succeed("a"))
                .append(Val.succeed("b"))
                .flatMap(s -> Val.fail(new RuntimeException()));


        Verifiers.<List<String>>verifyFailure()
                .accept(val,
                        context
                       );


    }

    @Test
    public void test_retry_if(VertxTestContext context) {
        List<String> expected = new ArrayList<>();
        expected.add("hi");
        expected.add("hi");
        ValOrErrorMock<String> hi = new ValOrErrorMock<>(3,
                                                         i -> new IllegalArgumentException(),
                                                         "hi"
        );
        ListExp.parallel(hi.get(),
                         hi.get()
                        )
               .retryEach(it -> it instanceof IllegalArgumentException,
                      RetryPolicies.limitRetries(3)
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
    public void test_retry_if_with_action(VertxTestContext context) {
        List<String> expected = new ArrayList<>();
        expected.add("hi");
        expected.add("hi");
        ValOrErrorMock<String> hi = new ValOrErrorMock<>(3,
                                                         i -> new IllegalArgumentException(),
                                                         "hi"
        );
        ListExp.parallel(hi.get(),
                         hi.get()
                        )
               .retryEach(e -> e instanceof IllegalArgumentException,
                      limitRetries(3)
                              .append(RetryPolicies.constantDelay(vertxRef.sleep(Duration.ofMillis(100))))
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
    public void test_quintuple_exp_flatmap_success(VertxTestContext context) {
        List<String> expected = new ArrayList<>();
        expected.add("A");
        expected.add("B");
        Val<List<String>> val = ListExp.<String>parallel()
                .append(Val.succeed("a"))
                .append(Val.succeed("b"))
                .flatMap(list -> Val.succeed(list.stream()
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
    public void test_append_all(VertxTestContext context) {
        List<String> expected = new ArrayList<>();
        expected.add("a");
        expected.add("b");
        expected.add("c");
        expected.add("d");
        ListExp<String> a = ListExp.parallel(Val.succeed("a"),
                                             Val.succeed("b")
                                            );

        ListExp<String> b = ListExp.parallel(Val.succeed("c"),
                                             Val.succeed("d")
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
    public void test_size(VertxTestContext context) {
        Assertions.assertEquals(3,
                                ListExp.parallel(Val.succeed(1),
                                                 Val.succeed(2),
                                                 Val.succeed(3)
                                                )
                                       .size()
                               );
        context.completeNow();
    }

    @Test
    public void test_is_empty(VertxTestContext context) {

        Assertions.assertTrue(ListExp.parallel()
                                     .isEmpty());
        context.completeNow();
    }

    @Test
    public void test_race(final VertxTestContext context) {
        Val<String> a = Val.succeed("a");
        Val<String> b = Val.succeed("b");
        ListExp.parallel(a,
                         b
                        )
               .race()
               .onSuccess(it -> context.verify(() -> {
                   Assertions.assertEquals("a",
                                           it
                                          );
                   context.completeNow();
               }))
               .get();
    }


}
