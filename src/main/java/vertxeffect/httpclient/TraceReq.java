package vertxeffect.httpclient;

import vertxeffect.core.HttpReq;

public class TraceReq extends HttpReq<TraceReq> {
    public TraceReq() {
        this.type = TYPE.TRACE;
    }
}