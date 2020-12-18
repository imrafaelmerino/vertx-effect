package vertx.effect.exp;

import io.vavr.collection.List;
import io.vertx.core.CompositeFuture;
import vertx.effect.Val;

import java.util.function.Supplier;
import java.util.stream.IntStream;

class Functions {

    private Functions() {
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    static <O> Val<O> race(List<Val<? extends O>> seq) {
        return Cons.of(() ->
                       {
                           java.util.List futures = seq.map(Supplier::get)
                                                       .toJavaList();
                           return CompositeFuture.any(futures)
                                                 .map(cf -> {
                                                          int index = IntStream.range(0,
                                                                                      futures.size()
                                                                                     )
                                                                               .filter(cf::succeeded)
                                                                               .findFirst()
                                                                               .getAsInt();//TODO
                                                          return cf.resultAt(index);
                                                      }
                                                     );
                       });
    }

}
