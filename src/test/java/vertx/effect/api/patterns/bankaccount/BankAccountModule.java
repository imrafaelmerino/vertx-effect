package vertx.effect.api.patterns.bankaccount;

import io.vertx.core.DeploymentOptions;
import jsonvalues.JsObj;
import vertx.effect.Lambda;
import vertx.effect.Validators;
import vertx.effect.VerticleRef;
import vertx.effect.VertxModule;

import java.util.function.BiFunction;
import java.util.function.Predicate;

public class BankAccountModule extends VertxModule {


    public static final Predicate<Integer> IS_OK_RESP = i -> i >= 0;
    /**
     * Code representing that an operation failed because some account doesn't have enough money
     */
    public static final int BROKE_RESP = -1;

    /**
     * Account -> Fut[Actor[Operation, Credit]] where Actor[Operation, Credit] is an actor representing an Account
     * it returns the credit of the account after the operation or -1 if
     * Spawns a person account to send operations like deposits and withdraws
     */
    public Lambda<JsObj, VerticleRef<JsObj, Integer>> registerAccount =
            account -> {
                Lambda<JsObj, JsObj> validateOp = vertxRef.spawn("validateOp",
                                                                 Validators.validateJsObj(Operation.spec)
                                                                );
                AccountVerticle lambda = new AccountVerticle(Account.creditLens.get.apply(account),
                                                             validateOp
                );
                return vertxRef.deploy(Account.nameLens.get.apply(account),
                                       lambda,
                                       new DeploymentOptions().setWorker(true)
                                                              .setInstances(1)
                                      );
            };


    /**
     * TransretryPolicy -> Code
     * Performs a transretryPolicy between two accounts. The transretryPolicy contains the accounts and the amount of money
     * to move
     */
    public BiFunction<Lambda<JsObj, Integer>, Lambda<JsObj, Integer>, Lambda<Integer, Integer>> makeTx =
            (from, to) -> vertxRef.spawn("tx",
                                         new TxVerticle(from,
                                                        to
                                         )
                                        );


    @Override
    protected void initialize() {
    }

    @Override
    protected void deploy() {
    }

}


