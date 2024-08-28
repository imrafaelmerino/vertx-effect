package vertx.effect.api;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsInt;
import jsonvalues.JsStr;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.*;
import vertx.effect.JsArrayExp;
import vertx.effect.JsObjExp;
import vertx.effect.PairExp;
import vertx.values.codecs.RegisterJsValuesCodecs;

import java.util.function.Function;
@SuppressWarnings("ReturnValueIgnored")
@ExtendWith(VertxExtension.class)
public class TestFutures extends VertxModule {

    public static Function<Integer, VIO<Integer>> multiplyBy10;
    public static Function<Integer, VIO<Integer>> add10;
    public static Function<String, VIO<String>> toUpper;


    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext testContext
                              ) {
        VertxRef vertxRef = new VertxRef(vertx);

        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );

        PairExp.seq(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                    vertxRef.deployVerticle(new TestFutures())
                   )
               .onComplete(event -> {
                   if (event.failed()) testContext.failNow(event.cause());
                   else testContext.completeNow();
               })
               .get();


    }

    @Test
    public void testJsObjFuture(final VertxTestContext context) {

        JsObjExp.par("a",
                     multiplyBy10.apply(10)
                                 .map(JsInt::of),
                     "b",
                     add10.apply(5)
                          .map(JsInt::of),
                     "c",
                     toUpper.apply("abc")
                            .map(JsStr::of),
                     "d",
                     JsArrayExp.seq(multiplyBy10.apply(1)
                                                .map(JsInt::of),
                                    multiplyBy10.apply(5)
                                                .map(JsInt::of)
                                   )
                    )
                .get()
                .onSuccess(o -> {
                    if (o.equals(jsonvalues.JsObj.of("a",
                                                     JsInt.of(100),
                                                     "b",
                                                     JsInt.of(15),
                                                     "c",
                                                     JsStr.of("ABC"),
                                                     "d",
                                                     jsonvalues.JsArray.of(10,
                                                                           50
                                                                          )
                                                    ))
                    ) context.completeNow();

                });
    }

    @Override
    protected void initialize() {
        toUpper = this.ask("toUpper");
        multiplyBy10 = this.ask("multiplyByTen");
        add10 = this.ask("addTen");

    }

    @Override
    protected void deploy() {
        Lambda<String, String> keysToUpper = i -> VIO.succeed(i.toUpperCase());
        Function<Integer, Lambda<Integer, Integer>> multiplyBy = i -> j -> VIO.succeed(j * i);
        Function<Integer, Lambda<Integer, Integer>> add = i -> j -> VIO.succeed(i + j);
        this.deploy("toUpper",
                    keysToUpper
                   );
        this.deploy("multiplyByTen",
                    multiplyBy.apply(10)
                   );
        this.deploy("addTen",
                    add.apply(10)
                   );

    }
}
