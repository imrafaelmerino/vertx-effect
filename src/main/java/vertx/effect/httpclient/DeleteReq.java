package vertx.effect.httpclient;

import vertx.effect.core.HttpReq;

public class DeleteReq extends HttpReq<DeleteReq> {
    public DeleteReq() {
        this.type = TYPE.DELETE;
    }
}