package vertx.effect.api.exp;


import fun.gen.Gen;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.MapExp;
import vertx.effect.RetryPolicies;
import vertx.effect.VIO;
import vertx.effect.VertxRef;
import vertx.effect.stub.StubBuilder;
import vertx.values.codecs.RegisterJsValuesCodecs;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static vertx.effect.RetryPolicies.limitRetries;

@SuppressWarnings("ReturnValueIgnored")
@ExtendWith(VertxExtension.class)
public class TestSequentialMapExp {
    static final int ATTEMPTS = 2;


    static StubBuilder<String> a =
            StubBuilder.ofGen(Gen.seq(counter ->
                                              counter < ATTEMPTS ? VIO.fail(new RuntimeException("counter: " + counter)) :
                                                      VIO.succeed("a")
                                     )
                             );

    static StubBuilder<String> b =
            StubBuilder.ofGen(Gen.seq(counter ->
                                              counter < ATTEMPTS ? VIO.fail(new RuntimeException("counter: " + counter)) :
                                                      VIO.succeed("b")
                                     )
                             );

    static StubBuilder<Integer> one =
            StubBuilder.ofGen(Gen.seq(counter ->
                                              counter < ATTEMPTS ? VIO.fail(new RuntimeException("counter: " + counter)) :
                                                      VIO.succeed(1)
                                     )
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

        MapExp.seq("a",
                   VIO.succeed("a")
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
        MapExp.seq("a",
                   VIO.succeed("a"),
                   "b",
                   VIO.succeed("b")
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
        MapExp.seq("a",
                   VIO.succeed("a"),
                   "b",
                   VIO.succeed("b"),
                   "c",
                   VIO.succeed("c")
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
        MapExp.seq("a",
                   VIO.succeed("a"),
                   "b",
                   VIO.succeed("b"),
                   "c",
                   VIO.succeed("c"),
                   "d",
                   VIO.succeed("d")
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
        MapExp.seq("a",
                   VIO.succeed("a"),
                   "b",
                   VIO.succeed("b"),
                   "c",
                   VIO.succeed("c"),
                   "d",
                   VIO.succeed("d"),
                   "e",
                   VIO.succeed("e")
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
        MapExp.seq("a",
                   VIO.succeed("a"),
                   "b",
                   VIO.succeed("b"),
                   "c",
                   VIO.succeed("c"),
                   "d",
                   VIO.succeed("d"),
                   "e",
                   VIO.succeed("e"),
                   "f",
                   VIO.succeed("f")
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
        MapExp.seq("a",
                   VIO.succeed("a"),
                   "b",
                   VIO.succeed("b"),
                   "c",
                   VIO.succeed("c"),
                   "d",
                   VIO.succeed("d"),
                   "e",
                   VIO.succeed("e"),
                   "f",
                   VIO.succeed("f"),
                   "g",
                   VIO.succeed("g")
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
        MapExp.seq("a",
                   VIO.succeed("a"),
                   "b",
                   VIO.succeed("b"),
                   "c",
                   VIO.succeed("c"),
                   "d",
                   VIO.succeed("d"),
                   "e",
                   VIO.succeed("e"),
                   "f",
                   VIO.succeed("f"),
                   "g",
                   VIO.succeed("g"),
                   "h",
                   VIO.succeed("h")
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
        MapExp.seq("a",
                   VIO.succeed("a"),
                   "b",
                   VIO.succeed("b"),
                   "c",
                   VIO.succeed("c"),
                   "d",
                   VIO.succeed("d"),
                   "e",
                   VIO.succeed("e"),
                   "f",
                   VIO.succeed("f"),
                   "g",
                   VIO.succeed("g"),
                   "h",
                   VIO.succeed("h"),
                   "i",
                   VIO.succeed("i")
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
        MapExp.seq("a",
                   VIO.succeed("a"),
                   "b",
                   VIO.succeed("b"),
                   "c",
                   VIO.succeed("c"),
                   "d",
                   VIO.succeed("d"),
                   "e",
                   VIO.succeed("e"),
                   "f",
                   VIO.succeed("f"),
                   "g",
                   VIO.succeed("g"),
                   "h",
                   VIO.succeed("h"),
                   "i",
                   VIO.succeed("i"),
                   "j",
                   VIO.succeed("j")
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
        MapExp.seq("a",
                   VIO.succeed("a"),
                   "b",
                   VIO.succeed("b"),
                   "c",
                   VIO.succeed("c"),
                   "d",
                   VIO.succeed("d"),
                   "e",
                   VIO.succeed("e"),
                   "f",
                   VIO.succeed("f"),
                   "g",
                   VIO.succeed("g"),
                   "h",
                   VIO.succeed("h"),
                   "i",
                   VIO.succeed("i"),
                   "j",
                   VIO.succeed("j"),
                   "k",
                   VIO.succeed("k")
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
        MapExp.seq("a",
                   VIO.succeed("a"),
                   "b",
                   VIO.succeed("b"),
                   "c",
                   VIO.succeed("c"),
                   "d",
                   VIO.succeed("d"),
                   "e",
                   VIO.succeed("e"),
                   "f",
                   VIO.succeed("f"),
                   "g",
                   VIO.succeed("g"),
                   "h",
                   VIO.succeed("h"),
                   "i",
                   VIO.succeed("i"),
                   "j",
                   VIO.succeed("j"),
                   "k",
                   VIO.succeed("k"),
                   "l",
                   VIO.succeed("l")
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
        MapExp.seq("a",
                   VIO.succeed("a"),
                   "b",
                   VIO.succeed("b"),
                   "c",
                   VIO.succeed("c"),
                   "d",
                   VIO.succeed("d"),
                   "e",
                   VIO.succeed("e"),
                   "f",
                   VIO.succeed("f"),
                   "g",
                   VIO.succeed("g"),
                   "h",
                   VIO.succeed("h"),
                   "i",
                   VIO.succeed("i"),
                   "j",
                   VIO.succeed("j"),
                   "k",
                   VIO.succeed("k"),
                   "l",
                   VIO.succeed("l"),
                   "m",
                   VIO.succeed("m")
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
        MapExp.seq("a",
                   VIO.succeed("a"),
                   "b",
                   VIO.succeed("b"),
                   "c",
                   VIO.succeed("c"),
                   "d",
                   VIO.succeed("d"),
                   "e",
                   VIO.succeed("e"),
                   "f",
                   VIO.succeed("f"),
                   "g",
                   VIO.succeed("g"),
                   "h",
                   VIO.succeed("h"),
                   "i",
                   VIO.succeed("i"),
                   "j",
                   VIO.succeed("j"),
                   "k",
                   VIO.succeed("k"),
                   "l",
                   VIO.succeed("l"),
                   "m",
                   VIO.succeed("m"),
                   "n",
                   VIO.succeed("n")
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
        MapExp.seq("a",
                   VIO.succeed("a"),
                   "b",
                   VIO.succeed("b"),
                   "c",
                   VIO.succeed("c"),
                   "d",
                   VIO.succeed("d"),
                   "e",
                   VIO.succeed("e"),
                   "f",
                   VIO.succeed("f"),
                   "g",
                   VIO.succeed("g"),
                   "h",
                   VIO.succeed("h"),
                   "i",
                   VIO.succeed("i"),
                   "j",
                   VIO.succeed("j"),
                   "k",
                   VIO.succeed("k"),
                   "l",
                   VIO.succeed("l"),
                   "m",
                   VIO.succeed("m"),
                   "n",
                   VIO.succeed("n"),
                   "o",
                   VIO.succeed("o")
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
        MapExp.seq("a",
                   VIO.succeed("a"),
                   "b",
                   VIO.succeed("b")
                  )
              .then(m -> VIO.succeed(m.entrySet()
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

        MapExp.seq("a",
                   VIO.succeed("a"),
                   "b",
                   VIO.succeed("b")
                  )
              .then(s -> VIO.fail(new RuntimeException()))
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
        MapExp.seq("a",
                   a.build(),
                   "b",
                   one.build()
                  )
              .retryEach(RetryPolicies.limitRetries(2))
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
        Map<String, String> empty = new LinkedHashMap<>();
        MapExp.seq("a",
                   VIO.fail(new RuntimeException()),
                   "b",
                   b.build()
                  )
              .recoverWith(e -> VIO.succeed(empty))
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

        MapExp.seq("a",
                   VIO.fail(new RuntimeException()),
                   "b",
                   b.build()
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
    public void test_mapval_exp_recover_with_success(VertxTestContext context) {
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("a",
                     "a"
                    );
        expected.put("b",
                     "b"
                    );
        MapExp.seq("a",
                   a.build(),
                   "b",
                   b.build()
                  )
              .retryEach(RetryPolicies.limitRetries(ATTEMPTS))
              .recoverWith(e -> VIO.fail(new IllegalArgumentException()))
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

        MapExp.seq("a",
                   a.build(),
                   "b",
                   b.build()
                  )
              .retryEach(limitRetries(ATTEMPTS)
                                 .append(RetryPolicies.constantDelay(vertxRef.delay(Duration.ofMillis(100))))
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
