package vertx.effect.core;

import io.vertx.core.http.HttpMethod;
import jdk.jfr.*;

@Label("http request")
@Name("jio.httpclient.Request")
@Category("JIO")
@Description("Http request made by JIO using the native HTTP Java client.")
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
