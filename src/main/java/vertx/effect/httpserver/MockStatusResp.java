package vertx.effect.httpserver;


import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;

import java.util.Random;
import java.util.function.Function;
import java.util.function.IntFunction;

public interface StatusRespHandler extends IntFunction<Function<Buffer, Function<HttpServerRequest, Integer>>> {

    static StatusRespHandler cons(final int code) {
        return n -> body -> req -> code;
    }

    static StatusRespHandler random(final int min,
                                    final int max
                                   ) {
        return n -> body -> req -> new Random().nextInt(max + min) - min;
    }

    StatusRespHandler _200 = n -> body -> req -> 200;
    StatusRespHandler _201 = n -> body -> req -> 201;
    StatusRespHandler _500 = n -> body -> req -> 500;
    StatusRespHandler _400 = n -> body -> req -> 400;
    StatusRespHandler _401 = n -> body -> req -> 400;
    StatusRespHandler _404 = n -> body -> req -> 404;
}
