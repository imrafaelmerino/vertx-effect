package vertx.effect.httpclient.oauth;

import io.vertx.core.eventbus.ReplyException;
import jsonvalues.JsObj;

import java.util.function.Function;

import static io.vertx.core.eventbus.ReplyFailure.RECIPIENT_FAILURE;
import static java.util.Objects.requireNonNull;
import static vertx.effect.Failures.*;

public class OauthFailures {

    private OauthFailures()
    {}
    public static final Function<String, ReplyException> GET_EMPTY_AUTHORIZATION_CODE_EXCEPTION =
            errorMessage -> new ReplyException(RECIPIENT_FAILURE,
                                               HTTP_EMPTY_AUTHORIZATION_CODE,
                                               requireNonNull(errorMessage)
            );

    public static final Function<String, ReplyException> GET_EMPTY_REDIRECT_URL_CODE_EXCEPTION =
            errorMessage -> new ReplyException(RECIPIENT_FAILURE,
                                               HTTP_EMPTY_REDIRECT_URL_CODE,
                                               requireNonNull(errorMessage)
            );

    public static final Function<JsObj, ReplyException> GET_ACCESS_TOKEN_NOT_FOUND_EXCEPTION =
            resp -> new ReplyException(RECIPIENT_FAILURE,
                                       HTTP_ACCESS_TOKEN_NOT_FOUND_CODE,
                                       "Access token not found. Http response:" + resp.toString()
            );

    public static final Function<JsObj, ReplyException> GET_REFRESH_TOKEN_NOT_FOUND_EXCEPTION =
            resp -> new ReplyException(RECIPIENT_FAILURE,
                                       HTTP_REFRESH_TOKEN_NOT_FOUND_CODE,
                                       "Refresh token not found. Http response:" + resp.toString());

}
