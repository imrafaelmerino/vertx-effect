package vertx.effect.readme;

import io.vertx.core.Vertx;
import io.vertx.junit5.*;
import jsonvalues.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.RegisterJsValuesCodecs;
import vertx.effect.VertxRef;
import vertx.effect.exp.Pair;
import vertx.effect.λ;
import static vertx.effect.VertxRef.EVENTS_ADDRESS;

@ExtendWith(VertxExtension.class)
public class TestMyModule {


    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext context)
    {
        VertxRef vertxRef = new VertxRef(vertx);

        // prints out events published by vertx-effect
        vertxRef.registerConsumer(EVENTS_ADDRESS, System.out::println);

        Pair.sequential(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                        vertxRef.deployVerticle(new MyModule())
                       )
            .onSuccess(pair -> {
                           System.out.println(String.format("Ids deployed: %s and %s",
                                                            pair._1,
                                                            pair._2
                                                           )
                                             );
                           context.completeNow();
                       }
                      )
            .get();
    }

    @Test
    public void test_composition(final VertxTestContext context)
    {
        λ<JsObj, JsObj> removeAndTrim = MyModule.removeNull.andThen(MyModule.trim);

        JsObj input = JsObj.of("a", JsStr.of("  hi  "),
                               "b", JsNull.NULL,
                               "c", JsObj.of("d", JsStr.of("  bye  "),
                                             "e", JsNull.NULL
                                            )
                              );

        JsObj expected = JsObj.of("a", JsStr.of("hi"),
                                  "c", JsObj.of("d", JsStr.of("bye"))
                                 );

        removeAndTrim.apply(input)
                     .onSuccess(it -> {
                                    context.verify(()-> {
                                                       Assertions.assertEquals(expected,it);
                                                       context.completeNow();
                                                   }
                                                  );
                                }
                               )
                     .get();
    }
}
