package vertx.effect.http.client;


public final class GetReq extends HttpReq<GetReq> {
    public GetReq() {
        this.type = TYPE.GET;
    }
}