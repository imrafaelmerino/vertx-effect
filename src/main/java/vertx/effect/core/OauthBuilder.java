package vertx.effect.core;

import io.vertx.core.http.HttpClientOptions;
import jsonvalues.JsObj;
import jsonvalues.JsPath;
import vertx.effect.Failures;
import vertx.effect.exp.Cons;
import vertx.effect.httpclient.HttpResp;
import vertx.effect.λ;

import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static vertx.effect.Failures.*;
import static vertx.effect.httpclient.oauth.OauthFailures.GET_ACCESS_TOKEN_NOT_FOUND_EXCEPTION;

@SuppressWarnings("unchecked")
public abstract class OauthBuilder<T extends OauthBuilder<T>> {

    protected final HttpClientOptions options;
    protected final String address;

    protected String authorizationHeaderName = "Authorization";
    protected Function<String, String> authorizationHeaderValue = token -> String.format("Bearer %s",
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
                        return Cons.failure(GET_ACCESS_TOKEN_NOT_FOUND_EXCEPTION.apply(resp));
                    return Cons.success(token);
                } catch (Exception e) {
                    return Cons.failure(GET_ACCESS_TOKEN_NOT_FOUND_EXCEPTION.apply(resp));
                }
            };
    protected Predicate<JsObj> refreshTokenPredicate = resp -> {
        Integer statusCode = HttpResp.STATUS_CODE_LENS.get.apply(resp);
        return 401 == statusCode;
    };
    protected Predicate<Throwable> retryAccessTokenPredicate = RETRY_ACCESS_TOKEN_REQ_PREDICATE;
    protected Predicate<Throwable> retryReqPredicate = RETRY_REQ_PREDICATE;
    protected int accessTokenReqAttempts = 3;
    protected int reqAttempts = 3;

    public OauthBuilder(final HttpClientOptions options,
                        final String address) {
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


    public T setReadNewAccessTokenAfterRefresh(final λ<JsObj, String> readNewAccessTokenAfterRefresh) {
        this.readNewAccessTokenAfterRefresh = requireNonNull(readNewAccessTokenAfterRefresh);
        return (T) this;
    }


    public T setRetryAccessTokenReqPredicate(final Predicate<Throwable> retryGetTokenPredicate) {
        this.retryAccessTokenPredicate = requireNonNull(retryGetTokenPredicate);
        return ((T) this);
    }

    public T setRetryReqPredicate(final Predicate<Throwable> retryReqPredicate) {
        this.retryReqPredicate = requireNonNull(retryReqPredicate);
        return ((T) this);
    }

    public T setAccessTokenReqAttempts(final int accessTokenReqAttempts) {
        if (accessTokenReqAttempts < 1) throw new IllegalArgumentException("accessTokenReqAttempts < 1");
        this.accessTokenReqAttempts = accessTokenReqAttempts;
        return ((T) this);
    }

    public T setReqAttempts(final int reqAttempts) {
        if (accessTokenReqAttempts < 1) throw new IllegalArgumentException("reqAttempts < 1");
        this.reqAttempts = reqAttempts;
        return ((T) this);
    }

    private static final Predicate<Throwable> RETRY_ACCESS_TOKEN_REQ_PREDICATE =
            Failures.anyOf(HTTP_UNKNOWN_HOST_CODE,
                           HTTP_CONNECT_TIMEOUT_CODE,
                           HTTP_ACCESS_TOKEN_NOT_FOUND_CODE
                          );


    private static final Predicate<Throwable> RETRY_REQ_PREDICATE =
            Failures.anyOf(HTTP_UNKNOWN_HOST_CODE,
                           HTTP_CONNECT_TIMEOUT_CODE
                          );

}
