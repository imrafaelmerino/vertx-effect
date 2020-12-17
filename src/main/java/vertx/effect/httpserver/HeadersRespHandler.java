package vertx.effect.httpserver;

import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;

import java.util.function.Function;
import java.util.function.IntFunction;


public interface HeadersRespHandler extends IntFunction<Function<Buffer, Function<HttpServerRequest, MultiMap>>> {

    HeadersRespHandler EMPTY = n -> body -> req -> HttpHeaders.headers();

    HeadersRespHandler JSON = n -> body -> req -> HttpHeaders.headers()
                                                             .add("Content-Type",
                                                               "application/json"
                                                              );


}
