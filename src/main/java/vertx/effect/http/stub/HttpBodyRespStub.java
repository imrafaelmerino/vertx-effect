package vertx.effect.http.stub;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import jsonvalues.JsObj;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.IntFunction;

public interface HttpBodyRespStub extends IntFunction<Function<Buffer, Function<HttpServerRequest, String>>> {

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

    HttpBodyRespStub EMPTY = n -> body -> req -> "";


}
