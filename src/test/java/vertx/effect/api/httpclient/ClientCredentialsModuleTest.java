package vertx.effect.api.httpclient;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.PairExp;
import vertx.effect.RetryPolicies;
import vertx.effect.VertxRef;
import vertx.effect.api.Port;
import vertx.effect.api.Verifiers;
import vertx.effect.http.client.GetReq;
import vertx.effect.http.client.HttpResp;
import vertx.effect.http.client.oauth.ClientCredentialsFlowBuilder;
import vertx.effect.http.client.oauth.ClientCredentialsModule;
import vertx.effect.http.client.oauth.GetAccessTokenRequest;
import vertx.effect.http.server.HttpServerBuilder;
import vertx.effect.http.stub.*;
import vertx.values.codecs.RegisterJsValuesCodecs;


import java.util.List;

import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static vertx.effect.http.stub.HttpRespStub.*;

@SuppressWarnings("ReturnValueIgnored")
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
                                                                          .setDefaultHost("0.0.0.0"),
                                                   "my-httpclient",
                                                   new GetAccessTokenRequest("",
                                                                             ""
                                                   ));

        httpClient = builder.createModule();

        vertxRef = new VertxRef(vertx);

        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  (JsObj message) ->
                                          System.out.println(message.filterKeys(key -> !key.equals("thread")
                                                                     && !key.equals("instant"))
                                                            )
                                 );

        PairExp.seq(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                    vertxRef.deployVerticle(httpClient)
                   )
               .onComplete(Verifiers.pipeTo(context))
               .get();

    }


    @Test
    public void test_get_success_after_three_retries_getting_token(Vertx vertx,
                                                                   VertxTestContext context
                                                                  ) {


        builder.setAccessTokenReqRetryPolicy(e -> true,
                                             RetryPolicies.limitRetries(3)
                                            );

        new HttpServerBuilder(vertx,
                              new HttpReqHandlerStub(List.of(HttpRespStub.when(REQ_LET.apply(3))
                                                                         .setBodyResp(HttpBodyRespStub.cons(JsObj.of("token_found",
                                                                                                                     FALSE
                                                                                                                    )
                                                                                                           )
                                                                                     )
                                                                         .setStatusCodeResp(HttpStatusCodeRespStub._401)
                                                                         .setHeadersResp(HttpHeadersRespStub.JSON),
                                                             HttpRespStub.when(FORTH_REQ)
                                                                         .setBodyResp(HttpBodyRespStub.cons(JsObj.of("token_found",
                                                                                                                     TRUE,
                                                                                                                     "access_token",
                                                                                                                     JsStr.of("foooo")
                                                                                                                    )
                                                                                                           )
                                                                                     )
                                                                         .setStatusCodeResp(HttpStatusCodeRespStub._200)
                                                                         .setHeadersResp(HttpHeadersRespStub.JSON),
                                                             HttpRespStub.when(REQ_GT.apply(4))
                                                                         .setBodyResp(HttpBodyRespStub.cons(JsObj.of("name",
                                                                                                                     JsStr.of("Rafael")
                                                                                                                    ))
                                                                                     )
                                                                         .setStatusCodeResp(HttpStatusCodeRespStub._200)
                                                                         .setHeadersResp(HttpHeadersRespStub.JSON)
                                                            )
                              )
        ).start(port)
         .get()
         .onSuccess(server -> Verifiers.<JsObj>verifySuccess(resp -> HttpResp.STATUS_CODE_LENS.get.apply(resp) == 200)
                                       .accept(httpClient.getOauth.apply(new GetReq().uri("/name")),
                                               context
                                              ));

    }

    @Test
    public void test_get_success_after_three_retries(Vertx vertx,
                                                     VertxTestContext context
                                                    ) {


        new HttpServerBuilder(vertx,
                              new HttpReqHandlerStub(HttpRespStub.when(REQ_LET.apply(3))
                                                                 .setBodyResp(HttpBodyRespStub.cons(JsObj.of("token_found",
                                                                                                             FALSE
                                                                                                            )
                                                                                                   )
                                                                             )
                                                                 .setStatusCodeResp(HttpStatusCodeRespStub._401),
                                                     HttpRespStub.when(FORTH_REQ)
                                                                 .setBodyResp(HttpBodyRespStub.cons(JsObj.of("token_found",
                                                                                                             TRUE,
                                                                                                             "access_token",
                                                                                                             JsStr.of("foooo")
                                                                                                            )
                                                                                                   )
                                                                             )
                                                                 .setStatusCodeResp(HttpStatusCodeRespStub._200),
                                                     HttpRespStub.when(REQ_LET.apply(7))
                                                                 .setBodyResp(c -> body -> req -> {
                                                                                  req.response().close();
                                                                                  return "{}";
                                                                              }
                                                                             )
                                                                 .setStatusCodeResp(HttpStatusCodeRespStub._500),
                                                     HttpRespStub.when(REQ_GT.apply(7))
                                                                 .setBodyResp(HttpBodyRespStub.cons(JsObj.of("name",
                                                                                                             JsStr.of("Rafael")
                                                                                                            ))
                                                                             )
                                                                 .setStatusCodeResp(HttpStatusCodeRespStub._200)

                              )
        ).start(port)
         .get()
         .onSuccess(server -> Verifiers.<JsObj>verifySuccess(resp -> HttpResp.STATUS_CODE_LENS.get.apply(resp) == 200)
                                       .accept(httpClient.getOauth.apply(new GetReq().uri("/name"))
                                                                  .retry(RetryPolicies.limitRetries(3)),
                                               context
                                              ));


    }

}