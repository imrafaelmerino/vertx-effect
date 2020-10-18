package vertx.effect.httpclient;

import vertx.effect.core.HttpReq;

public class TraceReq extends HttpReq<TraceReq> {
    public TraceReq() {
        this.type = TYPE.TRACE;
    }
}