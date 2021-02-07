package vertx.effect.httpclient.oauth;

import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpClientOptions;
import jsonvalues.JsObj;
import vertx.effect.core.OauthBuilder;
import vertx.effect.Val;
import vertx.effect.httpclient.HttpClientModule;

import java.util.function.BiFunction;

import static java.util.Objects.requireNonNull;

public class ClientCredentialsFlowBuilder extends OauthBuilder<ClientCredentialsFlowBuilder> {


    private final BiFunction<MultiMap, HttpClientModule, Val<JsObj>> accessTokenReq;


    public ClientCredentialsFlowBuilder(final HttpClientOptions options,
                                        final String address,
                                        final BiFunction<MultiMap,HttpClientModule, Val<JsObj>> accessTokenReq) {
        super(options,address);
        this.accessTokenReq = requireNonNull(accessTokenReq);
    }


    public ClientCredentialsModule createModule() {
        return new ClientCredentialsModule(options,
                                           address,
                                           accessTokenReq,
                                           authorizationHeaderName,
                                           authorizationHeaderValue,
                                           readNewAccessTokenAfterRefresh,
                                           refreshTokenPredicate,
                                           accessTokenRetryPolicy
        );
    }




}