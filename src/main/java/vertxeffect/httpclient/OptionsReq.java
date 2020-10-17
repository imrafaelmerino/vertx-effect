package vertxeffect.httpclient;

import vertxeffect.core.HttpReq;

public class OptionsReq extends HttpReq<OptionsReq> {
    public OptionsReq() {
        this.type = TYPE.OPTIONS;
    }
}