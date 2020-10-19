package vertx.effect;

import io.vertx.core.VertxException;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.eventbus.ReplyFailure;
import jsonvalues.JsObj;
import jsonvalues.Prism;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;

import static io.vertx.core.eventbus.ReplyFailure.RECIPIENT_FAILURE;
import static io.vertx.core.eventbus.ReplyFailure.TIMEOUT;
import static io.vertx.core.net.impl.ConnectionBase.CLOSED_EXCEPTION;
import static java.util.Objects.requireNonNull;

/**
 vertx-effect works with the exception {@link ReplyException} from the Vertx core. The ReplyException takes three
 arguments: {@link ReplyFailure}, which is always {@link ReplyFailure#RECIPIENT_FAILURE}, the failure code and a message. The
 failure codes handles from vertx-effect go from 3000 to 9999. Use codes between 0 and 2999 in your application.
 */
@SuppressWarnings({"serial", "squid:S110"})
public final class Failures {

    private Failures() {
    }

    public static final int BAD_MESSAGE_CODE = 3000;
    public static final int INTERNAL_ERROR_CODE = 3001;
    public static final int EXCEPTION_REGISTERING_CODECS_CODE = 3002;
    public static final int EXCEPTION_DEPLOYING_MODULE_CODE = 3003;
    public static final int EXCEPTION_DEPLOYING_VERTICLE_CODE = 3004;
    public static final int EXCEPTION_STOPPING_VERTICLE_CODE = 3005;
    public static final int EMPTY_CONTEXT_CODE = 3006;

    /**
     A ReplyException was expected, but another one was received
     */
    public static final int UNKNOWN_ERROR_CODE = 3999;


    /**
     Error that happens when the domain can't be resolved: wrong name or there is no internet connection.
     */
    public static final int UNKNOWN_HOST_CODE = 4000;
    public static final int CONNECT_TIMEOUT_CODE = 4001;
    public static final int REQUEST_TIMEOUT_CODE = 4002;
    public static final int EMPTY_AUTHORIZATION_CODE = 4003;
    public static final int ACCESS_TOKEN_NOT_FOUND = 4004;
    public static final int EMPTY_REDIRECT_URL_CODE = 4005;
    public static final int REFRESH_TOKEN_NOT_FOUND = 4006;
    public static final int HTTP_METHOD_NOT_IMPLEMENTED_CODE = 4098;
    public static final int DEFAULT_HTTP_EXCEPTION_CODE = 4999;


    public static final Prism<Throwable, ReplyException> UNKNOWN_HOST_PRISM =
            new Prism<>(
                    t -> {
                        if (t instanceof ReplyException) {
                            ReplyException replyException = (ReplyException) t;
                            if (replyException.failureCode() == UNKNOWN_HOST_CODE)
                                return Optional.of(replyException);
                            return Optional.empty();
                        }
                        else return Optional.empty();
                    },
                    v -> v
            );


    public static final Prism<Throwable, ReplyException> HTTP_CONNECT_TIMEOUT_PRISM =
            new Prism<>(
                    t -> {
                        if (t instanceof ReplyException) {
                            ReplyException replyException = (ReplyException) t;
                            if (replyException.failureCode() == CONNECT_TIMEOUT_CODE)
                                return Optional.of(replyException);
                            return Optional.empty();
                        }
                        else return Optional.empty();
                    },
                    v -> v
            );


    public static final Prism<Throwable, ReplyException> HTTP_TIMEOUTS_PRISM =
            new Prism<>(
                    t -> {
                        if (t instanceof ReplyException) {
                            ReplyException replyException = (ReplyException) t;
                            if (replyException.failureCode() == CONNECT_TIMEOUT_CODE
                                    || replyException.failureCode() == REQUEST_TIMEOUT_CODE)
                                return Optional.of(replyException);
                            return Optional.empty();
                        }
                        else return Optional.empty();
                    },
                    v -> v
            );

    public static final Prism<Throwable, VertxException> TCP_CONNECTION_CLOSED_PRISM =
            new Prism<>(
                    t -> {
                        if (t.equals(CLOSED_EXCEPTION))
                            return Optional.of(CLOSED_EXCEPTION);
                        else return Optional.empty();
                    },
                    v -> v
            );
    public static final Prism<Throwable, ReplyException> REPLY_EXCEPTION_PRISM =
            new Prism<>(
                    t -> {
                        if (t instanceof ReplyException) return Optional.of(((ReplyException) t));
                        else return Optional.empty();
                    },
                    v -> v
            );

    public static final Prism<Throwable, ReplyException> VERTICLE_TIMEOUT_PRISM =
            new Prism<>(
                    t -> {
                        if (t instanceof ReplyException) {
                            ReplyException replyException = (ReplyException) t;
                            if (replyException.failureType() == TIMEOUT)
                                return Optional.of(replyException);
                        }
                        return Optional.empty();
                    },
                    v -> v
            );

