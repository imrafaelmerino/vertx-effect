package vertx.effect;

import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.spec.JsArraySpec;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.SpecError;

import java.util.List;
import java.util.function.Predicate;

/**
 * Different constructors to create Lambdas that validates their input messages according to specs and predicates
 */
public final class Validators {
    private Validators() {
    }

    /**
     * returns a λ that validates a json object, returning it if it conforms to the given spec.
     * In case of error it returns a ReplyFailure with the code
     * {@link Failures#BAD_MESSAGE_CODE}
     *
     * @param spec the spec of the schema
     * @return a λ
     * @see Failures#BAD_MESSAGE_CODE to see that failure is returned
     */
    public static Lambda<JsObj, JsObj> validateJsObj(final JsObjSpec spec) {
        return obj -> {
            List<SpecError> errors = spec.test(obj);
            if (errors.isEmpty()) return VIO.succeed(obj);
            else return VIO.fail(Failures.GET_BAD_MESSAGE_EXCEPTION.apply(errors.toString()));
        };
    }

    /**
     * returns a λ that validates a json array, returning it if it conforms to the given spec.
     * In case of error it returns a ReplyFailure withe the code {@link Failures#BAD_MESSAGE_CODE}
     *
     * @param spec the spec of the schema
     * @return a λ
     * @see Failures#BAD_MESSAGE_CODE to see that failure is returned
     */
    public static Lambda<JsArray, JsArray> validateJsArray(final JsArraySpec spec) {
        return arr -> {
            List<SpecError> errors = spec.test(arr);
            if (errors.isEmpty()) return VIO.succeed(arr);
            else return VIO.fail(Failures.GET_BAD_MESSAGE_EXCEPTION.apply(errors.toString()));
        };
    }

    /**
     * returns a λ that validates the input against a predicate, returning the same input if the validation
     * succeeds, or returning an error otherwise. In case of error it returns a ReplyFailure with the code
     * {@link Failures#BAD_MESSAGE_CODE}
     *
     * @param predicate    the predicate on which the input will be tested on
     * @param errorMessage the message of the error returned
     * @param <I>          the type of the input
     * @return a λ
     * @see Failures#BAD_MESSAGE_CODE to see that failure is returned
     */
    public static <I> Lambda<I, I> validate(final Predicate<I> predicate,
                                            final String errorMessage
                                           ) {
        return message -> predicate.test(message) ?
                VIO.succeed(message) :
                VIO.fail(Failures.GET_BAD_MESSAGE_EXCEPTION.apply(errorMessage));
    }


}
