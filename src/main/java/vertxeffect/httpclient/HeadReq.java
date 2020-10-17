package vertxeffect.httpclient;

import vertxeffect.core.HttpReq;

public class HeadReq extends HttpReq<HeadReq> {
    public HeadReq() {
        this.type = TYPE.HEAD;
    }
}