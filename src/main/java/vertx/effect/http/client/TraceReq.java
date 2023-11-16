package vertx.effect.http.client;


public final class TraceReq extends HttpReq<TraceReq> {
    public TraceReq() {
        this.type = TYPE.TRACE;
    }
}