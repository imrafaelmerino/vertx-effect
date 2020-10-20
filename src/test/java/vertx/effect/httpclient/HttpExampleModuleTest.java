package vertx.effect.httpclient;

import io.vavr.Tuple2;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsObj;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.*;
import vertx.effect.exp.Pair;


@ExtendWith(VertxExtension.class)
public class HttpExampleModuleTest {

    static VertxRef vertxRef;
    static HttpExampleModule httpModule;
    static λ<String, JsObj> search;


    @BeforeAll
    public static void prepare(Vertx vertx,
                               VertxTestContext context
                              ) {
        vertxRef = new VertxRef(vertx);

        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );

        httpModule = new HttpExampleModule(new HttpClientOptions().setSsl(true)
                                                                  .setDefaultPort(443)
                                                                  .setTrustAll(true));

        search = term -> httpModule.get.apply(new GetReq().host("www.google.com")
                                                          .uri("/search?q=" + term)
                                             );

        Verifiers.<Tuple2<String, String>>verifySuccess()
                .accept(Pair.parallel(vertxRef.deploy(new RegisterJsValuesCodecs()),
                                      vertxRef.deploy(httpModule)
                                     ),
                        context
                       );

    }


    @Test
    @Disabled
    public void testTwoSearchesInGoogle(VertxTestContext context) {
        Val<JsObj> search1 = search.apply("vertx");
        Val<JsObj> search2 = search.apply("reactive");
        Pair.parallel(search1,
                      search2
                     )
            .onComplete(Verifiers.pipeTo(context))
            .get();

    }


    @Test
    @Disabled
    public void testSearchGoogle(VertxTestContext context) {
        search.apply("vertx")
              .onComplete(Verifiers.pipeTo(context))
              .get();

    }

}
