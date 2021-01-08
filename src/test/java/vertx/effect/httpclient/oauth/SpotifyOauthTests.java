package vertx.effect.httpclient.oauth;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.Failures;
import vertx.effect.RegisterJsValuesCodecs;
import vertx.effect.Verifiers;
import vertx.effect.VertxRef;
import vertx.effect.exp.Pair;
import vertx.effect.httpclient.GetReq;
import vertx.effect.httpclient.HttpResp;

@ExtendWith(VertxExtension.class)
public class SpotifyOauthTests {


    @Test
    public void test_authenticate(Vertx vertx,
                                  VertxTestContext context) {

        final String CLIENT_ID     = System.getProperty("SPOTIFY_CLIENT_ID");
        final String CLIENT_SECRET = System.getProperty("SPOTIFY_CLIENT_SECRET");
        final String REDIRECT_URI  = "http://localhost:8777/callback";
        final String CODE          = "CODE";

        HttpClientOptions options =
                new HttpClientOptions().setDefaultHost("api.spotify.com")
                                       .setDefaultPort(443)
                                       .setSsl(true);
        AuthorizationCodeModule module =
                new AuthorizationCodeFlowBuilder(options,
                                                 "spotify.http.client",
                                                 Spotify.refreshTokenReq(CLIENT_ID,
                                                                         CLIENT_SECRET
                                                                        )
                )
                        .createFromAuthReq(Spotify.authenticateReq(CLIENT_ID,
                                                                   CLIENT_SECRET
                                                                  )
                                          );


        VertxRef vertxRef = new VertxRef(vertx);
        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );
        Pair.sequential(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                        vertxRef.deployVerticle(module)
                       )
            .onSuccess(pair -> module.authenticate(JsObj.of("code",
                                                            JsStr.of(CODE),
                                                            "redirect_uri",
                                                            JsStr.of(REDIRECT_URI)
                                                           )
                                                  )
                                     .onComplete(event -> {
                                         if (event.succeeded()) {
                                             Verifiers.<JsObj>verifySuccess(resp -> 200 == HttpResp.STATUS_CODE_LENS.get.apply(resp))
                                                     .accept(module.getOauth.apply(new GetReq().uri("/v1/users/rmerinogarcia/playlists")),
                                                             context
                                                            );

                                         }
                                         else {
                                             context.verify(() -> {
                                                 Assertions.assertTrue(Failures.REPLY_EXCEPTION_PRISM.exists.apply(it -> it.failureCode() == Failures.HTTP_ACCESS_TOKEN_NOT_FOUND_CODE)
                                                                                                            .test(event.cause()));
                                                 context.completeNow();
                                             });
                                         }
                                     })
                                     .get()
                      )
            .get();

    }

    @Test
    public void test_req_from_refresh_token(Vertx vertx,
                                            VertxTestContext context) {
        final String CLIENT_ID     = System.getProperty("SPOTIFY_CLIENT_ID");
        final String CLIENT_SECRET = System.getProperty("SPOTIFY_CLIENT_SECRET");
        final String REFRESH_TOKEN = System.getProperty("SPOTIFY_REFRESH_TOKEN");

        HttpClientOptions options =
                new HttpClientOptions().setDefaultHost("api.spotify.com")
                                       .setDefaultPort(443)
                                       .setSsl(true);
        AuthorizationCodeModule module =
                new AuthorizationCodeFlowBuilder(options,
                                                 "spotify.http.client",
                                                 Spotify.refreshTokenReq(CLIENT_ID,
                                                                         CLIENT_SECRET
                                                                        )
                )
                        .createFromRefreshToken(REFRESH_TOKEN);


        VertxRef vertxRef = new VertxRef(vertx);
        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );
        Pair.sequential(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                        vertxRef.deployVerticle(module)
                       )
            .onSuccess(pair ->
                               Verifiers.<JsObj>verifySuccess(resp -> 200 == HttpResp.STATUS_CODE_LENS.get.apply(resp))
                                       .accept(module.getOauth.apply(new GetReq().uri("/v1/users/rmerinogarcia/playlists")),
                                               context
                                              )
                      )
            .get();


    }

    @Test
    public void test_req_client_credentials(Vertx vertx,
                                            VertxTestContext context) {
        final String CLIENT_ID     = System.getProperty("SPOTIFY_CLIENT_ID");
        final String CLIENT_SECRET = System.getProperty("SPOTIFY_CLIENT_SECRET");

        HttpClientOptions options =
                new HttpClientOptions().setDefaultHost("api.spotify.com")
                                       .setDefaultPort(443)
                                       .setSsl(true);
        ClientCredentialsModule module =
                new ClientCredentialsFlowBuilder(options,
                                                 "spotify.http.client",
                                                 Spotify.getAccessToken(CLIENT_ID,
                                                                        CLIENT_SECRET
                                                                       )
                ).createModule();


        VertxRef vertxRef = new VertxRef(vertx);
        vertxRef.registerConsumer(VertxRef.EVENTS_ADDRESS,
                                  System.out::println
                                 );
        Pair.sequential(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                        vertxRef.deployVerticle(module)
                       )
            .onSuccess(pair ->
                               Verifiers.<JsObj>verifySuccess(resp -> 200 == HttpResp.STATUS_CODE_LENS.get.apply(resp))
                                       .accept(module.getOauth.apply(new GetReq().uri("/v1/users/rmerinogarcia/playlists")),
                                               context
                                              )
                      )
            .get();


    }
}
