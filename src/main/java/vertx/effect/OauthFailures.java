package vertx.effect;

import io.vertx.core.eventbus.ReplyException;
import jsonvalues.JsObj;

import java.util.function.Function;

import static io.vertx.core.eventbus.ReplyFailure.RECIPIENT_FAILURE;
import static vertx.effect.Failures.HTTP_ACCESS_TOKEN_NOT_FOUND_CODE;

public class OauthFailures {

    public static final Function<JsObj, ReplyException> ACCESS_TOKEN_NOT_FOUND_EXCEPTION =
            resp -> new ReplyException(RECIPIENT_FAILURE,
                                       HTTP_ACCESS_TOKEN_NOT_FOUND_CODE,
                                       "Access token not found. Http response:" + resp.toString()
            );


    private OauthFailures() {
    }


}
