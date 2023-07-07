package vertx.effect.http.client;


public class TraceReq extends HttpReq<TraceReq> {
    public TraceReq() {
        this.type = TYPE.TRACE;
    }
}