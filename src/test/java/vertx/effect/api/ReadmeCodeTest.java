package vertx.effect.api;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.Lambda;
import vertx.effect.PairExp;
import vertx.effect.VertxRef;
import vertx.values.codecs.RegisterJsValuesCodecs;
@SuppressWarnings("ReturnValueIgnored")
@ExtendWith(VertxExtension.class)
public class ReadmeCodeTest {


    @BeforeAll
    public static void prepare(Vertx vertx,
                               VertxTestContext context
                              ) {

        VertxRef vertxRef = new VertxRef(vertx);

        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );

        PairExp.seq(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                    vertxRef.deployVerticle(new MyModule())
                   )
               .onSuccess(pair -> {
                   String id1 = pair.first();
                   String id2 = pair.second();
                   System.out.println(String.format("Ids deployed: %s and %s",
                                                    id1,
                                                    id2
                                                   ));
                   context.completeNow();
               })
               .get();

    }

    @Test
    public void test_to_lowercase(VertxTestContext context) {

        MyModule.toLowerCase.apply(MultiMap.caseInsensitiveMultiMap()
                                           .add("name",
                                                "rafa"
                                               ),
                                   "ABCDS"
                                  )
                            .get()
                            .onSuccess(it -> context.verify(() -> {
                                Assertions.assertEquals("abcds",
                                                        it
                                                       );
                                context.completeNow();
                            }));
    }

    @Test
    public void test_composition(VertxTestContext context) {

        Lambda<JsObj, JsObj> removeAndNull = MyModule.removeNull.then(MyModule.trim);

        JsObj obj = JsObj.of("a",
                             JsStr.of("  hi  "),
                             "b",
                             JsNull.NULL,
                             "c",
                             JsObj.of("d",
                                      JsStr.of("  bye  "),
                                      "e",
                                      JsNull.NULL
                                     )
                            );

        removeAndNull.apply(obj)
                     .onSuccess(it -> {
                         JsObj expected = JsObj.of("a",
                                                   JsStr.of("hi"),
                                                   "c",
                                                   JsObj.of("d",
                                                            JsStr.of("bye")
                                                           )
                                                  );
                         context.verify(() -> {
                             Assertions.assertEquals(expected,
                                                     it
                                                    );
                             context.completeNow();
                         });
                     })
                     .get();
    }
}