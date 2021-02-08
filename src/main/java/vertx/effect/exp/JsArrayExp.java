package vertx.effect.exp;

import jsonvalues.JsArray;
import jsonvalues.JsValue;
import vertx.effect.Val;

import static java.util.Objects.requireNonNull;

public abstract class JsArrayExp extends Exp<JsArray> {


    /**
     returns a JsArrayFuture from the given head and the tail

     @param head the head
     @param tail the tail
     @return a new JsArrayFuture
     */
    @SafeVarargs
    public static JsArrayExp sequential(final Val<? extends JsValue> head,
                                        final Val<? extends JsValue>... tail
                                       ) {
        return new SequentialJsArray(requireNonNull(head),
                                     requireNonNull(tail)
        );
    }

    public static JsArrayExp sequential() {
        return SequentialJsArray.EMPTY;
    }

    /**
     returns a JsArrayFuture from the given head and the tail

     @param head the head
     @param tail the tail
     @return a new JsArrayFuture
     */
    @SafeVarargs
    public static JsArrayExp parallel(final Val<? extends JsValue> head,
                                      final Val<? extends JsValue>... tail
                                     ) {
        return new ParallelJsArrayExp(requireNonNull(head),
                                      requireNonNull(tail)
        );
    }

    public static JsArrayExp parallel() {
        return ParallelJsArrayExp.EMPTY;
    }

    public abstract JsArrayExp append(final Val<? extends JsValue> val);

    public abstract Val<JsValue> race();

    public abstract Val<JsValue> head();

    public abstract JsArrayExp tail();


}