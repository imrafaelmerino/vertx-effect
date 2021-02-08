package vertx.effect.performance;

import vertx.effect.Val;
import vertx.effect.exp.ListExp;
import vertx.effect.λ;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static vertx.effect.performance.MyModule.*;
import static vertx.effect.performance.benchmarks.Inputs.DELAY;

public class SumJsonStringLengthWithProcesses implements λ<Integer, Integer> {

    @Override
    public Val<Integer> apply(final Integer times) {
        ListExp<Integer> reduce = IntStream.range(0,
                                                  times
                                                 )
                                           .mapToObj(
                                                   n -> Val.effect(() -> generatorProcess.apply(DELAY)
                                                                                         .flatMap(filterProcess.andThen(mapProcess)
                                                                                                               .andThen(reduceProcess)
                                                                                                 )
                                                                                         .get()
                                                                  )
                                                    )
                                           .reduce(ListExp.parallel(),
                                                   ListExp::append,
                                                   ListExp::appendAll
                                                  );
        return reduce
                .map(list -> {
                         Stream<Integer> stream = list.stream();
                         return stream
                                    .reduce(0,Integer::sum);
                     }
                    );

    }


}



