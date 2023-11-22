package vertx.effect;

import fun.optic.Lens;
import fun.optic.Option;
import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.JsValue;

import java.util.function.Function;

public final class HttpResp {

    private HttpResp(){}

    private static final String STATUS_CODE_FIELD = "status_code";
    private static final String STATUS_MESSAGE_FIELD = "status_message";
    private static final String COOKIES_FIELD = "cookies";
    private static final String HEADERS_FIELD = "headers";
    private static final String BODY_FIELD = "body";

    public static final Lens<JsObj, Integer> STATUS_CODE_LENS =
            JsObj.lens.intNum(STATUS_CODE_FIELD);
    public static final Option<JsObj, String> STATUS_MESSAGE_OPT =
            JsObj.optional.str(STATUS_MESSAGE_FIELD);
    public static final Lens<JsObj, JsArray> COOKIES_LENS =
            JsObj.lens.array(COOKIES_FIELD);
    public static final Lens<JsObj, JsObj> HEADERS_OPT =
            JsObj.lens.obj(HEADERS_FIELD);
    public static final Lens<JsObj, String> STR_BODY_LENS =
            JsObj.lens.str(BODY_FIELD);


    public static final Function<Function<String, JsValue>, Function<JsObj, JsObj>> mapBody =
            fn -> resp -> resp.set(BODY_FIELD,
                                   fn.apply(resp.getStr(BODY_FIELD))
                                  );


    public static final Function<JsObj, JsObj> mapBody2Json = mapBody.apply(JsObj::parse);


}
