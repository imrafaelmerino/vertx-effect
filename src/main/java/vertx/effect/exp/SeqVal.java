package vertx.effect.exp;

import io.vavr.collection.List;
import vertx.effect.Val;
import vertx.effect.core.AbstractVal;

import static java.util.Objects.requireNonNull;

public abstract class SeqVal<O> extends AbstractVal<List<O>> {

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <O> SeqVal<O> sequential(Val<O>... others) {
        if (requireNonNull(others).length == 0) return SequentialSeq.EMPTY;
        SeqVal<O> seq = SequentialSeq.EMPTY;
        for (final Val<O> other : others) {
            seq = seq.append(requireNonNull(other));
        }
        return seq;
    }

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <O> SeqVal<O> parallel(Val<O>... others) {
        if (requireNonNull(others).length == 0) return ParallelSeq.EMPTY;
        SeqVal<O> seq = ParallelSeq.EMPTY;
        for (final Val<O> other : others) {
            seq = seq.append(requireNonNull(other));
        }
        return seq;
    }

    protected static final String ATTEMPTS_LOWER_THAN_ONE_ERROR = "attempts < 1";

    protected final List<Val<? extends O>> seq;

    public SeqVal(final List<Val<? extends O>> seq) {
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
    public SeqVal<O> appendAll(final SeqVal<O> seq) {
        if (requireNonNull(seq).isEmpty()) return this;
        if (isEmpty()) return seq;
        return this.append(seq.head())
                   .appendAll(seq.tail());
    }

    public abstract SeqVal<O> tail();

    public abstract SeqVal<O> append(final Val<? extends O> val);

    public abstract SeqVal<O> prepend(final Val<? extends O> val);

    public abstract Val<O> race();

}
