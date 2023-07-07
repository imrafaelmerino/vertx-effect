package vertx.effect.api.patterns.useraccount;

import fun.tuple.Pair;
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
import vertx.effect.api.Verifiers;
import vertx.effect.VertxRef;
import vertx.effect.PairExp;
import vertx.values.codecs.RegisterJsValuesCodecs;
@SuppressWarnings("ReturnValueIgnored")
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
        Verifiers.<Pair<String, String>>verifySuccess()
                 .accept(PairExp.seq(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                                     vertxRef.deployVerticle(new UserAccountModule())
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
