package vertx.effect.http.client;


public final class HeadReq extends HttpReq<HeadReq> {
    public HeadReq() {
        this.type = TYPE.HEAD;
    }
}