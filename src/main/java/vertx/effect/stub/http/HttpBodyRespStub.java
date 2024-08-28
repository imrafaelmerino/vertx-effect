package vertx.effect.stub.http;

import fun.gen.Gen;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import jsonvalues.JsObj;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public interface HttpBodyRespStub extends IntFunction<Function<Buffer, Function<HttpServerRequest, String>>> {

    HttpBodyRespStub EMPTY = n -> body -> req -> "";

    static HttpBodyRespStub gen(final Gen<String> gen) {
        Supplier<String> supplier = Objects.requireNonNull(gen).sample();
        return n -> body -> req -> supplier.get();
    }

    static HttpBodyRespStub cons(final String respBody) {
        return n -> body -> req -> respBody;
    }

    static HttpBodyRespStub consAfter(final Duration duration,
                                      final String respBody
                                     ) {
        return n -> body -> req -> {
            try {
                TimeUnit.MILLISECONDS.sleep(duration.toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return respBody;
        };
    }

    static HttpBodyRespStub cons(final JsObj respBody) {
        return n -> body -> req -> respBody.toPrettyString();
    }

    static HttpBodyRespStub consAfter(final Duration duration,
                                      final JsObj respBody
                                     ) {
        return n -> body -> req -> {
            try {
                TimeUnit.MILLISECONDS.sleep(duration.toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return respBody.toPrettyString();
        };
    }


}
