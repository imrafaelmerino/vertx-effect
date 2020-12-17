package vertx.effect.httpserver;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import vertx.effect.Val;
import vertx.effect.exp.Cons;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HttpServerBuilder {

    private final Vertx vertx;
    private final List<ReqHandler> reqHandlers = new ArrayList<>();

    private final HttpServerOptions options;
    private static final String DEFAULT_HOST = "localhost";

    public HttpServerBuilder(final Vertx vertx,
                             final HttpServerOptions options) {
        this.vertx = Objects.requireNonNull(vertx);
        this.options = Objects.requireNonNull(options);
    }

    public HttpServerBuilder(final Vertx vertx) {
        this(vertx,
             new HttpServerOptions().setLogActivity(true)
            );
    }

    public HttpServerBuilder addHandler(final ReqHandler reqHandler) {
        reqHandlers.add(Objects.requireNonNull(reqHandler));
        return this;
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
        if (start > end) return Cons.failure(new IllegalArgumentException("start greater than end"));
        return startAtRandomRec(localhost,
                                start,
                                end
                               );
    }

    private Val<HttpServer> startAtRandomRec(final String localhost,
                                             final int start,
                                             final int end) {
        if (start == end) return Cons.failure(new IllegalArgumentException("range of ports exhausted"));
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
        return Cons.of(() -> vertx.createHttpServer(options.setHost(host))
                                  .requestHandler(new HttpServerHandler(reqHandlers))
                                  .listen(port)
                      );
    }

    public Val<HttpServer> start(final int port) {
        return start(DEFAULT_HOST,
                     port
                    );
    }


}
