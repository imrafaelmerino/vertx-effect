package vertx.effect.httpclient.oauth;

import io.vertx.core.MultiMap;
import jsonvalues.JsObj;
import vertx.effect.Val;
import vertx.effect.httpclient.HttpClientModule;
import vertx.effect.httpclient.PostReq;

import java.util.Base64;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import static vertx.effect.httpclient.oauth.OauthFailures.GET_EMPTY_AUTHORIZATION_CODE_EXCEPTION;
import static vertx.effect.httpclient.oauth.OauthFailures.GET_EMPTY_REDIRECT_URL_CODE_EXCEPTION;

public class Spotify {

    private static final String HOST = "accounts.spotify.com";
    private static final String URI = "/api/token";
    private static final int PORT = 443;


    private Spotify() {
    }

    public static Function<String, BiFunction<MultiMap, HttpClientModule, Val<JsObj>>> refreshTokenReq(final String clientId,
                                                                                                       final String clientSecret) {
        return new RefreshAccessTokenReq(clientId,
                                         clientSecret,
                                         HOST,
                                         URI,
                                         PORT,
                                         true
        );
    }

    public static BiFunction<HttpClientModule, JsObj, Val<JsObj>> authenticateReq(final String clientId,
                                                                                  final String clientSecret) {
        Objects.requireNonNull(clientId);
        Objects.requireNonNull(clientSecret);

        return (module, inputs) -> {
            String code = inputs.getStr("code");
            if (code == null || code.isEmpty())
                return Val.fail(GET_EMPTY_AUTHORIZATION_CODE_EXCEPTION.apply("Empty code."));
            String redirectUri = inputs.getStr("redirect_uri");
            if (redirectUri == null || redirectUri.isEmpty())
                return Val.fail(GET_EMPTY_REDIRECT_URL_CODE_EXCEPTION.apply("Empty redirect_uri."));

            String credentials = String.format("%s:%s",
                                               clientId,
                                               clientSecret
                                              );
            String body = String.format("grant_type=authorization_code&code=%s&redirect_uri=%s",
                                        code,
                                        redirectUri
                                       );
            PostReq message = new PostReq(body.getBytes());
            return module.post.apply(message.uri(URI)
                                            .host(HOST)
                                            .port(PORT)
                                            .ssl(true)
                                            .setHeader("accept",
                                                       "application/json"
                                                      )
                                            .setHeader("Authorization",
                                                       String.format("Basic %s",
                                                                  Base64.getEncoder()
                                                                        .encodeToString(credentials.getBytes())
                                                                 )
                                                      )
                                            .setHeader("Content-Type",
                                                       "application/x-www-form-urlencoded"
                                                      )
                                    );

        };
    }

    static BiFunction<MultiMap, HttpClientModule, Val<JsObj>> getAccessToken(final String clientId,
                                                                             final String clientSecret) {
        return new GetAccessTokenRequest(clientId,
                                         clientSecret,
                                         HOST,
                                         URI,
                                         PORT,
                                         true);
    }
}
