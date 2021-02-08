package vertx.effect.core;

import io.vertx.core.http.HttpClientOptions;
import jsonvalues.JsObj;
import jsonvalues.JsPath;
import vertx.effect.*;
import vertx.effect.httpclient.HttpResp;

import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static vertx.effect.httpclient.oauth.OauthFailures.GET_ACCESS_TOKEN_NOT_FOUND_EXCEPTION;

@SuppressWarnings("unchecked")
public abstract class OauthBuilder<T extends OauthBuilder<T>> {

    protected final HttpClientOptions options;
    protected final String address;

    protected String authorizationHeaderName = "Authorization";
    protected Function<String, String> authorizationHeaderValue =
            token -> String.format("Bearer %s",
                                   token
                                  );

    protected λ<JsObj, String> readNewAccessTokenAfterRefresh =
            resp -> {
                try {
                    String token = HttpResp.mapBody2Json
                            .apply(resp)
                            .getStr(JsPath.empty()
                                          .key("body")
                                          .key("access_token"));
                    if (token == null || token.isEmpty())
                        return Val.fail(GET_ACCESS_TOKEN_NOT_FOUND_EXCEPTION.apply(resp));
                    return Val.succeed(token);
                } catch (Exception e) {
                    return Val.fail(GET_ACCESS_TOKEN_NOT_FOUND_EXCEPTION.apply(resp));
                }
            };
    protected Predicate<JsObj> refreshTokenPredicate = resp -> {
        Integer statusCode = HttpResp.STATUS_CODE_LENS.get.apply(resp);
        return 401 == statusCode;
    };



    protected RetryPolicy accessTokenRetryPolicy = RetryPolicies.limitRetries(3);


    public OauthBuilder(final HttpClientOptions options,
                        final String address) {
        this.options = requireNonNull(options);
        this.address = address;
    }


    public T authorizationHeaderName(final String authorizationHeaderName) {
        this.authorizationHeaderName = requireNonNull(authorizationHeaderName);
        return ((T) this);
    }

    public T authorizationHeaderValue(final Function<String, String> authorizationHeaderValue) {
        this.authorizationHeaderValue = requireNonNull(authorizationHeaderValue);
        return ((T) this);
    }

    public T refreshTokenPredicate(final Predicate<JsObj> refreshTokenPredicate) {
        this.refreshTokenPredicate = requireNonNull(refreshTokenPredicate);
        return ((T) this);
    }


    public T readAccessTokenAfterRefresh(final λ<JsObj, String> readNewAccessTokenAfterRefresh) {
        this.readNewAccessTokenAfterRefresh = requireNonNull(readNewAccessTokenAfterRefresh);
        return (T) this;
    }



    public T accessTokenReqRetryPolicy(final RetryPolicy retryPolicy) {
        this.accessTokenRetryPolicy = requireNonNull(retryPolicy);
        return ((T) this);
    }


}
