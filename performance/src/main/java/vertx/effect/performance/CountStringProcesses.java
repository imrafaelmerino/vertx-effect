package vertx.effect.performance;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import vertx.effect.Val;
import vertx.effect.exp.Cons;
import vertx.effect.λ;

import java.util.ArrayList;
import java.util.List;

import static vertx.effect.performance.Functions.TIME_WAITING_MS;
import static vertx.effect.performance.MyModule.*;

public class CountStringProcesses implements λ<Integer, Integer> {

    @Override
    public Val<Integer> apply(final Integer times) {
        return Cons.of(() -> {
            List<Future> futures = new ArrayList<>();
            for (int i = 0; i < times; i++) {
                futures.add(
                        generatorProcess.apply(TIME_WAITING_MS)
                                        .flatMap(filterProcess.andThen(mapProcess)
                                                              .andThen(reduceProcess)
                                                )
                                        .get()
                           );

            }
            return CompositeFuture.all(futures)
                                  .map(val -> val.list()
                                                 .stream()
                                                 .map(it -> ((Integer) it))
                                                 .reduce((r, r2) -> r + r2)
                                                 .orElse(0)
                                      );
        });

    }


}



