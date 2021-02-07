package vertx.effect.exp;

import jsonvalues.JsArray;
import vertx.effect.Val;
import vertx.effect.core.AbstractVal;

import java.util.List;

import static java.util.Objects.requireNonNull;

public abstract class ListExp<O> extends AbstractVal<List<O>> implements Exp<List<O>>{

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <O> ListExp<O> sequential(Val<O>... others) {
        if (requireNonNull(others).length == 0) return SequentialSeq.EMPTY;
        ListExp<O> seq = SequentialSeq.EMPTY;
        for (final Val<O> other : others) {
            seq = seq.append(requireNonNull(other));
        }
        return seq;
    }

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <O> ListExp<O> parallel(Val<O>... others) {
        if (requireNonNull(others).length == 0) return ParallelSeq.EMPTY;
        ListExp<O> seq = ParallelSeq.EMPTY;
        for (final Val<O> other : others) {
            seq = seq.append(requireNonNull(other));
        }
        return seq;
    }

    protected static final String ATTEMPTS_LOWER_THAN_ONE_ERROR = "attempts < 1";

    protected final io.vavr.collection.List<Val<? extends O>> seq;

    ListExp(final io.vavr.collection.List<Val<? extends O>> seq) {
        this.seq = seq;
    }

    @SuppressWarnings("unchecked")
    public Val<O> head() {
        return (Val<O>) seq.head();
    }

    public int size() {
        return seq.size();
    }

    public boolean isEmpty() {
        return seq.isEmpty();
    }

    /**
     Appends the given seq to the end of this. It uses recursion.

     @param seq the sequence to be appended to the end
     @return a new sequence
     */
    public ListExp<O> appendAll(final ListExp<O> seq) {
        if (requireNonNull(seq).isEmpty()) return this;
        if (isEmpty()) return seq;
        return this.append(seq.head())
                   .appendAll(seq.tail());
    }

    public abstract ListExp<O> tail();

    public abstract ListExp<O> append(final Val<? extends O> val);

    public abstract ListExp<O> prepend(final Val<? extends O> val);

    public abstract Val<O> race();


}
