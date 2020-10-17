package vertxval.performance;

import jsonvalues.JsObj;
import vertxval.exp.Cons;
import vertxval.exp.Val;
import vertxval.exp.λ;

import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static vertxval.performance.Functions.generator;
import static vertxval.performance.Module.*;

public class CountStringOneVerticle implements λ<Integer, Integer> {


    private Supplier<JsObj> gen = generator.apply(new Random());

    private int total = 0;


    @Override
    public Val<Integer> apply(final Integer times) {
        return IntStream.range(0,
                               times
                              )
                        .mapToObj(it -> filter.andThen(map)
                                              .andThen(reduce)
                                              .apply(gen.get())

                                 )
                        .reduce(Cons.success(0),
                                (a, b) -> a.flatMap(n -> b.flatMap(m -> Cons.success(m + n)))
                               )
                        .onSuccess(it -> total += it);
    }
}
