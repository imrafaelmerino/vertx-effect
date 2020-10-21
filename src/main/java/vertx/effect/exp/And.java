package vertx.effect.exp;

import vertx.effect.Val;
import vertx.effect.core.AbstractVal;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public abstract class And extends AbstractVal<Boolean> {

    @SafeVarargs
    public static And parallel(final Val<Boolean> a,
                               final Val<Boolean>... others) {
        List<Val<Boolean>> exps = new ArrayList<>();
        exps.add(requireNonNull(a));
        for (final Val<Boolean> other : others) {
            exps.add(requireNonNull(other));
        }
        return new ParallelAnd(exps);
    }

    @SafeVarargs
    public static And sequential(final Val<Boolean> a,
                                 final Val<Boolean>... others) {
        List<Val<Boolean>> exps = new ArrayList<>();
        exps.add(requireNonNull(a));
        for (final Val<Boolean> other : others) {
            exps.add(requireNonNull(other));
        }
        return new SequentialAnd(exps);
    }

    public static Val<Boolean> of(final boolean a,
                                  final boolean... others) {
        List<Boolean> exps = new ArrayList<>();
        exps.add(a);
        for (final boolean other : others) {
            exps.add(other);
        }
        return Cons.success(exps.stream()
                                .allMatch(it -> it)
                           );
    }
}
