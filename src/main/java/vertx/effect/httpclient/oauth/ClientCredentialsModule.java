package vertx.effect.httpclient.oauth;

import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpClientOptions;
import jsonvalues.JsObj;
import vertx.effect.RetryPolicy;
import vertx.effect.core.OauthModule;
import vertx.effect.Val;
import vertx.effect.λ;
import vertx.effect.httpclient.HttpClientModule;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class ClientCredentialsModule extends OauthModule {

    ClientCredentialsModule(final HttpClientOptions options,
                            final String address,
                            final BiFunction<MultiMap,HttpClientModule, Val<JsObj>> accessTokenReq,
                            final String authorizationHeaderName,
                            final Function<String, String> authorizationHeaderValue,
                            final λ<JsObj, String> readNewAccessTokenAfterRefresh,
                            final Predicate<JsObj> refreshTokenPredicate,
                            final RetryPolicy accessTokenReqRetryPolicy,
                            final RetryPolicy reqRetryPolicy
                           ) {
        super(options,
              address,
              authorizationHeaderName,
              authorizationHeaderValue,
              readNewAccessTokenAfterRefresh,
              refreshTokenPredicate,
              accessTokenReqRetryPolicy,
              reqRetryPolicy
             );
        this.accessTokenReq = accessTokenReq;

    }
}
