package vertx.effect.mock;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import jsonvalues.JsObj;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.IntFunction;

public interface MockBodyResp extends IntFunction<Function<Buffer, Function<HttpServerRequest, String>>> {

    static MockBodyResp cons(final String respBody) {
        return n -> body -> req -> respBody;
    }

    static MockBodyResp consAfter(final Duration duration, final String respBody) {
        return n -> body -> req -> {
            try {
                TimeUnit.MILLISECONDS.sleep(duration.toMillis());
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
            return respBody;
        };
    }
    static MockBodyResp cons(final JsObj respBody) {
        return n -> body -> req -> respBody.toPrettyString();
    }

    static MockBodyResp consAfter(final Duration duration, final JsObj respBody) {
        return n -> body -> req -> {
            try {
                TimeUnit.MILLISECONDS.sleep(duration.toMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return respBody.toPrettyString();
        };
    }

    MockBodyResp EMPTY = n -> body -> req -> "";


}
