package vertx.effect.api.httpclient;


import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsObj;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.*;
import vertx.effect.api.Port;
import vertx.effect.api.Verifiers;
import vertx.effect.GetReq;
import vertx.effect.HttpClientModule;
import vertx.effect.HttpResp;
import vertx.effect.HttpServerBuilder;
import vertx.effect.stub.http.HttpHeadersRespStub;
import vertx.effect.stub.http.HttpReqHandlerStub;
import vertx.effect.stub.http.HttpRespStub;
import vertx.values.codecs.RegisterJsValuesCodecs;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static vertx.effect.RetryPolicies.constantDelay;
import static vertx.effect.RetryPolicies.limitRetries;


@ExtendWith(VertxExtension.class)
public class HttpClientTestRetryOnFailure {

    private static final int PORT = Port.number.incrementAndGet();
    static HttpClientModule httpClient;
    static VertxRef vertxRef;
    static HttpReqHandlerStub httpReqHandlerStub;

    @BeforeAll
    public static void prepare(
            final Vertx vertx,
            final VertxTestContext context
                              ) {
        vertxRef = new VertxRef(vertx);
        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );
        httpClient = new HttpClientModule(new HttpClientOptions().setDefaultHost("0.0.0.0"), "myhttp-client");

        HttpRespStub mockReqErrorResp =
                HttpRespStub.when((n, req) -> n <= 3)
                            .setStatusCodeResp(n -> body -> req -> 500)
                            .setBodyResp(n -> body -> req -> "{}")
                            .setHeadersResp(HttpHeadersRespStub.JSON_CONTENT_TYPE);
        HttpRespStub mockReqErrorSuccess =
                HttpRespStub.when((n, req) -> n > 3)
                            .setStatusCodeResp(n -> body -> req -> 200)
                            .setBodyResp(n -> body -> req -> "{}")
                            .setHeadersResp(HttpHeadersRespStub.JSON_CONTENT_TYPE);

        httpReqHandlerStub = new HttpReqHandlerStub(List.of(mockReqErrorResp,
                                                            mockReqErrorSuccess
                                                           ));
        TripleExp.seq(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                      new HttpServerBuilder(vertx,
                                            httpReqHandlerStub
                      ).create(PORT),
                      vertxRef.deployVerticle(httpClient)
                     )
                 .get()
                 .onComplete(Verifiers.pipeTo(context));


    }


    @Test
    public void test_retries(VertxTestContext context) {
        Verifiers.<JsObj>verifySuccess(resp -> {
                     Integer status = HttpResp.STATUS_CODE_LENS.get.apply(resp);
                     return status == 200;
                 })
                 .accept(httpClient.get.apply(new GetReq().port(PORT)
                                                          .uri("example")
                                             )
                                       .repeat(resp -> HttpResp.STATUS_CODE_LENS.get.apply(resp) == 500,
                                               limitRetries(3)
                                              ),
                         context
                        );

    }


    @Test
    public void test_retries_constant_delay(VertxTestContext context) {
        httpReqHandlerStub.resetCounter();
        long tic = Instant.now()
                          .toEpochMilli();
        Verifiers.<JsObj>verifySuccess(resp -> {
            long elapsed = Instant.now()
                                  .toEpochMilli() - tic;
            Integer status = HttpResp.STATUS_CODE_LENS.get.apply(resp);
            System.out.println(elapsed);
            return status == 200 && elapsed >= 300;

        }).accept(httpClient.get.apply(new GetReq().port(PORT)
                                                   .uri("example")
                                      )
                                .repeat(resp -> HttpResp.STATUS_CODE_LENS.get.apply(resp) == 500,
                                        limitRetries(3)
                                                .append(constantDelay(vertxRef.delay(Duration.ofMillis(100))))
                                       ),
                  context
                 );

    }


}
