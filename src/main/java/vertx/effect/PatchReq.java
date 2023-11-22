package vertx.effect;


import java.util.Objects;

public final class PatchReq extends BodyHttpReq<PatchReq> {
    public PatchReq(final byte[] body) {
        super(Objects.requireNonNull(body));
        this.type = TYPE.PATCH;
    }
}