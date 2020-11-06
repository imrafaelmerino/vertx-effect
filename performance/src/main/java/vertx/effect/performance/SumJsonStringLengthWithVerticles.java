package vertx.effect.performance;

import vertx.effect.Val;
import vertx.effect.exp.Cons;
import vertx.effect.exp.SeqVal;
import vertx.effect.λ;

import java.util.stream.IntStream;

import static vertx.effect.performance.MyModule.*;
import static vertx.effect.performance.benchmarks.Inputs.DELAY;


public class SumJsonStringLengthWithVerticles implements λ<Integer, Integer> {

    @Override
    public Val<Integer> apply(final Integer times) {

        return IntStream.range(0,
                               times
                              )
                        .mapToObj(n -> Cons.of(() -> generator.apply(DELAY)
                                                              .flatMap(filter.andThen(map)
                                                                             .andThen(reduce)
                                                                      )
                                                              .get()
                                              )
                                 )
                        .reduce(SeqVal.parallel(),
                                SeqVal::append,
                                SeqVal::appendAll
                               )
                        .map(list -> list
                                .map(it -> ((Integer) it))
                                .reduce(Integer::sum)
                            );

    }

}
