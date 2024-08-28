package vertx.effect;

import jdk.jfr.*;
import jdk.jfr.Event;

@Label("http request")
@Name("vertx.effect.httpclient.Request")
@Category("vertx.effect")
@Description("Http request made by vertx-effect http client.")
@StackTrace(value = false)
public class HttpReqEvent extends Event {

    private final long tic;

    public HttpReqEvent() {
        tic = System.nanoTime();
    }

    @Label("method")
    public String method;

    @Label("uri")
    public String uri;

    @Label("status_code")
    public int statusCode = -1;

    @Label("exception_class")
    public String exceptionClass;

    @Label("exception_message")
    public String exceptionMessage;

    @Label("host")
    public String host;

    @Label("time(ns)")
    public long time;
    public void tac() {
        time = System.nanoTime() - tic;
    }
}
