package vertx.effect.exp;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.RegisterJsValuesCodecs;
import vertx.effect.Val;
import vertx.effect.Verifiers;
import vertx.effect.VertxRef;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

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
                new ErrorWhile<>(counter -> counter <= ATTEMPTS,
                                 counter -> new RuntimeException("counter:+" + counter),
                                 "a"
                );

        Supplier<Val<String>> b =
                new ErrorWhile<>(counter -> counter <= ATTEMPTS,
                                 counter -> new RuntimeException("counter:+" + counter),
                                 "b"
                );

        Val<List<String>> val = ListExp.<String>sequential()
                .append(b.get())
                .prepend(a.get())
                .retry(ATTEMPTS);


        Verifiers.<List<String>>verifySuccess(list -> Objects.equals(list, expected))
                .accept(val,
                        context
                       );

    }

    @Test
    public void test_retry_if(VertxTestContext context) {
        List<String> expected = new ArrayList<>();
        expected.add("hi");
        expected.add("hi");
        ErrorWhile<String> hi = new ErrorWhile<>(3,
                                                 i -> new IllegalArgumentException(),
                                                 "hi"
        );
        ListExp.sequential(hi.get(),
                           hi.get()
                          )
               .retryIf(it -> it instanceof IllegalArgumentException,
                        3
                       )
               .onSuccess(it -> {
                   context.verify(() -> {
                       Assertions.assertEquals(expected,
                                               it
                                              );
                       context.completeNow();
                   });
               })
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
        ErrorWhile<String> hi = new ErrorWhile<>(3,
                                                 i -> new IllegalArgumentException(),
                                                 "hi"
        );
        ListExp.sequential(hi.get(),
                           hi.get()
                          )
               .retryIf(it -> it instanceof IllegalArgumentException,
                        3,
                        (e, i) -> vertxRef.delay(100,
                                                 MILLISECONDS
                                                )
                       )
               .onSuccess(it -> {
                   context.verify(() -> {
                       Assertions.assertEquals(expected,
                                               it
                                              );
                       context.completeNow();
                   });
               })
               .get();
    }

    @Test
    public void test_retry_with_delay(VertxTestContext context) {
        List<String> expected = new ArrayList<>();
        expected.add("a");
        expected.add("b");
        int ATTEMPTS = 3;
        Supplier<Val<String>> a =
                new ErrorWhile<>(counter -> counter <= ATTEMPTS,
                                 counter -> new RuntimeException("counter:+" + counter),
                                 "a"
                );

        Supplier<Val<String>> b =
                new ErrorWhile<>(counter -> counter <= ATTEMPTS,
                                 counter -> new RuntimeException("counter:+" + counter),
                                 "b"
                );


        long start = System.nanoTime();
        BiFunction<Throwable, Integer, Val<Void>> oneSecDelay =
                (error, n) -> vertxRef.delay(100,
                                             MILLISECONDS
                                            );
        Val<List<String>> val = ListExp.<String>sequential()
                .append(b.get())
                .prepend(a.get())
                .retry(ATTEMPTS,
                       oneSecDelay
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
                                                                     expected))
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
