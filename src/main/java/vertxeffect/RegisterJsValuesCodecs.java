package vertxeffect;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import jsonvalues.JsArray;
import jsonvalues.JsObj;
import vertxeffect.core.JsArrayMessageCodec;
import vertxeffect.core.JsObjMessageCodec;


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
           promise.fail(Failures.GET_EXCEPTION_REGISTERING_CODECS.apply(e));
        }


    }
}
