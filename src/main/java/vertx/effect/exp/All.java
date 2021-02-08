package vertx.effect.exp;

import vertx.effect.Val;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public abstract class All extends Exp<Boolean> {

    @SafeVarargs
    public static All parallel(final Val<Boolean> a,
                               final Val<Boolean>... others) {
        List<Val<Boolean>> exps = new ArrayList<>();
        exps.add(requireNonNull(a));
        for (final Val<Boolean> other : others) {
            exps.add(requireNonNull(other));
        }
        return new ParallelAll(exps);
    }

    @SafeVarargs
    public static All sequential(final Val<Boolean> a,
                                 final Val<Boolean>... others) {
        List<Val<Boolean>> exps = new ArrayList<>();
        exps.add(requireNonNull(a));
        for (final Val<Boolean> other : others) {
            exps.add(requireNonNull(other));
        }
        return new SequentialAll(exps);
    }

    public static Val<Boolean> of(final boolean a,
                                  final boolean... others) {
        List<Boolean> exps = new ArrayList<>();
        exps.add(a);
        for (final boolean other : others) {
            exps.add(other);
        }
        return Val.succeed(exps.stream()
                               .allMatch(it -> it)
                          );
    }
}
