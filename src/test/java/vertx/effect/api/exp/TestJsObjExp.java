package vertx.effect.api.exp;

import fun.gen.Gen;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.*;
import vertx.effect.stub.StubBuilder;
import vertx.values.codecs.RegisterJsValuesCodecs;

import java.time.Duration;
import java.time.Instant;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static vertx.effect.RetryPolicies.limitRetries;

@SuppressWarnings("ReturnValueIgnored")
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
        JsObjExp.seq("a",
                     VIO.succeed(JsStr.of("a"))
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
        JsObjExp.par("a",
                     VIO.succeed(JsStr.of("a"))
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
        JsObjExp.seq("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b"))
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
        JsObjExp.par("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b"))
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
        JsObjExp.par("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b")),
                     "c",
                     VIO.succeed(JsBool.TRUE)
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
        JsObjExp.seq("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b")),
                     "c",
                     VIO.succeed(JsBool.TRUE)
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
        JsObjExp.par("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b")),
                     "c",
                     VIO.succeed(JsBool.TRUE),
                     "d",
                     VIO.succeed(JsNull.NULL)
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
        JsObjExp.seq("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b")),
                     "c",
                     VIO.succeed(JsBool.TRUE),
                     "d",
                     VIO.succeed(JsNull.NULL)
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
        JsObjExp.seq("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b")),
                     "c",
                     VIO.succeed(JsBool.TRUE),
                     "d",
                     VIO.succeed(JsNull.NULL),
                     "e",
                     VIO.succeed(JsInt.of(1))
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
        JsObjExp.par("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b")),
                     "c",
                     VIO.succeed(JsBool.TRUE),
                     "d",
                     VIO.succeed(JsNull.NULL),
                     "e",
                     VIO.succeed(JsInt.of(1))
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
        JsObjExp.seq("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b")),
                     "c",
                     VIO.succeed(JsBool.TRUE),
                     "d",
                     VIO.succeed(JsNull.NULL),
                     "e",
                     VIO.succeed(JsInt.of(1)),
                     "f",
                     VIO.succeed(JsLong.of(1L))
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
        JsObjExp.par("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b")),
                     "c",
                     VIO.succeed(JsBool.TRUE),
                     "d",
                     VIO.succeed(JsNull.NULL),
                     "e",
                     VIO.succeed(JsInt.of(1)),
                     "f",
                     VIO.succeed(JsLong.of(1L))
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
        JsObjExp.seq("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b")),
                     "c",
                     VIO.succeed(JsBool.TRUE),
                     "d",
                     VIO.succeed(JsNull.NULL),
                     "e",
                     VIO.succeed(JsInt.of(1)),
                     "f",
                     VIO.succeed(JsLong.of(1L)),
                     "g",
                     VIO.succeed(JsArray.of(1,
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
        JsObjExp.par("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b")),
                     "c",
                     VIO.succeed(JsBool.TRUE),
                     "d",
                     VIO.succeed(JsNull.NULL),
                     "e",
                     VIO.succeed(JsInt.of(1)),
                     "f",
                     VIO.succeed(JsLong.of(1L)),
                     "g",
                     VIO.succeed(JsArray.of(1,
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
        JsObjExp.par("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b")),
                     "c",
                     VIO.succeed(JsBool.TRUE),
                     "d",
                     VIO.succeed(JsNull.NULL),
                     "e",
                     VIO.succeed(JsInt.of(1)),
                     "f",
                     VIO.succeed(JsLong.of(1L)),
                     "g",
                     VIO.succeed(JsArray.of(1,
                                            2,
                                            3
                                           )),
                     "h",
                     VIO.succeed(JsObj.empty())
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
        JsObjExp.seq("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b")),
                     "c",
                     VIO.succeed(JsBool.TRUE),
                     "d",
                     VIO.succeed(JsNull.NULL),
                     "e",
                     VIO.succeed(JsInt.of(1)),
                     "f",
                     VIO.succeed(JsLong.of(1L)),
                     "g",
                     VIO.succeed(JsArray.of(1,
                                            2,
                                            3
                                           )),
                     "h",
                     VIO.succeed(JsObj.empty())
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
        JsObjExp.par("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b")),
                     "c",
                     VIO.succeed(JsBool.TRUE),
                     "d",
                     VIO.succeed(JsNull.NULL),
                     "e",
                     VIO.succeed(JsInt.of(1)),
                     "f",
                     VIO.succeed(JsLong.of(1L)),
                     "g",
                     VIO.succeed(JsArray.of(1,
                                            2,
                                            3
                                           )),
                     "h",
                     VIO.succeed(JsObj.empty()),
                     "i",
                     VIO.succeed(JsInstant.of(now))
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
        JsObjExp.seq("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b")),
                     "c",
                     VIO.succeed(JsBool.TRUE),
                     "d",
                     VIO.succeed(JsNull.NULL),
                     "e",
                     VIO.succeed(JsInt.of(1)),
                     "f",
                     VIO.succeed(JsLong.of(1L)),
                     "g",
                     VIO.succeed(JsArray.of(1,
                                            2,
                                            3
                                           )),
                     "h",
                     VIO.succeed(JsObj.empty()),
                     "i",
                     VIO.succeed(JsInstant.of(now))
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
        JsObjExp.seq("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b")),
                     "c",
                     VIO.succeed(JsBool.TRUE),
                     "d",
                     VIO.succeed(JsNull.NULL),
                     "e",
                     VIO.succeed(JsInt.of(1)),
                     "f",
                     VIO.succeed(JsLong.of(1L)),
                     "g",
                     VIO.succeed(JsArray.of(1,
                                            2,
                                            3
                                           )),
                     "h",
                     VIO.succeed(JsObj.empty()),
                     "i",
                     VIO.succeed(JsInstant.of(now)),
                     "j",
                     VIO.succeed(JsBinary.of("hi"))
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
        JsObjExp.par("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b")),
                     "c",
                     VIO.succeed(JsBool.TRUE),
                     "d",
                     VIO.succeed(JsNull.NULL),
                     "e",
                     VIO.succeed(JsInt.of(1)),
                     "f",
                     VIO.succeed(JsLong.of(1L)),
                     "g",
                     VIO.succeed(JsArray.of(1,
                                            2,
                                            3
                                           )),
                     "h",
                     VIO.succeed(JsObj.empty()),
                     "i",
                     VIO.succeed(JsInstant.of(now)),
                     "j",
                     VIO.succeed(JsBinary.of("hi"))
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
        JsObjExp.par("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b")),
                     "c",
                     VIO.succeed(JsBool.TRUE),
                     "d",
                     VIO.succeed(JsNull.NULL),
                     "e",
                     VIO.succeed(JsInt.of(1)),
                     "f",
                     VIO.succeed(JsLong.of(1L)),
                     "g",
                     VIO.succeed(JsArray.of(1,
                                            2,
                                            3
                                           )),
                     "h",
                     VIO.succeed(JsObj.empty()),
                     "i",
                     VIO.succeed(JsInstant.of(now)),
                     "j",
                     VIO.succeed(JsBinary.of("hi")),
                     "k",
                     VIO.succeed(JsDouble.of(2.5d))
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
        JsObjExp.seq("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b")),
                     "c",
                     VIO.succeed(JsBool.TRUE),
                     "d",
                     VIO.succeed(JsNull.NULL),
                     "e",
                     VIO.succeed(JsInt.of(1)),
                     "f",
                     VIO.succeed(JsLong.of(1L)),
                     "g",
                     VIO.succeed(JsArray.of(1,
                                            2,
                                            3
                                           )),
                     "h",
                     VIO.succeed(JsObj.empty()),
                     "i",
                     VIO.succeed(JsInstant.of(now)),
                     "j",
                     VIO.succeed(JsBinary.of("hi")),
                     "k",
                     VIO.succeed(JsDouble.of(2.5d))
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
        JsObjExp.seq("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b")),
                     "c",
                     VIO.succeed(JsBool.TRUE),
                     "d",
                     VIO.succeed(JsNull.NULL),
                     "e",
                     VIO.succeed(JsInt.of(1)),
                     "f",
                     VIO.succeed(JsLong.of(1L)),
                     "g",
                     VIO.succeed(JsArray.of(1,
                                            2,
                                            3
                                           )),
                     "h",
                     VIO.succeed(JsObj.empty()),
                     "i",
                     VIO.succeed(JsInstant.of(now)),
                     "j",
                     VIO.succeed(JsBinary.of("hi")),
                     "k",
                     VIO.succeed(JsDouble.of(2.5d)),
                     "l",
                     VIO.succeed(JsArray.of(JsObj.empty()))
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
        JsObjExp.par("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b")),
                     "c",
                     VIO.succeed(JsBool.TRUE),
                     "d",
                     VIO.succeed(JsNull.NULL),
                     "e",
                     VIO.succeed(JsInt.of(1)),
                     "f",
                     VIO.succeed(JsLong.of(1L)),
                     "g",
                     VIO.succeed(JsArray.of(1,
                                            2,
                                            3
                                           )),
                     "h",
                     VIO.succeed(JsObj.empty()),
                     "i",
                     VIO.succeed(JsInstant.of(now)),
                     "j",
                     VIO.succeed(JsBinary.of("hi")),
                     "k",
                     VIO.succeed(JsDouble.of(2.5d)),
                     "l",
                     VIO.succeed(JsArray.of(JsObj.empty()))
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
        JsObjExp.par("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b")),
                     "c",
                     VIO.succeed(JsBool.TRUE),
                     "d",
                     VIO.succeed(JsNull.NULL),
                     "e",
                     VIO.succeed(JsInt.of(1)),
                     "f",
                     VIO.succeed(JsLong.of(1L)),
                     "g",
                     VIO.succeed(JsArray.of(1,
                                            2,
                                            3
                                           )),
                     "h",
                     VIO.succeed(JsObj.empty()),
                     "i",
                     VIO.succeed(JsInstant.of(now)),
                     "j",
                     VIO.succeed(JsBinary.of("hi")),
                     "k",
                     VIO.succeed(JsDouble.of(2.5d)),
                     "l",
                     VIO.succeed(JsArray.of(JsObj.empty())),
                     "m",
                     VIO.succeed(JsObj.of("a",
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
        JsObjExp.seq("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b")),
                     "c",
                     VIO.succeed(JsBool.TRUE),
                     "d",
                     VIO.succeed(JsNull.NULL),
                     "e",
                     VIO.succeed(JsInt.of(1)),
                     "f",
                     VIO.succeed(JsLong.of(1L)),
                     "g",
                     VIO.succeed(JsArray.of(1,
                                            2,
                                            3
                                           )),
                     "h",
                     VIO.succeed(JsObj.empty()),
                     "i",
                     VIO.succeed(JsInstant.of(now)),
                     "j",
                     VIO.succeed(JsBinary.of("hi")),
                     "k",
                     VIO.succeed(JsDouble.of(2.5d)),
                     "l",
                     VIO.succeed(JsArray.of(JsObj.empty())),
                     "m",
                     VIO.succeed(JsObj.of("a",
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
        JsObjExp.seq("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b")),
                     "c",
                     VIO.succeed(JsBool.TRUE),
                     "d",
                     VIO.succeed(JsNull.NULL),
                     "e",
                     VIO.succeed(JsInt.of(1)),
                     "f",
                     VIO.succeed(JsLong.of(1L)),
                     "g",
                     VIO.succeed(JsArray.of(1,
                                            2,
                                            3
                                           )),
                     "h",
                     VIO.succeed(JsObj.empty()),
                     "i",
                     VIO.succeed(JsInstant.of(now)),
                     "j",
                     VIO.succeed(JsBinary.of("hi")),
                     "k",
                     VIO.succeed(JsDouble.of(2.5d)),
                     "l",
                     VIO.succeed(JsArray.of(JsObj.empty())),
                     "m",
                     VIO.succeed(JsObj.of("a",
                                          JsBool.TRUE
                                         )),
                     "m",
                     VIO.succeed(JsStr.of("a"))

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
        JsObjExp.par("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b")),
                     "c",
                     VIO.succeed(JsBool.TRUE),
                     "d",
                     VIO.succeed(JsNull.NULL),
                     "e",
                     VIO.succeed(JsInt.of(1)),
                     "f",
                     VIO.succeed(JsLong.of(1L)),
                     "g",
                     VIO.succeed(JsArray.of(1,
                                            2,
                                            3
                                           )),
                     "h",
                     VIO.succeed(JsObj.empty()),
                     "i",
                     VIO.succeed(JsInstant.of(now)),
                     "j",
                     VIO.succeed(JsBinary.of("hi")),
                     "k",
                     VIO.succeed(JsDouble.of(2.5d)),
                     "l",
                     VIO.succeed(JsArray.of(JsObj.empty())),
                     "m",
                     VIO.succeed(JsObj.of("a",
                                          JsBool.TRUE
                                         )),
                     "m",
                     VIO.succeed(JsStr.of("a"))

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
        JsObjExp.par("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b")),
                     "c",
                     VIO.succeed(JsBool.TRUE),
                     "d",
                     VIO.succeed(JsNull.NULL),
                     "e",
                     VIO.succeed(JsInt.of(1)),
                     "f",
                     VIO.succeed(JsLong.of(1L)),
                     "g",
                     VIO.succeed(JsArray.of(1,
                                            2,
                                            3
                                           )),
                     "h",
                     VIO.succeed(JsObj.empty()),
                     "i",
                     VIO.succeed(JsInstant.of(now)),
                     "j",
                     VIO.succeed(JsBinary.of("hi")),
                     "k",
                     VIO.succeed(JsDouble.of(2.5d)),
                     "l",
                     VIO.succeed(JsArray.of(JsObj.empty())),
                     "m",
                     VIO.succeed(JsObj.of("a",
                                          JsBool.TRUE
                                         )),
                     "m",
                     VIO.succeed(JsStr.of("a")),
                     "n",
                     VIO.succeed(JsArray.empty())

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
        JsObjExp.seq("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b")),
                     "c",
                     VIO.succeed(JsBool.TRUE),
                     "d",
                     VIO.succeed(JsNull.NULL),
                     "e",
                     VIO.succeed(JsInt.of(1)),
                     "f",
                     VIO.succeed(JsLong.of(1L)),
                     "g",
                     VIO.succeed(JsArray.of(1,
                                            2,
                                            3
                                           )),
                     "h",
                     VIO.succeed(JsObj.empty()),
                     "i",
                     VIO.succeed(JsInstant.of(now)),
                     "j",
                     VIO.succeed(JsBinary.of("hi")),
                     "k",
                     VIO.succeed(JsDouble.of(2.5d)),
                     "l",
                     VIO.succeed(JsArray.of(JsObj.empty())),
                     "m",
                     VIO.succeed(JsObj.of("a",
                                          JsBool.TRUE
                                         )),
                     "m",
                     VIO.succeed(JsStr.of("a")),
                     "n",
                     VIO.succeed(JsArray.empty())

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


        StubBuilder<JsStr> val =
                StubBuilder.ofGen(Gen.seq(counter ->
                                                  counter == 1 || counter == 2
                                                          ? VIO.fail(new RuntimeException("counter: " + counter)) :
                                                          VIO.succeed(a)
                                         )
                                 );


        JsObjExp.par("a",
                     val.build(),
                     "b",
                     val.build(),
                     "c",
                     val.build(),
                     "d",
                     JsArrayExp.par(val.build(),
                                    val.build(),
                                    JsObjExp.par("a",
                                                 val.build()
                                                )
                                   )
                    )
                .retryEach(limitRetries(2))
                .onComplete(it -> context.verify(() -> {
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
                                                ))
                .get();


    }

    @Test
    public void test_sequential_retries(VertxTestContext context) {

        JsStr a = JsStr.of("a");

        StubBuilder<JsStr> val =
                StubBuilder.ofGen(Gen.seq(counter ->
                                                  counter == 1 || counter == 2
                                                          ? VIO.fail(new RuntimeException("counter: " + counter)) :
                                                          VIO.succeed(a)
                                         )
                                 );


        JsObjExp.seq("a",
                     val.build(),
                     "b",
                     val.build(),
                     "c",
                     val.build(),
                     "d",
                     JsArrayExp.par(val.build(),
                                    val.build(),
                                    JsObjExp.par("a",
                                                 val.build()
                                                )
                                   )
                    )
                .retryEach(limitRetries(2))
                .onComplete(it -> context.verify(() -> {
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
                                                ))
                .get();


    }

    @Test
    public void test_parallel_jsobj_exp_map(VertxTestContext context) {

        JsObjExp.par("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b"))
                    )
                .map(obj -> obj.mapValues(value -> JsStr.prism.modify.apply(String::toUpperCase)
                                                                     .apply(value)
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

        JsObjExp.seq("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b"))
                    )
                .map(obj -> obj.mapValues(value -> JsStr.prism.modify.apply(String::toUpperCase)
                                                                     .apply(value)
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

        JsObjExp.par("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b"))
                    )
                .then(obj -> VIO.succeed(obj.mapValues(value -> JsStr.prism.modify.apply(String::toUpperCase)
                                                                                  .apply(value)
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

        JsObjExp.seq("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b"))
                    )
                .then(obj -> VIO.succeed(obj.mapValues(value -> JsStr.prism.modify.apply(String::toUpperCase)
                                                                                  .apply(value)
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


        JsObjExp.par("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b"))
                    )
                .then(s -> VIO.fail(new RuntimeException()))
                .onComplete(r -> context.verify(() -> {
                    Assertions.assertTrue(r.failed());
                    context.completeNow();
                }))
                .get();


    }

    @Test
    public void test_sequential_jsobj_exp_flatmap_failure(VertxTestContext context) {


        JsObjExp.seq("a",
                     VIO.succeed(JsStr.of("a")),
                     "b",
                     VIO.succeed(JsStr.of("b"))
                    )
                .then(s -> VIO.fail(new RuntimeException()))
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


        StubBuilder<String> a =
                StubBuilder.ofGen(Gen.seq(counter ->
                                                  counter < ATTEMPTS ? VIO.fail(new RuntimeException("counter: " + counter)) :
                                                          VIO.succeed("a")
                                         )
                                 );

        StubBuilder<String> b =
                StubBuilder.ofGen(Gen.seq(counter ->
                                                  counter < ATTEMPTS ? VIO.fail(new RuntimeException("counter: " + counter)) :
                                                          VIO.succeed("b")
                                         )
                                 );


        JsObjExp.par("a",
                     a.build()
                      .map(JsStr::of),
                     "b",
                     b.build()
                      .map(JsStr::of)
                    )
                .retryEach(limitRetries(ATTEMPTS)
                                   .append(RetryPolicies.constantDelay(vertxRef.delay(Duration.ofMillis(100))))
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
        StubBuilder<String> a =
                StubBuilder.ofGen(Gen.seq(counter ->
                                                  counter < ATTEMPTS ? VIO.fail(new RuntimeException("counter: " + counter)) :
                                                          VIO.succeed("a")
                                         )
                                 );

        StubBuilder<String> b =
                StubBuilder.ofGen(Gen.seq(counter ->
                                                  counter < ATTEMPTS ? VIO.fail(new RuntimeException("counter: " + counter)) :
                                                          VIO.succeed("b")
                                         )
                                 );

        JsObjExp.seq("a",
                     a.build()
                      .map(JsStr::of),
                     "b",
                     b.build()
                      .map(JsStr::of)
                    )
                .retryEach(limitRetries(ATTEMPTS)
                                   .append(RetryPolicies.constantDelay(vertxRef.delay(Duration.ofMillis(100))))
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
