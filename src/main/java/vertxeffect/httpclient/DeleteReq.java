package vertxeffect.httpclient;

import vertxeffect.core.HttpReq;

public class DeleteReq extends HttpReq<DeleteReq> {
    public DeleteReq() {
        this.type = TYPE.DELETE;
    }
}