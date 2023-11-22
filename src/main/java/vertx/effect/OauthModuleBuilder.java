package vertx.effect;

import io.vertx.core.http.HttpClientOptions;
import jsonvalues.JsObj;
import jsonvalues.JsPath;

import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static vertx.effect.OauthFailures.ACCESS_TOKEN_NOT_FOUND_EXCEPTION;

@SuppressWarnings("unchecked")
 abstract class OauthModuleBuilder<T extends OauthModuleBuilder<T>> {

    protected final HttpClientOptions options;
    protected final String address;

    protected String authorizationHeaderName = "Authorization";
    protected Function<String, String> authorizationHeaderValue =
            token -> String.format("Bearer %s",
                                   token
                                  );

    protected Lambda<JsObj, String> readAccessToken =
            resp -> {
                try {
                    String token = HttpResp.mapBody2Json
                            .apply(resp)
                            .getStr(JsPath.empty()
                                          .key("body")
                                          .key("access_token"));
                    if (token == null || token.isEmpty())
                        return VIO.fail(ACCESS_TOKEN_NOT_FOUND_EXCEPTION.apply(resp));
                    return VIO.succeed(token);
                } catch (Exception e) {
                    return VIO.fail(ACCESS_TOKEN_NOT_FOUND_EXCEPTION.apply(resp));
                }
            };

    protected Predicate<JsObj> refreshTokenPredicate = resp -> {
        Integer statusCode = HttpResp.STATUS_CODE_LENS.get.apply(resp);
        return 401 == statusCode;
    };


    protected RetryPolicy accessTokenRetryPolicy = RetryPolicies.limitRetries(3);
    protected Predicate<Throwable> accessTokenRetryPredicate = e -> true;


    public OauthModuleBuilder(final HttpClientOptions options,
                              final String address
                             ) {
        this.options = requireNonNull(options);
        this.address = address;
    }


    public T setAuthorizationHeaderName(final String authorizationHeaderName) {
        this.authorizationHeaderName = requireNonNull(authorizationHeaderName);
        return ((T) this);
    }

    public T setAuthorizationHeaderValue(final Function<String, String> authorizationHeaderValue) {
        this.authorizationHeaderValue = requireNonNull(authorizationHeaderValue);
        return ((T) this);
    }

    public T setRefreshTokenPredicate(final Predicate<JsObj> refreshTokenPredicate) {
        this.refreshTokenPredicate = requireNonNull(refreshTokenPredicate);
        return ((T) this);
    }


    public T setReadAccessToken(final Lambda<JsObj, String> readNewAccessTokenAfterRefresh) {
        this.readAccessToken = requireNonNull(readNewAccessTokenAfterRefresh);
        return (T) this;
    }


    public T setAccessTokenReqRetryPolicy(final Predicate<Throwable> condition,
                                          final RetryPolicy retryPolicy
                                         ) {
        this.accessTokenRetryPolicy = requireNonNull(retryPolicy);
        this.accessTokenRetryPredicate = requireNonNull(condition);
        return ((T) this);
    }


}
