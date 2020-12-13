package vertx.effect.core;

import io.vertx.core.http.HttpMethod;
import jdk.jfr.*;

@Label("http request")
@Name("vertx.effect.httpclient.Request")
@Category("vertx.effect")
@Description("Http request made by vertx-effect http client.")
 public class ReqEvent extends Event {

    public enum RESULT {SUCCESS, FAILURE}

    @Label("method")
    public HttpMethod method;

    @Label("uri")
    public String uri;

    @Label("statusCode")
    public int statusCode;

   @Label("result")
   public RESULT result;

    @Label("failure")
    public Throwable failure;
}
