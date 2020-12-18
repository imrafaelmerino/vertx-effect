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
import vertx.effect.*;
import vertx.effect.exp.Triple;
import vertx.effect.httpclient.GetReq;
import vertx.effect.httpclient.PostReq;
import vertx.effect.mock.MockReqResp;
import vertx.effect.mock.HttpServerBuilder;
import vertx.effect.mock.MockHeadersResp;

import java.util.Objects;

import static vertx.effect.mock.MockReqResp.ALWAYS;

@ExtendWith(VertxExtension.class)
public class AuthorizationCodeFlowAuthenticateReq {

    static AuthorizationCodeModule httpClient;
    static final int PORT = Port.number.incrementAndGet();


    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext context
                              ) {

        VertxRef vertxRef = new VertxRef(vertx);
        MockReqResp mockReqResp =
                MockReqResp.when(ALWAYS)
                           .setBodyResp(n -> body -> req -> JsObj.empty()
                                                               .set("access_token",
                                                                    JsStr.of("access_token_value")
                                                                   )
                                                               .set("refresh_token",
                                                                    JsStr.of("refresh_token_value")
                                                                   )
                                                               .toPrettyString()
                                     )
                           .setHeadersResp(MockHeadersResp.JSON);

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
                                               .apply(exc -> Objects.equals(Failures.HTTP_UNKNOWN_HOST_CODE,
                                                                            exc.failureCode()
                                                                           ) ||
                                                       Objects.equals(Failures.HTTP_CONNECT_TIMEOUT_CODE,
                                                                      exc.failureCode()
                                                                     )
                                                       || Objects.equals(Failures.HTTP_REQUEST_TIMEOUT_CODE,
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


        Triple.sequential(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                          new HttpServerBuilder(vertx).addMock(mockReqResp)
                                                      .start(PORT),
                          vertxRef.deployVerticle(httpClient)
                         )
              .get()
              .onComplete(Verifiers.pipeTo(context));


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
