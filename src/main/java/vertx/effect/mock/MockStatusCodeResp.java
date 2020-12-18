package vertx.effect.mock;


import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;

import java.util.Random;
import java.util.function.Function;
import java.util.function.IntFunction;

public interface MockStatusCodeResp extends IntFunction<Function<Buffer, Function<HttpServerRequest, Integer>>> {

    static MockStatusCodeResp cons(final int code) {
        return n -> body -> req -> code;
    }

    static MockStatusCodeResp random(final int min,
                                     final int max
                                    ) {
        return n -> body -> req -> new Random().nextInt(max + min) - min;
    }

    MockStatusCodeResp _200 = n -> body -> req -> 200;
    MockStatusCodeResp _201 = n -> body -> req -> 201;
    MockStatusCodeResp _500 = n -> body -> req -> 500;
    MockStatusCodeResp _400 = n -> body -> req -> 400;
    MockStatusCodeResp _401 = n -> body -> req -> 400;
    MockStatusCodeResp _404 = n -> body -> req -> 404;
}
