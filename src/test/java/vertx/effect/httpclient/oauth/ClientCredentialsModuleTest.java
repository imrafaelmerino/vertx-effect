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
import vertx.effect.exp.Pair;
import vertx.effect.httpclient.GetReq;
import vertx.effect.httpclient.HttpResp;
import vertx.effect.httpserver.HttpServerBuilder;
import vertx.effect.mock.*;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static vertx.effect.mock.MockReqResp.*;

@ExtendWith(VertxExtension.class)
public class ClientCredentialsModuleTest {

    static ClientCredentialsModule httpClient;
    static ClientCredentialsFlowBuilder builder;
    static VertxRef vertxRef;
    static int port = Port.number.incrementAndGet();


    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext context
                              ) {
        builder = new ClientCredentialsFlowBuilder(new HttpClientOptions().setDefaultPort(port)
                                                                          .setDefaultHost("localhost"),
                                                   "my-httpclient",
                                                   new GetAccessTokenRequest("",
                                                                             ""
                                                   )
        ).setRetryReqPredicate(Failures.REPLY_EXCEPTION_PRISM
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
                                             ));

        httpClient = builder.createModule();


        vertxRef = new VertxRef(vertx);

        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );

        Pair.sequential(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                        vertxRef.deployVerticle(httpClient)
                       )
            .onComplete(Verifiers.pipeTo(context))
            .get();

    }


    @Test
    public void test_get_success_after_three_retries_getting_token(Vertx vertx,
                                                                   VertxTestContext context) {


        builder.setAccessTokenReqAttempts(3);

        new HttpServerBuilder(vertx,
                              new MockReqHandler(List.of(MockReqResp.when(REQ_LET.apply(3))
                                                                    .setBodyResp(MockBodyResp.cons(JsObj.of("token_found",
                                                                                                            FALSE
                                                                                                           )
                                                                                                  )
                                                                                )
                                                                    .setStatusCodeResp(MockStatusCodeResp._401)
                                                                    .setHeadersResp(MockHeadersResp.JSON),
                                                         MockReqResp.when(FORTH_REQ)
                                                                    .setBodyResp(MockBodyResp.cons(JsObj.of("token_found",
                                                                                                            TRUE,
                                                                                                            "access_token",
                                                                                                            JsStr.of("foooo")
                                                                                                           )
                                                                                                  )
                                                                                )
                                                                    .setStatusCodeResp(MockStatusCodeResp._200)
                                                                    .setHeadersResp(MockHeadersResp.JSON),
                                                         MockReqResp.when(REQ_GT.apply(4))
                                                                    .setBodyResp(MockBodyResp.cons(JsObj.of("name",
                                                                                                            JsStr.of("Rafael")
                                                                                                           ))
                                                                                )
                                                                    .setStatusCodeResp(MockStatusCodeResp._200)
                                                                    .setHeadersResp(MockHeadersResp.JSON)
                                                        )
                              )
        ).start(port)
         .get()
         .onSuccess(server -> {
             Verifiers.<JsObj>verifySuccess(resp -> HttpResp.STATUS_CODE_LENS.get.apply(resp) == 200)
                     .accept(httpClient.getOauth.apply(new GetReq().uri("/name")),
                             context
                            );
         });

    }

    @Test
    public void test_get_success_after_three_retries(Vertx vertx,
                                                     VertxTestContext context) {


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
                                                                    .setStatusCodeResp(MockStatusCodeResp._200),
                                                         MockReqResp.when(REQ_LET.apply(7))
                                                                    .setBodyResp(c -> body -> req -> {
                                                                                     req.response()
                                                                                        .close();
                                                                                     return "{}";
                                                                                 }
                                                                                )
                                                                    .setStatusCodeResp(MockStatusCodeResp._200),
                                                         MockReqResp.when(REQ_GT.apply(7))
                                                                    .setBodyResp(MockBodyResp.cons(JsObj.of("name",
                                                                                                            JsStr.of("Rafael")
                                                                                                           ))
                                                                                )
                                                                    .setStatusCodeResp(MockStatusCodeResp._200)

                                                        ))
        ).start(port)
         .get()
         .onSuccess(server -> {
             Verifiers.<JsObj>verifySuccess(resp -> HttpResp.STATUS_CODE_LENS.get.apply(resp) == 200)
                     .accept(httpClient.getOauth.apply(new GetReq().uri("/name")
                                                                   .timeout(300,
                                                                            TimeUnit.MILLISECONDS
                                                                           ))
                                                .retry(3),
                             context
                            );
         });


    }

}