package vertx.effect;

import fun.optic.Lens;
import fun.optic.Option;
import jsonvalues.JsObj;
import jsonvalues.JsValue;

import java.time.Instant;

import static jsonvalues.JsPath.path;

public final class Event {
    public static final Option<JsObj, String> idOption = JsObj.optional.str(path("/id"));
    public static final Option<JsObj, String> addressOpt = JsObj.optional.str(path("/address"));
    public static final Option<JsObj, String> toOpt = JsObj.optional.str(path("/to"));
    public static final Option<JsObj, String> fromOpt = JsObj.optional.str(path("/from"));
    public static final Option<JsObj, String> classOpt = JsObj.optional.str(path("/class"));
    public static final Option<JsObj, String> exceptionOpt = JsObj.optional.str(path("/exception/class"));
    public static final Option<JsObj, String> exceptionMessageOpt = JsObj.optional.str(path("/exception/message"));
    public static final Option<JsObj, String> exceptionStackOpt = JsObj.optional.str(path("/exception/stacktrace"));
    public static final Lens<JsObj, String> eventLens = JsObj.lens.str(path("/event"));
    public static final Lens<JsObj, Instant> instantLens = JsObj.lens.instant(path("/instant"));
    public static final Lens<JsObj, JsValue> contextLens = JsObj.lens.value(path("/context"));
    public static final Option<JsObj, String> threadNameLens = JsObj.optional.str(path("/thread"));
    public static final Lens<JsObj, JsValue> messageLens = JsObj.lens.value(path("/message"));
    public static final Option<JsObj, String> failureTypeLens = JsObj.optional.str(path("/failure/type"));
    public static final Option<JsObj, Integer> failureCodeLens = JsObj.optional.intNum(path("/failure/code"));
    public static final Option<JsObj, String> failureMessageLens = JsObj.optional.str(path("/failure/message"));
    public static final String SENT_MESSAGE_EVENT = "MESSAGE_SENT";
    public static final String RECEIVED_MESSAGE_EVENT = "MESSAGE_RECEIVED";
    public static final String DEPLOYED_VERTICLE = "VERTICLE_DEPLOYED";
    public static final String STARTED_SHELL_SERVICE = "SHELL_STARTED";
    public static final String UNDEPLOYED_VERTICLE = "VERTICLE_UNDEPLOYED";
    public static final String REPLIED_RESP_EVENT = "RESPONSE_REPLIED";
    public static final String REPLIED_FAILURE_EVENT = "FAILURE_REPLIED";
    public static final String RECEIVED_RESP_EVENT = "RESPONSE_RECEIVED";
    public static final String RECEIVED_FAILURE_EVENT = "FAILURE_RECEIVED";
    public static final String EXCEPTION_STARTING_VERTICLE = "EXCEPTION_STARTING_VERTICLE";
    public static final String EXCEPTION_PROCESSING_MESSAGE = "EXCEPTION_PROCESSING_MESSAGE";
    public static final String EXCEPTION_UNDEPLOYING_VERTICLE = "EXCEPTION_UNDEPLOYING_VERTICLE";
    public static final String EXCEPTION_DEPLOYING_VERTICLE = "EXCEPTION_DEPLOYING_VERTICLE";
    public static final String TIMER_STARTED = "TIMER_STARTED";
    public static final String TIMER_ENDED = "TIMER_ENDED";
    private Event() {
    }

}
