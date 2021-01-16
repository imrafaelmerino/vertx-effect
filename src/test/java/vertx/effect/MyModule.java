package vertx.effect;

import jsonvalues.JsObj;
import jsonvalues.JsStr;
import vertx.effect.exp.Cons;

public class MyModule extends VertxModule {
    private static final String REMOVE_NULL_ADDRESS = "removeNull";
    private static final String TRIM_ADDRESS = "trim";

    public static λ<JsObj, JsObj> removeNull;
    public static λ<JsObj, JsObj> trim;
    public static λc<String, String> toLowerCase;

    @Override
    public void deploy() {
        λ<JsObj, JsObj> removeNull = o ->
                Cons.success(o.filterAllValues(value -> value.isNotNull()));
        this.deploy(REMOVE_NULL_ADDRESS,
                    removeNull
                   );

        λ<JsObj, JsObj> trim = o ->
                Cons.success(o.mapAllValues(value -> JsStr.prism.modify.apply(String::trim)
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
        λc<String, String> toLowerCase = (context, string) -> Cons.success(string.toLowerCase());
        this.toLowerCase = vertxRef.spawn("toLowerCase",
                                          toLowerCase
                                         );
    }
}
