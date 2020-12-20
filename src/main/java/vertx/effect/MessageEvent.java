package vertx.effect;

import jdk.jfr.*;
import jdk.jfr.Event;

@Label("verticle message")
@Name("vertx.effect.Message")
@Category("vertx.effect")
@Description("Verticle sends a message to an address and receives a response")
@StackTrace(value = false)
public class MessageEvent extends Event {

    public enum Result{SUCCESS,FAILURE}

    @Label("address")
    public String address;

    @Label("result")
    public String result;

    @Label("failure_type")
    public String failureType;

    @Label("failure_code")
    public int failureCode = -1;

    @Label("failure_message")
    public String failureMessage;

    @Label("exception_class")
    public String exceptionClass;

    @Label("exception_message")
    public String exceptionMessage;



}
