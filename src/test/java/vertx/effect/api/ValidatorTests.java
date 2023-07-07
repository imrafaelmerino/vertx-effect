package vertx.effect.api;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import jsonvalues.JsArray;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vertx.effect.*;
import vertx.values.codecs.RegisterJsValuesCodecs;
@SuppressWarnings("ReturnValueIgnored")
@ExtendWith(VertxExtension.class)
public class ValidatorTests {

    public static VertxRef vertxRef;

    @BeforeAll
    public static void prepare(final Vertx vertx,
                               final VertxTestContext testContext
                              ) {

        vertxRef = new VertxRef(vertx);
        testContext.completeNow();


    }

    @Test
    public void test_validator_success(VertxTestContext context) {

        Lambda<Integer, Integer> validate = Validators.validate(i -> i > 0,
                                                                "lower than zero"
                                                               );

        vertxRef.deploy("validateInteger",
                        validate,
                        new DeploymentOptions().setInstances(1).setWorker(false)
                       )
                .onSuccess(vr -> Verifiers.<Integer>verifySuccess(n -> n == 10)
                                          .accept(vr.ask()
                                                    .apply(10),
                                                  context
                                                 )
                          )
                .get();
    }

    @Test
    public void test_validator_failure(VertxTestContext context) {
        Lambda<Integer, Integer> validate = Validators.validate(i -> i > 0,
                                                                "lower than zero"
                                                               );
        vertxRef.deploy("validateInteger",
                        validate
                       )
                .onSuccess(vr -> Verifiers.<Integer>verifyFailure(Failures.REPLY_EXCEPTION_PRISM.exists.apply(it -> it.failureCode() == Failures.BAD_MESSAGE_CODE))
                                          .accept(vr.ask()
                                                    .apply(-10),
                                                  context
                                                 )
                          )
                .get();
    }

    @Test
    public void test_array_validator_failure(VertxTestContext context) {
        Lambda<JsArray, JsArray> fn = Validators.validateJsArray(JsSpecs.arrayOfStr());


        VIO<JsArray> val = vertxRef.deployVerticle(new RegisterJsValuesCodecs())
                                   .then($ -> vertxRef.deploy("validateJsArr",
                                                              fn
                                                             )
                                        )
                                   .then(vr -> vr.ask()
                                                 .apply(JsArray.of(1,
                                                                   2
                                                                  )
                                                       )
                                        );

        Verifiers.<JsArray>verifyFailure(Failures.REPLY_EXCEPTION_PRISM
                                                 .exists
                                                 .apply(it -> it.failureCode() == Failures.BAD_MESSAGE_CODE)
                                        )
                 .accept(val,
                         context
                        );
    }

}
