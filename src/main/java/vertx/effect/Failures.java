package vertx.effect;

import fun.optic.Prism;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.eventbus.ReplyFailure;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.vertx.core.eventbus.ReplyFailure.RECIPIENT_FAILURE;
import static io.vertx.core.eventbus.ReplyFailure.TIMEOUT;
import static java.util.Objects.requireNonNull;

/**
 * vertx-effect works with the exception {@link ReplyException} from the Vertx core. The ReplyException takes three
 * arguments: {@link ReplyFailure}, which is always {@link ReplyFailure#RECIPIENT_FAILURE}, the failure code and a
 * message. The failure codes handles from vertx-effect go from 3000 to 9999. Use codes between 0 and 2999 in your
 * application.
 */
@SuppressWarnings({"serial"})
public final class Failures {

    /**
     * Error returned by a validator when the message is not valid
     *
     * @see Validators
     */
    public static final int BAD_MESSAGE_CODE = 3000;
    /**
     *
     */
    public static final int INTERNAL_ERROR_CODE = 3001;

    /**
     * When an error occurs deploying a module
     *
     * @see VertxModule
     */
    public static final int EXCEPTION_DEPLOYING_MODULE_CODE = 3002;
    /**
     * When an error occurs deploying a verticle
     */
    public static final int EXCEPTION_DEPLOYING_VERTICLE_CODE = 3003;
    /**
     * When an error occurs undeploying a verticle
     */
    public static final int EXCEPTION_UNDEPLOYING_VERTICLE_CODE = 3004;
    /**
     * Internal error when a ReplyException is expected and another one is received
     */
    public static final int UNKNOWN_ERROR_CODE = 3999;
    /**
     * when the domain can't be resolved: wrong name or there is no internet connection.
     */
    public static final int HTTP_UNKNOWN_HOST_CODE = 4000;
    /**
     * when a connection timeout expires making a http request
     */
    public static final int HTTP_CONNECT_TIMEOUT_CODE = 4001;
    /**
     * when a request timeout expires making a http request
     */
    public static final int HTTP_REQUEST_TIMEOUT_CODE = 4002;

    public static final int HTTP_ACCESS_TOKEN_NOT_FOUND_CODE = 4003;


    /**
     * when a http connection is closed by a server. Sometimes happens that you perform a http request and the server or
     * some element in between closes the connection.
     */
    public static final int HTTP_CONNECTION_WAS_CLOSED_CODE = 4004;
    /**
     * internal error
     */
    public static final int HTTP_METHOD_NOT_IMPLEMENTED_CODE = 4098;
    /**
     * exceptions not controlled by the http client the are mapped into this code
     */
    public static final int HTTP_FAILURE_CODE = 4999;
    /**
     * A prism where the Sum is Throwable and the part is a ReplyException.
     */
    public static final Prism<Throwable, ReplyException> REPLY_EXCEPTION_PRISM =
            new Prism<>(
                    t -> {
                        if (t instanceof ReplyException) return Optional.of(((ReplyException) t));
                        else return Optional.empty();
                    },
                    v -> v
            );
    /**
     * A prism where the Sum is Throwable and the part is the ReplyException that represents verticle timeouts.
     */
    public static final Prism<Throwable, ReplyException> VERTICLE_TIMEOUT_PRISM =
            new Prism<>(
                    t -> {
                        if (t instanceof ReplyException replyException) {
                            if (replyException.failureType() == TIMEOUT)
                                return Optional.of(replyException);
                        }
                        return Optional.empty();
                    },
                    v -> v
            );
    /**
     * function that takes an error message and returns a ReplyException with the code {@link #BAD_MESSAGE_CODE} and the
     * given message
     */
    public static final Function<String, ReplyException> GET_BAD_MESSAGE_EXCEPTION =
            errorMessage -> new ReplyException(
                    RECIPIENT_FAILURE,
                    BAD_MESSAGE_CODE,
                    requireNonNull(errorMessage)
            );

    private Failures() {
    }

    /**
     * predicate to test if an exception is a ReplyException and its code is one of the specified ones
     *
     * @param code   a failure code
     * @param others a set of failure codes
     * @return a predicate to test if
     * @see ReplyException
     */
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

}
