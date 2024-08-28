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
import vertx.effect.*;
import vertx.effect.api.Port;
import vertx.effect.api.Verifiers;
import vertx.effect.stub.http.*;
import vertx.values.codecs.RegisterJsValuesCodecs;

import java.util.List;

import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static vertx.effect.stub.http.HttpRespStub.*;

@SuppressWarnings("ReturnValueIgnored")
@ExtendWith(VertxExtension.class)
public class ClientCredentialsModuleTest {

    static ClientCredentialsModule httpClient;
    static ClientCredentialsModuleBuilder builder;
    static VertxRef vertxRef;
    static int port = Port.number.incrementAndGet();


    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext context
                              ) {
        builder =
                new ClientCredentialsModuleBuilder(new HttpClientOptions().setDefaultPort(port)
                                                                          .setDefaultHost("0.0.0.0"),
                                                   "my-httpclient",
                                                   new AccessTokenRequest("client_id",
                                                                          "client_secret"
                                                   ));

        httpClient = builder.createModule();

        vertxRef = new VertxRef(vertx);

        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
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

        List<HttpRespStub> httpRespStubs =
                List.of(when(REQ_LET.apply(3))
                                .setBodyResp(HttpBodyRespStub.cons(JsObj.of("token_found",
                                                                            FALSE
                                                                           )
                                                                  )
                                            )
                                .setStatusCodeResp(HttpStatusCodeRespStub._401)
                                .setHeadersResp(HttpHeadersRespStub.JSON_CONTENT_TYPE),
                        when(FORTH_REQ)
                                .setBodyResp(HttpBodyRespStub.cons(JsObj.of("token_found",
                                                                            TRUE,
                                                                            "access_token",
                                                                            JsStr.of("foooo")
                                                                           )
                                                                  )
                                            )
                                .setStatusCodeResp(HttpStatusCodeRespStub._200)
                                .setHeadersResp(HttpHeadersRespStub.JSON_CONTENT_TYPE),
                        when(REQ_GT.apply(4))
                                .setBodyResp(HttpBodyRespStub.cons(JsObj.of("name",
                                                                            JsStr.of("Rafael")
                                                                           ))
                                            )
                                .setStatusCodeResp(HttpStatusCodeRespStub._200)
                                .setHeadersResp(HttpHeadersRespStub.JSON_CONTENT_TYPE));

        VIO<JsObj> getReq = httpClient.getOauth.apply(new GetReq().uri("/name"));

        new HttpServerBuilder(vertx,
                              new HttpReqHandlerStub(httpRespStubs

                              )
        ).create(port)
         .get()
         .onSuccess(server -> {
             Verifiers.<JsObj>verifySuccess(resp -> HttpResp.STATUS_CODE_LENS.get.apply(resp) == 200)
                      .accept(getReq,
                              context
                             );
         });

    }

    @Test
    public void test_get_success_after_three_retries(Vertx vertx,
                                                     VertxTestContext context
                                                    ) {


        List<HttpRespStub> httpRespStubs =
                List.of(when(REQ_LET.apply(3))
                                .setBodyResp(HttpBodyRespStub.cons(JsObj.of("token_found",
                                                                            FALSE
                                                                           )
                                                                  )
                                            )
                                .setStatusCodeResp(HttpStatusCodeRespStub._401),
                        when(FORTH_REQ)
                                .setBodyResp(HttpBodyRespStub.cons(JsObj.of("token_found",
                                                                            TRUE,
                                                                            "access_token",
                                                                            JsStr.of("foooo")
                                                                           )
                                                                  )
                                            )
                                .setStatusCodeResp(HttpStatusCodeRespStub._200),
                        when(REQ_LET.apply(7))
                                .setBodyResp(c -> body -> req -> {
                                                 req.response().close();
                                                 return "{}";
                                             }
                                            )
                                .setStatusCodeResp(HttpStatusCodeRespStub._500),
                        when(REQ_GT.apply(7))
                                .setBodyResp(HttpBodyRespStub.cons(JsObj.of("name",
                                                                            JsStr.of("Rafael")
                                                                           ))
                                            )
                                .setStatusCodeResp(HttpStatusCodeRespStub._200));

        VIO<JsObj> getReq = httpClient.getOauth.apply(new GetReq().uri("/name"))
                                               .retry(RetryPolicies.limitRetries(3));

        new HttpServerBuilder(vertx,
                              new HttpReqHandlerStub(httpRespStubs)
        ).create(port)
         .get()
         .onSuccess(server -> {
             Verifiers.<JsObj>verifySuccess(resp -> HttpResp.STATUS_CODE_LENS.get.apply(resp) == 200)
                      .accept(getReq,
                              context
                             );
         });


    }

}