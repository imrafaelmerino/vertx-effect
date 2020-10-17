package vertxeffect.exp;

import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.Map;
import io.vavr.collection.TreeMap;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertxeffect.Val;
import vertxeffect.VertxRef;
import vertxeffect.RegisterJsValuesCodecs;

import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

@ExtendWith(VertxExtension.class)
public class TestMapVal {
    static final int ATTEMPTS = 2;

    static final Supplier<Val<String>> a =
            new ErrorWhile<>(ATTEMPTS,
                             counter -> new RuntimeException("counter: " + counter),
                             "a"
            );

    static final Supplier<Val<String>> b =
            new ErrorWhile<>(ATTEMPTS,
                             counter -> new RuntimeException("counter: " + counter),
                             "b"
            );
    static final Supplier<Val<Integer>> one =
            new ErrorWhile<>(ATTEMPTS,
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
                                  System.out::println);
        vertxRef.deploy(new RegisterJsValuesCodecs())
                .onComplete(it -> testContext.completeNow())
                .get();
    }

    @Test
    public void test_map_exp_map_one_element(VertxTestContext context) {

        MapVal.of("a",
                  Cons.success("a")
                 )
              .map(m -> m.mapValues(String::toUpperCase))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(TreeMap.<String, String>empty()
                                                  .put("a",
                                                       "A"
                                                      ),
                                          r
                                         );
                  context.completeNow();
              }))
              .get();

    }

    @Test
    public void test_map_exp_map_two_elements(VertxTestContext context) {

        MapVal.of("a",
                  Cons.success("a"),
                  "b",
                  Cons.success("b")
                 )
              .map(m -> m.mapValues(String::toUpperCase))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(TreeMap.<String, String>empty()
                                                  .put("a",
                                                       "A"
                                                      )
                                                  .put("b",
                                                       "B"
                                                      ),
                                          r
                                         );
                  context.completeNow();
              }))
              .get();

    }

    @Test
    public void test_map_exp_map_three_elements(VertxTestContext context) {

        MapVal.of("a",
                  Cons.success("a"),
                  "b",
                  Cons.success("b"),
                  "c",
                  Cons.success("c")
                 )
              .map(m -> m.mapValues(String::toUpperCase))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(TreeMap.<String, String>empty()
                                                  .put("a",
                                                       "A"
                                                      )
                                                  .put("b",
                                                       "B"
                                                      )
                                                  .put("c",
                                                       "C"
                                                      ),
                                          r
                                         );
                  context.completeNow();
              }))
              .get();

    }


    @Test
    public void test_map_exp_map_four_elements(VertxTestContext context) {

        MapVal.of("a",
                  Cons.success("a"),
                  "b",
                  Cons.success("b"),
                  "c",
                  Cons.success("c"),
                  "d",
                  Cons.success("d")
                 )
              .map(m -> m.mapValues(String::toUpperCase))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(TreeMap.<String, String>empty()
                                                  .put("a",
                                                       "A"
                                                      )
                                                  .put("b",
                                                       "B"
                                                      )
                                                  .put("c",
                                                       "C"
                                                      )
                                                  .put("d",
                                                       "D"
                                                      ),
                                          r
                                         );
                  context.completeNow();
              }))
              .get();

    }

    @Test
    public void test_map_exp_map_five_elements(VertxTestContext context) {

        MapVal.of("a",
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
              .map(m -> m.mapValues(String::toUpperCase))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(TreeMap.<String, String>empty()
                                                  .put("a",
                                                       "A"
                                                      )
                                                  .put("b",
                                                       "B"
                                                      )
                                                  .put("c",
                                                       "C"
                                                      )
                                                  .put("d",
                                                       "D"
                                                      )
                                                  .put("e",
                                                       "E"
                                                      ),
                                          r
                                         );
                  context.completeNow();
              }))
              .get();

    }


    @Test
    public void test_map_exp_map_six_elements(VertxTestContext context) {

        MapVal.of("a",
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
              .map(m -> m.mapValues(String::toUpperCase))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(TreeMap.<String, String>empty()
                                                  .put("a",
                                                       "A"
                                                      )
                                                  .put("b",
                                                       "B"
                                                      )
                                                  .put("c",
                                                       "C"
                                                      )
                                                  .put("d",
                                                       "D"
                                                      )
                                                  .put("e",
                                                       "E"
                                                      )
                                                  .put("f",
                                                       "F"
                                                      ),
                                          r
                                         );
                  context.completeNow();
              }))
              .get();

    }

    @Test
    public void test_map_exp_map_seven_elements(VertxTestContext context) {

        MapVal.of("a",
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
              .map(m -> m.mapValues(String::toUpperCase))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(TreeMap.<String, String>empty()
                                                  .put("a",
                                                       "A"
                                                      )
                                                  .put("b",
                                                       "B"
                                                      )
                                                  .put("c",
                                                       "C"
                                                      )
                                                  .put("d",
                                                       "D"
                                                      )
                                                  .put("e",
                                                       "E"
                                                      )
                                                  .put("f",
                                                       "F"
                                                      )
                                                  .put("g",
                                                       "G"
                                                      ),
                                          r
                                         );
                  context.completeNow();
              }))
              .get();

    }

    @Test
    public void test_map_exp_map_eight_elements(VertxTestContext context) {

        MapVal.of("a",
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
              .map(m -> m.mapValues(String::toUpperCase))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(TreeMap.<String, String>empty()
                                                  .put("a",
                                                       "A"
                                                      )
                                                  .put("b",
                                                       "B"
                                                      )
                                                  .put("c",
                                                       "C"
                                                      )
                                                  .put("d",
                                                       "D"
                                                      )
                                                  .put("e",
                                                       "E"
                                                      )
                                                  .put("f",
                                                       "F"
                                                      )
                                                  .put("g",
                                                       "G"
                                                      )
                                                  .put("h",
                                                       "H"
                                                      ),
                                          r
                                         );
                  context.completeNow();
              }))
              .get();

    }

    @Test
    public void test_map_exp_map_nine_elements(VertxTestContext context) {

        MapVal.of("a",
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
              .map(m -> m.mapValues(String::toUpperCase))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(TreeMap.<String, String>empty()
                                                  .put("a",
                                                       "A"
                                                      )
                                                  .put("b",
                                                       "B"
                                                      )
                                                  .put("c",
                                                       "C"
                                                      )
                                                  .put("d",
                                                       "D"
                                                      )
                                                  .put("e",
                                                       "E"
                                                      )
                                                  .put("f",
                                                       "F"
                                                      )
                                                  .put("g",
                                                       "G"
                                                      )
                                                  .put("h",
                                                       "H"
                                                      )
                                                  .put("i",
                                                       "I"
                                                      ),
                                          r
                                         );
                  context.completeNow();
              }))
              .get();

    }

    @Test
    public void test_map_exp_map_ten_elements(VertxTestContext context) {

        MapVal.of("a",
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
              .map(m -> m.mapValues(String::toUpperCase))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(TreeMap.<String, String>empty()
                                                  .put("a",
                                                       "A"
                                                      )
                                                  .put("b",
                                                       "B"
                                                      )
                                                  .put("c",
                                                       "C"
                                                      )
                                                  .put("d",
                                                       "D"
                                                      )
                                                  .put("e",
                                                       "E"
                                                      )
                                                  .put("f",
                                                       "F"
                                                      )
                                                  .put("g",
                                                       "G"
                                                      )
                                                  .put("h",
                                                       "H"
                                                      )
                                                  .put("i",
                                                       "I"
                                                      )
                                                  .put("j",
                                                       "J"
                                                      ),
                                          r
                                         );
                  context.completeNow();
              }))
              .get();

    }

    @Test
    public void test_map_exp_map_eleven_elements(VertxTestContext context) {

        MapVal.of("a",
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
              .map(m -> m.mapValues(String::toUpperCase))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(TreeMap.<String, String>empty()
                                                  .put("a",
                                                       "A"
                                                      )
                                                  .put("b",
                                                       "B"
                                                      )
                                                  .put("c",
                                                       "C"
                                                      )
                                                  .put("d",
                                                       "D"
                                                      )
                                                  .put("e",
                                                       "E"
                                                      )
                                                  .put("f",
                                                       "F"
                                                      )
                                                  .put("g",
                                                       "G"
                                                      )
                                                  .put("h",
                                                       "H"
                                                      )
                                                  .put("i",
                                                       "I"
                                                      )
                                                  .put("j",
                                                       "J"
                                                      )
                                                  .put("k",
                                                       "K"
                                                      ),
                                          r
                                         );
                  context.completeNow();
              }))
              .get();

    }

    @Test
    public void test_map_exp_map_twelve_elements(VertxTestContext context) {

        MapVal.of("a",
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
              .map(m -> m.mapValues(String::toUpperCase))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(TreeMap.<String, String>empty()
                                                  .put("a",
                                                       "A"
                                                      )
                                                  .put("b",
                                                       "B"
                                                      )
                                                  .put("c",
                                                       "C"
                                                      )
                                                  .put("d",
                                                       "D"
                                                      )
                                                  .put("e",
                                                       "E"
                                                      )
                                                  .put("f",
                                                       "F"
                                                      )
                                                  .put("g",
                                                       "G"
                                                      )
                                                  .put("h",
                                                       "H"
                                                      )
                                                  .put("i",
                                                       "I"
                                                      )
                                                  .put("j",
                                                       "J"
                                                      )
                                                  .put("k",
                                                       "K"
                                                      )
                                                  .put("l",
                                                       "L"
                                                      ),
                                          r
                                         );
                  context.completeNow();
              }))
              .get();
    }

    @Test
    public void test_map_exp_map_thirteen_elements(VertxTestContext context) {

        MapVal.of("a",
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
              .map(m -> m.mapValues(String::toUpperCase))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(TreeMap.<String, String>empty()
                                                  .put("a",
                                                       "A"
                                                      )
                                                  .put("b",
                                                       "B"
                                                      )
                                                  .put("c",
                                                       "C"
                                                      )
                                                  .put("d",
                                                       "D"
                                                      )
                                                  .put("e",
                                                       "E"
                                                      )
                                                  .put("f",
                                                       "F"
                                                      )
                                                  .put("g",
                                                       "G"
                                                      )
                                                  .put("h",
                                                       "H"
                                                      )
                                                  .put("i",
                                                       "I"
                                                      )
                                                  .put("j",
                                                       "J"
                                                      )
                                                  .put("k",
                                                       "K"
                                                      )
                                                  .put("l",
                                                       "L"
                                                      )
                                                  .put("m",
                                                       "M"
                                                      ),
                                          r
                                         );
                  context.completeNow();
              }))
              .get();
    }

    @Test
    public void test_map_exp_map_fourteen_elements(VertxTestContext context) {

        MapVal.of("a",
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
              .map(m -> m.mapValues(String::toUpperCase))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(TreeMap.<String, String>empty()
                                                  .put("a",
                                                       "A"
                                                      )
                                                  .put("b",
                                                       "B"
                                                      )
                                                  .put("c",
                                                       "C"
                                                      )
                                                  .put("d",
                                                       "D"
                                                      )
                                                  .put("e",
                                                       "E"
                                                      )
                                                  .put("f",
                                                       "F"
                                                      )
                                                  .put("g",
                                                       "G"
                                                      )
                                                  .put("h",
                                                       "H"
                                                      )
                                                  .put("i",
                                                       "I"
                                                      )
                                                  .put("j",
                                                       "J"
                                                      )
                                                  .put("k",
                                                       "K"
                                                      )
                                                  .put("l",
                                                       "L"
                                                      )
                                                  .put("m",
                                                       "M"
                                                      )
                                                  .put("n",
                                                       "N"
                                                      ),
                                          r
                                         );
                  context.completeNow();
              }))
              .get();
    }

    @Test
    public void test_map_exp_map_fifteen_elements(VertxTestContext context) {

        MapVal.of("a",
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
              .map(m -> m.mapValues(String::toUpperCase))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(TreeMap.<String, String>empty()
                                                  .put("a",
                                                       "A"
                                                      )
                                                  .put("b",
                                                       "B"
                                                      )
                                                  .put("c",
                                                       "C"
                                                      )
                                                  .put("d",
                                                       "D"
                                                      )
                                                  .put("e",
                                                       "E"
                                                      )
                                                  .put("f",
                                                       "F"
                                                      )
                                                  .put("g",
                                                       "G"
                                                      )
                                                  .put("h",
                                                       "H"
                                                      )
                                                  .put("i",
                                                       "I"
                                                      )
                                                  .put("j",
                                                       "J"
                                                      )
                                                  .put("k",
                                                       "K"
                                                      )
                                                  .put("l",
                                                       "L"
                                                      )
                                                  .put("m",
                                                       "M"
                                                      )
                                                  .put("n",
                                                       "N"
                                                      )
                                                  .put("o",
                                                       "O"
                                                      ),
                                          r
                                         );
                  context.completeNow();
              }))
              .get();
    }

    @Test
    public void test_map_exp_flatmap_success(VertxTestContext context) {

        MapVal.of("a",
                  Cons.success("a"),
                  "b",
                  Cons.success("b")
                 )
              .flatMap(m -> Cons.success(m.mapValues(String::toUpperCase)))
              .onSuccess(r -> context.verify(() -> {
                  Assertions.assertEquals(TreeMap.<String, String>empty()
                                                  .put("a",
                                                       "A"
                                                      )
                                                  .put("b",
                                                       "B"
                                                      ),
                                          r
                                         );
                  context.completeNow();
              }))
              .get();


    }

    @Test
    public void test_map_exp_flatmap_failure(VertxTestContext context) {

        MapVal.of("a",
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


        MapVal.<Object>of("a",
                          a.get(),
                          "b",
                          one.get()
                         )
                .retry(2)
                .onComplete(map ->
                                    context.verify(() -> {
                                        Map<String, ?> expected =
                                                LinkedHashMap.<String, Object>empty()
                                                        .put("a",
                                                             "a"
                                                            )
                                                        .put("b",
                                                             1

                                                            );
                                        Assertions.assertEquals(expected,
                                                                map.result()
                                                               );
                                        context.completeNow();
                                    }))
                .get();


    }

    @Test
    public void test_mapval_exp_fails_and_recover_with_success(VertxTestContext context) {

        MapVal.of("a",
                  Cons.failure(new RuntimeException()),
                  "b",
                  b.get()
                 )
              .recoverWith(e -> Cons.success(LinkedHashMap.empty()))
              .onSuccess(map -> context.verify(() -> {
                  Assertions.assertEquals(LinkedHashMap.empty(),
                                          map
                                         );
                  context.completeNow();
              }))
              .get();
    }

    @Test
    public void test_mapval_exp_fails_and_recover_with_failure(VertxTestContext context) {

        MapVal.of("a",
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
        MapVal.of("a",
                  a.get(),
                  "b",
                  b.get()
                 )
              .retry(ATTEMPTS)
              .recoverWith(e -> Cons.failure(new IllegalArgumentException()))
              .onSuccess(map -> context.verify(() -> {
                  Assertions.assertEquals(LinkedHashMap.<String, Object>empty()
                                                  .put("a",
                                                       "a"
                                                      )
                                                  .put("b",
                                                       "b"
                                                      ),
                                          map
                                         );
                  context.completeNow();
              }))
              .get();
    }


    @Test
    public void test_retry_with_delay(VertxTestContext context) {

        long start = System.nanoTime();

        MapVal.of("a",
                  a.get(),
                  "b",
                  b.get()
                 )
              .retry(ATTEMPTS,
                     (error, n) -> vertxRef.timer(1,
                                                  SECONDS,
                                                  "next attempt"
                                                 )
                    )
              .get()
              .onComplete(r -> context.verify(() -> {
                  Assertions.assertEquals(LinkedHashMap.<String, Object>empty()
                                                  .put("a",
                                                       "a"
                                                      )
                                                  .put("b",
                                                       "b"
                                                      ),
                                          r.result()
                                         );
                  Assertions.assertTrue(NANOSECONDS.toSeconds(System.nanoTime() - start) >= ATTEMPTS);
                  context.completeNow();

              }));

    }

}
