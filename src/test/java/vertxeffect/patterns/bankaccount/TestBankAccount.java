package vertxeffect.patterns.bankaccount;

import io.vavr.Tuple2;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsObj;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertxeffect.VertxRef;
import vertxeffect.Verifiers;
import vertxeffect.VerticleRef;
import vertxeffect.RegisterJsValuesCodecs;
import vertxeffect.exp.Pair;
import vertxeffect.Val;

@ExtendWith(VertxExtension.class)
public class TestBankAccount {

    private static BankAccountModule module;

    @BeforeAll
    public static void prepare(Vertx vertx,
                               VertxTestContext context
                              ) {

        final VertxRef vertxRef = new VertxRef(vertx);

        module = new BankAccountModule();

        Verifiers.<Tuple2<String, String>>verifySuccess()
                .accept(Pair.of(vertxRef.deploy(new RegisterJsValuesCodecs()),
                                vertxRef.deploy(module)
                               ),
                        context
                       );
    }


    @Test
    public void testCreateAndStopPersons(VertxTestContext context) {


        Val<VerticleRef<JsObj, Integer>> futRafaRef =
                module.registerAccount.apply(Account.nameLens.set.apply("Rafa")
                                                                 .andThen(Account.creditLens.set.apply(10000))
                                                                 .apply(JsObj.empty())
                                            );

        Val<VerticleRef<JsObj, Integer>> futPhilipRef =
                module.registerAccount.apply(Account.nameLens.set.apply("Philip")
                                                                 .andThen(Account.creditLens.set.apply(1000))
                                                                 .apply(JsObj.empty())
                                            );


        Pair.of(futRafaRef,
                futPhilipRef
               )
            .onComplete(pair -> {
                            VerticleRef<JsObj, Integer> rafaRef   = pair._1;
                            VerticleRef<JsObj, Integer> carmenRef = pair._2;
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


        Val<VerticleRef<JsObj, Integer>> futRafaRef =
                module.registerAccount.apply(Account.nameLens.set.apply("Rafa")
                                                                 .andThen(Account.creditLens.set.apply(10000))
                                                                 .apply(JsObj.empty())
                                            );

        Val<VerticleRef<JsObj, Integer>> futPhilipRef =
                module.registerAccount.apply(Account.nameLens.set.apply("Philip")
                                                                 .andThen(Account.creditLens.set.apply(1000))
                                                                 .apply(JsObj.empty())
                                            );

        Val<Integer> val = Pair.of(futRafaRef,
                                   futPhilipRef
                                  )
                               .flatMap(
                                       pair ->
                                               module.makeTx.apply(pair._1.ask(),
                                                                   pair._2.ask()
                                                                  )
                                                            .apply(20)
                                                            .onComplete(it -> {
                                                                pair._1.undeploy();
                                                                pair._2.undeploy();
                                                            })


                                       );
        Verifiers.<Integer>verifySuccess()
                .accept(val,
                        context
                       );
    }


}
