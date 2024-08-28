package vertx.effect;


public final class OptionsReq extends HttpReq<OptionsReq> {
    public OptionsReq() {
        this.type = TYPE.OPTIONS;
    }
}