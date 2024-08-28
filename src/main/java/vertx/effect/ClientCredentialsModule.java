package vertx.effect;

import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpClientOptions;
import jsonvalues.JsObj;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class ClientCredentialsModule extends OauthModule {

    ClientCredentialsModule(final HttpClientOptions options,
                            final String address,
                            final BiFunction<MultiMap, HttpClientModule, VIO<JsObj>> accessTokenReq,
                            final String authorizationHeaderName,
                            final Function<String, String> authorizationHeaderValue,
                            final Lambda<JsObj, String> readNewAccessTokenAfterRefresh,
                            final Predicate<JsObj> refreshTokenPredicate,
                            final RetryPolicy accessTokenReqRetryPolicy,
                            final Predicate<Throwable> accessTokenReqRetryPredicate
                           ) {
        super(options,
              address,
              authorizationHeaderName,
              authorizationHeaderValue,
              readNewAccessTokenAfterRefresh,
              refreshTokenPredicate,
              accessTokenReqRetryPolicy,
              accessTokenReqRetryPredicate
             );
        this.accessTokenReq = accessTokenReq;

    }
}
