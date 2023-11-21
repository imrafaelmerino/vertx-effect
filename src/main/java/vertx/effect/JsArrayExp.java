package vertx.effect;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public abstract sealed class JsArrayExp extends Exp<JsArray> permits JsArrayExpPar, JsArrayExpSeq {

    List<VIO<? extends JsValue>> seq = new ArrayList<>();

    /**
     * returns a JsArrayFuture from the given head and the tail
     *
     * @param head the head
     * @param tail the tail
     * @return a new JsArrayFuture
     */
    @SafeVarargs
    public static JsArrayExp seq(final VIO<? extends JsValue> head,
                                 final VIO<? extends JsValue>... tail
                                ) {
        return new JsArrayExpSeq(requireNonNull(head),
                                 requireNonNull(tail)
        );
    }

    public static JsArrayExp seq() {
        return new JsArrayExpSeq();
    }

    /**
     * returns a JsArrayFuture from the given head and the tail
     *
     * @param head the head
     * @param tail the tail
     * @return a new JsArrayFuture
     */
    @SafeVarargs
    public static JsArrayExp par(final VIO<? extends JsValue> head,
                                 final VIO<? extends JsValue>... tail
                                ) {
        return new JsArrayExpPar(requireNonNull(head),
                                 requireNonNull(tail)
        );
    }

    public static JsArrayExp par() {
        return new JsArrayExpPar();
    }

    public abstract JsArrayExp append(final VIO<? extends JsValue> val);

    public abstract VIO<JsValue> head();

    public abstract JsArrayExp tail();


}