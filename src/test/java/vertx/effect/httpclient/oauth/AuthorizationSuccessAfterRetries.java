package vertx.effect.httpclient.oauth;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.Port;
import vertx.effect.RegisterJsValuesCodecs;
import vertx.effect.Verifiers;
import vertx.effect.VertxRef;
import vertx.effect.exp.Triple;
import vertx.effect.httpclient.GetReq;
import vertx.effect.httpclient.HttpResp;
import vertx.effect.httpserver.HttpServerBuilder;
import vertx.effect.mock.MockBodyResp;
import vertx.effect.mock.MockReqHandler;
import vertx.effect.mock.MockReqResp;
import vertx.effect.mock.MockStatusCodeResp;

import java.util.List;

import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static vertx.effect.mock.MockReqResp.*;

@ExtendWith(VertxExtension.class)
public class AuthorizationSuccessAfterRetries {

    static AuthorizationCodeModule httpClient;
    static AuthorizationCodeFlowBuilder builder;
    static int port = Port.number.incrementAndGet();

    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext context
                              ) {
        builder = new AuthorizationCodeFlowBuilder(new HttpClientOptions().setDefaultPort(port)
                                                                          .setDefaultHost("localhost"),
                                                   "testing",
                                                   new RefreshAccessTokenReq("",
                                                                             ""
                                                   )
        );


        httpClient = builder.createFromRefreshToken("");

        VertxRef vertxRef = new VertxRef(vertx);


        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );


        Triple.parallel(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                        new HttpServerBuilder(vertx,
                                              new MockReqHandler(List.of(MockReqResp.when(REQ_LET.apply(3))
                                                                                    .setBodyResp(MockBodyResp.cons(JsObj.of("token_found",
                                                                                                                            FALSE
                                                                                                                           )
                                                                                                                  )
                                                                                                )
                                                                                    .setStatusCodeResp(MockStatusCodeResp._401),
                                                                         MockReqResp.when(FORTH_REQ)
                                                                                    .setBodyResp(MockBodyResp.cons(JsObj.of("token_found",
                                                                                                                            TRUE,
                                                                                                                            "access_token",
                                                                                                                            JsStr.of("foooo")
                                                                                                                           )
                                                                                                                  )
                                                                                                )
                                                                                    .setStatusCodeResp(MockStatusCodeResp._200)
                                                      ,
                                                                         MockReqResp.when(REQ_GT.apply(4))
                                                                                    .setBodyResp(MockBodyResp.cons(JsObj.of("name",
                                                                                                                            JsStr.of("Rafael")
                                                                                                                           )
                                                                                                                  )
                                                                                                )
                                                                                    .setStatusCodeResp(MockStatusCodeResp._200)

                                                                        )
                                              )
                        ).start(port),
                        vertxRef.deployVerticle(httpClient)
                       )
              .onComplete(
                      Verifiers.pipeTo(context)
                         )
              .get();


    }

    @Test
    public void test_get_success_after_three_retries(VertxTestContext context) {

        Verifiers.<JsObj>verifySuccess(resp -> HttpResp.STATUS_CODE_LENS.get.apply(resp) == 200)
                .accept(httpClient.getOauth.apply(new GetReq().uri("/name")),
                        context
                       );

    }


}