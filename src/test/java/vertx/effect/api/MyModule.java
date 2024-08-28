package vertx.effect.api;

import jsonvalues.JsObj;
import jsonvalues.JsStr;
import jsonvalues.JsValue;
import vertx.effect.VIO;
import vertx.effect.Lambda;
import vertx.effect.Lambdac;
import vertx.effect.VertxModule;

public class MyModule extends VertxModule {
    private static final String REMOVE_NULL_ADDRESS = "removeNull";
    private static final String TRIM_ADDRESS = "trim";

    public static Lambda<JsObj, JsObj> removeNull;
    public static Lambda<JsObj, JsObj> trim;
    public static Lambdac<String, String> toLowerCase;

    @Override
    public void deploy() {
        Lambda<JsObj, JsObj> removeNull = o ->
                VIO.succeed(o.filterValues(JsValue::isNotNull));
        this.deploy(REMOVE_NULL_ADDRESS,
                    removeNull
                   );

        Lambda<JsObj, JsObj> trim = o ->
                VIO.succeed(o.mapValues(value -> JsStr.prism.modify.apply(String::trim)
                                                                   .apply(value)
                                       )
                           );
        this.deploy(TRIM_ADDRESS,
                    trim
                   );
    }

    @Override
    protected void initialize() {
        removeNull = this.ask(REMOVE_NULL_ADDRESS);
        trim = this.ask(TRIM_ADDRESS);
        Lambdac<String, String> toLowerCaseLambda = (context, string) -> VIO.succeed(string.toLowerCase());
        toLowerCase = vertxRef.spawn("toLowerCase",
                                     toLowerCaseLambda
                                    );
    }
}
