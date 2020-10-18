package vertx.effect.patterns.oauth;

import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.Validators;


@ExtendWith(VertxExtension.class)
public class TestOauth {



    @Test
    public void test_Get_TokenVerticle(VertxTestContext context) {
        JsObj GET_MESSAGE = JsObj.of("op",
                                     JsStr.of("get")
                                    );

        JsObj RENEW_MESSAGE = JsObj.of("op",
                                       JsStr.of("renew")
                                      );

        GetTokenVerticle getTokenVerticle =
                new GetTokenVerticle(new GetTokenReqVerticle(),
                                     Validators.validateJsObj(JsObjSpec.strict("op",
                                                                               JsSpecs.cons(JsStr.of("get"))
                                                                              )
                                                             ),
                                     Validators.validateJsObj(JsObjSpec.strict("op",
                                                                               JsSpecs.cons(JsStr.of("renew"))
                                                                              )
                                                             )

                );

        getTokenVerticle.apply(GET_MESSAGE)
                        .onSuccess(token -> context.verify(() -> {
                                                               Assertions.assertEquals("1",
                                                                                       token
                                                                                      );
                                                               context.completeNow();
                                                           }
                                                          )
                                  )
                        .get();

        getTokenVerticle.apply(GET_MESSAGE)
                        .onSuccess(token -> context.verify(() -> {
                                                               Assertions.assertEquals("1",
                                                                                       token
                                                                                      );
                                                               context.completeNow();
                                                           }
                                                          )
                                  )
                        .get();

        getTokenVerticle.apply(RENEW_MESSAGE)
                        .onSuccess(token -> context.verify(() -> {
                                                               Assertions.assertEquals("2",
                                                                                       token
                                                                                      );
                                                               context.completeNow();
                                                           }
                                                          )
                                  )
                        .get();

        getTokenVerticle.apply(GET_MESSAGE)
                        .onSuccess(token -> context.verify(() -> {
                                                               Assertions.assertEquals("2",
                                                                                       token
                                                                                      );
                                                               context.completeNow();
                                                           }
                                                          )
                                  )
                        .get();

        getTokenVerticle.apply(RENEW_MESSAGE)
                        .onSuccess(token -> context.verify(() -> {
                                                               Assertions.assertEquals("3",
                                                                                       token
                                                                                      );
                                                               context.completeNow();
                                                           }
                                                          )
                                  )
                        .get();

        getTokenVerticle.apply(RENEW_MESSAGE)
                        .onSuccess(token -> context.verify(() -> {
                                                               Assertions.assertEquals("4",
                                                                                       token
                                                                                      );
                                                               context.completeNow();
                                                           }
                                                          )
                                  )
                        .get();
    }


}
