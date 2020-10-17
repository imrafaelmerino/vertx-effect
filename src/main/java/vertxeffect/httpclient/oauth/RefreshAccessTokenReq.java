package vertxeffect.httpclient.oauth;

import io.vertx.core.MultiMap;
import jsonvalues.JsObj;
import vertxeffect.Val;
import vertxeffect.httpclient.HttpClientModule;
import vertxeffect.httpclient.PostReq;

import java.util.Base64;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 Default req to refresh the token in an Authorization code flow
 POST https://host:port/uri
 grant_type=refresh_token&refresh_token={{REFRESH_TOKEN}}
 Accept: application/json
 Authorization: Base64(ClientId:ClientSecret)
 Content-Type: application/x-www-form-urlencoded
 the default uri is /token
 the default host and port are the ones specified in the HttpClientModule where the requests that
 need to refresh the tokens are defined
 */
public class RefreshAccessTokenReq implements Function<String, BiFunction<MultiMap, HttpClientModule, Val<JsObj>>> {


    private static final String DEFAULT_URI = "/token";
    private final String clientIdSecretBase64;
    private final String uri;
    private String host;
    private Integer port;
    private Boolean ssl;

    public RefreshAccessTokenReq(final String clientId,
                                 final String clientSecret,
                                 final String host,
                                 final String uri,
                                 final int port,
                                 final boolean ssl
                                ) {
        String credentials = clientId + ":" + clientSecret;
        this.clientIdSecretBase64 = Base64.getEncoder()
                                          .encodeToString(credentials.getBytes());
        this.host = requireNonNull(host);
        this.uri = requireNonNull(uri);
        this.port = port;
        this.ssl = ssl;
    }

    public RefreshAccessTokenReq(final String clientId,
                                 final String clientSecret) {
        String credentials = clientId + ":" + clientSecret;
        this.clientIdSecretBase64 = Base64.getEncoder()
                                          .encodeToString(credentials.getBytes());
        this.uri = DEFAULT_URI;
    }

    public RefreshAccessTokenReq(final String clientId,
                                 final String clientSecret,
                                 final String host,
                                 final int port) {
        this(clientId,
             clientSecret
            );
        this.port = port;
        this.host = host;
    }

    @Override
    public BiFunction<MultiMap, HttpClientModule, Val<JsObj>> apply(final String refreshToken) {
        return (context, module) -> {
            String body = String.format("grant_type=refresh_token&refresh_token=%s",
                                        refreshToken
                                       );
            PostReq req = new PostReq(body.getBytes());
            if (host != null && !host.isEmpty()) req = req.host(host);
            if (port != null) req = req.port(port);
            if (ssl != null) req = req.ssl(ssl);
            return module.post.apply(context,
                                     req.uri(uri)
                                        .header("Accept",
                                                "application/json"
                                               )
                                        .header("Authorization",
                                                String.format("Basic %s",
                                                              clientIdSecretBase64
                                                             )
                                               )
                                        .header("Content-Type",
                                                "application/x-www-form-urlencoded"
                                               )
                                    );
        };
    }

}
