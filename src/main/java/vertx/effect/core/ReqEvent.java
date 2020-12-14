package vertx.effect.core;

import io.vertx.core.http.HttpMethod;
import jdk.jfr.*;

@Label("http request")
@Name("vertx.effect.httpclient.Request")
@Category("vertx.effect")
@Description("Http request made by vertx-effect http client.")
@StackTrace(value = false)
public class ReqEvent extends Event {

    public enum RESULT {SUCCESS, FAILURE}

    @Label("method")
    public String method;

    @Label("uri")
    public String uri;

    @Label("statusCode")

    public int statusCode = -1;

    @Label("result")
    public String result;

    @Label("exception_class")
    public String exceptionClass;

    @Label("exception_message")
    public String exceptionMessage;

    @Label("host")
    public String host;

}
