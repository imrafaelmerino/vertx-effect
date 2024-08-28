package vertx.effect;

import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpClientOptions;
import jsonvalues.JsObj;

import java.util.function.BiFunction;

import static java.util.Objects.requireNonNull;

public class ClientCredentialsModuleBuilder extends OauthModuleBuilder<ClientCredentialsModuleBuilder> {


    private final BiFunction<MultiMap, HttpClientModule, VIO<JsObj>> accessTokenReq;


    public ClientCredentialsModuleBuilder(final HttpClientOptions options,
                                          final String address,
                                          final BiFunction<MultiMap, HttpClientModule, VIO<JsObj>> accessTokenReq
                                         ) {
        super(options, address);
        this.accessTokenReq = requireNonNull(accessTokenReq);
    }


    public ClientCredentialsModule createModule() {
        return new ClientCredentialsModule(options,
                                           address,
                                           accessTokenReq,
                                           authorizationHeaderName,
                                           authorizationHeaderValue,
                                           readAccessToken,
                                           refreshTokenPredicate,
                                           accessTokenRetryPolicy,
                                           accessTokenRetryPredicate
        );
    }


}