package vertx.effect;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public abstract sealed class AllExp extends Exp<Boolean> permits AllExpPar, AllExpSeq {

    @SafeVarargs
    public static AllExp par(final VIO<Boolean> a,
                             final VIO<Boolean>... others
                            ) {
        List<VIO<Boolean>> exps = new ArrayList<>();
        exps.add(requireNonNull(a));
        for (final VIO<Boolean> other : others) exps.add(requireNonNull(other));
        return new AllExpPar(exps);
    }

    @SafeVarargs
    public static AllExp seq(final VIO<Boolean> a,
                             final VIO<Boolean>... others
                            ) {
        List<VIO<Boolean>> exps = new ArrayList<>();
        exps.add(requireNonNull(a));
        for (VIO<Boolean> other : others) exps.add(requireNonNull(other));
        return new AllExpSeq(exps);
    }

}
