package vertx.effect.patterns.exp;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsStr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.RegisterJsValuesCodecs;
import vertx.effect.VertxRef;
import vertx.effect.exp.JsArrayVal;
import vertx.effect.exp.Pair;

@ExtendWith(VertxExtension.class)
public class TestJsArrayVal {

    @Test
    public void test_race(final VertxTestContext context,
                          final Vertx vertx) {

        VertxRef ref = new VertxRef(vertx);
        Pair.sequential(ref.deployVerticle(new RegisterJsValuesCodecs()),
                        ref.deployVerticle(new Module())
                       )
            .onSuccess(pair ->
                               JsArrayVal.parallel()
                                         .append(Module.head.apply("Aa")
                                                            .map(JsStr::of))
                                         .append(Module.tail.apply("Aa")
                                                            .map(JsStr::of))
                                         .append(Module.toUpperCase.apply("Aa")
                                                                   .map(JsStr::of))
                                         .append(Module.toLowerCase.apply("aA")
                                                                   .map(JsStr::of))
                                         .race()
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
