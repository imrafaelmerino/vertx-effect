package vertx.effect.httpserver;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import jsonvalues.JsObj;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.IntFunction;

public interface BodyRespHandler extends IntFunction<Function<Buffer, Function<HttpServerRequest, String>>> {

    static BodyRespHandler cons(final String respBody) {
        return n -> body -> req -> respBody;
    }

    static BodyRespHandler consAfter(final Duration duration, final String respBody) {
        return n -> body -> req -> {
            try {
                TimeUnit.MILLISECONDS.sleep(duration.toMillis());
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
            return respBody;
        };
    }
    static BodyRespHandler cons(final JsObj respBody) {
        return n -> body -> req -> respBody.toPrettyString();
    }

    static BodyRespHandler consAfter(final Duration duration, final JsObj respBody) {
        return n -> body -> req -> {
            try {
                TimeUnit.MILLISECONDS.sleep(duration.toMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return respBody.toPrettyString();
        };
    }

    BodyRespHandler EMPTY = n -> body -> req -> "";


}
