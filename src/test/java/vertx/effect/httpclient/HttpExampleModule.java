package vertx.effect.httpclient;

import io.vertx.core.http.HttpClientOptions;

public class HttpExampleModule extends HttpClientModule {


    public HttpExampleModule(final HttpClientOptions options) {
        super(options,
              "myhttp-client"
             );
    }


}
