package vertx.effect.exp;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.RegisterJsValuesCodecs;

import vertx.effect.RetryPolicies;
import vertx.effect.Val;
import vertx.effect.VertxRef;
import vertx.effect.mock.ValOrErrorMock;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static vertx.effect.RetryPolicies.limitRetries;

@ExtendWith(VertxExtension.class)
public class TestParallelMap {
    static final int ATTEMPTS = 2;

    static final Supplier<Val<String>> a =
            new ValOrErrorMock<>(ATTEMPTS,
                             counter -> new RuntimeException("counter: " + counter),
                                 "a"
            );

    static final Supplier<Val<String>> b =
            new ValOrErrorMock<>(ATTEMPTS,
                             counter -> new RuntimeException("counter: " + counter),
                                 "b"
            );
    static final Supplier<Val<Integer>> one =
            new ValOrErrorMock<>(ATTEMPTS,
                             counter -> new RuntimeException("counter: " + counter),
                                 1
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
                .onComplete(it -> testContext.completeNow())
                .get();
    }

    @Test
    public void test_map_exp_map_one_element(VertxTestContext context) {

        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("a",
                     "A"
                    );

        MapExp.parallel("a",
                        Val.succeed("a")
                       )
              .map(m -> m.entrySet()
                         .stream()
                         .collect(Collectors.toMap(java.util.Map.Entry::getKey,
                                                   e -> e.getValue()
                                                         .toUpperCase()
                                                  )))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(expected,
                                          r
                                         );
                  context.completeNow();
              }))
              .get();

    }

    @Test
    public void test_map_exp_map_two_elements(VertxTestContext context) {
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("a",
                     "A"
                    );
        expected.put("b",
                     "B"
                    );
        MapExp.parallel("a",
                        Val.succeed("a"),
                        "b",
                        Val.succeed("b")
                       )
              .map(m -> m.entrySet()
                         .stream()
                         .collect(Collectors.toMap(Map.Entry::getKey,
                                                   e -> e.getValue()
                                                         .toUpperCase()
                                                  ))
                  )
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(expected,
                                          r
                                         );
                  context.completeNow();
              }))
              .get();

    }

    @Test
    public void test_map_exp_map_three_elements(VertxTestContext context) {
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("a",
                     "A"
                    );
        expected.put("b",
                     "B"
                    );
        expected.put("c",
                     "C"
                    );
        MapExp.parallel("a",
                        Val.succeed("a"),
                        "b",
                        Val.succeed("b"),
                        "c",
                        Val.succeed("c")
                       )
              .map(m -> m.entrySet()
                         .stream()
                         .collect(Collectors.toMap(Map.Entry::getKey,
                                                   e -> e.getValue()
                                                         .toUpperCase()
                                                  )))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(expected,
                                          r
                                         );
                  context.completeNow();
              }))
              .get();

    }


    @Test
    public void test_map_exp_map_four_elements(VertxTestContext context) {
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("a",
                     "A"
                    );
        expected.put("b",
                     "B"
                    );
        expected.put("c",
                     "C"
                    );
        expected.put("d",
                     "D"
                    );
        MapExp.parallel("a",
                        Val.succeed("a"),
                        "b",
                        Val.succeed("b"),
                        "c",
                        Val.succeed("c"),
                        "d",
                        Val.succeed("d")
                       )
              .map(m -> m.entrySet()
                         .stream()
                         .collect(Collectors.toMap(Map.Entry::getKey,
                                                   e -> e.getValue()
                                                         .toUpperCase()
                                                  )))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(expected,
                                          r
                                         );
                  context.completeNow();
              }))
              .get();

    }

    @Test
    public void test_map_exp_map_five_elements(VertxTestContext context) {
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("a",
                     "A"
                    );
        expected.put("b",
                     "B"
                    );
        expected.put("c",
                     "C"
                    );
        expected.put("d",
                     "D"
                    );
        expected.put("e",
                     "E"
                    );
        MapExp.parallel("a",
                        Val.succeed("a"),
                        "b",
                        Val.succeed("b"),
                        "c",
                        Val.succeed("c"),
                        "d",
                        Val.succeed("d"),
                        "e",
                        Val.succeed("e")
                       )
              .map(m -> m.entrySet()
                         .stream()
                         .collect(Collectors.toMap(Map.Entry::getKey,
                                                   e -> e.getValue()
                                                         .toUpperCase()
                                                  )))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(expected,
                                          r
                                         );
                  context.completeNow();
              }))
              .get();

    }


    @Test
    public void test_map_exp_map_six_elements(VertxTestContext context) {
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("a",
                     "A"
                    );
        expected.put("b",
                     "B"
                    );
        expected.put("c",
                     "C"
                    );
        expected.put("d",
                     "D"
                    );
        expected.put("e",
                     "E"
                    );
        expected.put("f",
                     "F"
                    );
        MapExp.parallel("a",
                        Val.succeed("a"),
                        "b",
                        Val.succeed("b"),
                        "c",
                        Val.succeed("c"),
                        "d",
                        Val.succeed("d"),
                        "e",
                        Val.succeed("e"),
                        "f",
                        Val.succeed("f")
                       )
              .map(m -> m.entrySet()
                         .stream()
                         .collect(Collectors.toMap(Map.Entry::getKey,
                                                   e -> e.getValue()
                                                         .toUpperCase()
                                                  )))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(expected,
                                          r
                                         );
                  context.completeNow();
              }))
              .get();

    }

    @Test
    public void test_map_exp_map_seven_elements(VertxTestContext context) {
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("a",
                     "A"
                    );
        expected.put("b",
                     "B"
                    );
        expected.put("c",
                     "C"
                    );
        expected.put("d",
                     "D"
                    );
        expected.put("e",
                     "E"
                    );
        expected.put("f",
                     "F"
                    );
        expected.put("g",
                     "G"
                    );
        MapExp.parallel("a",
                        Val.succeed("a"),
                        "b",
                        Val.succeed("b"),
                        "c",
                        Val.succeed("c"),
                        "d",
                        Val.succeed("d"),
                        "e",
                        Val.succeed("e"),
                        "f",
                        Val.succeed("f"),
                        "g",
                        Val.succeed("g")
                       )
              .map(m -> m.entrySet()
                         .stream()
                         .collect(Collectors.toMap(Map.Entry::getKey,
                                                   e -> e.getValue()
                                                         .toUpperCase()
                                                  )))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(expected,
                                          r
                                         );
                  context.completeNow();
              }))
              .get();

    }

    @Test
    public void test_map_exp_map_eight_elements(VertxTestContext context) {
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("a",
                     "A"
                    );
        expected.put("b",
                     "B"
                    );
        expected.put("c",
                     "C"
                    );
        expected.put("d",
                     "D"
                    );
        expected.put("e",
                     "E"
                    );
        expected.put("f",
                     "F"
                    );
        expected.put("g",
                     "G"
                    );
        expected.put("h",
                     "H"
                    );
        MapExp.parallel("a",
                        Val.succeed("a"),
                        "b",
                        Val.succeed("b"),
                        "c",
                        Val.succeed("c"),
                        "d",
                        Val.succeed("d"),
                        "e",
                        Val.succeed("e"),
                        "f",
                        Val.succeed("f"),
                        "g",
                        Val.succeed("g"),
                        "h",
                        Val.succeed("h")
                       )
              .map(m -> m.entrySet()
                         .stream()
                         .collect(Collectors.toMap(Map.Entry::getKey,
                                                   e -> e.getValue()
                                                         .toUpperCase()
                                                  )))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(expected,
                                          r
                                         );
                  context.completeNow();
              }))
              .get();

    }

    @Test
    public void test_map_exp_map_nine_elements(VertxTestContext context) {
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("a",
                     "A"
                    );
        expected.put("b",
                     "B"
                    );
        expected.put("c",
                     "C"
                    );
        expected.put("d",
                     "D"
                    );
        expected.put("e",
                     "E"
                    );
        expected.put("f",
                     "F"
                    );
        expected.put("g",
                     "G"
                    );
        expected.put("h",
                     "H"
                    );
        expected.put("i",
                     "I"
                    );
        MapExp.parallel("a",
                        Val.succeed("a"),
                        "b",
                        Val.succeed("b"),
                        "c",
                        Val.succeed("c"),
                        "d",
                        Val.succeed("d"),
                        "e",
                        Val.succeed("e"),
                        "f",
                        Val.succeed("f"),
                        "g",
                        Val.succeed("g"),
                        "h",
                        Val.succeed("h"),
                        "i",
                        Val.succeed("i")
                       )
              .map(m -> m.entrySet()
                         .stream()
                         .collect(Collectors.toMap(Map.Entry::getKey,
                                                   e -> e.getValue()
                                                         .toUpperCase()
                                                  )))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(expected,
                                          r
                                         );
                  context.completeNow();
              }))
              .get();

    }

    @Test
    public void test_map_exp_map_ten_elements(VertxTestContext context) {
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("a",
                     "A"
                    );
        expected.put("b",
                     "B"
                    );
        expected.put("c",
                     "C"
                    );
        expected.put("d",
                     "D"
                    );
        expected.put("e",
                     "E"
                    );
        expected.put("f",
                     "F"
                    );
        expected.put("g",
                     "G"
                    );
        expected.put("h",
                     "H"
                    );
        expected.put("i",
                     "I"
                    );
        expected.put("j",
                     "J"
                    );
        MapExp.parallel("a",
                        Val.succeed("a"),
                        "b",
                        Val.succeed("b"),
                        "c",
                        Val.succeed("c"),
                        "d",
                        Val.succeed("d"),
                        "e",
                        Val.succeed("e"),
                        "f",
                        Val.succeed("f"),
                        "g",
                        Val.succeed("g"),
                        "h",
                        Val.succeed("h"),
                        "i",
                        Val.succeed("i"),
                        "j",
                        Val.succeed("j")
                       )
              .map(m -> m.entrySet()
                         .stream()
                         .collect(Collectors.toMap(Map.Entry::getKey,
                                                   e -> e.getValue()
                                                         .toUpperCase()
                                                  )))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(expected,
                                          r
                                         );
                  context.completeNow();
              }))
              .get();

    }

    @Test
    public void test_map_exp_map_eleven_elements(VertxTestContext context) {
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("a",
                     "A"
                    );
        expected.put("b",
                     "B"
                    );
        expected.put("c",
                     "C"
                    );
        expected.put("d",
                     "D"
                    );
        expected.put("e",
                     "E"
                    );
        expected.put("f",
                     "F"
                    );
        expected.put("g",
                     "G"
                    );
        expected.put("h",
                     "H"
                    );
        expected.put("i",
                     "I"
                    );
        expected.put("j",
                     "J"
                    );
        expected.put("k",
                     "K"
                    );
        MapExp.parallel("a",
                        Val.succeed("a"),
                        "b",
                        Val.succeed("b"),
                        "c",
                        Val.succeed("c"),
                        "d",
                        Val.succeed("d"),
                        "e",
                        Val.succeed("e"),
                        "f",
                        Val.succeed("f"),
                        "g",
                        Val.succeed("g"),
                        "h",
                        Val.succeed("h"),
                        "i",
                        Val.succeed("i"),
                        "j",
                        Val.succeed("j"),
                        "k",
                        Val.succeed("k")
                       )
              .map(m -> m.entrySet()
                         .stream()
                         .collect(Collectors.toMap(Map.Entry::getKey,
                                                   e -> e.getValue()
                                                         .toUpperCase()
                                                  )))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(expected,
                                          r
                                         );
                  context.completeNow();
              }))
              .get();

    }

    @Test
    public void test_map_exp_map_twelve_elements(VertxTestContext context) {
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("a",
                     "A"
                    );
        expected.put("b",
                     "B"
                    );
        expected.put("c",
                     "C"
                    );
        expected.put("d",
                     "D"
                    );
        expected.put("e",
                     "E"
                    );
        expected.put("f",
                     "F"
                    );
        expected.put("g",
                     "G"
                    );
        expected.put("h",
                     "H"
                    );
        expected.put("i",
                     "I"
                    );
        expected.put("j",
                     "J"
                    );
        expected.put("k",
                     "K"
                    );
        expected.put("l",
                     "L"
                    );
        MapExp.parallel("a",
                        Val.succeed("a"),
                        "b",
                        Val.succeed("b"),
                        "c",
                        Val.succeed("c"),
                        "d",
                        Val.succeed("d"),
                        "e",
                        Val.succeed("e"),
                        "f",
                        Val.succeed("f"),
                        "g",
                        Val.succeed("g"),
                        "h",
                        Val.succeed("h"),
                        "i",
                        Val.succeed("i"),
                        "j",
                        Val.succeed("j"),
                        "k",
                        Val.succeed("k"),
                        "l",
                        Val.succeed("l")
                       )
              .map(m -> m.entrySet()
                         .stream()
                         .collect(Collectors.toMap(Map.Entry::getKey,
                                                   e -> e.getValue()
                                                         .toUpperCase()
                                                  )))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(expected,
                                          r
                                         );
                  context.completeNow();
              }))
              .get();
    }

    @Test
    public void test_map_exp_map_thirteen_elements(VertxTestContext context) {
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("a",
                     "A"
                    );
        expected.put("b",
                     "B"
                    );
        expected.put("c",
                     "C"
                    );
        expected.put("d",
                     "D"
                    );
        expected.put("e",
                     "E"
                    );
        expected.put("f",
                     "F"
                    );
        expected.put("g",
                     "G"
                    );
        expected.put("h",
                     "H"
                    );
        expected.put("i",
                     "I"
                    );
        expected.put("j",
                     "J"
                    );
        expected.put("k",
                     "K"
                    );
        expected.put("l",
                     "L"
                    );
        expected.put("m",
                     "M"
                    );
        MapExp.parallel("a",
                        Val.succeed("a"),
                        "b",
                        Val.succeed("b"),
                        "c",
                        Val.succeed("c"),
                        "d",
                        Val.succeed("d"),
                        "e",
                        Val.succeed("e"),
                        "f",
                        Val.succeed("f"),
                        "g",
                        Val.succeed("g"),
                        "h",
                        Val.succeed("h"),
                        "i",
                        Val.succeed("i"),
                        "j",
                        Val.succeed("j"),
                        "k",
                        Val.succeed("k"),
                        "l",
                        Val.succeed("l"),
                        "m",
                        Val.succeed("m")
                       )
              .map(m -> m.entrySet()
                         .stream()
                         .collect(Collectors.toMap(Map.Entry::getKey,
                                                   e -> e.getValue()
                                                         .toUpperCase()
                                                  )))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(expected,
                                          r
                                         );
                  context.completeNow();
              }))
              .get();
    }

    @Test
    public void test_map_exp_map_fourteen_elements(VertxTestContext context) {
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("a",
                     "A"
                    );
        expected.put("b",
                     "B"
                    );
        expected.put("c",
                     "C"
                    );
        expected.put("d",
                     "D"
                    );
        expected.put("e",
                     "E"
                    );
        expected.put("f",
                     "F"
                    );
        expected.put("g",
                     "G"
                    );
        expected.put("h",
                     "H"
                    );
        expected.put("i",
                     "I"
                    );
        expected.put("j",
                     "J"
                    );
        expected.put("k",
                     "K"
                    );
        expected.put("l",
                     "L"
                    );
        expected.put("m",
                     "M"
                    );
        expected.put("n",
                     "N"
                    );
        MapExp.parallel("a",
                        Val.succeed("a"),
                        "b",
                        Val.succeed("b"),
                        "c",
                        Val.succeed("c"),
                        "d",
                        Val.succeed("d"),
                        "e",
                        Val.succeed("e"),
                        "f",
                        Val.succeed("f"),
                        "g",
                        Val.succeed("g"),
                        "h",
                        Val.succeed("h"),
                        "i",
                        Val.succeed("i"),
                        "j",
                        Val.succeed("j"),
                        "k",
                        Val.succeed("k"),
                        "l",
                        Val.succeed("l"),
                        "m",
                        Val.succeed("m"),
                        "n",
                        Val.succeed("n")
                       )
              .map(m -> m.entrySet()
                         .stream()
                         .collect(Collectors.toMap(Map.Entry::getKey,
                                                   e -> e.getValue()
                                                         .toUpperCase()
                                                  )))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(expected,
                                          r
                                         );
                  context.completeNow();
              }))
              .get();
    }

    @Test
    public void test_map_exp_map_fifteen_elements(VertxTestContext context) {
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("a",
                     "A"
                    );
        expected.put("b",
                     "B"
                    );
        expected.put("c",
                     "C"
                    );
        expected.put("d",
                     "D"
                    );
        expected.put("e",
                     "E"
                    );
        expected.put("f",
                     "F"
                    );
        expected.put("g",
                     "G"
                    );
        expected.put("h",
                     "H"
                    );
        expected.put("i",
                     "I"
                    );
        expected.put("j",
                     "J"
                    );
        expected.put("k",
                     "K"
                    );
        expected.put("l",
                     "L"
                    );
        expected.put("m",
                     "M"
                    );
        expected.put("n",
                     "N"
                    );
        expected.put("o",
                     "O"
                    );
        MapExp.parallel("a",
                        Val.succeed("a"),
                        "b",
                        Val.succeed("b"),
                        "c",
                        Val.succeed("c"),
                        "d",
                        Val.succeed("d"),
                        "e",
                        Val.succeed("e"),
                        "f",
                        Val.succeed("f"),
                        "g",
                        Val.succeed("g"),
                        "h",
                        Val.succeed("h"),
                        "i",
                        Val.succeed("i"),
                        "j",
                        Val.succeed("j"),
                        "k",
                        Val.succeed("k"),
                        "l",
                        Val.succeed("l"),
                        "m",
                        Val.succeed("m"),
                        "n",
                        Val.succeed("n"),
                        "o",
                        Val.succeed("o")
                       )
              .map(m -> m.entrySet()
                         .stream()
                         .collect(Collectors.toMap(Map.Entry::getKey,
                                                   e -> e.getValue()
                                                         .toUpperCase()
                                                  )))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(expected,
                                          r
                                         );
                  context.completeNow();
              }))
              .get();
    }

    @Test
    public void test_map_exp_flatmap_success(VertxTestContext context) {
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("a",
                     "A"
                    );
        expected.put("b",
                     "B"
                    );
        MapExp.parallel("a",
                        Val.succeed("a"),
                        "b",
                        Val.succeed("b")
                       )
              .flatMap(m -> Val.succeed(m.entrySet()
                                         .stream()
                                         .collect(Collectors.toMap(Map.Entry::getKey,
                                                                    e -> e.getValue()
                                                                          .toUpperCase()
                                                                   ))))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(expected,
                                          r
                                         );
                  context.completeNow();
              }))
              .get();


    }

    @Test
    public void test_map_exp_flatmap_failure(VertxTestContext context) {

        MapExp.parallel("a",
                        Val.succeed("a"),
                        "b",
                        Val.succeed("b")
                       )
              .flatMap(s -> Val.fail(new RuntimeException()))
              .onComplete(r -> context.verify(() -> {
                  Assertions.assertTrue(r.failed());
                  context.completeNow();
              }))
              .get();


    }


    @Test
    public void test_retries(VertxTestContext context) {

        Map<String, Object> expected = new LinkedHashMap<>();
        expected.put("a",
                     "a"
                    );
        expected.put("b",
                     1
                    );
        MapExp.parallel("a",
                        a.get(),
                        "b",
                        one.get()
                       )
              .retryEach(limitRetries(2))
              .onComplete(map ->
                                  context.verify(() -> {
                                      Assertions.assertEquals(expected,
                                                              map.result()
                                                             );
                                      context.completeNow();
                                  }))
              .get();


    }

    @Test
    public void test_mapval_exp_fails_and_recover_with_success(VertxTestContext context) {
        Map empty = new LinkedHashMap();
        MapExp.parallel("a",
                        Val.fail(new RuntimeException()),
                        "b",
                        b.get()
                       )
              .recoverWith(e -> Val.succeed(empty))
              .onSuccess(map -> context.verify(() -> {
                  Assertions.assertEquals(empty,
                                          map
                                         );
                  context.completeNow();
              }))
              .get();
    }

    @Test
    public void test_mapval_exp_fails_and_recover_with_failure(VertxTestContext context) {

        MapExp.parallel("a",
                        Val.fail(new RuntimeException()),
                        "b",
                        b.get()
                       )
              .recoverWith(e -> Val.fail(new IllegalArgumentException()))
              .onComplete(r -> context.verify(() -> {
                  Assertions.assertTrue(r.failed());
                  Assertions.assertTrue(r.cause() instanceof IllegalArgumentException);
                  context.completeNow();
              }))
              .get();
    }

    @Test
    public void test_mapval_exp_recover_with_success(VertxTestContext context) {
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("a",
                     "a"
                    );
        expected.put("b",
                     "b"
                    );
        MapExp.parallel("a",
                        a.get(),
                        "b",
                        b.get()
                       )
              .retryEach(limitRetries(ATTEMPTS)
                             )
              .recoverWith(e -> Val.fail(new IllegalArgumentException()))
              .onSuccess(map -> context.verify(() -> {
                  Assertions.assertEquals(expected,
                                          map
                                         );
                  context.completeNow();
              }))
              .get();
    }


    @Test
    public void test_retry_with_delay(VertxTestContext context) {
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("a",
                     "a"
                    );
        expected.put("b",
                     "b"
                    );
        long start = System.nanoTime();

        MapExp.parallel("a",
                        a.get(),
                        "b",
                        b.get()
                       )
              .retryEach(limitRetries(ATTEMPTS)
                             .append(RetryPolicies.constantDelay(vertxRef.sleep(Duration.ofMillis(100))))
                    )
              .get()
              .onComplete(r -> context.verify(() -> {
                  Assertions.assertEquals(expected,
                                          r.result()
                                         );
                  Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= ATTEMPTS);
                  context.completeNow();

              }));

    }

}
