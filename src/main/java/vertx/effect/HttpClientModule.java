package vertx.effect;

import io.vertx.core.http.HttpClientOptions;
import jsonvalues.JsObj;


/**
 * Module that exposes a set of functions to send different requests to a server.
 * It's created from a {@link HttpClientOptions} instance.
 * It's just another verticle that needs to be deployed. You can create as many as you want,
 * with different configurations:
 * <pre>
 * {@code
 * HttpClientOptions server1Options = new HttpClientOptions();
 * HttpClientOptions server2Options = new HttpClientOptions();
 *
 * HttpClientModule  httpServer1Module = new HttpClientModule(server1Options,address1);
 * HttpClientModule  httpServer2Module = new HttpClientModule(server2Options,address2);
 *
 * vertx.deployVerticle(httpServer1Module);
 * vertx.deployVerticle(httpServer2Module);
 * }
 * </pre>
 * Once deployed, you can use the defined functions {@link HttpClientModule#get get}, {@link HttpClientModule#post post},
 * {@link HttpClientModule#put put}, {@link HttpClientModule#delete delete} and so on.
 */
public class HttpClientModule extends AbstractHttpClientModule {

    public HttpClientModule(final HttpClientOptions options,
                            final String address
                           ) {
        super(options, address);
    }


    /**
     * represents a GET request. It takes as input a {@link GetReq} instance and returns a response in a JsObj.
     * The class {@link HttpResp} contains all the lenses and functions to get info from the response
     * and manipulate it
     */
    public final Lambdac<GetReq, JsObj> get =
            (context, builder) -> httpClient.apply(context,
                                                   builder.createHttpReq()
                                                  );

    /**
     * represents a POST request. It takes as input a {@link PostReq} instance and returns a response in a JsObj.
     * The class {@link HttpResp} contains all the lenses and functions to get info from the response
     * and manipulate it
     */
    public final Lambdac<PostReq, JsObj> post =
            (context, builder) -> httpClient.apply(context,
                                                   builder.createHttpReq()
                                                  );

    /**
     * represents a PUT request. It takes as input a {@link PutReq} instance and returns a response in a JsObj.
     * The class {@link HttpResp} contains all the lenses and functions to get info from the response
     * and manipulate it
     */
    public final Lambdac<PutReq, JsObj> put =
            (context, builder) -> httpClient.apply(context,
                                                   builder.createHttpReq()
                                                  );

    /**
     * represents a DELETE request. It takes as input a {@link DeleteReq} instance and returns a response in a JsObj.
     * The class {@link HttpResp} contains all the lenses and functions to get info from the response
     * and manipulate it
     */
    public final Lambdac<DeleteReq, JsObj> delete =
            (context, builder) -> httpClient.apply(context,
                                                   builder.createHttpReq()
                                                  );

    /**
     * represents a HEAD request. It takes as input a {@link HeadReq} instance and returns a response in a JsObj.
     * The class {@link HttpResp} contains all the lenses and functions to get info from the response
     * and manipulate it
     */
    public final Lambdac<HeadReq, JsObj> head =
            (context, builder) -> httpClient.apply(context,
                                                   builder.createHttpReq()
                                                  );

    /**
     * represents a OPTIONS request. It takes as input a {@link OptionsReq} instance and returns a response in a JsObj.
     * The class {@link HttpResp} contains all the lenses and functions to get info from the response
     * and manipulate it
     */
    public final Lambdac<OptionsReq, JsObj> options =
            (context, builder) -> httpClient.apply(context,
                                                   builder.createHttpReq()
                                                  );

    /**
     * represents a PATCH request. It takes as input a {@link PatchReq} instance and returns a response in a JsObj.
     * The class {@link HttpResp} contains all the lenses and functions to get info from the response
     * and manipulate it
     */
    public final Lambdac<PatchReq, JsObj> patch =
            (context, builder) -> httpClient.apply(context,
                                                   builder.createHttpReq()
                                                  );

    /**
     * represents a TRACE request. It takes as input a {@link TraceReq} instance and returns a response in a JsObj.
     * The class {@link HttpResp} contains all the lenses and functions to get info from the response
     * and manipulate it
     */
    public final Lambdac<TraceReq, JsObj> trace =
            (context, builder) -> httpClient.apply(context,
                                                   builder.createHttpReq()
                                                  );

}
