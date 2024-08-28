package vertx.effect.api.httpclient;

import fun.tuple.Pair;
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
import vertx.effect.api.Verifiers;
import vertx.effect.GetReq;
import vertx.values.codecs.RegisterJsValuesCodecs;

@SuppressWarnings("ReturnValueIgnored")
@ExtendWith(VertxExtension.class)
public class HttpExampleModuleTest {

    static VertxRef vertxRef;
    static HttpClientModule httpModule;
    static Lambda<String, JsObj> search;

    @BeforeAll
    public static void prepare(Vertx vertx,
                               VertxTestContext context
                              ) {
        vertxRef = new VertxRef(vertx);

        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );

        httpModule =
                new HttpClientModule(new HttpClientOptions().setSsl(true)
                                                            .setDefaultPort(443)
                                                            .setTrustAll(true),
                                     "myhttp-client");

        search =
                term -> httpModule.get.apply(new GetReq().host("www.google.com")
                                                         .uri("/search?q=" + term)
                                            );

        Verifiers.<Pair<String, String>>verifySuccess()
                 .accept(PairExp.seq(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                                     vertxRef.deployVerticle(httpModule)
                                    ),
                         context
                        );

    }


    @Test
    @Disabled
    public void testTwoSearchesInGoogle(VertxTestContext context) {
        VIO<JsObj> search1 = search.apply("vertx");
        VIO<JsObj> search2 = search.apply("reactive");
        PairExp.par(search1,
                    search2
                   )
               .onComplete(Verifiers.pipeTo(context))
               .get().onComplete(System.out::println);
    }


    @Test
    @Disabled
    public void testSearchGoogle(VertxTestContext context) {
        search.apply("vertx")
              .onComplete(Verifiers.pipeTo(context))
              .get();

    }

}
