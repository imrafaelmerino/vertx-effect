package vertx.effect.exp;

import jsonvalues.JsArray;
import jsonvalues.JsValue;
import vertx.effect.Val;
import vertx.effect.core.AbstractVal;

import static java.util.Objects.requireNonNull;

public abstract class JsArrayVal extends AbstractVal<JsArray> {


    /**
     returns a JsArrayFuture from the given head and the tail

     @param head the head
     @param tail the tail
     @return a new JsArrayFuture
     */
    @SafeVarargs
    public static JsArrayVal sequential(final Val<? extends JsValue> head,
                                        final Val<? extends JsValue>... tail
                                       ) {
        return new SequentialJsArray(requireNonNull(head),
                                     requireNonNull(tail)
        );
    }

    public static JsArrayVal sequential() {
        return SequentialJsArray.EMPTY;
    }

    /**
     returns a JsArrayFuture from the given head and the tail

     @param head the head
     @param tail the tail
     @return a new JsArrayFuture
     */
    @SafeVarargs
    public static JsArrayVal parallel(final Val<? extends JsValue> head,
                                      final Val<? extends JsValue>... tail
                                     ) {
        return new ParallelJsArray(requireNonNull(head),
                                   requireNonNull(tail)
        );
    }

    public static JsArrayVal parallel() {
        return ParallelJsArray.EMPTY;
    }

    public abstract JsArrayVal append(final Val<? extends JsValue> val);

    public abstract Val<JsValue> race();

    public abstract Val<JsValue> head();

    public abstract JsArrayVal tail();


}