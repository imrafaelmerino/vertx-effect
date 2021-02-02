package vertx.effect.httpclient.oauth;

import io.vavr.Tuple2;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpClientOptions;
import jsonvalues.JsObj;
import jsonvalues.JsPath;
import vertx.effect.Val;
import vertx.effect.core.OauthBuilder;
import vertx.effect.exp.Cons;
import vertx.effect.httpclient.HttpClientModule;
import vertx.effect.httpclient.HttpResp;
import vertx.effect.λ;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static vertx.effect.httpclient.oauth.OauthFailures.GET_ACCESS_TOKEN_NOT_FOUND_EXCEPTION;
import static vertx.effect.httpclient.oauth.OauthFailures.GET_REFRESH_TOKEN_NOT_FOUND_EXCEPTION;


public class AuthorizationCodeFlowBuilder extends OauthBuilder<AuthorizationCodeFlowBuilder> {

    public AuthorizationCodeFlowBuilder(final HttpClientOptions options,
                                        final String address,
                                        final Function<String, BiFunction<MultiMap, HttpClientModule, Val<JsObj>>> refreshAccessTokenReq) {
        super(options,
              address
             );
        this.accessTokenReq = requireNonNull(refreshAccessTokenReq);
    }

    public AuthorizationCodeFlowBuilder setReadTokensAfterAuthentication(final λ<JsObj, Tuple2<String, String>> readTokensAfterAuthentication) {
        this.readTokensAfterAuthentication = Objects.requireNonNull(readTokensAfterAuthentication);
        return this;
    }

    public AuthorizationCodeModule createFromRefreshToken(final String refreshToken) {
        return new AuthorizationCodeModule(options,
                                           address,
                                           accessTokenReq,
                                           authorizationHeaderName,
                                           authorizationHeaderValue,
                                           readNewAccessTokenAfterRefresh,
                                           refreshTokenPredicate,
                                           accessTokenRetryPolicy,
                                           reqRetryPolicy,
                                           refreshToken
        );
    }

    /**
     @param authenticateReq a function that makes the authenticate request to get the access token and the refresh token.
     It takes two arguments, the HttpClientModule is the module that is being defined and will perform the authenticate
     request and a JsObj with all the required info to do the authentication ( in most cases a code and a redirect_uri )
     that will be passed in when calling {@link AuthorizationCodeModule#authenticate(JsObj) }.
     @return an AuthorizationCodeModule instance
     @see Spotify#authenticateReq(String, String)
     */
    public AuthorizationCodeModule createFromAuthReq(final BiFunction<HttpClientModule, JsObj, Val<JsObj>> authenticateReq) {

        return new AuthorizationCodeModule(options,
                                           address,
                                           authenticateReq,
                                           accessTokenReq,
                                           readTokensAfterAuthentication,
                                           authorizationHeaderName,
                                           authorizationHeaderValue,
                                           readNewAccessTokenAfterRefresh,
                                           refreshTokenPredicate,
                                           accessTokenRetryPolicy,
                                           reqRetryPolicy
        );
    }


    private final Function<String, BiFunction<MultiMap, HttpClientModule, Val<JsObj>>> accessTokenReq;
    private static final JsPath REFRESH_TOKEN = JsPath.empty()
                                                      .key("body")
                                                      .key("refresh_token");
    private static final JsPath ACCESS_TOKEN = JsPath.empty()
                                                     .key("body")
                                                     .key("access_token");

    private λ<JsObj, Tuple2<String, String>> readTokensAfterAuthentication =
            resp -> {
                try {
                    JsObj  jsonResp    = HttpResp.mapBody2Json.apply(resp);
                    String accessToken = jsonResp.getStr(ACCESS_TOKEN);
                    if (accessToken == null || accessToken.isEmpty())
                        return Cons.failure(GET_ACCESS_TOKEN_NOT_FOUND_EXCEPTION.apply(resp));
                    String refreshToken = jsonResp.getStr(REFRESH_TOKEN);
                    if (refreshToken == null || refreshToken.isEmpty())
                        return Cons.failure(GET_REFRESH_TOKEN_NOT_FOUND_EXCEPTION.apply(resp));
                    return Cons.success(new Tuple2<>(accessToken,
                                                     refreshToken
                                        )
                                       );
                } catch (Exception e) {
                    return Cons.failure(GET_ACCESS_TOKEN_NOT_FOUND_EXCEPTION.apply(resp));
                }
            };


}