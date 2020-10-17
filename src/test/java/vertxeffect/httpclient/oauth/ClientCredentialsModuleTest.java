package vertxeffect.httpclient.oauth;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertxeffect.RegisterJsValuesCodecs;
import vertxeffect.Verifiers;
import vertxeffect.VertxRef;
import vertxeffect.Failures;
import vertxeffect.exp.Cons;
import vertxeffect.exp.Pair;
import vertxeffect.exp.Triple;
import vertxeffect.Val;
import vertxeffect.httpclient.GetReq;
import vertxeffect.httpclient.MyHttpServer;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static vertxeffect.Failures.*;
import static vertxeffect.httpclient.HttpResp.STATUS_CODE_LENS;

@ExtendWith(VertxExtension.class)
public class ClientCredentialsModuleTest {

    static ClientCredentialsModule httpClient;
    static ClientCredentialsFlowBuilder builder;
    static MyHttpServer server;

    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext context
                              ) {
        int port = 1223;
        builder = new ClientCredentialsFlowBuilder(new HttpClientOptions().setDefaultPort(port)
                                                                          .setDefaultHost("localhost"),
                                                   "my-httpclient",
                                                   new GetAccessTokenRequest("",
                                                                             ""
                                                   )
        ).setRetryReqPredicate(Failures.prism
                                       .exists
                                       .apply(exc -> Objects.equals(UNKNOWN_HOST_CODE,
                                                                    exc.failureCode()
                                                                   ) ||
                                               Objects.equals(CONNECT_TIMEOUT_CODE,
                                                              exc.failureCode()
                                                             )
                                               || Objects.equals(REQUEST_TIMEOUT_CODE,
                                                                 exc.failureCode()
                                                                )
                                             ));
        ;


        httpClient = builder.createModule();


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
    public void test_get_success_after_three_retries_getting_token(VertxTestContext context) {

        server.resetCounter();

        server.setStatusCodeRes(counter ->
                                        req -> body -> counter <= 3 ? 401 : 200);

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


        builder.setAccessTokenReqAttempts(3);

        Verifiers.<JsObj>verifySuccess(resp -> STATUS_CODE_LENS.get.apply(resp) == 200)
                .accept(httpClient.getOauth.apply(new GetReq().uri("/name")),
                        context
                       );

    }

    @Test
    public void test_get_success_after_three_retries(VertxTestContext context) {

        server.resetCounter();

        server.setStatusCodeRes(counter ->
                                        req -> body -> {
                                            if (counter <= 3) return 401;
                                            else if (counter == 4) return 200;
                                            else if (counter <= 7) throw new RuntimeException();
                                            else return 200;
                                        });

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


        Verifiers.<JsObj>verifySuccess(resp -> STATUS_CODE_LENS.get.apply(resp) == 200)
                .accept(httpClient.getOauth.apply(new GetReq().uri("/name")
                                                              .timeout(1,
                                                                       TimeUnit.SECONDS
                                                                      )),
                        context
                       );

    }

    @Test
    @Disabled
    public void test(Vertx vertx,
                     VertxTestContext context) {
        String clientId     = "";
        String clientSecret = "";

        String host = "";
        int    port = 8243;
        HttpClientOptions options = new HttpClientOptions().setDefaultHost(host)
                                                           .setDefaultPort(port)
                                                           .setSsl(true)
                                                           .setTrustAll(true);
        VertxRef vertxRef = new VertxRef(vertx);

        ClientCredentialsFlowBuilder builder =
                new ClientCredentialsFlowBuilder(options,
                                                 "http-client",
                                                 new GetAccessTokenRequest(clientId,
                                                                           clientSecret
                                                 )
                );
        ClientCredentialsModule module = builder.createModule();


        Pair.of(vertxRef.deploy(module),
                vertxRef.deploy(new RegisterJsValuesCodecs())
               )
            .onSuccess(ids -> {
                Val<JsObj> customer = module.getOauth.apply(new GetReq().uri("/uniqueregistry/crud/customer/0024403438")
                                                                        .header("accept",
                                                                                "application/json"
                                                                               )
                                                           );

                Verifiers.<JsObj>verifySuccess().accept(customer,
                                                        context
                                                       );
            })
            .get();

    }
}