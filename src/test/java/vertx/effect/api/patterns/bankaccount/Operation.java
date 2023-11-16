package vertx.effect.api.patterns.bankaccount;

import fun.optic.Lens;
import jsonvalues.JsObj;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpecs;

import java.util.function.Function;
import java.util.function.Predicate;

import static jsonvalues.spec.JsSpecs.oneStringOf;

public class Operation {

    public static final String OP_FIELD = "op";
    public static final String AMOUNT_FIELD = "amount";

    public static final String DEPOSIT_OP = "DEPOSIT";
    public static final String WITHDRAW_OP = "WITHDRAW";

    public static final Lens<JsObj, String> opLens = JsObj.lens.str(OP_FIELD);
    public static final Predicate<JsObj> IS_DEPOSIT = opLens.exists.apply(DEPOSIT_OP::equals);
    public static final Lens<JsObj, Integer> amountLens = JsObj.lens.intNum(AMOUNT_FIELD);
    public static final Function<Integer, JsObj> makeDeposit =
            amount -> opLens.set.apply(DEPOSIT_OP)
                                .andThen(amountLens.set.apply(amount))
                                .apply(JsObj.empty());
    public static final Function<Integer, JsObj> makeWithdraw =
            amount -> opLens.set.apply(WITHDRAW_OP)
                                .andThen(amountLens.set.apply(amount))
                                .apply(JsObj.empty());
    public static final JsObjSpec spec = JsObjSpec.of(OP_FIELD,
                                                      oneStringOf(DEPOSIT_OP,
                                                                  WITHDRAW_OP
                                                                 ),
                                                      AMOUNT_FIELD,
                                                      JsSpecs.integer()
                                                     );
}
