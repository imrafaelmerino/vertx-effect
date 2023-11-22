package vertx.effect.stub.http;


import fun.gen.Gen;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public interface HttpStatusCodeRespStub extends IntFunction<Function<Buffer, Function<HttpServerRequest, Integer>>> {

    HttpStatusCodeRespStub _200 = n -> body -> req -> 200;
    HttpStatusCodeRespStub _201 = n -> body -> req -> 201;
    HttpStatusCodeRespStub _500 = n -> body -> req -> 500;
    HttpStatusCodeRespStub _400 = n -> body -> req -> 400;
    HttpStatusCodeRespStub _401 = n -> body -> req -> 401;
    HttpStatusCodeRespStub _404 = n -> body -> req -> 404;

    static HttpStatusCodeRespStub cons(final int code) {
        return n -> body -> req -> code;
    }

    static HttpStatusCodeRespStub gen(Gen<Integer> gen) {
        Supplier<Integer> supplier = Objects.requireNonNull(gen).sample();
        return n -> body -> req -> supplier.get();
    }
}
