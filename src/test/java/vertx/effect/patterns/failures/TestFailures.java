package vertx.effect.patterns.failures;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.eventbus.ReplyFailure;
import io.vertx.core.http.HttpServer;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsObj;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.*;
import vertx.effect.exp.Quadruple;
import vertx.effect.httpclient.GetReq;
import vertx.effect.httpclient.HttpResp;
import vertx.effect.httpserver.HttpServerBuilder;
import vertx.effect.mock.MockBodyResp;
import vertx.effect.mock.MockReqHandler;
import vertx.effect.mock.MockReqResp;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoUnit.SECONDS;
import static vertx.effect.Failures.*;
import static vertx.effect.mock.MockReqResp.FIRST_REQ;
import static vertx.effect.mock.MockReqResp.REQ_LT;

@ExtendWith(VertxExtension.class)
public class TestFailures {


    final static MyHttpClient client = new MyHttpClient();
    final static int port = Port.number.incrementAndGet();
    final static int portServerClosesConnection = Port.number.incrementAndGet();
    static HttpServer server;
    static HttpServerBuilder serverCloseConnection;

    @BeforeAll
    public static void prepare(VertxTestContext context,
                               Vertx vertx) {

        VertxRef ref = new VertxRef(vertx);

        ref.registerConsumer(VertxRef.EVENTS_ADDRESS,
                             System.out::println
                            );


        Quadruple.sequential(ref.deployVerticle(new RegisterJsValuesCodecs()),
                             ref.deployVerticle(new Module()),
                             ref.deployVerticle(client),
                             new HttpServerBuilder(vertx,
                                                   new MockReqHandler(List.of(MockReqResp.when(FIRST_REQ)
                                                                                         .setBodyResp(MockBodyResp.consAfter(Duration.of(1,
                                                                                                                                         SECONDS
                                                                                                                                        ),
                                                                                                                             JsObj.empty()
                                                                                                                            )
                                                                                                     ),
                                                                              MockReqResp.when(REQ_LT.apply(1))
                                                                                         .setBodyResp(MockBodyResp.cons(JsObj.empty()))
                                                                             )
                                                   )
                             ).start(port)
                            )
                 .onComplete(tuple -> {
                     context.completeNow();
                     server = tuple.result()._4;
                 })
                 .get();

        serverCloseConnection =
                new HttpServerBuilder(vertx,
                                      new MockReqHandler(List.of(MockReqResp.when(FIRST_REQ)
                                                                            .setBodyResp(c -> b -> req -> {
                                                                                req.response()
                                                                                   .close();
                                                                                return "{}";
                                                                            }),
                                                                 MockReqResp.when(MockReqResp.REQ_GT.apply(1))
                                                                            .setBodyResp(MockBodyResp.cons(JsObj.empty()))

                                                                )
                                      )
                );

    }

    @Test
    public void test_close_connection_fails(final Vertx vertx,
                                            final VertxTestContext context) {

        serverCloseConnection.start(portServerClosesConnection)
                             .get()
                             .onSuccess(server ->
                                                client.get.apply(new GetReq().host("localhost")
                                                                             .port(portServerClosesConnection)
                                                                             .uri("/hi")
                                                                )
                                                          .onComplete(it -> {
                                                              if (it.succeeded())
                                                                  context.failNow(new RuntimeException("the server was supposed to close the connection"));
                                                              else {
                                                                  context.verify(() -> {
                                                                      Assertions.assertTrue(Failures.anyOf(HTTP_CONNECTION_WAS_CLOSED_CODE)
                                                                                                    .test(it.cause()));
                                                                      context.completeNow();
                                                                  });
                                                              }
                                                          })
                                                          .get()
                                       );
    }


    @Test
    public void test_close_connection_success(VertxTestContext context) {
        serverCloseConnection.start(portServerClosesConnection)
                             .get()
                             .onSuccess(server -> {
                                 client.get.apply(new GetReq().host("localhost")
                                                              .port(portServerClosesConnection)
                                                              .uri("/hi")
                                                 )
                                           .retry(Failures.anyOf(HTTP_CONNECTION_WAS_CLOSED_CODE),
                                                  RetryPolicies.limitRetries(1)
                                                 )
                                           .onComplete(it -> {
                                               if (it.succeeded())
                                                   context.verify(() -> {
                                                       Assertions.assertEquals(200,
                                                                               HttpResp.STATUS_CODE_LENS.get.apply(it.result())
                                                                              );
                                                       context.completeNow();
                                                   });
                                               else {
                                                   context.failNow(it.cause());
                                               }
                                           })
                                           .get();
                             });

    }

    @Test
    public void test_http_unkown_host(VertxTestContext context) {
        serverCloseConnection.start(portServerClosesConnection)
                             .get()
                             .onSuccess(server -> {
                                 client.get.apply(new GetReq().host("abcd")
                                                              .port(portServerClosesConnection)
                                                              .timeout(900,
                                                                       TimeUnit.MILLISECONDS
                                                                      )
                                                              .uri("/hi")
                                                 )
                                           .retry(Failures.anyOf(HTTP_CONNECTION_WAS_CLOSED_CODE),
                                                  RetryPolicies.limitRetries(1)
                                                 )
                                           .onComplete(it -> {
                                               if (it.succeeded())
                                                   context.failNow(new RuntimeException("abcd is really a host!"));
                                               else {
                                                   context.verify(() -> {
                                                       Assertions.assertTrue(Failures.anyOf(HTTP_UNKNOWN_HOST_CODE)
                                                                                     .test(it.cause()));
                                                       context.completeNow();
                                                   });
                                               }
                                           })
                                           .get();
                             });


    }

    @Test
    public void test_http_req_timeout(VertxTestContext context) {

        client.get.apply(new GetReq().host("localhost")
                                     .port(port)
                                     .timeout(900,
                                              TimeUnit.MILLISECONDS
                                             )
                                     .uri("/hi")
                        )
                  .retry(Failures.anyOf(HTTP_REQUEST_TIMEOUT_CODE,
                                        HTTP_CONNECT_TIMEOUT_CODE
                                       ),
                         RetryPolicies.limitRetries(1)

                        )
                  .onComplete(it -> {
                      if (it.succeeded()) context.completeNow();
                      else context.failNow(it.cause());
                  })
                  .get();

    }

    @Test
    public void test_verticle_timeout(VertxTestContext context) {

        Module.sum100.apply(100)
                     .apply(10)
                     .retry(it -> VERTICLE_TIMEOUT_PRISM.getOptional.apply(it)
                                                                    .isPresent(),
                            RetryPolicies.limitRetries(2)
                           )
                     .onComplete(it -> context.verify(() -> {
                         Assertions.assertTrue(it.failed());
                         Assertions.assertEquals(ReplyFailure.TIMEOUT,
                                                 ((ReplyException) it.cause()).failureType()
                                                );
                         context.completeNow();
                     }))
                     .get();
    }
}
