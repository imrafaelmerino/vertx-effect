package vertx.effect;


import vertx.effect.http.client.HttpReq;

public class DeleteReq extends HttpReq<DeleteReq> {
    public DeleteReq() {
        this.type = TYPE.DELETE;
    }
}