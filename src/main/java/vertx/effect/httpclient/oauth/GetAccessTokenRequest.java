package vertx.effect.httpclient.oauth;

import io.vertx.core.MultiMap;
import jsonvalues.JsObj;
import vertx.effect.Val;
import vertx.effect.httpclient.HttpClientModule;
import vertx.effect.httpclient.PostReq;
import java.util.Base64;
import java.util.function.BiFunction;
import static java.util.Objects.requireNonNull;

/**
 Default req to get the access token in a Client Credentials Code

 POST https://host:port/uri
 grant_type=client_credentials

 Accept: application/json
 Authorization: Base64(ClientId:ClientSecret)
 Content-Type: application/x-www-form-urlencoded

 the default uri is /token
 the default host and port are the ones specified in the HttpClientModule where the requests that
 need the token are defined

 */
public class GetAccessTokenRequest implements BiFunction<MultiMap, HttpClientModule, Val<JsObj>> {

    private static final String DEFAULT_URI = "/token";
    private final String uri;
    private final String clientIdSecretBase64;
    private String host;
    private Integer port;
    private Boolean ssl;

    public GetAccessTokenRequest(final String clientId,
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

    public GetAccessTokenRequest(final String clientId,
                                 final String clientSecret) {
        this.uri = DEFAULT_URI;
        String credentials = clientId + ":" + clientSecret;
        this.clientIdSecretBase64 = Base64.getEncoder()
                                          .encodeToString(credentials.getBytes());
    }

    public GetAccessTokenRequest(final String clientId,
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
    public Val<JsObj> apply(final MultiMap context,
                            final HttpClientModule module) {
        PostReq message = new PostReq("grant_type=client_credentials".getBytes());
        if (host != null && !host.isEmpty()) message = message.host(host);
        if (port != null) message = message.port(port);
        if (ssl != null) message = message.ssl(ssl);

        return module.post.apply(context,
                                 message.uri(uri)
                                        .setHeader("Accept",
                                                   "application/json"
                                                  )
                                        .setHeader("Authorization",
                                                   String.format("Basic %s",
                                                              clientIdSecretBase64
                                                             )
                                                  )
                                        .setHeader("Content-Type",
                                                   "application/x-www-form-urlencoded"
                                                  )
                                );
    }


}
