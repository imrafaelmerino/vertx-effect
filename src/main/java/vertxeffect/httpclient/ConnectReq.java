package vertxeffect.httpclient;

import vertxeffect.core.HttpReq;

public class ConnectReq extends HttpReq<ConnectReq> {
    public ConnectReq() {
        this.type = TYPE.CONNECT;
    }
}