package vertx.effect;


public final class DeleteReq extends HttpReq<DeleteReq> {
    public DeleteReq() {
        this.type = TYPE.DELETE;
    }
}