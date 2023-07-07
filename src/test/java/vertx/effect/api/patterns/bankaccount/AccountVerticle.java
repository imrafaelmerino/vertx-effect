package vertx.effect.api.patterns.bankaccount;

import jsonvalues.JsObj;
import vertx.effect.Lambda;
import vertx.effect.VIO;

import static vertx.effect.api.patterns.bankaccount.Operation.IS_DEPOSIT;
import static vertx.effect.api.patterns.bankaccount.Operation.amountLens;

public class AccountVerticle implements Lambda<JsObj, Integer> {

    private int credit;

    private final Lambda<JsObj, JsObj> validateOp;

    public AccountVerticle(final int credit,
                           final Lambda<JsObj, JsObj> validateOp
                          ) {
        this.credit = credit;
        this.validateOp = validateOp;
    }

    @Override
    public VIO<Integer> apply(final JsObj op) {
        return validateOp.apply(op)
                         .then(o -> {
                             int amount = amountLens.get.apply(op);
                             if (IS_DEPOSIT.test(op)) return VIO.succeed(credit += amount);
                             else {
                                 if (credit - amount < 0) return VIO.succeed(BankAccountModule.BROKE_RESP);
                                 else return VIO.succeed(credit -= amount);
                             }
                         });
    }
}
