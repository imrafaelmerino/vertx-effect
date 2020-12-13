package vertx.effect;

import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.eventbus.ReplyFailure;
import jsonvalues.Prism;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import static io.vertx.core.eventbus.ReplyFailure.RECIPIENT_FAILURE;
import static io.vertx.core.eventbus.ReplyFailure.TIMEOUT;
import static java.util.Objects.requireNonNull;

/**
 vertx-effect works with the exception {@link ReplyException} from the Vertx core. The ReplyException takes three
 arguments: {@link ReplyFailure}, which is always {@link ReplyFailure#RECIPIENT_FAILURE}, the failure code and a message. The
 failure codes handles from vertx-effect go from 3000 to 9999. Use codes between 0 and 2999 in your application.
 */
@SuppressWarnings({"serial", "squid:S110"})
public final class Failures {

    private Failures() {}

    public static final int BAD_MESSAGE_CODE = 3000;

    public static final int INTERNAL_ERROR_CODE = 3001;
    public static final int EXCEPTION_REGISTERING_CODECS_CODE = 3002;
    public static final int EXCEPTION_DEPLOYING_MODULE_CODE = 3003;
    public static final int EXCEPTION_DEPLOYING_VERTICLE_CODE = 3004;
    public static final int EXCEPTION_STOPPING_VERTICLE_CODE = 3005;
    public static final int RETRIES_EXHAUSTED_CODE = 3006;

    /**
     A ReplyException was expected, but another one was received
     */
    public static final int UNKNOWN_ERROR_CODE = 3999;


    /**
     Error that happens when the domain can't be resolved: wrong name or there is no internet connection.
     */
    public static final int HTTP_UNKNOWN_HOST_CODE = 4000;
    public static final int HTTP_CONNECT_TIMEOUT_CODE = 4001;
    public static final int HTTP_REQUEST_TIMEOUT_CODE = 4002;
    public static final int HTTP_EMPTY_AUTHORIZATION_CODE = 4003;
    public static final int HTTP_ACCESS_TOKEN_NOT_FOUND_CODE = 4004;
    public static final int HTTP_EMPTY_REDIRECT_URL_CODE = 4005;
    public static final int HTTP_REFRESH_TOKEN_NOT_FOUND_CODE = 4006;
    public static final int HTTP_CONNECTION_WAS_CLOSED_CODE = 4007;
    public static final int HTTP_METHOD_NOT_IMPLEMENTED_CODE = 4098;
    public static final int HTTP_FAILURE_CODE = 4999;


    public static Predicate<Throwable> anyOf(final int code,
                                             final int... others
                                            ) {
        return exc -> {
            if (exc instanceof ReplyException) {
                ReplyException replyException = (ReplyException) exc;
                if (replyException.failureCode() == code) return true;
                for (final int other : others) {
                    if (replyException.failureCode() == other) return true;
                }
                return false;
            }
            return false;
        };
    }

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


    public static final Function<String, ReplyException> GET_BAD_MESSAGE_EXCEPTION =
            errorMessage -> new ReplyException(RECIPIENT_FAILURE,
                                               BAD_MESSAGE_CODE,
                                               requireNonNull(errorMessage)
            );

}
