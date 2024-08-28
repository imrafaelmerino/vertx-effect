package vertx.effect;

import static java.util.Objects.requireNonNull;

public final class PostReq extends BodyHttpReq<PostReq> {
    public PostReq(final byte[] body) {
        super(requireNonNull(body));
        this.type = TYPE.POST;
    }
}