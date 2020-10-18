package vertx.effect;

import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.spec.JsArraySpec;
import jsonvalues.spec.JsErrorPair;
import jsonvalues.spec.JsObjSpec;
import vertx.effect.exp.Cons;

import java.util.Set;
import java.util.function.Predicate;

public class Validators {
    private Validators(){}

    /**
     returns a λ that validates the input json object, returning the same input if it conforms the given spec

     @param spec the spec of the schema that has to conform the input
     @return a λ
     */
    public static λ<JsObj, JsObj> validateJsObj(final JsObjSpec spec) {
        return obj -> {
            Set<JsErrorPair> errors = spec.test(obj);
            if (errors.isEmpty()) return Cons.success(obj);
            else return Cons.failure(Failures.GET_BAD_MESSAGE_EXCEPTION.apply(errors.toString()));
        };
    }

    /**
     returns a λ that validates the input json array, returning the same input if it conforms the given spec

     @param spec the spec of the schema that has to conform the input
     @return a λ
     */
    public static λ<JsArray, JsArray> validateJsArray(final JsArraySpec spec) {
        return arr -> {
            Set<JsErrorPair> errors = spec.test(arr);
            if (errors.isEmpty()) return Cons.success(arr);
            else return Cons.failure(Failures.GET_BAD_MESSAGE_EXCEPTION.apply(errors.toString()));
        };
    }

    /**
     returns a λ that validates the input against a  predicate, returning the same input if the validations
     succeeds, or returning an error otherwise

     @param predicate    the predicate on which the input will be tested on
     @param errorMessage the message of the error returned
     @param <I>          the type of the input
     @return a λ
     */
    public static <I> λ<I, I> validate(final Predicate<I> predicate,
                                       final String errorMessage) {
        return message -> {
            if (predicate.test(message)) return Cons.success(message);
            else return Cons.failure(Failures.GET_BAD_MESSAGE_EXCEPTION.apply(errorMessage));
        };
    }


}
