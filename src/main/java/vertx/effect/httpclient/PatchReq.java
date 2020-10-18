package vertx.effect.httpclient;

import vertx.effect.core.BodyHttpReq;

import java.util.Objects;

public class PatchReq extends BodyHttpReq<PatchReq> {
    public PatchReq(final byte[] body) {
        super(Objects.requireNonNull(body));
        this.type = TYPE.PATCH;
    }
}