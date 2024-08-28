package vertx.effect;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public abstract sealed class AnyExp extends Exp<Boolean> permits AnyExpPar, AnyExpSeq {

    @SafeVarargs
    public static AnyExp par(final VIO<Boolean> a,
                             final VIO<Boolean>... others
                            ) {
        List<VIO<Boolean>> exps = new ArrayList<>();
        exps.add(requireNonNull(a));
        for (final VIO<Boolean> other : others) exps.add(requireNonNull(other));
        return new AnyExpPar(exps);
    }

    @SafeVarargs
    public static AnyExp seq(final VIO<Boolean> a,
                             final VIO<Boolean>... others
                            ) {
        List<VIO<Boolean>> exps = new ArrayList<>();
        exps.add(requireNonNull(a));
        for (final VIO<Boolean> other : others) exps.add(requireNonNull(other));
        return new AnyExpSeq(exps);
    }

}
