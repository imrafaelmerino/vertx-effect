package vertx.effect.stub.http;

import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;

import java.util.function.Function;
import java.util.function.IntFunction;


public interface HttpHeadersRespStub extends IntFunction<Function<Buffer, Function<HttpServerRequest, MultiMap>>> {

    HttpHeadersRespStub EMPTY = n -> body -> req -> HttpHeaders.headers();

    HttpHeadersRespStub JSON_CONTENT_TYPE = n -> body -> req -> HttpHeaders.headers()
                                                                           .add("Content-Type",
                                                                                "application/json"
                                                                               );


}
