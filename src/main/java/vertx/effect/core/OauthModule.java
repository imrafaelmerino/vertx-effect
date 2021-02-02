package vertx.effect.core;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpClientOptions;
import jsonvalues.JsObj;
import vertx.effect.RetryPolicy;
import vertx.effect.Val;
import vertx.effect.exp.Cons;
import vertx.effect.exp.IfElse;
import vertx.effect.httpclient.*;
import vertx.effect.λ;
import vertx.effect.λc;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class OauthModule extends HttpClientModule {

    private final RetryPolicy accessTokenReqRetryPolicy;
    protected String accessToken;

    protected BiFunction<MultiMap, HttpClientModule, Val<JsObj>> accessTokenReq;
    protected final λ<JsObj, String> readNewAccessTokenAfterRefresh;
    protected final String authorizationHeaderName;
    protected final Function<String, String> authorizationHeaderValue;
    protected final Predicate<JsObj> refreshTokenPredicate;

    public OauthModule(final HttpClientOptions options,
                       final String address,
                       final String authorizationHeaderName,
                       final Function<String, String> authorizationHeaderValue,
                       final λ<JsObj, String> readNewAccessTokenAfterRefresh,
                       final Predicate<JsObj> refreshTokenPredicate,
                       final RetryPolicy accessTokenReqRetryPolicy,
                       final RetryPolicy reqRetryPolicy
                       ) {

        super(options,
              address
             );

        this.readNewAccessTokenAfterRefresh = readNewAccessTokenAfterRefresh;
        this.authorizationHeaderName = authorizationHeaderName;
        this.authorizationHeaderValue = authorizationHeaderValue;
        this.refreshTokenPredicate = refreshTokenPredicate;
        this.accessTokenReqRetryPolicy = accessTokenReqRetryPolicy;
        this.getOauth = oauth((context, reqParams) -> httpClient.apply(reqParams.createHttpReq()),
                              reqRetryPolicy.get(),
                              false
                             );

        this.postOauth = oauth((context, reqParams) -> httpClient.apply(reqParams.createHttpReq()),
                               reqRetryPolicy.get(),
                               false
                              );

        this.putOauth = oauth((context, reqParams) -> httpClient.apply(reqParams.createHttpReq()),
                              reqRetryPolicy.get(),
                              false
                             );

        this.deleteOauth = oauth((context, reqParams) -> httpClient.apply(reqParams.createHttpReq()),
                                 reqRetryPolicy.get(),
                                 false
                                );

        this.patchOauth = oauth((context, reqParams) -> httpClient.apply(reqParams.createHttpReq()),
                                reqRetryPolicy.get(),
                                false
                               );

        this.headOauth = oauth((context, reqParams) -> httpClient.apply(reqParams.createHttpReq()),
                               reqRetryPolicy.get(),
                               false
                              );

        this.connectOauth = oauth((context, reqParams) -> httpClient.apply(reqParams.createHttpReq()),
                                  reqRetryPolicy.get(),
                                  false
                                 );

        this.optionsOauth = oauth((context, reqParams) -> httpClient.apply(reqParams.createHttpReq()),
                                  reqRetryPolicy.get(),
                                  false
                                 );

        this.traceOauth = oauth((context, reqParams) -> httpClient.apply(reqParams.createHttpReq()),
                                reqRetryPolicy.get(),
                                false
                               );
    }


    public final λc<GetReq, JsObj> getOauth;
    public final λc<PostReq, JsObj> postOauth;
    public final λc<PutReq, JsObj> putOauth;
    public final λc<DeleteReq, JsObj> deleteOauth;
    public final λc<PatchReq, JsObj> patchOauth;
    public final λc<HeadReq, JsObj> headOauth;
    public final λc<ConnectReq, JsObj> connectOauth;
    public final λc<OptionsReq, JsObj> optionsOauth;
    public final λc<TraceReq, JsObj> traceOauth;

    protected <I extends HttpReq<I>> λc<I, JsObj> resilientReq(final λc<I, JsObj> req,
                                                               final Function<Throwable, Val<Boolean>> reqPolicy
                                                              ) {

        return (context, reqParams) -> req.apply(context,
                                                 reqParams
                                                )
                                          .recoverWith(e ->
                                                               IfElse.<JsObj>predicate(reqPolicy.apply(e)
                                                                                      )
                                                                       .consequence(this.resilientReq(req,
                                                                                                      reqPolicy
                                                                                                     )
                                                                                        .apply(reqParams)
                                                                                   )
                                                                       .alternative(Cons.failure(e))
                                                      )
                                          .flatMap(resp ->
                                                           IfElse.<JsObj>predicate(refreshTokenPredicate.test(resp))
                                                                   .consequence(this.oauth(req,
                                                                                           reqPolicy,
                                                                                           true
                                                                                          )
                                                                                    .apply(reqParams)
                                                                               )
                                                                   .alternative(Cons.success(resp))


                                                  );


    }

    protected <I extends HttpReq<I>> λc<I, JsObj> oauth(final λc<I, JsObj> req,
                                                        final Function<Throwable, Val<Boolean>> reqPolicy,
                                                        final boolean refreshToken
                                                       ) {
        return (context, reqParams) ->
                //really important: Cons.of instead of Cons.success to capture the state of this.accessToken
                IfElse.<String>predicate(Cons.of(() -> Future.succeededFuture(refreshToken || accessToken == null)))
                        .consequence(
                                accessTokenReq.apply(context,
                                                     this
                                                    )
                                              .flatMap(readNewAccessTokenAfterRefresh)
                                              .retry(accessTokenReqRetryPolicy)
                                              .onSuccess(newToken -> this.accessToken = newToken)
                                    )
                        //really important: Cons.of instead of Cons.success to capture the state of this.accessToken
                        .alternative(Cons.of(() -> Future.succeededFuture(this.accessToken)))
                        .flatMap(token -> resilientReq(req,
                                                       reqPolicy
                                                      ).apply(reqParams.setHeader(authorizationHeaderName,
                                                                                  authorizationHeaderValue.apply(token)
                                                                                 )
                                                             )
                                );
    }


}
