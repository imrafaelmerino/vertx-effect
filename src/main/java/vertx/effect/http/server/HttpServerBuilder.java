package vertx.effect.http.server;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import vertx.effect.VIO;

import java.util.Objects;

/**
 * Provides a constructor to create http servers, and different methods to wrapped their deployment into a {@link VIO}.
 * It allows to define some interesting methods like {@link #startAtRandom(int, int)}, that deploys the server on the
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

    public VIO<HttpServer> startAtRandom(final int start,
                                         final int end
                                        ) {
        return startAtRandom(DEFAULT_HOST,
                             start,
                             end
                            );
    }

    public VIO<HttpServer> startAtRandom(final String localhost,
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
        return start(localhost,
                     start
                    ).recoverWith(error -> startAtRandomRec(localhost,
                                                            start + 1,
                                                            end
                                                           )
                                 );
    }

    public VIO<HttpServer> start(final String host,
                                 final int port
                                ) {
        return VIO.effect(() -> {
                              Future<HttpServer> fut = vertx.createHttpServer(options.setHost(host))
                                                            .requestHandler(reqHandler)
                                                            .listen(port, host);

                              System.out.println(fut.result());
                              return fut;
                          }
                         );
    }

    public VIO<HttpServer> start(final int port) {
        return start(DEFAULT_HOST,
                     port
                    );
    }


}
