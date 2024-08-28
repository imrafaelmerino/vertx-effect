package vertx.effect;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public abstract sealed class ListExp<O> extends Exp<List<O>> permits ListExpPar, ListExpSeq {

    protected final List<VIO<? extends O>> seq;

    ListExp(final List<VIO<? extends O>> seq) {
        this.seq = seq;
    }

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <O> ListExp<O> seq(VIO<O>... others) {
        if (requireNonNull(others).length == 0) return new ListExpSeq<>();
        List<VIO<? extends O>> exp = new ArrayList<>();
        for (final VIO<O> other : others) exp.add(requireNonNull(other));
        return new ListExpSeq<>(exp);
    }

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <O> ListExp<O> par(VIO<O>... others) {
        if (requireNonNull(others).length == 0) return new ListExpPar<>();
        List<VIO<? extends O>> exp = new ArrayList<>();
        for (final VIO<O> other : others) exp.add(requireNonNull(other));
        return new ListExpPar<>(exp);
    }

    @SuppressWarnings("unchecked")
    public VIO<O> head() {
        return (VIO<O>) seq.get(0);
    }

    public int size() {
        return seq.size();
    }

    public boolean isEmpty() {
        return seq.isEmpty();
    }

    /**
     * Appends the given seq to the end of this. It uses recursion.
     *
     * @param seq the sequence to be appended to the end
     * @return a new sequence
     */
    public ListExp<O> appendAll(final ListExp<O> seq) {
        if (requireNonNull(seq).isEmpty()) return this;
        if (isEmpty()) return seq;
        return this.append(seq.head())
                   .appendAll(seq.tail());
    }

    public abstract ListExp<O> tail();

    public abstract ListExp<O> append(final VIO<? extends O> val);

    public abstract ListExp<O> prepend(final VIO<? extends O> val);


}
