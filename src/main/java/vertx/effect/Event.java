package vertx.effect;

import jsonvalues.*;

import java.time.Instant;

import static jsonvalues.JsPath.path;

public final class Event {
    private Event(){}
    public static final Option<JsObj, String> idOption = JsObj.optional.str(path("/id"));
    public static final Option<JsObj, String> addressOpt = JsObj.optional.str(path("/address"));
    public static final Option<JsObj, String> toOpt = JsObj.optional.str(path("/to"));
    public static final Option<JsObj, String> fromOpt = JsObj.optional.str(path("/from"));
    public static final Option<JsObj, String> descriptionOpt = JsObj.optional.str(path("/desc"));
    public static final Option<JsObj, String> classOpt = JsObj.optional.str(path("/class"));
    public static final Option<JsObj, String> exceptionOpt = JsObj.optional.str(path("/exception/class"));
    public static final Option<JsObj, String> exceptionMessageOpt = JsObj.optional.str(path("/exception/message"));
    public static final Option<JsObj, String> exceptionStackOpt = JsObj.optional.str(path("/exception/stacktrace"));
    public static final Lens<JsObj, String> eventLens = JsObj.lens.str(path("/event"));
    public static final Lens<JsObj, Instant> instantLens = JsObj.lens.instant(path("/instant"));
    public static final Lens<JsObj, JsValue> contextLens = JsObj.lens.value(path("/context"));
    public static final Lens<JsObj, String> threadLens = JsObj.lens.str(path("/thread"));
    public static final Lens<JsObj, JsValue> messageLens = JsObj.lens.value(path("/message"));
    public static final Lens<JsObj, String> failureTypeLens = JsObj.lens.str(path("/failure/type"));
    public static final Lens<JsObj, Integer> failureCodeLens = JsObj.lens.intNum(path("/failure/code"));
    public static final Lens<JsObj, String> failureMessageLens = JsObj.lens.str(path("/failure/message"));
    public static final String SENT_MESSAGE_EVENT = "SENT_MESSAGE";
    public static final String RECEIVED_MESSAGE_EVENT = "RECEIVED_MESSAGE";
    public static final String DEPLOYED_VERTICLE = "DEPLOYED_VERTICLE";
    public static final String UNDEPLOYED_VERTICLE = "UNDEPLOYED_VERTICLE";
    public static final String REPLIED_RESP_EVENT = "REPLIED_RESP";
    public static final String REPLIED_FAILURE_EVENT = "REPLIED_FAILURE";
    public static final String RECEIVED_RESP_EVENT = "RECEIVED_RESP";
    public static final String RECEIVED_FAILURE_EVENT = "RECEIVED_FAILURE";
    public static final String INTERNAL_ERROR_STARTING_VERTICLE = "INTERNAL_ERROR_STARTING_VERTICLE";
    public static final String INTERNAL_ERROR_PROCESSING_MESSAGE = "INTERNAL_ERROR_PROCESSING_MESSAGE";
    public static final String INTERNAL_ERROR_UNDEPLOYING_VERTICLE = "INTERNAL_ERROR_UNDEPLOYING_VERTICLE";
    public static final String INTERNAL_ERROR_DEPLOYING_VERTICLE = "INTERNAL_ERROR_DEPLOYING_VERTICLE";
    public static final String INTERNAL_ERROR_PROCESSING_HTTP_RESP = "INTERNAL_ERROR_PROCESSING_HTTP_RESP";
    public static final String INTERNAL_ERROR_READING_HTTP_RESP_BODY = "INTERNAL_ERROR_READING_HTTP_RESP_BODY";
    public static final String TIMER_STARTS_EVENT = "TIMER_STARTS";
    public static final String TIMER_ENDS_EVENT = "TIMER_ENDS";

}
