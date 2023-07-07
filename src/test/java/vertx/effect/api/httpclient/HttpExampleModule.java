package vertx.effect.api.httpclient;

import io.vertx.core.http.HttpClientOptions;
import vertx.effect.http.client.HttpClientModule;

public class HttpExampleModule extends HttpClientModule {


    public HttpExampleModule(final HttpClientOptions options) {
        super(options,
              "myhttp-client"
             );
    }


}
