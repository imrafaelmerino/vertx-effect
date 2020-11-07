package vertx.effect.httpclient;

import io.vavr.Tuple2;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import jsonvalues.JsObj;
import org.junit.jupiter.api.Test;
import vertx.effect.Val;
import vertx.effect.VertxRef;
import vertx.effect.httpclient.oauth.*;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
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
                                        int n = counter.incrementAndGet();
                                        req.body()
                                           .onComplete(event -> {

                                                           if (event.succeeded()) {
                                                               System.out.println("server req #" + n);
                                                               req.response()
                                                                  .putHeader("Content-Type",
                                                                             "application/json"
                                                                            );
                                                               String bodyStr = event.result()
                                                                                     .toString();

                                                               Integer statusCode = statusCodeRes.apply(n)
                                                                                                 .apply(req)
                                                                                                 .apply(bodyStr);

                                                               String response = bodyRes.apply(n)
                                                                                        .apply(req)
                                                                                        .apply(bodyStr)
                                                                                        .toPrettyString();
                                                               req.response()
                                                                  .setStatusCode(statusCode)
                                                                  .end(response);
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
                                   )
                    .listen(port);
    }

}
