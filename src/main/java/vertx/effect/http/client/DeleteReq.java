package vertx.effect.http.client;


public final class DeleteReq extends HttpReq<DeleteReq> {
    public DeleteReq() {
        this.type = TYPE.DELETE;
    }
}