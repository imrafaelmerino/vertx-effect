package vertx.effect.httpclient;
import vertx.effect.core.BodyHttpReq;

import static java.util.Objects.requireNonNull;

public class PostReq extends BodyHttpReq<PostReq> {
    public PostReq(final byte[] body) {
        super(requireNonNull(body));
        this.type = TYPE.POST;
    }
}