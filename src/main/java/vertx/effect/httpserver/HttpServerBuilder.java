package vertx.effect.httpserver;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import vertx.effect.Val;

import java.util.Objects;

/**
 Provides a constructor to create http servers, and different methods to wrapped their deployment into
 a {@link Val}. It allows to define some interesting methods like {@link #startAtRandom(int, int)}, that
 deploys the server on the first free port it finds.
 */
public class HttpServerBuilder {

    private final Vertx vertx;

    private final HttpServerOptions options;
    private static final String DEFAULT_HOST = "localhost";
    private final Handler<HttpServerRequest> reqHandler;

    public HttpServerBuilder(final Vertx vertx,
                             final HttpServerOptions options,
                             final Handler<HttpServerRequest> reqHandler) {
        this.vertx = Objects.requireNonNull(vertx);
        this.options = Objects.requireNonNull(options);
        this.reqHandler = Objects.requireNonNull(reqHandler);
    }

    public HttpServerBuilder(final Vertx vertx,
                             final Handler<HttpServerRequest> reqHandler) {
        this(vertx,
             new HttpServerOptions().setLogActivity(true),
             reqHandler
            );
    }

    public Val<HttpServer> startAtRandom(final int start,
                                         final int end) {
        return startAtRandom(DEFAULT_HOST,
                             start,
                             end
                            );
    }

    public Val<HttpServer> startAtRandom(final String localhost,
                                         final int start,
                                         final int end) {
        if (start > end) return Val.fail(new IllegalArgumentException("start greater than end"));
        return startAtRandomRec(localhost,
                                start,
                                end
                               );
    }

    private Val<HttpServer> startAtRandomRec(final String localhost,
                                             final int start,
                                             final int end) {
        if (start == end) return Val.fail(new IllegalArgumentException("range of ports exhausted"));
        return start(localhost,
                     start
                    ).recoverWith(error -> startAtRandomRec(localhost,
                                                            start + 1,
                                                            end
                                                           )
                                 );
    }

    public Val<HttpServer> start(final String host,
                                 final int port) {
        return Val.effect(() -> vertx.createHttpServer(options.setHost(host))
                                     .requestHandler(reqHandler)
                                     .listen(port)
                         );
    }

    public Val<HttpServer> start(final int port) {
        return start(DEFAULT_HOST,
                     port
                    );
    }


}
