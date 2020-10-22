package vertx.effect.patterns.failures;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.eventbus.ReplyFailure;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsObj;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.Failures;
import vertx.effect.RegisterJsValuesCodecs;
import vertx.effect.VertxRef;
import vertx.effect.exp.Cons;
import vertx.effect.exp.Quadruple;
import vertx.effect.httpclient.GetReq;
import vertx.effect.httpclient.MyHttpServer;

import java.util.concurrent.TimeUnit;

import static vertx.effect.Failures.*;

@ExtendWith(VertxExtension.class)
public class TestFailures {


    final static MyHttpClient client = new MyHttpClient();
    final static int port = 9765;

    @BeforeAll
    public static void prepare(VertxTestContext context,
                               Vertx vertx) {

        VertxRef ref = new VertxRef(vertx);

        ref.registerConsumer(VertxRef.EVENTS_ADDRESS,
                             System.out::println
                            );

        MyHttpServer server = new MyHttpServer(vertx,
                                               port,
                                               counter -> req -> body -> {
                                                   if (counter == 1) {
                                                       try {
                                                           Thread.sleep(1000);
                                                       } catch (InterruptedException e) {
                                                           e.printStackTrace();
                                                       }
                                                       return new JsObj();
                                                   }
                                                   else {
                                                       return new JsObj();
                                                   }
                                               },
                                               counter -> req -> body -> 200
        );

        Quadruple.sequential(ref.deploy(new RegisterJsValuesCodecs()),
                             ref.deploy(new Module()),
                             ref.deploy(client),
                             Cons.of(server::start)
                            )
                 .onComplete(result -> context.completeNow())
                 .get();

    }


    @Test
    public void test_http_unkown_host(VertxTestContext context) {

        client.get.apply(new GetReq().host("abcd")
                                     .port(port)
                                     .timeout(900,
                                              TimeUnit.MILLISECONDS
                                             )
                                     .uri("/hi")
                        )
                  .retryIf(or(UNKNOWN_HOST_PRISM), 1)
                  .onComplete(it -> {
                      if (it.succeeded()) context.failNow(new RuntimeException("abcd is really a host!"));
                      else{
                          context.verify(()-> {
                              Assertions.assertTrue(UNKNOWN_HOST_PRISM.getOptional.apply(it.cause()).isPresent());
                              context.completeNow();
                          });
                      }
                  })
                  .get();

    }

    @Test
    public void test_http_req_timeout(VertxTestContext context,
                                      Vertx vertx) {

        client.get.apply(new GetReq().host("localhost")
                                     .port(port)
                                     .timeout(900,
                                              TimeUnit.MILLISECONDS
                                             )
                                     .uri("/hi")
                        )
                  .retryIf(Failures.or(HTTP_REQUEST_TIMEOUT_PRISM,HTTP_CONNECT_TIMEOUT_PRISM),1)
                  .onComplete(it -> {
                      if (it.succeeded()) context.completeNow();
                      else context.failNow(it.cause());
                  })
                  .get();

    }

    @Test
    public void test_verticle_timeout(VertxTestContext context,
                                      Vertx vertx) {

        Module.sum100.apply(100)
                     .apply(10)
                     .retryIf(it -> VERTICLE_TIMEOUT_PRISM
                                      .getOptional.apply(it)
                                                  .isPresent(),
                              2
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
