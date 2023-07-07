package vertx.effect.api.patterns.bankaccount;

import fun.tuple.Pair;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsObj;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.*;
import vertx.effect.api.Verifiers;
import vertx.effect.PairExp;
import vertx.values.codecs.RegisterJsValuesCodecs;
@SuppressWarnings("ReturnValueIgnored")
@ExtendWith(VertxExtension.class)
public class TestBankAccount {

    private static BankAccountModule module;

    @BeforeAll
    public static void prepare(Vertx vertx,
                               VertxTestContext context
                              ) {

        final VertxRef vertxRef = new VertxRef(vertx);

        module = new BankAccountModule();

        Verifiers.<Pair<String, String>>verifySuccess()
                 .accept(PairExp.seq(vertxRef.deployVerticle(new RegisterJsValuesCodecs()),
                                     vertxRef.deployVerticle(module)
                                    ),
                         context
                        );
    }


    @Test
    public void testCreateAndStopPersons(VertxTestContext context) {


        VIO<VerticleRef<JsObj, Integer>> futRafaRef =
                module.registerAccount.apply(Account.nameLens.set.apply("Rafa")
                                                                 .andThen(Account.creditLens.set.apply(10000))
                                                                 .apply(JsObj.empty())
                                            );

        VIO<VerticleRef<JsObj, Integer>> futPhilipRef =
                module.registerAccount.apply(Account.nameLens.set.apply("Philip")
                                                                 .andThen(Account.creditLens.set.apply(1000))
                                                                 .apply(JsObj.empty())
                                            );


        PairExp.par(futRafaRef,
                    futPhilipRef
                   )
               .onComplete(pair -> {
                               VerticleRef<JsObj, Integer> rafaRef = pair.first();
                               VerticleRef<JsObj, Integer> carmenRef = pair.second();
                               rafaRef.undeploy();
                               carmenRef.undeploy();
                               context.completeNow();
                           },
                           context::failNow
                          )
               .get();
    }


    @Test
    public void testMakeTx(VertxTestContext context) {


        VIO<VerticleRef<JsObj, Integer>> futRafaRef =
                module.registerAccount.apply(Account.nameLens.set.apply("Rafa")
                                                                 .andThen(Account.creditLens.set.apply(10000))
                                                                 .apply(JsObj.empty())
                                            );

        VIO<VerticleRef<JsObj, Integer>> futPhilipRef =
                module.registerAccount.apply(Account.nameLens.set.apply("Philip")
                                                                 .andThen(Account.creditLens.set.apply(1000))
                                                                 .apply(JsObj.empty())
                                            );

        VIO<Integer> val = PairExp.par(futRafaRef,
                                       futPhilipRef
                                      )
                                  .then(
                                          pair ->
                                                  module.makeTx.apply(pair.first().ask(),
                                                                      pair.second().ask()
                                                                     )
                                                               .apply(20)
                                                               .onComplete(it -> {
                                                                   pair.first().undeploy();
                                                                   pair.second().undeploy();
                                                               })


                                       );
        Verifiers.<Integer>verifySuccess()
                 .accept(val,
                         context
                        );
    }


}
