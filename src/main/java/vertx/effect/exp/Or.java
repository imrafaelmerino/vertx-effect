package vertx.effect.exp;

import vertx.effect.Val;
import vertx.effect.core.AbstractVal;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public abstract class Or extends AbstractVal<Boolean> {

    @SafeVarargs
    public static Or parallel(final Val<Boolean> a,
                              final Val<Boolean>... others) {
        List<Val<Boolean>> exps = new ArrayList<>();
        exps.add(requireNonNull(a));
        for (final Val<Boolean> other : others) {
            exps.add(requireNonNull(other));
        }
        return new ParallelOr(exps);
    }

    @SafeVarargs
    public static Or sequential(final Val<Boolean> a,
                                final Val<Boolean>... others) {
        List<Val<Boolean>> exps = new ArrayList<>();
        exps.add(requireNonNull(a));
        for (final Val<Boolean> other : others) {
            exps.add(requireNonNull(other));
        }
        return new SequentialOr(exps);
    }

    public static Val<Boolean> of(final boolean a,
                                  final boolean... others) {
        List<Boolean> exps = new ArrayList<>();
        exps.add(a);
        for (final boolean other : others) {
            exps.add(other);
        }
        return Cons.success(exps.stream()
                                .anyMatch(it -> it)
                           );
    }
}
