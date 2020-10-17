package vertxeffect.patterns.bankaccount;

import vertxeffect.λ;
import vertxeffect.Val;
import vertxeffect.exp.Cons;
import jsonvalues.JsObj;

public class TxVerticle implements λ<Integer, Integer> {

    private final λ<JsObj, Integer> from;
    private final λ<JsObj, Integer> to;

    public TxVerticle(final λ<JsObj, Integer> from,
                      final λ<JsObj, Integer> to) {
        this.from = from;
        this.to = to;
    }


    @Override
    public Val<Integer> apply(final Integer amount) {
        return from.apply(Operation.makeWithdraw.apply(amount))
                   .flatMap(resp -> BankAccountModule.IS_OK_RESP.test(resp) ?
                                    to.apply(Operation.makeDeposit.apply(amount)) :
                                    Cons.success(resp)
                           );
    }
}