    public static final Function<String, ReplyException> GET_EMPTY_CONTEXT_EXCEPTION =
            address -> new ReplyException(RECIPIENT_FAILURE,
                                          EMPTY_CONTEXT_CODE,
                                          String.format("Verticle listening on %s received empty headers",
                                                        address
                                                       )
            );

    public static final Function<Throwable, ReplyException> GET_UNKNOWN_ERROR_EXCEPTION =
            exc -> new ReplyException(RECIPIENT_FAILURE,
                                      UNKNOWN_ERROR_CODE,
                                      getMessage(exc)
            );


    public static final Function<JsObj, ReplyException> GET_ACCESS_TOKEN_NOT_FOUND_EXCEPTION =
            resp -> new ReplyException(RECIPIENT_FAILURE,
                                       ACCESS_TOKEN_NOT_FOUND,
                                       "Access token not found. Http response:" + resp.toString()
            );

    public static final Function<JsObj, ReplyException> GET_REFRESH_TOKEN_NOT_FOUND_EXCEPTION =
            resp -> new ReplyException(RECIPIENT_FAILURE,
                                       REFRESH_TOKEN_NOT_FOUND,
                                       "Refresh token not found. Http response:" + resp.toString()
            );


    public static final Function<String, ReplyException> GET_EMPTY_AUTHORIZATION_CODE_EXCEPTION =
            errorMessage -> new ReplyException(RECIPIENT_FAILURE,
                                               EMPTY_AUTHORIZATION_CODE,
                                               requireNonNull(errorMessage)
            );

    public static final Function<String, ReplyException> GET_EMPTY_REDIRECT_URL_CODE_EXCEPTION =
            errorMessage -> new ReplyException(RECIPIENT_FAILURE,
                                               EMPTY_REDIRECT_URL_CODE,
                                               requireNonNull(errorMessage)
            );

    public static final Function<String, ReplyException> GET_BAD_MESSAGE_EXCEPTION =
            errorMessage -> new ReplyException(RECIPIENT_FAILURE,
                                               BAD_MESSAGE_CODE,
                                               requireNonNull(errorMessage)
            );

    public static final Function<Throwable, ReplyException> GET_INTERNAL_ERROR_EXCEPTION =
            exc -> new ReplyException(RECIPIENT_FAILURE,
                                      INTERNAL_ERROR_CODE,
                                      getMessage(exc)
            );


    public static final Function<Throwable, ReplyException> GET_EXCEPTION_REGISTERING_CODECS =
            exc -> new ReplyException(RECIPIENT_FAILURE,
                                      EXCEPTION_REGISTERING_CODECS_CODE,
                                      getMessage(exc)
            );

    public static final Function<Throwable, ReplyException> GET_EXCEPTION_DEPLOYING_MODULE =
            exc -> new ReplyException(RECIPIENT_FAILURE,
                                      EXCEPTION_DEPLOYING_MODULE_CODE,
                                      getMessage(exc)
            );

    public static final Function<Throwable, ReplyException> GET_EXCEPTION_DEPLOYING_VERTICLE =
            exc -> new ReplyException(RECIPIENT_FAILURE,
                                      EXCEPTION_DEPLOYING_VERTICLE_CODE,
                                      getMessage(exc)
            );

    public static final Function<Throwable, ReplyException> GET_EXCEPTION_STOPPING_VERTICLE =
            exc -> new ReplyException(RECIPIENT_FAILURE,
                                      EXCEPTION_STOPPING_VERTICLE_CODE,
                                      getMessage(exc)
            );

    public static final IntFunction<ReplyException> GET_HTTP_METHOD_NOT_IMPLEMENTED_EXCEPTION =
            method -> new ReplyException(RECIPIENT_FAILURE,
                                         HTTP_METHOD_NOT_IMPLEMENTED_CODE,
                                         "The method " + method + "is not supported. Supported types are in enum HttpReqBuilder.TYPE. Use a provided builder to make requests."
            );

    public static final Function<Throwable, ReplyException> GET_HTTP_REPLY_EXCEPTION =
            exc -> {
                switch (exc.getClass()
                           .getSimpleName()) {
                    case "ConnectTimeoutException":
                        return new ReplyException(RECIPIENT_FAILURE,
                                                  CONNECT_TIMEOUT_CODE,
                                                  getMessage(exc)
                        );
                    case "UnknownHostException":
                        return new ReplyException(RECIPIENT_FAILURE,
                                                  UNKNOWN_HOST_CODE,
                                                  getMessage(exc)
                        );
                    case "NoStackTraceTimeoutException":
                        return new ReplyException(RECIPIENT_FAILURE,
                                                  REQUEST_TIMEOUT_CODE,
                                                  getMessage(exc)
                        );

                    default:
                        return new ReplyException(RECIPIENT_FAILURE,
                                                  DEFAULT_HTTP_EXCEPTION_CODE,
                                                  getMessage(exc)
                        );
                }
            };


    private static String getMessage(final Throwable e) {
        return e.getStackTrace().length == 0 ?
               e.toString() :
               e.toString() + "@" + Arrays.toString(e.getStackTrace());
    }


}
