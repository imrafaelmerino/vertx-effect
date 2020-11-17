package vertx.effect.httpclient;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import jsonvalues.JsObj;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class MyHttpServer {

    final Vertx vertx;
    final AtomicInteger counter = new AtomicInteger();
    Function<Integer, Function<HttpServerRequest, Function<String, JsObj>>> bodyRes;
    Function<Integer, Function<HttpServerRequest, Function<String, Integer>>> statusCodeRes;
    final int port;

    public void resetCounter() {
        counter.set(0);
    }

    public void setBodyRes(final Function<Integer, Function<HttpServerRequest, Function<String, JsObj>>> bodyRes) {
        this.bodyRes = bodyRes;
    }

    public void setStatusCodeRes(final Function<Integer, Function<HttpServerRequest, Function<String, Integer>>> statusCodeRes) {
        this.statusCodeRes = statusCodeRes;
    }

    public MyHttpServer(final Vertx vertx,
                        final int port,
                        final Function<Integer, Function<HttpServerRequest, Function<String, JsObj>>> bodyResp,
                        final Function<Integer, Function<HttpServerRequest, Function<String, Integer>>> statusCodeResp) {
        this.vertx = Objects.requireNonNull(vertx);
        this.port = port;
        this.bodyRes = Objects.requireNonNull(bodyResp);
        this.statusCodeRes = Objects.requireNonNull(statusCodeResp);
    }

    public Future<io.vertx.core.http.HttpServer> start() {
        return vertx.createHttpServer(new HttpServerOptions())
                    .requestHandler(req -> {
                                        String method = req.method()
                                                           .name();

                                        int n = counter.incrementAndGet();
                                        if (method.equals("POST") ||
                                                method.equals("PUT") || method.equals("PATCH")) {
                                            req.body()
                                               .onComplete(event -> {

                                                               if (event.succeeded()) {
                                                                   response(req,
                                                                            n,
                                                                            event.result()
                                                                                 .toString()
                                                                           );
                                                               }
                                                               else {
                                                                   event.cause()
                                                                        .printStackTrace();
                                                                   req.response()
                                                                      .setStatusCode(500)
                                                                      .end(event.cause()
                                                                                .getMessage());
                                                               }

                                                           }
                                                          );
                                        }
                                        else {
                                            response(req,
                                                     n,
                                                     ""
                                                    );
                                        }

                                    }
                                   )
                    .listen(port);
    }

    private void response(final HttpServerRequest req,
                          final int n,
                          final String bodyStr) {
        System.out.println("server req #" + n);
        System.out.println("req method" + req.method().name());

        Integer statusCode = statusCodeRes.apply(n)
                                          .apply(req)
                                          .apply(bodyStr);

        String response = bodyRes.apply(n)
                                 .apply(req)
                                 .apply(bodyStr)
                                 .toPrettyString();
        req.response()
           .putHeader("Content-Type",
                      "application/json"
                     )
           .setStatusCode(statusCode)
           .end(response);
    }

}
