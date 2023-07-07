package vertx.effect.api.patterns.exp;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsStr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.VIO;
import vertx.effect.VertxRef;
import vertx.effect.JsArrayExp;
import vertx.effect.PairExp;
import vertx.values.codecs.RegisterJsValuesCodecs;
@SuppressWarnings("ReturnValueIgnored")
@ExtendWith(VertxExtension.class)
public class TestRace {

    @Test
    public void test_race(final VertxTestContext context,
                          final Vertx vertx
                         ) {

        VertxRef ref = new VertxRef(vertx);
        PairExp.seq(ref.deployVerticle(new RegisterJsValuesCodecs()),
                    ref.deployVerticle(new Module())
                   )
               .onSuccess(pair ->
                                  VIO.race(Module.head.apply("Aa")
                                                      .map(JsStr::of),
                                           Module.tail.apply("Aa")
                                                      .map(JsStr::of),
                                           Module.toUpperCase.apply("Aa")
                                                             .map(JsStr::of),
                                           Module.toLowerCase.apply("aA")
                                                             .map(JsStr::of)
                                          )
                                     .onSuccess(val -> {
                                         context.verify(() -> {
                                                            Assertions.assertEquals(JsStr.of("aa"),
                                                                                    val
                                                                                   );
                                                            context.completeNow();
                                                        }
                                                       );
                                     })
                                     .get()


                         )
               .get();
    }
}
