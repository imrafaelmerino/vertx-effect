package vertx.effect.http.stub;


import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;

import java.util.Random;
import java.util.function.Function;
import java.util.function.IntFunction;

public interface HttpStatusCodeRespStub extends IntFunction<Function<Buffer, Function<HttpServerRequest, Integer>>> {

    static HttpStatusCodeRespStub cons(final int code) {
        return n -> body -> req -> code;
    }

    static HttpStatusCodeRespStub random(final int min,
                                         final int max
                                        ) {
        return n -> body -> req -> new Random().nextInt(max + min) - min;
    }

    HttpStatusCodeRespStub _200 = n -> body -> req -> 200;
    HttpStatusCodeRespStub _201 = n -> body -> req -> 201;
    HttpStatusCodeRespStub _500 = n -> body -> req -> 500;
    HttpStatusCodeRespStub _400 = n -> body -> req -> 400;
    HttpStatusCodeRespStub _401 = n -> body -> req -> 401;
    HttpStatusCodeRespStub _404 = n -> body -> req -> 404;
}
