package vertx.effect.httpclient;

import vertx.effect.core.HttpReq;

public class GetReq extends HttpReq<GetReq> {
    public GetReq() {
        this.type = TYPE.GET;
    }
}