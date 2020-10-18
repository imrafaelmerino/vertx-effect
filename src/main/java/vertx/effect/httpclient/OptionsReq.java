package vertx.effect.httpclient;

import vertx.effect.core.HttpReq;

public class OptionsReq extends HttpReq<OptionsReq> {
    public OptionsReq() {
        this.type = TYPE.OPTIONS;
    }
}