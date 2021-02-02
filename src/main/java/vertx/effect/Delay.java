package vertx.effect;

import java.util.Objects;

public class Delay {

    final Val<Void> val;

    Delay(Val<Void> val) {
        this.val = Objects.requireNonNull(val);
    }

    final Delay times(int n) {
        Val<Void> acc = val;
        for (int i = 0; i < n; i++) {
            acc = acc.flatMap(it -> val);
        }
        return new Delay(acc);
    }

    final Delay plus(Delay delay) {
        return new Delay(val.flatMap(it -> delay.val));
    }

}
