package vertx.effect;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpClientOptions;
import jsonvalues.JsObj;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

abstract class OauthModule extends HttpClientModule {

    public final Lambdac<GetReq, JsObj> getOauth;
    public final Lambdac<PostReq, JsObj> postOauth;
    public final Lambdac<PutReq, JsObj> putOauth;
    public final Lambdac<DeleteReq, JsObj> deleteOauth;
    public final Lambdac<PatchReq, JsObj> patchOauth;
    public final Lambdac<HeadReq, JsObj> headOauth;
    public final Lambdac<OptionsReq, JsObj> optionsOauth;
    public final Lambdac<TraceReq, JsObj> traceOauth;
    protected final Lambda<JsObj, String> readAccessToken;
    protected final String authorizationHeaderName;
    protected final Function<String, String> authorizationHeaderValue;
    protected final Predicate<JsObj> refreshTokenPredicate;
    private final RetryPolicy accessTokenReqRetryPolicy;
    private final Predicate<Throwable> accessTokenReqRetryPredicate;
    protected String accessToken;
    protected BiFunction<MultiMap, HttpClientModule, VIO<JsObj>> accessTokenReq;
    public OauthModule(final HttpClientOptions options,
                       final String address,
                       final String authorizationHeaderName,
                       final Function<String, String> authorizationHeaderValue,
                       final Lambda<JsObj, String> readAccessToken,
                       final Predicate<JsObj> refreshTokenPredicate,
                       final RetryPolicy accessTokenReqRetryPolicy,
                       final Predicate<Throwable> accessTokenReqRetryPredicate
                      ) {

        super(options,
              address
             );

        this.accessTokenReqRetryPredicate = accessTokenReqRetryPredicate;
        this.readAccessToken = readAccessToken;
        this.authorizationHeaderName = authorizationHeaderName;
        this.authorizationHeaderValue = authorizationHeaderValue;
        this.refreshTokenPredicate = refreshTokenPredicate;
        this.accessTokenReqRetryPolicy = accessTokenReqRetryPolicy;
        this.getOauth = oauth((context, reqParams) -> httpClient.apply(reqParams.createHttpReq()),
                              false
                             );

        this.postOauth = oauth((context, reqParams) -> httpClient.apply(reqParams.createHttpReq()),
                               false
                              );

        this.putOauth = oauth((context, reqParams) -> httpClient.apply(reqParams.createHttpReq()),
                              false
                             );

        this.deleteOauth = oauth((context, reqParams) -> httpClient.apply(reqParams.createHttpReq()),
                                 false
                                );

        this.patchOauth = oauth((context, reqParams) -> httpClient.apply(reqParams.createHttpReq()),
                                false
                               );

        this.headOauth = oauth((context, reqParams) -> httpClient.apply(reqParams.createHttpReq()),
                               false
                              );

        this.optionsOauth = oauth((context, reqParams) -> httpClient.apply(reqParams.createHttpReq()),
                                  false
                                 );

        this.traceOauth = oauth((context, reqParams) -> httpClient.apply(reqParams.createHttpReq()),
                                false
                               );
    }

    protected <I extends HttpReq<I>> Lambdac<I, JsObj> resilientReq(final Lambdac<I, JsObj> req) {

        return (context, reqParams) -> req.apply(context,
                                                 reqParams
                                                )
                                          .then(resp ->
                                                        refreshTokenPredicate.test(resp) ?
                                                                this.oauth(req, true).apply(reqParams) :
                                                                VIO.succeed(resp)
                                               );


    }

    protected <I extends HttpReq<I>> Lambdac<I, JsObj> oauth(final Lambdac<I, JsObj> req,
                                                             final boolean refreshToken
                                                            ) {
        return (context, reqParams) ->
                IfElseExp.<String>predicate( () -> refreshToken || accessToken == null)
                         .consequence(() -> accessTokenReq.apply(context,
                                                                 this
                                                                )
                                                          .then(readAccessToken)
                                                          .retry(accessTokenReqRetryPredicate,
                                                                 accessTokenReqRetryPolicy
                                                                )
                                                          .onSuccess(newToken -> this.accessToken = newToken))
                         .alternative(() -> VIO.effect(() -> Future.succeededFuture(this.accessToken)))
                         .then(token ->
                                       resilientReq(req).apply(reqParams.setHeader(authorizationHeaderName,
                                                                                   authorizationHeaderValue.apply(token)
                                                                                  )
                                                              )
                              );
    }


}
