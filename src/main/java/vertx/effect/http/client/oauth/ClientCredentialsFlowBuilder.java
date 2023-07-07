package vertx.effect.http.client.oauth;

import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpClientOptions;
import jsonvalues.JsObj;
import vertx.effect.VIO;
import vertx.effect.http.client.HttpClientModule;

import java.util.function.BiFunction;

import static java.util.Objects.requireNonNull;

public class ClientCredentialsFlowBuilder extends OauthBuilder<ClientCredentialsFlowBuilder> {


    private final BiFunction<MultiMap, HttpClientModule, VIO<JsObj>> accessTokenReq;


    public ClientCredentialsFlowBuilder(final HttpClientOptions options,
                                        final String address,
                                        final BiFunction<MultiMap,HttpClientModule, VIO<JsObj>> accessTokenReq) {
        super(options,address);
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