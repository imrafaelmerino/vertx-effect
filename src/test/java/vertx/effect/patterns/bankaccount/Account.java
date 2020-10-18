package vertx.effect.patterns.bankaccount;
import jsonvalues.JsInt;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import jsonvalues.Lens;
import jsonvalues.spec.JsObjSpec;
import java.util.function.BiFunction;
import java.util.function.IntUnaryOperator;
import static jsonvalues.spec.JsSpecs.integer;
import static jsonvalues.spec.JsSpecs.str;

public class Account {

    private static final String NAME_FIELD = "name";
    private static final String CREDIT_FIELD = "credit";

    public static final Lens<JsObj, String> nameLens = JsObj.lens.str(NAME_FIELD);
    public static final Lens<JsObj, Integer> creditLens = JsObj.lens.intNum(CREDIT_FIELD);

    public static final BiFunction<String, Integer, JsObj> accountMessage =
            (name, credit) -> JsObj.of(NAME_FIELD,
                                       JsStr.of(name),
                                       CREDIT_FIELD,
                                       JsInt.of(credit)
                                      );

    public static final BiFunction<IntUnaryOperator,JsObj,JsObj> creditOp =
            (op,account) -> creditLens.modify.apply(op::applyAsInt).apply(account);

    public static final BiFunction<Integer,JsObj,JsObj> deposit =
            (amount,account) -> creditOp.apply(credit-> credit + amount,account);

    public static final BiFunction<Integer,JsObj,JsObj> withdraw =
            (amount,account) -> creditOp.apply(credit-> credit - amount,account);

}
