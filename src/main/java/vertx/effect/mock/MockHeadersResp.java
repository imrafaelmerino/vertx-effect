package vertx.effect.mock;

import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;

import java.util.function.Function;
import java.util.function.IntFunction;


public interface MockHeadersResp extends IntFunction<Function<Buffer, Function<HttpServerRequest, MultiMap>>> {

    MockHeadersResp EMPTY = n -> body -> req -> HttpHeaders.headers();

    MockHeadersResp JSON = n -> body -> req -> HttpHeaders.headers()
                                                          .add("Content-Type",
                                                               "application/json"
                                                              );


}
