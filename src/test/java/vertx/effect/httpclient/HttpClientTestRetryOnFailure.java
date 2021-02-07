package vertx.effect.httpclient;


import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsObj;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.Port;
import vertx.effect.RegisterJsValuesCodecs;
import vertx.effect.Verifiers;
import vertx.effect.VertxRef;
import vertx.effect.exp.Triple;
import vertx.effect.httpserver.HttpServerBuilder;
import vertx.effect.mock.MockHeadersResp;
import vertx.effect.mock.MockReqHandler;
import vertx.effect.mock.MockReqResp;

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
    static MockReqHandler mockReqHandler;

    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext context
                              ) {
        vertxRef = new VertxRef(vertx);
        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );
        httpClient = new HttpExampleModule(new HttpClientOptions());

        MockReqResp mockReqErrorResp =
                MockReqResp.when((n, req) -> n <= 3)
                           .setStatusCodeResp(n -> body -> req -> 500)
                           .setBodyResp(n -> body -> req -> "{}")
                           .setHeadersResp(MockHeadersResp.JSON);
        MockReqResp mockReqErrorSuccess =
                MockReqResp.when((n, req) -> n > 3)
                           .setStatusCodeResp(n -> body -> req -> 200)
                           .setBodyResp(n -> body -> req -> "{}")
                           .setHeadersResp(MockHeadersResp.JSON);

        mockReqHandler = new MockReqHandler(List.of(mockReqErrorResp,
                                                    mockReqErrorSuccess
                                                   ));
        Triple.sequential(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                          new HttpServerBuilder(vertx,
                                                mockReqHandler
                          ).startAtRandom(PORT,
                                          PORT + 100
                                         ),
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
                                      .retryOnFailure(resp -> HttpResp.STATUS_CODE_LENS.get.apply(resp) == 500,
                                                      limitRetries(3)
                                                     ),
                        context
                       );

    }


    @Test
    public void test_retries_constant_delay(VertxTestContext context) {
        mockReqHandler.resetCounter();
        long tic = Instant.now()
                          .toEpochMilli();
        Verifiers.<JsObj>verifySuccess(resp -> {
            long elapsed = Instant.now()
                                  .toEpochMilli() - tic;
            Integer status = HttpResp.STATUS_CODE_LENS.get.apply(resp);
            System.out.println(elapsed);
            return status == 200 && elapsed >= 300;

        })
                .accept(httpClient.get.apply(new GetReq().port(PORT)
                                                         .uri("example")
                                            )
                                      .retryOnFailure(resp -> HttpResp.STATUS_CODE_LENS.get.apply(resp) == 500,
                                                      limitRetries(3)
                                                              .append(constantDelay(vertxRef.sleep(Duration.ofMillis(100))))
                                                     ),
                        context
                       );

    }


}
