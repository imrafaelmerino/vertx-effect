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
import vertx.effect.httpserver.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@ExtendWith(VertxExtension.class)
public class AuthorizationCodeModuleTest {

    static AuthorizationCodeModule httpClient;

    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext context
                              ) {
        int      port     = Port.number.incrementAndGet();
        VertxRef vertxRef = new VertxRef(vertx);


        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );
        HttpClientOptions options =
                new HttpClientOptions().setDefaultHost("localhost")
                                       .setDefaultPort(port);
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
                 .createFromRefreshToken("refresh_token");

        Triple.parallel(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                        new HttpServerBuilder(vertx).addHandler(ReqHandler.when(ReqHandler.REQ_LET.apply(3))
                                                                          .setBodyResp(BodyRespHandler.cons(JsObj.of("error",
                                                                                                                     JsStr.of("Error generating token")
                                                                                                                    )
                                                                                                           )
                                                                                       )
                                                                          .setStatusCodeResp(StatusRespHandler._401)
                                                                          .setHeadersResp(HeadersRespHandler.JSON)
                                                               )
                                                    .addHandler(ReqHandler.when(ReqHandler.FORTH_REQ)
                                                                          .setBodyResp(BodyRespHandler.cons(JsObj.of("access_token",
                                                                                                                     JsStr.of(UUID.randomUUID()
                                                                                                                                .toString())
                                                                                                                    ))
                                                                                       )
                                                                          .setStatusCodeResp(StatusRespHandler._200)
                                                                          .setHeadersResp(HeadersRespHandler.JSON))
                                                    .addHandler(ReqHandler.when(ReqHandler.REQ_LET.apply(7))
                                                                          .setBodyResp(BodyRespHandler.consAfter(Duration.of(500,
                                                                                                                             ChronoUnit.MILLIS
                                                                                                                            ),
                                                                                                                 JsObj.empty()
                                                                                                                ))
                                                                          .setStatusCodeResp(StatusRespHandler._200)
                                                                          .setHeadersResp(HeadersRespHandler.JSON))
                                                    .addHandler(ReqHandler.when(ReqHandler.REQ_GT.apply(7))
                                                                          .setBodyResp(BodyRespHandler.cons(JsObj.empty()))
                                                                          .setStatusCodeResp(StatusRespHandler._200)
                                                                          .setHeadersResp(HeadersRespHandler.JSON))
                                                    .start(port),
                        vertxRef.deployVerticle(httpClient)
                       )
              .onComplete(Verifiers.pipeTo(context))
              .get();


    }


    @Test
    public void test_create_from_refresh_token_with_retries(VertxTestContext context) {
        GetReq t = new GetReq().uri("/uri")
                               .timeout(500,
                                        TimeUnit.MILLISECONDS
                                       );
        httpClient.getOauth.apply(t)
                           .onComplete(it -> {
                               if (it.succeeded())
                                   context.completeNow();
                               else context.failNow(it.cause());
                           })
                           .get();

    }
}