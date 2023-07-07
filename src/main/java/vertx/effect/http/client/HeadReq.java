package vertx.effect.http.client;


public class HeadReq extends HttpReq<HeadReq> {
    public HeadReq() {
        this.type = TYPE.HEAD;
    }
}