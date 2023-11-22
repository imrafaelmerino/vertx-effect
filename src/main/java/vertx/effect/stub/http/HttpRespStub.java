package vertx.effect.stub.http;

import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntFunction;


public final class HttpRespStub {

    public static final BiPredicate<Integer, HttpServerRequest> ALWAYS =
            (c, req) -> true;
    public static final BiPredicate<Integer, HttpServerRequest> FIRST_REQ =
            (c, req) -> c == 1;
    public static final BiPredicate<Integer, HttpServerRequest> SECOND_REQ =
            (c, req) -> c == 2;
    public static final BiPredicate<Integer, HttpServerRequest> THIRD_REQ =
            (c, req) -> c == 3;
    public static final BiPredicate<Integer, HttpServerRequest> FORTH_REQ =
            (c, req) -> c == 4;
    public static final IntFunction<BiPredicate<Integer, HttpServerRequest>> REQ_GT =
            lowerLimit -> (c, req) -> c > lowerLimit;
    public static final IntFunction<BiPredicate<Integer, HttpServerRequest>> REQ_GET =
            lowerLimit -> (c, req) -> c >= lowerLimit;
    public static final IntFunction<BiPredicate<Integer, HttpServerRequest>> REQ_LT =
            upperLimit -> (c, req) -> c < upperLimit;
    public static final IntFunction<BiPredicate<Integer, HttpServerRequest>> REQ_LET =
            upperLimit -> (c, req) -> c <= upperLimit;


    final BiPredicate<Integer, HttpServerRequest> predicate;
    IntFunction<Function<Buffer, Function<HttpServerRequest, MultiMap>>> headers;
    IntFunction<Function<Buffer, Function<HttpServerRequest, Integer>>> code;
    IntFunction<Function<Buffer, Function<HttpServerRequest, String>>> body;

    private HttpRespStub(final BiPredicate<Integer, HttpServerRequest> predicate) {
        this.predicate = predicate;
        this.headers = HttpHeadersRespStub.EMPTY;
        this.code = HttpStatusCodeRespStub._200;
        this.body = HttpBodyRespStub.EMPTY;
    }

    public static HttpRespStub when(final BiPredicate<Integer, HttpServerRequest> predicate) {
        return new HttpRespStub(predicate);
    }

    public HttpRespStub setHeadersResp(final IntFunction<Function<Buffer, Function<HttpServerRequest, MultiMap>>> headersResp) {
        this.headers = Objects.requireNonNull(headersResp);
        return this;
    }

    public HttpRespStub setBodyResp(final IntFunction<Function<Buffer, Function<HttpServerRequest, String>>> bodyResp) {
        this.body = Objects.requireNonNull(bodyResp);
        return this;
    }

    public HttpRespStub setStatusCodeResp(final IntFunction<Function<Buffer, Function<HttpServerRequest, Integer>>> statusCodeResp) {
        this.code = Objects.requireNonNull(statusCodeResp);
        return this;
    }

}
