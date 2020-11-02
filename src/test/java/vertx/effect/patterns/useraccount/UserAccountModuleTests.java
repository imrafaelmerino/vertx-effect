package vertx.effect.patterns.useraccount;

import io.vavr.Tuple2;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsInt;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.RegisterJsValuesCodecs;
import vertx.effect.Verifiers;
import vertx.effect.VertxRef;
import vertx.effect.exp.Pair;

@ExtendWith(VertxExtension.class)
public class UserAccountModuleTests {


    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext context
                              ) {

        VertxRef vertxRef = new VertxRef(vertx);
        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );
        Verifiers.<Tuple2<String, String>>verifySuccess()
                .accept(Pair.parallel(vertxRef.deployVerticle(new UserAccountModule()),
                                      vertxRef.deployVerticle(new RegisterJsValuesCodecs())
                                     ),
                        context
                       );


    }

    @Test
    public void _15_is_not_legal_age(final VertxTestContext context) {
        UserAccountModule.isLegalAge
                .apply(13)
                .onComplete(it -> context.verify(() ->
                                                 {
                                                     Assertions.assertFalse(it.result());
                                                     context.completeNow();
                                                 }
                                                )
                           )
                .get()
        ;
    }


    @Test
    public void _17_is_not_legal_age(final VertxTestContext context) {
        UserAccountModule.isLegalAge
                .apply(17)
                .onComplete(it -> context.verify(() ->
                                                 {
                                                     Assertions.assertTrue(it.result());
                                                     context.completeNow();
                                                 }
                                                )
                           )
                .get();


    }


    @Test
    public void _user_is_valid(final VertxTestContext context) {
        JsObj user = JsObj.of("email",
                              JsStr.of("imrafaelmerino@gmail.com"),
                              "age",
                              JsInt.of(17),
                              "id",
                              JsStr.of("03786761>")
                             );


        UserAccountModule.isValid
                .apply(MultiMap.caseInsensitiveMultiMap()
                               .add("email",
                                    user.getStr("email")
                                   ),
                       user
                      )
                .onComplete(it -> context.verify(() ->
                                                 {
                                                     Assertions.assertTrue(it.succeeded());
                                                     Assertions.assertTrue(it.result());
                                                     context.completeNow();
                                                 }
                                                )
                           )
                .get();


    }
}
