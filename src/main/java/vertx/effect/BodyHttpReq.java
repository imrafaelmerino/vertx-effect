package vertx.effect;


import jsonvalues.JsObj;

import static java.util.Objects.requireNonNull;

abstract sealed class BodyHttpReq<T extends BodyHttpReq<T>> extends HttpReq<T> permits PatchReq, PostReq, PutReq {
    private final byte[] body;

    public BodyHttpReq(final byte[] body) {
        this.body = requireNonNull(body);
    }

    @Override
    public JsObj createHttpReq() {
        return BYTES_BODY_LENS.set.apply(body)
                                  .apply(super.createHttpReq());
    }
}