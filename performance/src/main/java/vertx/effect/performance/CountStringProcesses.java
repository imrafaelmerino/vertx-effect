package vertx.effect;

import vertx.effect.exp.Cons;

import java.util.stream.IntStream;

import static vertx.effect.Functions.TIME_WAITING_MS;
import static vertx.effect.Module.*;

public class CountStringProcesses implements λ<Integer, Integer> {
    private int total = 0;


    @Override
    public Val<Integer> apply(final Integer times) {
        return IntStream.range(0,
                               times
                              )
                        .mapToObj(it -> generatorProcess.apply(TIME_WAITING_MS)
                                                        .flatMap(obj -> filterProcess.andThen(mapProcess)
                                                                                     .andThen(reduceProcess)
                                                                                     .apply(obj))


                                 )
                        .reduce(Cons.success(0),
                                (a, b) -> a.flatMap(n -> b.flatMap(m -> Cons.success(m + n)))
                               )
                        .onSuccess(it -> total += it);


    }


}
