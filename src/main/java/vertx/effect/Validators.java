package vertx.effect;

import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.spec.JsArraySpec;
import jsonvalues.spec.JsErrorPair;
import jsonvalues.spec.JsObjSpec;
import vertx.effect.exp.Cons;

import java.util.Set;
import java.util.function.Predicate;

/**
 Different constructors to create Lambdas that validates their input messages according to specs and predicates
 */
public class Validators {
    private Validators(){}

    /**
     returns a λ that validates a json object, returning it if it conforms to the given spec.
     In case of error it returns a ReplyFailure withe the code
     {@link Failures#BAD_MESSAGE_CODE}
     @param spec the spec of the schema
     @return a λ
     @see Failures#BAD_MESSAGE_CODE to see that failure is returned
     */
    public static λ<JsObj, JsObj> validateJsObj(final JsObjSpec spec) {
        return obj -> {
            Set<JsErrorPair> errors = spec.test(obj);
            if (errors.isEmpty()) return Cons.success(obj);
            else return Cons.failure(Failures.GET_BAD_MESSAGE_EXCEPTION.apply(errors.toString()));
        };
    }

    /**
     returns a λ that validates a json array, returning it if it conforms to the given spec.
     In case of error it returns a ReplyFailure withe the code {@link Failures#BAD_MESSAGE_CODE}
     @param spec the spec of the schema
     @return a λ
     @see Failures#BAD_MESSAGE_CODE to see that failure is returned
     */
    public static λ<JsArray, JsArray> validateJsArray(final JsArraySpec spec) {
        return arr -> {
            Set<JsErrorPair> errors = spec.test(arr);
            if (errors.isEmpty()) return Cons.success(arr);
            else return Cons.failure(Failures.GET_BAD_MESSAGE_EXCEPTION.apply(errors.toString()));
        };
    }

    /**
     returns a λ that validates the input against a predicate, returning the same input if the validation
     succeeds, or returning an error otherwise. In case of error it returns a ReplyFailure withe the code
     {@link Failures#BAD_MESSAGE_CODE}
     @param predicate    the predicate on which the input will be tested on
     @param errorMessage the message of the error returned
     @param <I>          the type of the input
     @return a λ
     @see Failures#BAD_MESSAGE_CODE to see that failure is returned
     */
    public static <I> λ<I, I> validate(final Predicate<I> predicate,
                                       final String errorMessage) {
        return message -> {
            if (predicate.test(message)) return Cons.success(message);
            else return Cons.failure(Failures.GET_BAD_MESSAGE_EXCEPTION.apply(errorMessage));
        };
    }


}
