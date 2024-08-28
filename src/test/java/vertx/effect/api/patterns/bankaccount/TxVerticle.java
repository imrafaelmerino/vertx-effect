package vertx.effect.api.patterns.bankaccount;

import vertx.effect.Lambda;
import vertx.effect.VIO;
import jsonvalues.JsObj;

public class TxVerticle implements Lambda<Integer, Integer> {

    private final Lambda<JsObj, Integer> from;
    private final Lambda<JsObj, Integer> to;

    public TxVerticle(final Lambda<JsObj, Integer> from,
                      final Lambda<JsObj, Integer> to
                     ) {
        this.from = from;
        this.to = to;
    }


    @Override
    public VIO<Integer> apply(final Integer amount) {
        return from.apply(Operation.makeWithdraw.apply(amount))
                   .then(resp -> BankAccountModule.IS_OK_RESP.test(resp) ?
                           to.apply(Operation.makeDeposit.apply(amount)) :
                           VIO.succeed(resp)
                        );
    }
}
