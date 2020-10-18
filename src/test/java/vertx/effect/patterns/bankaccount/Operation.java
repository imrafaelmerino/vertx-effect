package vertx.effect.patterns.bankaccount;

import jsonvalues.JsObj;
import jsonvalues.JsStr;
import jsonvalues.Lens;
import jsonvalues.spec.JsObjSpec;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

import static jsonvalues.spec.JsSpecs.integer;
import static jsonvalues.spec.JsSpecs.oneOf;

public class Operation {

    public static final String OP_FIELD = "op";
    public static final String AMOUNT_FIELD = "amount";

    public static final String DEPOSIT_OP = "DEPOSIT";
    public static final String WITHDRAW_OP = "WITHDRAW";

    public static final Lens<JsObj, String> opLens = JsObj.lens.str(OP_FIELD);
    public static final Lens<JsObj, Integer> amountLens = JsObj.lens.intNum(AMOUNT_FIELD);

    public static final JsObjSpec spec = JsObjSpec.strict(OP_FIELD,
                                                          oneOf(Arrays.asList(JsStr.of(DEPOSIT_OP),
                                                                              JsStr.of(WITHDRAW_OP)
                                                                             )
                                                               ),
                                                          AMOUNT_FIELD,
                                                          integer
                                                         );

    public static final Predicate<JsObj> IS_DEPOSIT = opLens.exists.apply(DEPOSIT_OP::equals);

    public static final Predicate<JsObj> IS_WITHDRAW = opLens.exists.apply(WITHDRAW_OP::equals);

    public static final Function<Integer, JsObj> makeDeposit =
            amount -> opLens.set.apply(DEPOSIT_OP)
                                .andThen(amountLens.set.apply(amount))
                                .apply(JsObj.empty());


    public static final Function<Integer, JsObj> makeWithdraw =
            amount -> opLens.set.apply(WITHDRAW_OP)
                                .andThen(amountLens.set.apply(amount))
                                .apply(JsObj.empty());
}
