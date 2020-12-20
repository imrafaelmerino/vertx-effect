package vertx.effect.exp;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.RegisterJsValuesCodecs;
import vertx.effect.Val;
import vertx.effect.VertxRef;
import vertx.effect.mock.ValOrErrorMock;

import java.time.Instant;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.*;

@ExtendWith(VertxExtension.class)
public class TestJsObjExp {

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
    public void test_sequential_jsobj_exp_one_elem(VertxTestContext context) {
        JsObjExp.sequential("a",
                            Cons.success(JsStr.of("a"))
                           )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a")
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_parallel_jsobj_exp_one_elem(VertxTestContext context) {
        JsObjExp.parallel("a",
                          Cons.success(JsStr.of("a"))
                         )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a")
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_sequential_jsobj_exp_two_elems(VertxTestContext context) {
        JsObjExp.sequential("a",
                            Cons.success(JsStr.of("a")),
                            "b",
                            Cons.success(JsStr.of("b"))
                           )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b")
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_parallel_jsobj_exp_two_elems(VertxTestContext context) {
        JsObjExp.parallel("a",
                          Cons.success(JsStr.of("a")),
                          "b",
                          Cons.success(JsStr.of("b"))
                         )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b")
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_parallel_jsobj_exp_three_elems(VertxTestContext context) {
        JsObjExp.parallel("a",
                          Cons.success(JsStr.of("a")),
                          "b",
                          Cons.success(JsStr.of("b")),
                          "c",
                          Cons.success(JsBool.TRUE)
                         )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b"),
                                                     "c",
                                                     JsBool.TRUE
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_sequential_jsobj_exp_three_elems(VertxTestContext context) {
        JsObjExp.sequential("a",
                            Cons.success(JsStr.of("a")),
                            "b",
                            Cons.success(JsStr.of("b")),
                            "c",
                            Cons.success(JsBool.TRUE)
                           )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b"),
                                                     "c",
                                                     JsBool.TRUE
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_parallel_jsobj_exp_four_elems(VertxTestContext context) {
        JsObjExp.parallel("a",
                          Cons.success(JsStr.of("a")),
                          "b",
                          Cons.success(JsStr.of("b")),
                          "c",
                          Cons.success(JsBool.TRUE),
                          "d",
                          Cons.success(JsNull.NULL)
                         )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b"),
                                                     "c",
                                                     JsBool.TRUE,
                                                     "d",
                                                     JsNull.NULL
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_sequential_jsobj_exp_four_elems(VertxTestContext context) {
        JsObjExp.sequential("a",
                            Cons.success(JsStr.of("a")),
                            "b",
                            Cons.success(JsStr.of("b")),
                            "c",
                            Cons.success(JsBool.TRUE),
                            "d",
                            Cons.success(JsNull.NULL)
                           )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b"),
                                                     "c",
                                                     JsBool.TRUE,
                                                     "d",
                                                     JsNull.NULL
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_sequential_jsobj_exp_five_elems(VertxTestContext context) {
        JsObjExp.sequential("a",
                            Cons.success(JsStr.of("a")),
                            "b",
                            Cons.success(JsStr.of("b")),
                            "c",
                            Cons.success(JsBool.TRUE),
                            "d",
                            Cons.success(JsNull.NULL),
                            "e",
                            Cons.success(JsInt.of(1))
                           )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b"),
                                                     "c",
                                                     JsBool.TRUE,
                                                     "d",
                                                     JsNull.NULL,
                                                     "e",
                                                     JsInt.of(1)
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_parallel_jsobj_exp_five_elems(VertxTestContext context) {
        JsObjExp.parallel("a",
                          Cons.success(JsStr.of("a")),
                          "b",
                          Cons.success(JsStr.of("b")),
                          "c",
                          Cons.success(JsBool.TRUE),
                          "d",
                          Cons.success(JsNull.NULL),
                          "e",
                          Cons.success(JsInt.of(1))
                         )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b"),
                                                     "c",
                                                     JsBool.TRUE,
                                                     "d",
                                                     JsNull.NULL,
                                                     "e",
                                                     JsInt.of(1)
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_sequential_jsobj_exp_six_elems(VertxTestContext context) {
        JsObjExp.sequential("a",
                            Cons.success(JsStr.of("a")),
                            "b",
                            Cons.success(JsStr.of("b")),
                            "c",
                            Cons.success(JsBool.TRUE),
                            "d",
                            Cons.success(JsNull.NULL),
                            "e",
                            Cons.success(JsInt.of(1)),
                            "f",
                            Cons.success(JsLong.of(1L))
                           )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b"),
                                                     "c",
                                                     JsBool.TRUE,
                                                     "d",
                                                     JsNull.NULL,
                                                     "e",
                                                     JsInt.of(1),
                                                     "f",
                                                     JsLong.of(1L)
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_parallel_jsobj_exp_six_elems(VertxTestContext context) {
        JsObjExp.parallel("a",
                          Cons.success(JsStr.of("a")),
                          "b",
                          Cons.success(JsStr.of("b")),
                          "c",
                          Cons.success(JsBool.TRUE),
                          "d",
                          Cons.success(JsNull.NULL),
                          "e",
                          Cons.success(JsInt.of(1)),
                          "f",
                          Cons.success(JsLong.of(1L))
                         )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b"),
                                                     "c",
                                                     JsBool.TRUE,
                                                     "d",
                                                     JsNull.NULL,
                                                     "e",
                                                     JsInt.of(1),
                                                     "f",
                                                     JsLong.of(1L)
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_sequential_jsobj_exp_seven_elems(VertxTestContext context) {
        JsObjExp.sequential("a",
                            Cons.success(JsStr.of("a")),
                            "b",
                            Cons.success(JsStr.of("b")),
                            "c",
                            Cons.success(JsBool.TRUE),
                            "d",
                            Cons.success(JsNull.NULL),
                            "e",
                            Cons.success(JsInt.of(1)),
                            "f",
                            Cons.success(JsLong.of(1L)),
                            "g",
                            Cons.success(JsArray.of(1,
                                                    2,
                                                    3
                                                   ))
                           )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b"),
                                                     "c",
                                                     JsBool.TRUE,
                                                     "d",
                                                     JsNull.NULL,
                                                     "e",
                                                     JsInt.of(1),
                                                     "f",
                                                     JsLong.of(1L),
                                                     "g",
                                                     JsArray.of(1,
                                                                2,
                                                                3
                                                               )
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_parallel_jsobj_exp_seven_elems(VertxTestContext context) {
        JsObjExp.parallel("a",
                          Cons.success(JsStr.of("a")),
                          "b",
                          Cons.success(JsStr.of("b")),
                          "c",
                          Cons.success(JsBool.TRUE),
                          "d",
                          Cons.success(JsNull.NULL),
                          "e",
                          Cons.success(JsInt.of(1)),
                          "f",
                          Cons.success(JsLong.of(1L)),
                          "g",
                          Cons.success(JsArray.of(1,
                                                  2,
                                                  3
                                                 ))
                         )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b"),
                                                     "c",
                                                     JsBool.TRUE,
                                                     "d",
                                                     JsNull.NULL,
                                                     "e",
                                                     JsInt.of(1),
                                                     "f",
                                                     JsLong.of(1L),
                                                     "g",
                                                     JsArray.of(1,
                                                                2,
                                                                3
                                                               )
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_parallel_jsobj_exp_eight_elems(VertxTestContext context) {
        JsObjExp.parallel("a",
                          Cons.success(JsStr.of("a")),
                          "b",
                          Cons.success(JsStr.of("b")),
                          "c",
                          Cons.success(JsBool.TRUE),
                          "d",
                          Cons.success(JsNull.NULL),
                          "e",
                          Cons.success(JsInt.of(1)),
                          "f",
                          Cons.success(JsLong.of(1L)),
                          "g",
                          Cons.success(JsArray.of(1,
                                                  2,
                                                  3
                                                 )),
                          "h",
                          Cons.success(JsObj.empty())
                         )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b"),
                                                     "c",
                                                     JsBool.TRUE,
                                                     "d",
                                                     JsNull.NULL,
                                                     "e",
                                                     JsInt.of(1),
                                                     "f",
                                                     JsLong.of(1L),
                                                     "g",
                                                     JsArray.of(1,
                                                                2,
                                                                3
                                                               ),
                                                     "h",
                                                     JsObj.empty()
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_sequential_jsobj_exp_eight_elems(VertxTestContext context) {
        JsObjExp.sequential("a",
                            Cons.success(JsStr.of("a")),
                            "b",
                            Cons.success(JsStr.of("b")),
                            "c",
                            Cons.success(JsBool.TRUE),
                            "d",
                            Cons.success(JsNull.NULL),
                            "e",
                            Cons.success(JsInt.of(1)),
                            "f",
                            Cons.success(JsLong.of(1L)),
                            "g",
                            Cons.success(JsArray.of(1,
                                                    2,
                                                    3
                                                   )),
                            "h",
                            Cons.success(JsObj.empty())
                           )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b"),
                                                     "c",
                                                     JsBool.TRUE,
                                                     "d",
                                                     JsNull.NULL,
                                                     "e",
                                                     JsInt.of(1),
                                                     "f",
                                                     JsLong.of(1L),
                                                     "g",
                                                     JsArray.of(1,
                                                                2,
                                                                3
                                                               ),
                                                     "h",
                                                     JsObj.empty()
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_parallel_jsobj_exp_nine_elems(VertxTestContext context) {
        Instant now = Instant.now();
        JsObjExp.parallel("a",
                          Cons.success(JsStr.of("a")),
                          "b",
                          Cons.success(JsStr.of("b")),
                          "c",
                          Cons.success(JsBool.TRUE),
                          "d",
                          Cons.success(JsNull.NULL),
                          "e",
                          Cons.success(JsInt.of(1)),
                          "f",
                          Cons.success(JsLong.of(1L)),
                          "g",
                          Cons.success(JsArray.of(1,
                                                  2,
                                                  3
                                                 )),
                          "h",
                          Cons.success(JsObj.empty()),
                          "i",
                          Cons.success(JsInstant.of(now))
                         )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b"),
                                                     "c",
                                                     JsBool.TRUE,
                                                     "d",
                                                     JsNull.NULL,
                                                     "e",
                                                     JsInt.of(1),
                                                     "f",
                                                     JsLong.of(1L),
                                                     "g",
                                                     JsArray.of(1,
                                                                2,
                                                                3
                                                               ),
                                                     "h",
                                                     JsObj.empty(),
                                                     "i",
                                                     JsInstant.of(now)
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_sequential_jsobj_exp_nine_elems(VertxTestContext context) {
        Instant now = Instant.now();
        JsObjExp.sequential("a",
                            Cons.success(JsStr.of("a")),
                            "b",
                            Cons.success(JsStr.of("b")),
                            "c",
                            Cons.success(JsBool.TRUE),
                            "d",
                            Cons.success(JsNull.NULL),
                            "e",
                            Cons.success(JsInt.of(1)),
                            "f",
                            Cons.success(JsLong.of(1L)),
                            "g",
                            Cons.success(JsArray.of(1,
                                                    2,
                                                    3
                                                   )),
                            "h",
                            Cons.success(JsObj.empty()),
                            "i",
                            Cons.success(JsInstant.of(now))
                           )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b"),
                                                     "c",
                                                     JsBool.TRUE,
                                                     "d",
                                                     JsNull.NULL,
                                                     "e",
                                                     JsInt.of(1),
                                                     "f",
                                                     JsLong.of(1L),
                                                     "g",
                                                     JsArray.of(1,
                                                                2,
                                                                3
                                                               ),
                                                     "h",
                                                     JsObj.empty(),
                                                     "i",
                                                     JsInstant.of(now)
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_sequential_jsobj_exp_ten_elems(VertxTestContext context) {
        Instant now = Instant.now();
        JsObjExp.sequential("a",
                            Cons.success(JsStr.of("a")),
                            "b",
                            Cons.success(JsStr.of("b")),
                            "c",
                            Cons.success(JsBool.TRUE),
                            "d",
                            Cons.success(JsNull.NULL),
                            "e",
                            Cons.success(JsInt.of(1)),
                            "f",
                            Cons.success(JsLong.of(1L)),
                            "g",
                            Cons.success(JsArray.of(1,
                                                    2,
                                                    3
                                                   )),
                            "h",
                            Cons.success(JsObj.empty()),
                            "i",
                            Cons.success(JsInstant.of(now)),
                            "j",
                            Cons.success(JsBinary.of("hi"))
                           )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b"),
                                                     "c",
                                                     JsBool.TRUE,
                                                     "d",
                                                     JsNull.NULL,
                                                     "e",
                                                     JsInt.of(1),
                                                     "f",
                                                     JsLong.of(1L),
                                                     "g",
                                                     JsArray.of(1,
                                                                2,
                                                                3
                                                               ),
                                                     "h",
                                                     JsObj.empty(),
                                                     "i",
                                                     JsInstant.of(now),
                                                     "j",
                                                     JsBinary.of("hi")
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_parallel_jsobj_exp_ten_elems(VertxTestContext context) {
        Instant now = Instant.now();
        JsObjExp.parallel("a",
                          Cons.success(JsStr.of("a")),
                          "b",
                          Cons.success(JsStr.of("b")),
                          "c",
                          Cons.success(JsBool.TRUE),
                          "d",
                          Cons.success(JsNull.NULL),
                          "e",
                          Cons.success(JsInt.of(1)),
                          "f",
                          Cons.success(JsLong.of(1L)),
                          "g",
                          Cons.success(JsArray.of(1,
                                                  2,
                                                  3
                                                 )),
                          "h",
                          Cons.success(JsObj.empty()),
                          "i",
                          Cons.success(JsInstant.of(now)),
                          "j",
                          Cons.success(JsBinary.of("hi"))
                         )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b"),
                                                     "c",
                                                     JsBool.TRUE,
                                                     "d",
                                                     JsNull.NULL,
                                                     "e",
                                                     JsInt.of(1),
                                                     "f",
                                                     JsLong.of(1L),
                                                     "g",
                                                     JsArray.of(1,
                                                                2,
                                                                3
                                                               ),
                                                     "h",
                                                     JsObj.empty(),
                                                     "i",
                                                     JsInstant.of(now),
                                                     "j",
                                                     JsBinary.of("hi")
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_parallel_jsobj_exp_eleven_elems(VertxTestContext context) {
        Instant now = Instant.now();
        JsObjExp.parallel("a",
                          Cons.success(JsStr.of("a")),
                          "b",
                          Cons.success(JsStr.of("b")),
                          "c",
                          Cons.success(JsBool.TRUE),
                          "d",
                          Cons.success(JsNull.NULL),
                          "e",
                          Cons.success(JsInt.of(1)),
                          "f",
                          Cons.success(JsLong.of(1L)),
                          "g",
                          Cons.success(JsArray.of(1,
                                                  2,
                                                  3
                                                 )),
                          "h",
                          Cons.success(JsObj.empty()),
                          "i",
                          Cons.success(JsInstant.of(now)),
                          "j",
                          Cons.success(JsBinary.of("hi")),
                          "k",
                          Cons.success(JsDouble.of(2.5d))
                         )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b"),
                                                     "c",
                                                     JsBool.TRUE,
                                                     "d",
                                                     JsNull.NULL,
                                                     "e",
                                                     JsInt.of(1),
                                                     "f",
                                                     JsLong.of(1L),
                                                     "g",
                                                     JsArray.of(1,
                                                                2,
                                                                3
                                                               ),
                                                     "h",
                                                     JsObj.empty(),
                                                     "i",
                                                     JsInstant.of(now),
                                                     "j",
                                                     JsBinary.of("hi"),
                                                     "k",
                                                     JsDouble.of(2.5d)
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_sequential_jsobj_exp_eleven_elems(VertxTestContext context) {
        Instant now = Instant.now();
        JsObjExp.sequential("a",
                            Cons.success(JsStr.of("a")),
                            "b",
                            Cons.success(JsStr.of("b")),
                            "c",
                            Cons.success(JsBool.TRUE),
                            "d",
                            Cons.success(JsNull.NULL),
                            "e",
                            Cons.success(JsInt.of(1)),
                            "f",
                            Cons.success(JsLong.of(1L)),
                            "g",
                            Cons.success(JsArray.of(1,
                                                    2,
                                                    3
                                                   )),
                            "h",
                            Cons.success(JsObj.empty()),
                            "i",
                            Cons.success(JsInstant.of(now)),
                            "j",
                            Cons.success(JsBinary.of("hi")),
                            "k",
                            Cons.success(JsDouble.of(2.5d))
                           )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b"),
                                                     "c",
                                                     JsBool.TRUE,
                                                     "d",
                                                     JsNull.NULL,
                                                     "e",
                                                     JsInt.of(1),
                                                     "f",
                                                     JsLong.of(1L),
                                                     "g",
                                                     JsArray.of(1,
                                                                2,
                                                                3
                                                               ),
                                                     "h",
                                                     JsObj.empty(),
                                                     "i",
                                                     JsInstant.of(now),
                                                     "j",
                                                     JsBinary.of("hi"),
                                                     "k",
                                                     JsDouble.of(2.5d)
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_sequential_jsobj_exp_twelve_elems(VertxTestContext context) {
        Instant now = Instant.now();
        JsObjExp.sequential("a",
                            Cons.success(JsStr.of("a")),
                            "b",
                            Cons.success(JsStr.of("b")),
                            "c",
                            Cons.success(JsBool.TRUE),
                            "d",
                            Cons.success(JsNull.NULL),
                            "e",
                            Cons.success(JsInt.of(1)),
                            "f",
                            Cons.success(JsLong.of(1L)),
                            "g",
                            Cons.success(JsArray.of(1,
                                                    2,
                                                    3
                                                   )),
                            "h",
                            Cons.success(JsObj.empty()),
                            "i",
                            Cons.success(JsInstant.of(now)),
                            "j",
                            Cons.success(JsBinary.of("hi")),
                            "k",
                            Cons.success(JsDouble.of(2.5d)),
                            "l",
                            Cons.success(JsArray.of(JsObj.empty()))
                           )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b"),
                                                     "c",
                                                     JsBool.TRUE,
                                                     "d",
                                                     JsNull.NULL,
                                                     "e",
                                                     JsInt.of(1),
                                                     "f",
                                                     JsLong.of(1L),
                                                     "g",
                                                     JsArray.of(1,
                                                                2,
                                                                3
                                                               ),
                                                     "h",
                                                     JsObj.empty(),
                                                     "i",
                                                     JsInstant.of(now),
                                                     "j",
                                                     JsBinary.of("hi"),
                                                     "k",
                                                     JsDouble.of(2.5d),
                                                     "l",
                                                     JsArray.of(JsObj.empty())
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_parallel_jsobj_exp_twelve_elems(VertxTestContext context) {
        Instant now = Instant.now();
        JsObjExp.parallel("a",
                          Cons.success(JsStr.of("a")),
                          "b",
                          Cons.success(JsStr.of("b")),
                          "c",
                          Cons.success(JsBool.TRUE),
                          "d",
                          Cons.success(JsNull.NULL),
                          "e",
                          Cons.success(JsInt.of(1)),
                          "f",
                          Cons.success(JsLong.of(1L)),
                          "g",
                          Cons.success(JsArray.of(1,
                                                  2,
                                                  3
                                                 )),
                          "h",
                          Cons.success(JsObj.empty()),
                          "i",
                          Cons.success(JsInstant.of(now)),
                          "j",
                          Cons.success(JsBinary.of("hi")),
                          "k",
                          Cons.success(JsDouble.of(2.5d)),
                          "l",
                          Cons.success(JsArray.of(JsObj.empty()))
                         )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b"),
                                                     "c",
                                                     JsBool.TRUE,
                                                     "d",
                                                     JsNull.NULL,
                                                     "e",
                                                     JsInt.of(1),
                                                     "f",
                                                     JsLong.of(1L),
                                                     "g",
                                                     JsArray.of(1,
                                                                2,
                                                                3
                                                               ),
                                                     "h",
                                                     JsObj.empty(),
                                                     "i",
                                                     JsInstant.of(now),
                                                     "j",
                                                     JsBinary.of("hi"),
                                                     "k",
                                                     JsDouble.of(2.5d),
                                                     "l",
                                                     JsArray.of(JsObj.empty())
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_parallel_jsobj_exp_thirteen_elems(VertxTestContext context) {
        Instant now = Instant.now();
        JsObjExp.parallel("a",
                          Cons.success(JsStr.of("a")),
                          "b",
                          Cons.success(JsStr.of("b")),
                          "c",
                          Cons.success(JsBool.TRUE),
                          "d",
                          Cons.success(JsNull.NULL),
                          "e",
                          Cons.success(JsInt.of(1)),
                          "f",
                          Cons.success(JsLong.of(1L)),
                          "g",
                          Cons.success(JsArray.of(1,
                                                  2,
                                                  3
                                                 )),
                          "h",
                          Cons.success(JsObj.empty()),
                          "i",
                          Cons.success(JsInstant.of(now)),
                          "j",
                          Cons.success(JsBinary.of("hi")),
                          "k",
                          Cons.success(JsDouble.of(2.5d)),
                          "l",
                          Cons.success(JsArray.of(JsObj.empty())),
                          "m",
                          Cons.success(JsObj.of("a",
                                                JsBool.TRUE
                                               ))
                         )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b"),
                                                     "c",
                                                     JsBool.TRUE,
                                                     "d",
                                                     JsNull.NULL,
                                                     "e",
                                                     JsInt.of(1),
                                                     "f",
                                                     JsLong.of(1L),
                                                     "g",
                                                     JsArray.of(1,
                                                                2,
                                                                3
                                                               ),
                                                     "h",
                                                     JsObj.empty(),
                                                     "i",
                                                     JsInstant.of(now),
                                                     "j",
                                                     JsBinary.of("hi"),
                                                     "k",
                                                     JsDouble.of(2.5d),
                                                     "l",
                                                     JsArray.of(JsObj.empty()),
                                                     "m",
                                                     JsObj.of("a",
                                                              JsBool.TRUE
                                                             )
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_sequential_jsobj_exp_thirteen_elems(VertxTestContext context) {
        Instant now = Instant.now();
        JsObjExp.sequential("a",
                            Cons.success(JsStr.of("a")),
                            "b",
                            Cons.success(JsStr.of("b")),
                            "c",
                            Cons.success(JsBool.TRUE),
                            "d",
                            Cons.success(JsNull.NULL),
                            "e",
                            Cons.success(JsInt.of(1)),
                            "f",
                            Cons.success(JsLong.of(1L)),
                            "g",
                            Cons.success(JsArray.of(1,
                                                    2,
                                                    3
                                                   )),
                            "h",
                            Cons.success(JsObj.empty()),
                            "i",
                            Cons.success(JsInstant.of(now)),
                            "j",
                            Cons.success(JsBinary.of("hi")),
                            "k",
                            Cons.success(JsDouble.of(2.5d)),
                            "l",
                            Cons.success(JsArray.of(JsObj.empty())),
                            "m",
                            Cons.success(JsObj.of("a",
                                                  JsBool.TRUE
                                                 ))
                           )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b"),
                                                     "c",
                                                     JsBool.TRUE,
                                                     "d",
                                                     JsNull.NULL,
                                                     "e",
                                                     JsInt.of(1),
                                                     "f",
                                                     JsLong.of(1L),
                                                     "g",
                                                     JsArray.of(1,
                                                                2,
                                                                3
                                                               ),
                                                     "h",
                                                     JsObj.empty(),
                                                     "i",
                                                     JsInstant.of(now),
                                                     "j",
                                                     JsBinary.of("hi"),
                                                     "k",
                                                     JsDouble.of(2.5d),
                                                     "l",
                                                     JsArray.of(JsObj.empty()),
                                                     "m",
                                                     JsObj.of("a",
                                                              JsBool.TRUE
                                                             )
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_sequential_jsobj_exp_fourteen_elems(VertxTestContext context) {
        Instant now = Instant.now();
        JsObjExp.sequential("a",
                            Cons.success(JsStr.of("a")),
                            "b",
                            Cons.success(JsStr.of("b")),
                            "c",
                            Cons.success(JsBool.TRUE),
                            "d",
                            Cons.success(JsNull.NULL),
                            "e",
                            Cons.success(JsInt.of(1)),
                            "f",
                            Cons.success(JsLong.of(1L)),
                            "g",
                            Cons.success(JsArray.of(1,
                                                    2,
                                                    3
                                                   )),
                            "h",
                            Cons.success(JsObj.empty()),
                            "i",
                            Cons.success(JsInstant.of(now)),
                            "j",
                            Cons.success(JsBinary.of("hi")),
                            "k",
                            Cons.success(JsDouble.of(2.5d)),
                            "l",
                            Cons.success(JsArray.of(JsObj.empty())),
                            "m",
                            Cons.success(JsObj.of("a",
                                                  JsBool.TRUE
                                                 )),
                            "m",
                            Cons.success(JsStr.of("a"))

                           )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b"),
                                                     "c",
                                                     JsBool.TRUE,
                                                     "d",
                                                     JsNull.NULL,
                                                     "e",
                                                     JsInt.of(1),
                                                     "f",
                                                     JsLong.of(1L),
                                                     "g",
                                                     JsArray.of(1,
                                                                2,
                                                                3
                                                               ),
                                                     "h",
                                                     JsObj.empty(),
                                                     "i",
                                                     JsInstant.of(now),
                                                     "j",
                                                     JsBinary.of("hi"),
                                                     "k",
                                                     JsDouble.of(2.5d),
                                                     "l",
                                                     JsArray.of(JsObj.empty()),
                                                     "m",
                                                     JsObj.of("a",
                                                              JsBool.TRUE
                                                             ),
                                                     "m",
                                                     JsStr.of("a")
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_parallel_jsobj_exp_fourteen_elems(VertxTestContext context) {
        Instant now = Instant.now();
        JsObjExp.parallel("a",
                          Cons.success(JsStr.of("a")),
                          "b",
                          Cons.success(JsStr.of("b")),
                          "c",
                          Cons.success(JsBool.TRUE),
                          "d",
                          Cons.success(JsNull.NULL),
                          "e",
                          Cons.success(JsInt.of(1)),
                          "f",
                          Cons.success(JsLong.of(1L)),
                          "g",
                          Cons.success(JsArray.of(1,
                                                  2,
                                                  3
                                                 )),
                          "h",
                          Cons.success(JsObj.empty()),
                          "i",
                          Cons.success(JsInstant.of(now)),
                          "j",
                          Cons.success(JsBinary.of("hi")),
                          "k",
                          Cons.success(JsDouble.of(2.5d)),
                          "l",
                          Cons.success(JsArray.of(JsObj.empty())),
                          "m",
                          Cons.success(JsObj.of("a",
                                                JsBool.TRUE
                                               )),
                          "m",
                          Cons.success(JsStr.of("a"))

                         )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b"),
                                                     "c",
                                                     JsBool.TRUE,
                                                     "d",
                                                     JsNull.NULL,
                                                     "e",
                                                     JsInt.of(1),
                                                     "f",
                                                     JsLong.of(1L),
                                                     "g",
                                                     JsArray.of(1,
                                                                2,
                                                                3
                                                               ),
                                                     "h",
                                                     JsObj.empty(),
                                                     "i",
                                                     JsInstant.of(now),
                                                     "j",
                                                     JsBinary.of("hi"),
                                                     "k",
                                                     JsDouble.of(2.5d),
                                                     "l",
                                                     JsArray.of(JsObj.empty()),
                                                     "m",
                                                     JsObj.of("a",
                                                              JsBool.TRUE
                                                             ),
                                                     "m",
                                                     JsStr.of("a")
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_parallel_jsobj_exp_fifteen_elems(VertxTestContext context) {
        Instant now = Instant.now();
        JsObjExp.parallel("a",
                          Cons.success(JsStr.of("a")),
                          "b",
                          Cons.success(JsStr.of("b")),
                          "c",
                          Cons.success(JsBool.TRUE),
                          "d",
                          Cons.success(JsNull.NULL),
                          "e",
                          Cons.success(JsInt.of(1)),
                          "f",
                          Cons.success(JsLong.of(1L)),
                          "g",
                          Cons.success(JsArray.of(1,
                                                  2,
                                                  3
                                                 )),
                          "h",
                          Cons.success(JsObj.empty()),
                          "i",
                          Cons.success(JsInstant.of(now)),
                          "j",
                          Cons.success(JsBinary.of("hi")),
                          "k",
                          Cons.success(JsDouble.of(2.5d)),
                          "l",
                          Cons.success(JsArray.of(JsObj.empty())),
                          "m",
                          Cons.success(JsObj.of("a",
                                                JsBool.TRUE
                                               )),
                          "m",
                          Cons.success(JsStr.of("a")),
                          "n",
                          Cons.success(JsArray.empty())

                         )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b"),
                                                     "c",
                                                     JsBool.TRUE,
                                                     "d",
                                                     JsNull.NULL,
                                                     "e",
                                                     JsInt.of(1),
                                                     "f",
                                                     JsLong.of(1L),
                                                     "g",
                                                     JsArray.of(1,
                                                                2,
                                                                3
                                                               ),
                                                     "h",
                                                     JsObj.empty(),
                                                     "i",
                                                     JsInstant.of(now),
                                                     "j",
                                                     JsBinary.of("hi"),
                                                     "k",
                                                     JsDouble.of(2.5d),
                                                     "l",
                                                     JsArray.of(JsObj.empty()),
                                                     "m",
                                                     JsObj.of("a",
                                                              JsBool.TRUE
                                                             ),
                                                     "m",
                                                     JsStr.of("a"),
                                                     "n",
                                                     JsArray.empty()
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_sequential_jsobj_exp_fifteen_elems(VertxTestContext context) {
        Instant now = Instant.now();
        JsObjExp.sequential("a",
                            Cons.success(JsStr.of("a")),
                            "b",
                            Cons.success(JsStr.of("b")),
                            "c",
                            Cons.success(JsBool.TRUE),
                            "d",
                            Cons.success(JsNull.NULL),
                            "e",
                            Cons.success(JsInt.of(1)),
                            "f",
                            Cons.success(JsLong.of(1L)),
                            "g",
                            Cons.success(JsArray.of(1,
                                                    2,
                                                    3
                                                   )),
                            "h",
                            Cons.success(JsObj.empty()),
                            "i",
                            Cons.success(JsInstant.of(now)),
                            "j",
                            Cons.success(JsBinary.of("hi")),
                            "k",
                            Cons.success(JsDouble.of(2.5d)),
                            "l",
                            Cons.success(JsArray.of(JsObj.empty())),
                            "m",
                            Cons.success(JsObj.of("a",
                                                  JsBool.TRUE
                                                 )),
                            "m",
                            Cons.success(JsStr.of("a")),
                            "n",
                            Cons.success(JsArray.empty())

                           )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b"),
                                                     "c",
                                                     JsBool.TRUE,
                                                     "d",
                                                     JsNull.NULL,
                                                     "e",
                                                     JsInt.of(1),
                                                     "f",
                                                     JsLong.of(1L),
                                                     "g",
                                                     JsArray.of(1,
                                                                2,
                                                                3
                                                               ),
                                                     "h",
                                                     JsObj.empty(),
                                                     "i",
                                                     JsInstant.of(now),
                                                     "j",
                                                     JsBinary.of("hi"),
                                                     "k",
                                                     JsDouble.of(2.5d),
                                                     "l",
                                                     JsArray.of(JsObj.empty()),
                                                     "m",
                                                     JsObj.of("a",
                                                              JsBool.TRUE
                                                             ),
                                                     "m",
                                                     JsStr.of("a"),
                                                     "n",
                                                     JsArray.empty()
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();
    }

    @Test
    public void test_parallel_retries(VertxTestContext context) {

        JsStr a = JsStr.of("a");

        final Supplier<Val<JsStr>> val =
                new ValOrErrorMock<>(counter -> counter == 1 || counter == 2,
                                 counter -> new RuntimeException("counter: " + counter),
                                     a
                );


        JsObjExp.parallel("a",
                          val.get(),
                          "b",
                          val.get(),
                          "c",
                          val.get(),
                          "d",
                          JsArrayExp.parallel(val.get(),
                                              val.get(),
                                              JsObjExp.parallel("a",
                                                                val.get()
                                                               )
                                             )
                         )
                .retry(2)
                .onComplete(it -> {
                    context.verify(() -> {
                                       Assertions.assertEquals(it.result(),
                                                               JsObj.of("a",
                                                                        a,
                                                                        "b",
                                                                        a,
                                                                        "c",
                                                                        a,
                                                                        "d",
                                                                        JsArray.of(a,
                                                                                   a,
                                                                                   JsObj.of("a",
                                                                                            a
                                                                                           )
                                                                                  )
                                                                       )
                                                              );
                                       context.completeNow();
                                   }
                                  );
                })
                .get();


    }

    @Test
    public void test_sequential_retries(VertxTestContext context) {

        JsStr a = JsStr.of("a");

        final Supplier<Val<JsStr>> val =
                new ValOrErrorMock<>(counter -> counter == 1 || counter == 2,
                                 counter -> new RuntimeException("counter: " + counter),
                                     a
                );


        JsObjExp.sequential("a",
                            val.get(),
                            "b",
                            val.get(),
                            "c",
                            val.get(),
                            "d",
                            JsArrayExp.parallel(val.get(),
                                                val.get(),
                                                JsObjExp.parallel("a",
                                                                  val.get()
                                                                 )
                                               )
                           )
                .retry(2)
                .onComplete(it -> {
                    context.verify(() -> {
                                       Assertions.assertEquals(it.result(),
                                                               JsObj.of("a",
                                                                        a,
                                                                        "b",
                                                                        a,
                                                                        "c",
                                                                        a,
                                                                        "d",
                                                                        JsArray.of(a,
                                                                                   a,
                                                                                   JsObj.of("a",
                                                                                            a
                                                                                           )
                                                                                  )
                                                                       )
                                                              );
                                       context.completeNow();
                                   }
                                  );
                })
                .get();


    }

    @Test
    public void test_parallel_jsobj_exp_map(VertxTestContext context) {

        JsObjExp.parallel("a",
                          Cons.success(JsStr.of("a")),
                          "b",
                          Cons.success(JsStr.of("b"))
                         )
                .map(obj -> obj.mapValues(p -> JsStr.prism.modify.apply(String::toUpperCase)
                                                                 .apply(p.value)
                                         )
                    )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("A"),
                                                     "b",
                                                     JsStr.of("B")
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();

    }

    @Test
    public void test_sequential_jsobj_exp_map(VertxTestContext context) {

        JsObjExp.sequential("a",
                            Cons.success(JsStr.of("a")),
                            "b",
                            Cons.success(JsStr.of("b"))
                           )
                .map(obj -> obj.mapValues(p -> JsStr.prism.modify.apply(String::toUpperCase)
                                                                 .apply(p.value)
                                         )
                    )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("A"),
                                                     "b",
                                                     JsStr.of("B")
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();

    }

    @Test
    public void test_parallel_jsobj_exp_flatmap_success(VertxTestContext context) {

        JsObjExp.parallel("a",
                          Cons.success(JsStr.of("a")),
                          "b",
                          Cons.success(JsStr.of("b"))
                         )
                .flatMap(obj -> Cons.success(obj.mapValues(p -> JsStr.prism.modify.apply(String::toUpperCase)
                                                                                  .apply(p.value)
                                                          ))
                        )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("A"),
                                                     "b",
                                                     JsStr.of("B")
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();


    }

    @Test
    public void test_sequential_jsobj_exp_flatmap_success(VertxTestContext context) {

        JsObjExp.sequential("a",
                            Cons.success(JsStr.of("a")),
                            "b",
                            Cons.success(JsStr.of("b"))
                           )
                .flatMap(obj -> Cons.success(obj.mapValues(p -> JsStr.prism.modify.apply(String::toUpperCase)
                                                                                  .apply(p.value)
                                                          ))
                        )
                .onSuccess(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("A"),
                                                     "b",
                                                     JsStr.of("B")
                                                    ),
                                            r
                                           );
                    context.completeNow();
                }))
                .get();


    }

    @Test
    public void test_parallel_jsobj_exp_flatmap_failure(VertxTestContext context) {


        JsObjExp.parallel("a",
                          Cons.success(JsStr.of("a")),
                          "b",
                          Cons.success(JsStr.of("b"))
                         )
                .flatMap(s -> Cons.failure(new RuntimeException()))
                .onComplete(r -> context.verify(() -> {
                    Assertions.assertTrue(r.failed());
                    context.completeNow();
                }))
                .get();


    }

    @Test
    public void test_sequential_jsobj_exp_flatmap_failure(VertxTestContext context) {


        JsObjExp.sequential("a",
                            Cons.success(JsStr.of("a")),
                            "b",
                            Cons.success(JsStr.of("b"))
                           )
                .flatMap(s -> Cons.failure(new RuntimeException()))
                .onComplete(r -> context.verify(() -> {
                    Assertions.assertTrue(r.failed());
                    context.completeNow();
                }))
                .get();


    }

    @Test
    public void test_parallel_jsobj_exp_retry_with_delay(VertxTestContext context) {
        int ATTEMPTS = 3;

        long start = System.nanoTime();
        ValOrErrorMock<String> a = new ValOrErrorMock<>(ATTEMPTS,
                                                counter -> new RuntimeException("counter: " + counter),
                                                        "a"
        );
        ValOrErrorMock<String> b = new ValOrErrorMock<>(ATTEMPTS,
                                                counter -> new RuntimeException("counter: " + counter),
                                                        "b"
        );

        JsObjExp.parallel("a",
                          a.get()
                           .map(JsStr::of),
                          "b",
                          b.get()
                           .map(JsStr::of)
                         )
                .retry(ATTEMPTS,
                       (error, n) -> vertxRef.delay(100,
                                                    MILLISECONDS
                                                   )
                      )
                .get()
                .onComplete(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b")
                                                    ),
                                            r.result()
                                           );
                    Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= ATTEMPTS);
                    context.completeNow();

                }));

    }

    @Test
    public void test_sequential_jsobj_exp_retry_with_delay(VertxTestContext context) {
        int ATTEMPTS = 3;

        long start = System.nanoTime();
        ValOrErrorMock<String> a = new ValOrErrorMock<>(ATTEMPTS,
                                                counter -> new RuntimeException("counter: " + counter),
                                                        "a"
        );
        ValOrErrorMock<String> b = new ValOrErrorMock<>(ATTEMPTS,
                                                counter -> new RuntimeException("counter: " + counter),
                                                        "b"
        );

        JsObjExp.sequential("a",
                            a.get()
                             .map(JsStr::of),
                            "b",
                            b.get()
                             .map(JsStr::of)
                           )
                .retry(ATTEMPTS,
                       (error, n) -> vertxRef.delay(100,
                                                    MILLISECONDS
                                                   )
                      )
                .get()
                .onComplete(r -> context.verify(() -> {
                    Assertions.assertEquals(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsStr.of("b")
                                                    ),
                                            r.result()
                                           );
                    Assertions.assertTrue(NANOSECONDS.toMillis(System.nanoTime() - start) >= ATTEMPTS);
                    context.completeNow();

                }));

    }
}
