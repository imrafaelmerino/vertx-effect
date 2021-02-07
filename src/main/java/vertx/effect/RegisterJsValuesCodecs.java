package vertx.effect;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.ReplyException;
import jsonvalues.JsArray;
import jsonvalues.JsObj;
import vertx.effect.core.Functions;
import vertx.effect.core.JsArrayMessageCodec;
import vertx.effect.core.JsObjMessageCodec;

import static io.vertx.core.eventbus.ReplyFailure.RECIPIENT_FAILURE;


/**
 Verticle to register the codecs to be able to send json values ({@link JsObj} and {@link JsArray}) to the even bus.
 If this verticle is deployed more than once, it returns a promise that is completed with a failure saying that
 the codecs has already been registered
 */
public class RegisterJsValuesCodecs extends AbstractVerticle {

    @Override
    public void start(final Promise<Void> promise)  {
        try {
            vertx.eventBus()
                 .registerDefaultCodec(JsObj.class,
                                       JsObjMessageCodec.INSTANCE
                                      );

            vertx.eventBus()
                 .registerDefaultCodec(JsArray.class,
                                       JsArrayMessageCodec.INSTANCE
                                      );
            promise.complete();
        } catch (Exception e) {
           promise.fail(new ReplyException(RECIPIENT_FAILURE,
                                           Failures.EXCEPTION_REGISTERING_CODECS_CODE,
                                           Functions.getErrorMessage(e)));
        }


    }
}
