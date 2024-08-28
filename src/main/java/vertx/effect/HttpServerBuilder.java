package vertx.effect;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;

import java.util.Objects;

/**
 * Provides a constructor to create http servers, and different methods to wrapped their deployment into a {@link VIO}.
 * It allows to define some interesting methods like {@link #createAtRandom(int, int)}, that deploys the server on the
 * first free port it finds.
 */
public class HttpServerBuilder {

    private static final String DEFAULT_HOST = "0.0.0.0";
    private final Vertx vertx;
    private final HttpServerOptions options;
    private final Handler<HttpServerRequest> reqHandler;

    public HttpServerBuilder(final Vertx vertx,
                             final HttpServerOptions options,
                             final Handler<HttpServerRequest> reqHandler
                            ) {
        this.vertx = Objects.requireNonNull(vertx);
        this.options = Objects.requireNonNull(options);
        this.reqHandler = Objects.requireNonNull(reqHandler);
    }

    public HttpServerBuilder(final Vertx vertx,
                             final Handler<HttpServerRequest> reqHandler
                            ) {
        this(vertx,
             new HttpServerOptions().setLogActivity(true),
             reqHandler
            );
    }

    public VIO<HttpServer> createAtRandom(final int start,
                                          final int end
                                         ) {
        return createAtRandom(DEFAULT_HOST,
                              start,
                              end
                             );
    }

    public VIO<HttpServer> createAtRandom(final String localhost,
                                          final int start,
                                          final int end
                                         ) {
        if (start > end) return VIO.fail(new IllegalArgumentException("start greater than end"));
        return startAtRandomRec(localhost,
                                start,
                                end
                               );
    }

    private VIO<HttpServer> startAtRandomRec(final String localhost,
                                             final int start,
                                             final int end
                                            ) {
        if (start == end) return VIO.fail(new IllegalArgumentException("range of ports exhausted"));
        return create(localhost,
                      start
                     ).recoverWith(error -> startAtRandomRec(localhost,
                                                            start + 1,
                                                            end
                                                           )
                                 );
    }

    public VIO<HttpServer> create(final String host,
                                  final int port
                                 ) {
        return VIO.effect(() -> vertx.createHttpServer(options.setHost(host))
                                     .requestHandler(reqHandler)
                                     .listen(port, host)
                         );
    }

    public VIO<HttpServer> create(final int port) {
        return create(DEFAULT_HOST,
                      port
                     );
    }


}
