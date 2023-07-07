package vertx.effect;


import jsonvalues.JsObj;
import vertx.effect.http.client.HttpReq;

import static java.util.Objects.requireNonNull;

public abstract class BodyHttpReq<T extends BodyHttpReq<T>> extends HttpReq<T> {
    public BodyHttpReq(final byte[] body) {
        this.body = requireNonNull(body);
    }

    private final byte[] body;

    @Override
    public JsObj createHttpReq() {
        return BYTES_BODY_LENS.set.apply(body)
                                  .apply(super.createHttpReq());
    }
}