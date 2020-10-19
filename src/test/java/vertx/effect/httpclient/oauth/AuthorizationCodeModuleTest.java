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
import vertx.effect.VertxRef;
import vertx.effect.httpclient.GetReq;
import vertx.effect.httpclient.MyHttpServer;
import vertx.effect.Verifiers;
import vertx.effect.exp.Cons;
import vertx.effect.exp.Triple;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@ExtendWith(VertxExtension.class)
public class AuthorizationCodeModuleTest {

    static AuthorizationCodeModule httpClient;
    static MyHttpServer server;

    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext context
                              ) {
        int port = 3333;


        server = new MyHttpServer(vertx,
                                  port,
                                  counter -> req -> body -> {
                                      if (counter <= 3) return JsObj.empty()
                                                                    .set("error",
                                                                         JsStr.of("Error generating token")
                                                                        );
                                      else if (counter == 4) return JsObj.empty()
                                                                         .set("access_token",
                                                                              JsStr.of(UUID.randomUUID().toString())
                                                                             );
                                      else if (counter <= 7) {
                                          try {
                                              Thread.sleep(500);
                                          } catch (InterruptedException e) {
                                              throw new RuntimeException(e);
                                          }
                                          return JsObj.empty();
                                      }
                                      else return JsObj.empty();

                                  },
                                  counter -> req -> body -> (counter <= 3) ? 401 : 200
        );
        VertxRef vertxRef = new VertxRef(vertx);

        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );
        HttpClientOptions options =
                new HttpClientOptions().setDefaultHost("localhost")
                                       .setDefaultPort(3333);
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
                 .createFromRefreshToken("refresh_token");

        Triple.of(vertxRef.deploy(new RegisterJsValuesCodecs()),
                  Cons.of(() -> server.start()),
                  vertxRef.deploy(httpClient)
                 )
              .onComplete(Verifiers.pipeTo(context))
              .get();


    }


    @Test
    public void test_create_from_refresh_token_with_retries(VertxTestContext context) {
        GetReq t = new GetReq().uri("/uri")
                               .timeout(400,
                                        TimeUnit.MILLISECONDS
                                       );
        httpClient.getOauth.apply(t)
                           .onComplete(it -> {
                               if (it.succeeded()) {
                                   context.completeNow();
                               }
                               else
                                   context.failNow(it.cause());
                           })
                           .get();

    }
}