package vertx.effect;

import io.vertx.core.MultiMap;
import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.JsStr;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

class Functions {

    static BiFunction<Map.Entry<String, String>, JsObj, JsObj> addHeader =
            (header, result) -> {
                if (!result.containsKey(header.getKey()))
                    return result.set(header.getKey(),
                                      JsArray.of(header.getValue())
                                     );
                else {
                    JsArray headerValues = result.getArray(header.getKey())
                                                 .append(JsStr.of(header.getValue()));
                    return result.set(header.getKey(),
                                      headerValues
                                     );
                }
            };
    public static final Function<MultiMap, JsObj> headers2JsObj =
            headers -> {
                JsObj result = JsObj.empty();
                if (headers == null || headers.isEmpty()) return JsObj.empty();
                for (final Map.Entry<String, String> header : headers) {
                    result = addHeader.apply(header,
                                             result
                                            );
                }
                return result;
            };


    private Functions() {
    }

    public static String getErrorMessage(final Throwable e) {
        return e.getStackTrace().length == 0 ?
                e.toString() :
                e + "@" + Arrays.toString(e.getStackTrace());
    }
}
