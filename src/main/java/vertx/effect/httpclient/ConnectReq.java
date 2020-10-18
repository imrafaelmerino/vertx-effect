package vertx.effect.httpclient;

import vertx.effect.core.HttpReq;

public class ConnectReq extends HttpReq<ConnectReq> {
    public ConnectReq() {
        this.type = TYPE.CONNECT;
    }
}