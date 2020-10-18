package vertx.effect.patterns.bankaccount;

import io.vertx.core.DeploymentOptions;

import jsonvalues.JsObj;
import vertx.effect.Validators;
import vertx.effect.VerticleRef;
import vertx.effect.VertxModule;
import vertx.effect.λ;

import java.util.function.BiFunction;
import java.util.function.Predicate;

public class BankAccountModule extends VertxModule {


    public static final Predicate<Integer> IS_OK_RESP = i -> i >= 0;
    /**
     Code representing that an operation failed because some account doesn't have enough money
     */
    public static final int BROKE_RESP = -1;

    /**
     Account -> Fut[Actor[Operation, Credit]] where Actor[Operation, Credit] is an actor representing an Account
     it returns the credit of the account after the operation or -1 if
     Spawns a person account to send operations like deposits and withdraws
     */
    public λ<JsObj, VerticleRef<JsObj, Integer>> registerAccount =
            account -> {
                λ<JsObj, JsObj> validateOp = vertxRef.spawn("validateOp",
                                                            Validators.validateJsObj(Operation.spec)
                                                           );
                AccountVerticle lambda = new AccountVerticle(Account.creditLens.get.apply(account),
                                                             validateOp
                );
                return vertxRef.deploy(Account.nameLens.get.apply(account),
                                       lambda,
                                       new DeploymentOptions().setWorker(true).setInstances(1)
                                      );
            };


    /**
     TransactionBeforeRetry -> Code
     Performs a transactionBeforeRetry between two accounts. The transactionBeforeRetry contains the accounts and the amount of money
     to move
     */
    public BiFunction<λ<JsObj, Integer>, λ<JsObj, Integer>, λ<Integer, Integer>> makeTx =
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


