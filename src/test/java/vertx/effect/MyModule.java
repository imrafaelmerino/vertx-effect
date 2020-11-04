package vertx.effect;

import io.vertx.core.http.HttpClientOptions;
import jsonvalues.JsObj;
import vertx.effect.exp.Cons;
import vertx.effect.exp.Pair;
import vertx.effect.httpclient.GetReq;
import vertx.effect.httpclient.HttpClientModule;

import java.util.function.BiFunction;

import static java.util.concurrent.TimeUnit.SECONDS;
import static vertx.effect.Failures.*;

public class MyModule extends VertxModule {


    public class MyHttpModule extends HttpClientModule {

        public MyHttpModule(final HttpClientOptions options) {
            super(options,
                  "myhttp-client-address"
                 );
        }

    }

    @Override
    protected void deploy() {

        HttpClientOptions options = new HttpClientOptions().setDefaultHost("www.google.com")
                                                           .setDefaultPort(80);
        MyHttpModule httpModule = new MyHttpModule(options);

        Pair.sequential(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                        vertxRef.deployVerticle(httpModule)
                       )
            .get();

        BiFunction<Integer, String, Val<JsObj>> search =
                (attempts, term) ->
                        httpModule.get.apply(new GetReq().uri("/search?q=" + term))
                                      .retryIf(Failures.or(HTTP_CONNECT_TIMEOUT_PRISM,
                                                           HTTP_REQUEST_TIMEOUT_PRISM,
                                                           TCP_CONNECTION_CLOSED_PRISM
                                                          ),
                                               attempts,
                                               (error, remainingAttempt) ->
                                                       vertxRef.timer(attempts - remainingAttempt + 1,
                                                                      SECONDS
                                                                     )
                                              )
                                      .recoverWith(e -> Cons.success(JsObj.EMPTY));

        search.apply(3,
                     "vertx"
                    )
              .get();

    }
}
