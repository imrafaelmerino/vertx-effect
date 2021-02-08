package vertx.effect.patterns.bankaccount;

import jsonvalues.JsObj;
import vertx.effect.Val;
import vertx.effect.λ;

import static vertx.effect.patterns.bankaccount.Operation.IS_DEPOSIT;
import static vertx.effect.patterns.bankaccount.Operation.amountLens;

public class AccountVerticle implements λ<JsObj, Integer> {

    private int credit;

    private final λ<JsObj, JsObj> validateOp;

    public AccountVerticle(final int credit,
                           final λ<JsObj, JsObj> validateOp) {
        this.credit = credit;
        this.validateOp = validateOp;
    }

    @Override
    public Val<Integer> apply(final JsObj op) {
        return validateOp.apply(op)
                         .flatMap(o -> {
                             int amount = amountLens.get.apply(op);
                             if (IS_DEPOSIT.test(op)) return Val.succeed(credit += amount);
                             else {
                                 if (credit - amount < 0) return Val.succeed(BankAccountModule.BROKE_RESP);
                                 else return Val.succeed(credit -= amount);
                             }
                         });
    }
}
