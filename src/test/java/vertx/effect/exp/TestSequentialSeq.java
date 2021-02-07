package vertx.effect.exp;

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


        Val<String> val = ListExp.<String>sequential()
                .append(Cons.success("a"))
                .append(Cons.success("b"))
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

        List<String> expected = new ArrayList<>();
        expected.add("b");
        expected.add("c");
        ListExp<String> val = ListExp.<String>sequential()
                .append(Cons.success("a"))
                .append(Cons.success("b"))
                .append(Cons.success("c"))
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

        Val<List<String>> val = ListExp.<String>sequential()
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
        ValOrErrorMock<String> hi = new ValOrErrorMock<>(3,
                                                         i -> new IllegalArgumentException(),
                                                         "hi"
        );
        ListExp.sequential(hi.get(),
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
        ListExp<String> a = ListExp.sequential(Cons.success("a"),
                                               Cons.success("b")
                                              );

        ListExp<String> b = ListExp.sequential(Cons.success("c"),
                                               Cons.success("d")
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
        ValOrErrorMock<String> hi = new ValOrErrorMock<>(3,
                                                         i -> new IllegalArgumentException(),
                                                         "hi"
        );
        ListExp.sequential(hi.get(),
                           hi.get()
                          )
               .retryEach(it -> it instanceof IllegalArgumentException,
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
    public void test_retry_with_delay(VertxTestContext context) {
        List<String> expected = new ArrayList<>();
        expected.add("a");
        expected.add("b");
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

        Val<List<String>> val = ListExp.<String>sequential()
                .append(b.get())
                .prepend(a.get())
                .retryEach(limitRetries(ATTEMPTS)
                               .append(RetryPolicies.constantDelay(vertxRef.sleep(Duration.ofMillis(100))))
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
        Val<List<String>> val = ListExp.<String>sequential()
                .append(Cons.success("a"))
                .append(Cons.success("b"))
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

        Val<List<String>> val = ListExp.<String>sequential()
                .append(Cons.success("a"))
                .append(Cons.success("b"))
                .flatMap(s -> Cons.failure(new RuntimeException()));


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
        Val<List<String>> val = ListExp.<String>sequential()
                .append(Cons.success("a"))
                .append(Cons.success("b"))
                .flatMap(list -> Cons.success(list.stream()
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
                                ListExp.sequential(Cons.success(1),
                                                   Cons.success(2),
                                                   Cons.success(3)
                                                  )
                                       .size()
                               );
        context.completeNow();
    }

    @Test
    public void test_is_empty(VertxTestContext context) {

        Assertions.assertTrue(ListExp.sequential()
                                     .isEmpty());
        context.completeNow();
    }
}
