package vertx.effect.api.patterns.bankaccount;

import jsonvalues.JsObj;
import fun.optic.Lens;

public class Account {

    private static final String NAME_FIELD = "name";
    private static final String CREDIT_FIELD = "credit";

    public static final Lens<JsObj, String> nameLens = JsObj.lens.str(NAME_FIELD);
    public static final Lens<JsObj, Integer> creditLens = JsObj.lens.intNum(CREDIT_FIELD);

}
