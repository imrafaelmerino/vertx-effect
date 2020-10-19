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
import vertx.effect.Failures;
import vertx.effect.RegisterJsValuesCodecs;
import vertx.effect.httpclient.GetReq;
import vertx.effect.httpclient.PostReq;
import vertx.effect.Verifiers;
import vertx.effect.VertxRef;
import vertx.effect.exp.Cons;
import vertx.effect.exp.Triple;
import vertx.effect.httpclient.MyHttpServer;

import java.util.Objects;

@ExtendWith(VertxExtension.class)
public class AuthorizationCodeFlowAuthenticateReq {

    static AuthorizationCodeModule httpClient;
    static MyHttpServer server;
    static final int PORT = 1111;


    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext context
                              ) {


        server = new MyHttpServer(vertx,
                                  PORT,
                                  counter -> req -> body -> JsObj.empty()
                                                                 .set("access_token",
                                                                      JsStr.of("access_token_value")
                                                                     )
                                                                 .set("refresh_token",
                                                                      JsStr.of("refresh_token_value")
                                                                     ),
                                  counter -> req -> body -> 200
        );
        VertxRef vertxRef = new VertxRef(vertx);

        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );
        HttpClientOptions options =
                new HttpClientOptions().setDefaultHost("localhost")
                                       .setDefaultPort(PORT);
        httpClient =
                new AuthorizationCodeFlowBuilder(options,
                                                 "oauth-http-client",
                                                 new RefreshAccessTokenReq("client_id",
                                                                           "client_secret"
                                                 )


                ).setReqAttempts(4)
                 .setRetryReqPredicate(Failures.REPLY_EXCEPTION_PRISM
                                               .exists
                                               .apply(exc -> Objects.equals(Failures.UNKNOWN_HOST_CODE,
                                                                            exc.failureCode()
                                                                           ) ||
                                                       Objects.equals(Failures.CONNECT_TIMEOUT_CODE,
                                                                      exc.failureCode()
                                                                     )
                                                       || Objects.equals(Failures.REQUEST_TIMEOUT_CODE,
                                                                         exc.failureCode()
                                                                        )
                                                     ))
                 .createFromAuthReq((mod, input) ->
                                            mod.post.apply(new PostReq(String.format("code=%s&redirect_uri=%s",
                                                                                     input.getStr("code"),
                                                                                     input.getStr("redirect_uri")
                                                                                    )
                                                                             .getBytes()).uri("/authenticate"))
                                   );

        Triple.of(vertxRef.deploy(new RegisterJsValuesCodecs()),
                  Cons.of(() -> server.start()),
                  vertxRef.deploy(httpClient)
                 )
              .onComplete(Verifiers.pipeTo(context))
              .get();


    }

    @Test
    public void test_authenticate(VertxTestContext context) {


        httpClient.authenticate(JsObj.of("code",
                                         JsStr.of("code"),
                                         "redirect_uri",
                                         JsStr.of("redirect_uri")
                                        )
                               )
                  .onComplete(resp -> {
                      if (resp.succeeded()) {
                          Verifiers.<JsObj>verifySuccess(tuple -> "access_token_value".equals(resp.result()._1) &&
                                                                 "refresh_token_value".equals(resp.result()._2)
                                                        )
                                  .accept(httpClient.getOauth.apply(new GetReq().uri("/name")),
                                          context
                                         );
                      }
                      else {
                          context.failNow(resp.cause());

                      }
                  })
                  .get();

    }

}
