package vertx.effect.readme;


import jsonvalues.JsObj;
import jsonvalues.JsStr;
import jsonvalues.JsValue;
import vertx.effect.VertxModule;
import vertx.effect.exp.Cons;
import vertx.effect.λ;

import java.util.function.Function;


public class MyModule extends VertxModule {

    private static final String REMOVE_NULL_ADDRESS = "removeNull";
    private static final String TRIM_ADDRESS = "trim";

    public static λ<JsObj, JsObj> removeNull;
    public static λ<JsObj, JsObj> trim;

    @Override
    public void deploy() {

        this.deploy(REMOVE_NULL_ADDRESS,
                    (JsObj o) -> Cons.success(o.filterAllValues(pair -> pair.value.isNotNull()))
                   );

        Function<JsValue, JsValue> trim = JsStr.prism.modify.apply(String::trim);
        this.deploy(TRIM_ADDRESS,
                    (JsObj o)->  Cons.success(o.mapAllValues(pair -> trim.apply(pair.value)))
                   );
    }

    @Override
    protected void initialize() {
        removeNull = this.ask(REMOVE_NULL_ADDRESS);
        trim = this.ask(TRIM_ADDRESS);
    }

}