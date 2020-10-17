package vertxeffect.httpclient.oauth;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertxeffect.RegisterJsValuesCodecs;
import vertxeffect.httpclient.MyHttpServer;
import vertxeffect.Verifiers;
import vertxeffect.VertxRef;
import vertxeffect.exp.Cons;
import vertxeffect.exp.Triple;
import vertxeffect.httpclient.GetReq;
import vertxeffect.httpclient.HttpResp;

import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;

@ExtendWith(VertxExtension.class)
public class AuthorizationSuccessAfterRetries {

    static AuthorizationCodeModule httpClient;
    static AuthorizationCodeFlowBuilder builder;
    static MyHttpServer server;

    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext context
                              ) {
        int port = 5555;
        builder = new AuthorizationCodeFlowBuilder(new HttpClientOptions().setDefaultPort(port)
                                                                          .setDefaultHost("localhost"),
                                                   "testing",
                                                   new RefreshAccessTokenReq("",
                                                                             ""
                                                   )
        );


        httpClient = builder.createFromRefreshToken("");


        server = new MyHttpServer(vertx,
                                  port,
                                  c -> req -> body -> JsObj.empty(),
                                  counter -> req -> body -> 200
        );
        VertxRef vertxRef = new VertxRef(vertx);

        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );


        Triple.of(vertxRef.deploy(new RegisterJsValuesCodecs()),
                  Cons.of(() -> server.start()),
                  vertxRef.deploy(httpClient)
                 )
              .onComplete(Verifiers.pipeTo(context))
              .get();


    }

    @Test
    public void test_get_success_after_three_retries(VertxTestContext context) {

        server.resetCounter();

        server.setStatusCodeRes(counter -> req -> body -> counter <= 3 ? 401 : 200);

        server.setBodyRes(counter -> req -> body -> {
            if (counter <= 3)
                return JsObj.of("token_found",
                                FALSE
                               );
            else if (counter == 4) {
                return JsObj.of("token_found",
                                TRUE,
                                "access_token",
                                JsStr.of("foooo")
                               );

            }
            else return JsObj.of("name",
                                 JsStr.of("Rafael")
                                );
        });


        Verifiers.<JsObj>verifySuccess(resp -> HttpResp.STATUS_CODE_LENS.get.apply(resp) == 200)
                .accept(httpClient.getOauth.apply(new GetReq().uri("/name")),
                        context
                       );

    }


}