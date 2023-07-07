package vertx.effect.http.client;


import vertx.effect.BodyHttpReq;

import static java.util.Objects.requireNonNull;

public class PutReq extends BodyHttpReq<PutReq> {
    public PutReq(final byte[] body) {
        super(requireNonNull(body));
        this.type = TYPE.PUT;
    }
}