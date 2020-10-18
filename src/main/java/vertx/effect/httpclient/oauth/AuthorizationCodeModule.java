package vertx.effect.httpclient.oauth;

import io.vavr.Tuple2;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpClientOptions;
import jsonvalues.JsObj;
import vertx.effect.core.OauthModule;
import vertx.effect.Val;
import vertx.effect.λ;
import vertx.effect.httpclient.HttpClientModule;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class AuthorizationCodeModule extends OauthModule {

    protected String refreshToken;


    protected BiFunction<HttpClientModule, JsObj, Val<JsObj>> authenticateReq;
    protected λ<JsObj, Tuple2<String, String>> readTokensAfterAuthentication;

    AuthorizationCodeModule(final HttpClientOptions options,
                            final String address,
                            final Function<String, BiFunction<MultiMap, HttpClientModule, Val<JsObj>>> accessTokenReq,
                            final String authorizationHeaderName,
                            final Function<String, String> authorizationHeaderValue,
                            final λ<JsObj, String> readNewAccessTokenAfterRefresh,
                            final Predicate<JsObj> refreshTokenPredicate,
                            final Predicate<Throwable> retryAccessTokenReqPredicate,
                            final Predicate<Throwable> retryReqPredicate,
                            final int accessTokenReqAttempts,
                            final int reqAttempts,
                            final String refreshToken
                           ) {

        super(options,
              address,
              authorizationHeaderName,
              authorizationHeaderValue,
              readNewAccessTokenAfterRefresh,
              refreshTokenPredicate,
              retryAccessTokenReqPredicate,
              retryReqPredicate,
              accessTokenReqAttempts,
              reqAttempts
             );
        this.accessTokenReq = accessTokenReq.apply(refreshToken);
        this.refreshToken = refreshToken;
    }


    AuthorizationCodeModule(final HttpClientOptions options,
                            final String address,
                            final BiFunction<HttpClientModule, JsObj, Val<JsObj>> authenticateReq,
                            final Function<String, BiFunction<MultiMap, HttpClientModule, Val<JsObj>>> accessTokenReq,
                            final λ<JsObj, Tuple2<String, String>> readTokensAfterAuthentication,
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
              address,
              authorizationHeaderName,
              authorizationHeaderValue,
              readNewAccessTokenAfterRefresh,
              refreshTokenPredicate,
              retryAccessTokenReqPredicate,
              retryReqPredicate,
              accessTokenReqAttempts,
              reqAttempts
             );
        this.accessTokenReq = accessTokenReq.apply(refreshToken);
        this.authenticateReq = authenticateReq;
        this.readTokensAfterAuthentication = readTokensAfterAuthentication;

    }


    public Val<Tuple2<String, String>> authenticate(JsObj input) {
        return authenticateReq.apply(this,
                                     input
                                    )
                              .flatMap(readTokensAfterAuthentication)
                              .onSuccess(tokens -> {
                                  this.accessToken = tokens._1;
                                  this.refreshToken = tokens._2;
                              });
    }


}
