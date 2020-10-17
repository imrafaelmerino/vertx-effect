package vertxeffect.httpclient;

import vertxeffect.core.HttpReq;

public class GetReq extends HttpReq<GetReq> {
    public GetReq() {
        this.type = TYPE.GET;
    }
}