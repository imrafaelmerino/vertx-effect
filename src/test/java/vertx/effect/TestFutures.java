package vertx.effect;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsInt;
import jsonvalues.JsStr;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.exp.Cons;
import vertx.effect.exp.JsArrayExp;
import vertx.effect.exp.JsObjExp;

import java.util.function.Function;

@ExtendWith(VertxExtension.class)
public class TestFutures extends VertxModule {

    public static Function<Integer, Val<Integer>> multiplyBy10;
    public static Function<Integer, Val<Integer>> add10;
    public static Function<String, Val<String>> toUpper;


    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext testContext
                              ) {
        VertxRef vertxRef = new VertxRef(vertx);

        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );

        CompositeFuture.all(vertx.deployVerticle(new TestFutures()),
                            vertx.deployVerticle(new RegisterJsValuesCodecs())
                           )
                       .onComplete(event -> {
                           if (event.failed()) testContext.failNow(event.cause());
                           else testContext.completeNow();
                       });


    }

    @Test
    public void testJsObjFuture(final VertxTestContext context) {

        JsObjExp.parallel("a",
                          multiplyBy10.apply(10)
                                .map(JsInt::of),
                          "b",
                          add10.apply(5)
                         .map(JsInt::of),
                          "c",
                          toUpper.apply("abc")
                           .map(JsStr::of),
                          "d",
                          JsArrayExp.sequential(multiplyBy10.apply(1)
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
        λ<String, String>                      keysToUpper = i -> Cons.success(i.toUpperCase());
        Function<Integer, λ<Integer, Integer>> multiplyBy  = i -> j -> Cons.success(j * i);
        Function<Integer, λ<Integer, Integer>> add = i -> j -> Cons.success(i + j);
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
