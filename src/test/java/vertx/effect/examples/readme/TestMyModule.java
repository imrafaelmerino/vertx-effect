package vertx.effect.examples.readme;

import io.vertx.core.Vertx;
import io.vertx.junit5.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import jsonvalues.JsArray;
import jsonvalues.JsInt;
import jsonvalues.JsObj;
import vertx.effect.PairExp;
import vertx.effect.VertxRef;
import vertx.values.codecs.RegisterJsValuesCodecs;
@SuppressWarnings("ReturnValueIgnored")
@ExtendWith(VertxExtension.class)
public class TestMyModule {

    @BeforeAll
    // register a MessageCodec for json-values and deploy MyModule
    public static void prepare(final Vertx vertx,
                               final VertxTestContext context
                              ) {
        VertxRef ref = new VertxRef(vertx);
        PairExp.seq(ref.deployVerticle(new RegisterJsValuesCodecs()),
                    ref.deployVerticle(new MyModule())
                   )
               .onComplete(r -> {
                               if(r.succeeded()) context.completeNow();
                               else context.failNow(r.cause());
                           })


               .get();
    }

    @Test
    public void empty_json_is_sent_and_failure_is_received(VertxTestContext context) {

        MyModule.validateAndMap.apply(JsObj.empty())
                               .onComplete(result ->
                                                   context.verify(() -> {
                                                       Assertions.assertTrue(result.failed());
                                                       System.out.println(result.cause());
                                                       context.completeNow();
                                                   })
                                          )
                               .get();
    }

    @Test
    public void valid_json_is_sent_and_is_mapped_successfully(VertxTestContext context) {

        JsObj input = JsObj.of("a", JsInt.of(1), "b", JsArray.of("FOO", "foo"));

        JsObj expected = JsObj.of("a", JsInt.of(2), "b", JsArray.of("foo", "FOO"));

        MyModule.validateAndMap.apply(input)
                               .onSuccess(output -> {
                                   context.verify(() -> {
                                       Assertions.assertEquals(expected, output);
                                       context.completeNow();
                                   });
                               })
                               .get();
    }
}