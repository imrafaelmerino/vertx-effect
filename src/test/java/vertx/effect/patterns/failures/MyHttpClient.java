package vertx.effect.patterns.failures;

import io.vertx.core.http.HttpClientOptions;
import vertx.effect.httpclient.HttpClientModule;

public class MyHttpClient extends HttpClientModule {

    public MyHttpClient() {
        super(new HttpClientOptions(),
              "my-http-client-module"
             );
    }

}
