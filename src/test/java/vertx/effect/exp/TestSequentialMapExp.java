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
import vertx.effect.VertxRef;
import vertx.effect.mock.ValOrErrorMock;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

@ExtendWith(VertxExtension.class)
public class TestSequentialMapExp {
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

        MapExp.sequential("a",
                          Cons.success("a")
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
        MapExp.sequential("a",
                          Cons.success("a"),
                          "b",
                          Cons.success("b")
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
        MapExp.sequential("a",
                          Cons.success("a"),
                          "b",
                          Cons.success("b"),
                          "c",
                          Cons.success("c")
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
        MapExp.sequential("a",
                          Cons.success("a"),
                          "b",
                          Cons.success("b"),
                          "c",
                          Cons.success("c"),
                          "d",
                          Cons.success("d")
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
        MapExp.sequential("a",
                          Cons.success("a"),
                          "b",
                          Cons.success("b"),
                          "c",
                          Cons.success("c"),
                          "d",
                          Cons.success("d"),
                          "e",
                          Cons.success("e")
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
        MapExp.sequential("a",
                          Cons.success("a"),
                          "b",
                          Cons.success("b"),
                          "c",
                          Cons.success("c"),
                          "d",
                          Cons.success("d"),
                          "e",
                          Cons.success("e"),
                          "f",
                          Cons.success("f")
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
        MapExp.sequential("a",
                          Cons.success("a"),
                          "b",
                          Cons.success("b"),
                          "c",
                          Cons.success("c"),
                          "d",
                          Cons.success("d"),
                          "e",
                          Cons.success("e"),
                          "f",
                          Cons.success("f"),
                          "g",
                          Cons.success("g")
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
        MapExp.sequential("a",
                          Cons.success("a"),
                          "b",
                          Cons.success("b"),
                          "c",
                          Cons.success("c"),
                          "d",
                          Cons.success("d"),
                          "e",
                          Cons.success("e"),
                          "f",
                          Cons.success("f"),
                          "g",
                          Cons.success("g"),
                          "h",
                          Cons.success("h")
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
        MapExp.sequential("a",
                          Cons.success("a"),
                          "b",
                          Cons.success("b"),
                          "c",
                          Cons.success("c"),
                          "d",
                          Cons.success("d"),
                          "e",
                          Cons.success("e"),
                          "f",
                          Cons.success("f"),
                          "g",
                          Cons.success("g"),
                          "h",
                          Cons.success("h"),
                          "i",
                          Cons.success("i")
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
        MapExp.sequential("a",
                          Cons.success("a"),
                          "b",
                          Cons.success("b"),
                          "c",
                          Cons.success("c"),
                          "d",
                          Cons.success("d"),
                          "e",
                          Cons.success("e"),
                          "f",
                          Cons.success("f"),
                          "g",
                          Cons.success("g"),
                          "h",
                          Cons.success("h"),
                          "i",
                          Cons.success("i"),
                          "j",
                          Cons.success("j")
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
        MapExp.sequential("a",
                          Cons.success("a"),
                          "b",
                          Cons.success("b"),
                          "c",
                          Cons.success("c"),
                          "d",
                          Cons.success("d"),
                          "e",
                          Cons.success("e"),
                          "f",
                          Cons.success("f"),
                          "g",
                          Cons.success("g"),
                          "h",
                          Cons.success("h"),
                          "i",
                          Cons.success("i"),
                          "j",
                          Cons.success("j"),
                          "k",
                          Cons.success("k")
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
        MapExp.sequential("a",
                          Cons.success("a"),
                          "b",
                          Cons.success("b"),
                          "c",
                          Cons.success("c"),
                          "d",
                          Cons.success("d"),
                          "e",
                          Cons.success("e"),
                          "f",
                          Cons.success("f"),
                          "g",
                          Cons.success("g"),
                          "h",
                          Cons.success("h"),
                          "i",
                          Cons.success("i"),
                          "j",
                          Cons.success("j"),
                          "k",
                          Cons.success("k"),
                          "l",
                          Cons.success("l")
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
        MapExp.sequential("a",
                          Cons.success("a"),
                          "b",
                          Cons.success("b"),
                          "c",
                          Cons.success("c"),
                          "d",
                          Cons.success("d"),
                          "e",
                          Cons.success("e"),
                          "f",
                          Cons.success("f"),
                          "g",
                          Cons.success("g"),
                          "h",
                          Cons.success("h"),
                          "i",
                          Cons.success("i"),
                          "j",
                          Cons.success("j"),
                          "k",
                          Cons.success("k"),
                          "l",
                          Cons.success("l"),
                          "m",
                          Cons.success("m")
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
        MapExp.sequential("a",
                          Cons.success("a"),
                          "b",
                          Cons.success("b"),
                          "c",
                          Cons.success("c"),
                          "d",
                          Cons.success("d"),
                          "e",
                          Cons.success("e"),
                          "f",
                          Cons.success("f"),
                          "g",
                          Cons.success("g"),
                          "h",
                          Cons.success("h"),
                          "i",
                          Cons.success("i"),
                          "j",
                          Cons.success("j"),
                          "k",
                          Cons.success("k"),
                          "l",
                          Cons.success("l"),
                          "m",
                          Cons.success("m"),
                          "n",
                          Cons.success("n")
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
        MapExp.sequential("a",
                          Cons.success("a"),
                          "b",
                          Cons.success("b"),
                          "c",
                          Cons.success("c"),
                          "d",
                          Cons.success("d"),
                          "e",
                          Cons.success("e"),
                          "f",
                          Cons.success("f"),
                          "g",
                          Cons.success("g"),
                          "h",
                          Cons.success("h"),
                          "i",
                          Cons.success("i"),
                          "j",
                          Cons.success("j"),
                          "k",
                          Cons.success("k"),
                          "l",
                          Cons.success("l"),
                          "m",
                          Cons.success("m"),
                          "n",
                          Cons.success("n"),
                          "o",
                          Cons.success("o")
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
        MapExp.sequential("a",
                          Cons.success("a"),
                          "b",
                          Cons.success("b")
                         )
              .flatMap(m -> Cons.success(m.entrySet()
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

        MapExp.sequential("a",
                          Cons.success("a"),
                          "b",
                          Cons.success("b")
                         )
              .flatMap(s -> Cons.failure(new RuntimeException()))
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
        MapExp.sequential("a",
                          a.get(),
                          "b",
                          one.get()
                         )
              .retry(2)
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
        MapExp.sequential("a",
                          Cons.failure(new RuntimeException()),
                          "b",
                          b.get()
                         )
              .recoverWith(e -> Cons.success(empty))
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

        MapExp.sequential("a",
                          Cons.failure(new RuntimeException()),
                          "b",
                          b.get()
                         )
              .recoverWith(e -> Cons.failure(new IllegalArgumentException()))
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
        MapExp.sequential("a",
                          a.get(),
                          "b",
                          b.get()
                         )
              .retry(ATTEMPTS)
              .recoverWith(e -> Cons.failure(new IllegalArgumentException()))
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

        MapExp.sequential("a",
                          a.get(),
                          "b",
                          b.get()
                         )
              .retry(ATTEMPTS,
                     (error, n) -> vertxRef.delay(100,
                                                  MILLISECONDS
                                                 )
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
