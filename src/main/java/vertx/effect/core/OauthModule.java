package vertx.effect.core;

import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpClientOptions;
import jsonvalues.JsObj;
import vertx.effect.Val;
import vertx.effect.exp.And;
import vertx.effect.exp.Cons;
import vertx.effect.exp.IfElse;
import vertx.effect.httpclient.*;
import vertx.effect.λ;
import vertx.effect.λc;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class OauthModule extends HttpClientModule {

    protected String accessToken;

    protected BiFunction<MultiMap, HttpClientModule, Val<JsObj>> accessTokenReq;
    protected final λ<JsObj, String> readNewAccessTokenAfterRefresh;
    protected final int accessTokenReqAttempts;
    protected final String authorizationHeaderName;
    protected final Function<String, String> authorizationHeaderValue;

    protected final Predicate<JsObj> refreshTokenPredicate;
    protected final Predicate<Throwable> retryAccessTokenReqPredicate;
    protected final Predicate<Throwable> retryReqPredicate;

    public OauthModule(final HttpClientOptions options,
                       final String address,
                       final String authorizationHeaderName,
                       final Function<String, String> authorizationHeaderValue,
                       final λ<JsObj, String> readNewAccessTokenAfterRefresh,
                       final Predicate<JsObj> refreshTokenPredicate,
                       final Predicate<Throwable> retryAccessTokenReqPredicate,
                       final Predicate<Throwable> retryReqPredicate,
                       final int accessTokenReqAttempts,
                       final int reqAttempts
                      ) {

        super(options,
              address
             );

        this.readNewAccessTokenAfterRefresh = readNewAccessTokenAfterRefresh;
        this.authorizationHeaderName = authorizationHeaderName;
        this.authorizationHeaderValue = authorizationHeaderValue;
        this.refreshTokenPredicate = refreshTokenPredicate;
        this.retryAccessTokenReqPredicate = retryAccessTokenReqPredicate;
        this.retryReqPredicate = retryReqPredicate;
        this.accessTokenReqAttempts = accessTokenReqAttempts;
        this.getOauth = oauth((context, reqParams) -> httpClient.apply(reqParams.createHttpReq()),
                              reqAttempts,
                              false
                             );

        this.postOauth = oauth((context, reqParams) -> httpClient.apply(reqParams.createHttpReq()),
                               reqAttempts,
                               false
                              );

        this.putOauth = oauth((context, reqParams) -> httpClient.apply(reqParams.createHttpReq()),
                              reqAttempts,
                              false
                             );

        this.deleteOauth = oauth((context, reqParams) -> httpClient.apply(reqParams.createHttpReq()),
                                 reqAttempts,
                                 false
                                );

        this.patchOauth = oauth((context, reqParams) -> httpClient.apply(reqParams.createHttpReq()),
                                reqAttempts,
                                false
                               );

        this.headOauth = oauth((context, reqParams) -> httpClient.apply(reqParams.createHttpReq()),
                               reqAttempts,
                               false
                              );

        this.connectOauth = oauth((context, reqParams) -> httpClient.apply(reqParams.createHttpReq()),
                                  reqAttempts,
                                  false
                                 );

        this.optionsOauth = oauth((context, reqParams) -> httpClient.apply(reqParams.createHttpReq()),
                                  reqAttempts,
                                  false
                                 );

        this.traceOauth = oauth((context, reqParams) -> httpClient.apply(reqParams.createHttpReq()),
                                reqAttempts,
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
                                                               final int reqAttempts
                                                              ) {
        return (context, reqParams) -> req.apply(context,
                                                 reqParams
                                                )
                                          .recoverWith(e ->
                                                               IfElse.<JsObj>predicate(And.of(retryReqPredicate.test(e),
                                                                                              reqAttempts > 0
                                                                                             )
                                                                                      )
                                                                       .consequence(this.resilientReq(req,
                                                                                                      reqAttempts - 1
                                                                                                     )
                                                                                        .apply(reqParams)
                                                                                   )
                                                                       .alternative(Cons.failure(e))
                                                      )
                                          .flatMap(resp ->
                                                           IfElse.<JsObj>predicate(Cons.success(refreshTokenPredicate.test(resp)))
                                                                   .consequence(this.oauth(req,
                                                                                           reqAttempts - 1,
                                                                                           true
                                                                                          )
                                                                                    .apply(reqParams)
                                                                               )
                                                                   .alternative(Cons.success(resp))


                                                  );


    }

    protected <I extends HttpReq<I>> λc<I, JsObj> oauth(final λc<I, JsObj> req,
                                                        final int reqAttempts,
                                                        final boolean refreshToken
                                                       ) {
        return (context, reqParams) ->
                IfElse.<String>predicate(Cons.success(refreshToken || accessToken == null))
                        .consequence(
                                accessTokenReq.apply(context,
                                                     this
                                                    )
                                              .flatMap(readNewAccessTokenAfterRefresh)
                                              .retryIf(retryAccessTokenReqPredicate,
                                                       accessTokenReqAttempts
                                                      )
                                              .onSuccess(newToken -> this.accessToken = newToken)
                                    )
                        .alternative(Cons.success(accessToken))
                        .flatMap(token -> resilientReq(req,
                                                       reqAttempts
                                                      ).apply(reqParams.header(authorizationHeaderName,
                                                                               authorizationHeaderValue.apply(token)
                                                                              )
                                                             )
                                );
    }


}
