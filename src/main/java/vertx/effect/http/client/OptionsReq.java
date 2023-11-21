package vertx.effect.http.client;


public final class OptionsReq extends HttpReq<OptionsReq> {
    public OptionsReq() {
        this.type = TYPE.OPTIONS;
    }
}