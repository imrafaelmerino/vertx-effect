package vertx.effect;


import static java.util.Objects.requireNonNull;

public final class PutReq extends BodyHttpReq<PutReq> {
    public PutReq(final byte[] body) {
        super(requireNonNull(body));
        this.type = TYPE.PUT;
    }
}