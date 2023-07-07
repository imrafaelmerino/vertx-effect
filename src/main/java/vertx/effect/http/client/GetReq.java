package vertx.effect.http.client;


public class GetReq extends HttpReq<GetReq> {
    public GetReq() {
        this.type = TYPE.GET;
    }
}