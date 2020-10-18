package vertx.effect.httpclient;

import vertx.effect.core.HttpReq;

public class HeadReq extends HttpReq<HeadReq> {
    public HeadReq() {
        this.type = TYPE.HEAD;
    }
}