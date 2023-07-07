package vertx.effect.http.client;


public class OptionsReq extends HttpReq<OptionsReq> {
    public OptionsReq() {
        this.type = TYPE.OPTIONS;
    }
}