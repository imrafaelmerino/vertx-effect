package vertxeffect.httpclient;

import io.vertx.core.http.HttpClientOptions;
import vertxeffect.httpclient.HttpClientModule;

public class HttpExampleModule extends HttpClientModule {


    public HttpExampleModule(final HttpClientOptions options) {
        super(options,
              "myhttp-client"
             );
    }


}
