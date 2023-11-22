package vertx.effect;


public final class TraceReq extends HttpReq<TraceReq> {
    public TraceReq() {
        this.type = TYPE.TRACE;
    }
}