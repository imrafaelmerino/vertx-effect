package vertx.effect.exp;

import io.vavr.Tuple2;
import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import vertx.effect.Val;
import vertx.effect.core.AbstractVal;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 Represents a supplier of a vertx future which result is a json object. It has the same
 recursive structure as a json object. Each key has a future associated that it's
 executed asynchronously. When all the futures are completed, all the results are combined into
 a json object.
 */
public final class MapVal<O> extends AbstractVal<Map<String, O>> {

    public static final MapVal<?> EMPTY = new MapVal<>();
    private static final String ATTEMPTS_LOWER_THAN_ONE_ERROR = "attempts < 1";

    private Map<String, Val<? extends O>> bindings = LinkedHashMap.empty();

    private MapVal() {
    }

    private MapVal(final Map<String, Val<? extends O>> bindings) {
        this.bindings = requireNonNull(bindings);
    }


    /**
     static factory method to create a JsObjFuture of one mapping

     @param key the key
     @param exp the mapping associated to the key
     @param <O> the type of the map values
     @return a JsObjFuture
     */
    public static <O> MapVal<O> of(final String key,
                                   final Val<? extends O> exp
                                  ) {
        MapVal<O> obj = new MapVal<>();
        obj.bindings = obj.bindings.put(requireNonNull(key),
                                        requireNonNull(exp)
                                       );
        return obj;
    }


    /**
     static factory method to create a JsObjFuture of one mapping

     @param key1 the first key
     @param exp1 the mapping associated to the first key
     @param key2 the second key
     @param exp2 the mapping associated to the second key
     @param <O>  the type of the map values
     @return a JsObjFuture
     */
    public static <O> MapVal<O> of(final String key1,
                                   final Val<? extends O> exp1,
                                   final String key2,
                                   final Val<? extends O> exp2
                                  ) {
        MapVal<O> obj = new MapVal<>();
        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(exp1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(exp2)
                                       );
        return obj;
    }

    /**
     static factory method to create a JsObjexpure of three mappings

     @param key1 the first key
     @param exp1 the mapping associated to the first key
     @param key2 the second key
     @param exp2 the mapping associated to the second key
     @param key3 the third key
     @param exp3 the mapping associated to the third key
     @param <O>  the type of the map values
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapVal<O> of(final String key1,
                                   final Val<? extends O> exp1,
                                   final String key2,
                                   final Val<? extends O> exp2,
                                   final String key3,
                                   final Val<? extends O> exp3
                                  ) {
        MapVal<O> obj = new MapVal<>();
        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(exp1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(exp2)
                                       )
                                   .put(requireNonNull(key3),
                                        requireNonNull(exp3)
                                       );
        return obj;
    }


    /**
     static factory method to create a JsObjFuture of four mappings

     @param key1 the first key
     @param exp1 the mapping associated to the first key
     @param key2 the second key
     @param exp2 the mapping associated to the second key
     @param key3 the third key
     @param exp3 the mapping associated to the third key
     @param key4 the fourth key
     @param exp4 the mapping associated to the fourth key
     @param <O>  the type of the map values
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapVal<O> of(final String key1,
                                   final Val<? extends O> exp1,
                                   final String key2,
                                   final Val<? extends O> exp2,
                                   final String key3,
                                   final Val<? extends O> exp3,
                                   final String key4,
                                   final Val<? extends O> exp4
                                  ) {
        MapVal<O> obj = new MapVal<>();
        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(exp1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(exp2)
                                       )
                                   .put(requireNonNull(key3),
                                        requireNonNull(exp3)
                                       )
                                   .put(requireNonNull(key4),
                                        requireNonNull(exp4)
                                       );
        return obj;
    }


    /**
     static factory method to create a JsObjFuture of five mappings

     @param key1 the first key
     @param exp1 the mapping associated to the first key
     @param key2 the second key
     @param exp2 the mapping associated to the second key
     @param key3 the third key
     @param exp3 the mapping associated to the third key
     @param key4 the fourth key
     @param exp4 the mapping associated to the fourth key
     @param key5 the fifth key
     @param exp5 the mapping associated to the fifth key
     @param <O>  the type of the map values
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapVal<O> of(final String key1,
                                   final Val<? extends O> exp1,
                                   final String key2,
                                   final Val<? extends O> exp2,
                                   final String key3,
                                   final Val<? extends O> exp3,
                                   final String key4,
                                   final Val<? extends O> exp4,
                                   final String key5,
                                   final Val<? extends O> exp5
                                  ) {
        MapVal<O> obj = new MapVal<>();
        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(exp1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(exp2)
                                       )
                                   .put(requireNonNull(key3),
                                        requireNonNull(exp3)
                                       )
                                   .put(requireNonNull(key4),
                                        requireNonNull(exp4)
                                       )
                                   .put(requireNonNull(key5),
                                        requireNonNull(exp5)
                                       );

        return obj;
    }


    /**
     static factory method to create a JsObjFuture of six mappings

     @param key1 the first key
     @param exp1 the mapping associated to the first key
     @param key2 the second key
     @param exp2 the mapping associated to the second key
     @param key3 the third key
     @param exp3 the mapping associated to the third key
     @param key4 the fourth key
     @param exp4 the mapping associated to the fourth key
     @param key5 the fifth key
     @param exp5 the mapping associated to the fifth key
     @param key6 the sixth key
     @param exp6 the mapping associated to the sixth key
     @param <O>  the type of the map values
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapVal<O> of(final String key1,
                                   final Val<? extends O> exp1,
                                   final String key2,
                                   final Val<? extends O> exp2,
                                   final String key3,
                                   final Val<? extends O> exp3,
                                   final String key4,
                                   final Val<? extends O> exp4,
                                   final String key5,
                                   final Val<? extends O> exp5,
                                   final String key6,
                                   final Val<? extends O> exp6
                                  ) {

        MapVal<O> obj = new MapVal<>();
        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(exp1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(exp2)
                                       )
                                   .put(requireNonNull(key3),
                                        requireNonNull(exp3)
                                       )
                                   .put(requireNonNull(key4),
                                        requireNonNull(exp4)
                                       )
                                   .put(requireNonNull(key5),
                                        requireNonNull(exp5)
                                       )
                                   .put(requireNonNull(key6),
                                        requireNonNull(exp6)
                                       );
        return obj;
    }


    /**
     static factory method to create a JsObjFuture of seven mappings

     @param key1 the first key
     @param exp1 the mapping associated to the first key
     @param key2 the second key
     @param exp2 the mapping associated to the second key
     @param key3 the third key
     @param exp3 the mapping associated to the third key
     @param key4 the fourth key
     @param exp4 the mapping associated to the fourth key
     @param key5 the fifth key
     @param exp5 the mapping associated to the fifth key
     @param key6 the sixth key
     @param exp6 the mapping associated to the sixth key
     @param key7 the seventh key
     @param exp7 the mapping associated to the seventh key
     @param <O>  the type of the map values
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapVal<O> of(final String key1,
                                   final Val<? extends O> exp1,
                                   final String key2,
                                   final Val<? extends O> exp2,
                                   final String key3,
                                   final Val<? extends O> exp3,
                                   final String key4,
                                   final Val<? extends O> exp4,
                                   final String key5,
                                   final Val<? extends O> exp5,
                                   final String key6,
                                   final Val<? extends O> exp6,
                                   final String key7,
                                   final Val<? extends O> exp7
                                  ) {
        MapVal<O> obj = new MapVal<>();
        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(exp1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(exp2)
                                       )
                                   .put(requireNonNull(key3),
                                        requireNonNull(exp3)
                                       )
                                   .put(requireNonNull(key4),
                                        requireNonNull(exp4)
                                       )
                                   .put(requireNonNull(key5),
                                        requireNonNull(exp5)
                                       )
                                   .put(requireNonNull(key6),
                                        requireNonNull(exp6)
                                       )
                                   .put(requireNonNull(key7),
                                        requireNonNull(exp7)
                                       );

        return obj;
    }


    /**
     static factory method to create a JsObjFuture of eight mappings

     @param key1 the first key
     @param exp1 the mapping associated to the first key
     @param key2 the second key
     @param exp2 the mapping associated to the second key
     @param key3 the third key
     @param exp3 the mapping associated to the third key
     @param key4 the fourth key
     @param exp4 the mapping associated to the fourth key
     @param key5 the fifth key
     @param exp5 the mapping associated to the fifth key
     @param key6 the sixth key
     @param exp6 the mapping associated to the sixth key
     @param key7 the seventh key
     @param exp7 the mapping associated to the seventh key
     @param key8 the eighth key
     @param exp8 the mapping associated to the eighth key
     @param <O>  the type of the map values
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapVal<O> of(final String key1,
                                   final Val<? extends O> exp1,
                                   final String key2,
                                   final Val<? extends O> exp2,
                                   final String key3,
                                   final Val<? extends O> exp3,
                                   final String key4,
                                   final Val<? extends O> exp4,
                                   final String key5,
                                   final Val<? extends O> exp5,
                                   final String key6,
                                   final Val<? extends O> exp6,
                                   final String key7,
                                   final Val<? extends O> exp7,
                                   final String key8,
                                   final Val<? extends O> exp8
                                  ) {
        MapVal<O> obj = new MapVal<>();
        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(exp1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(exp2)
                                       )
                                   .put(requireNonNull(key3),
                                        requireNonNull(exp3)
                                       )
                                   .put(requireNonNull(key4),
                                        requireNonNull(exp4)
                                       )
                                   .put(requireNonNull(key5),
                                        requireNonNull(exp5)
                                       )
                                   .put(requireNonNull(key6),
                                        requireNonNull(exp6)
                                       )
                                   .put(requireNonNull(key7),
                                        requireNonNull(exp7)
                                       )
                                   .put(requireNonNull(key8),
                                        requireNonNull(exp8)
                                       );

        return obj;

    }


    /**
     static factory method to create a JsObjFuture of nine mappings

     @param key1 the first key
     @param exp1 the mapping associated to the first key
     @param key2 the second key
     @param exp2 the mapping associated to the second key
     @param key3 the third key
     @param exp3 the mapping associated to the third key
     @param key4 the fourth key
     @param exp4 the mapping associated to the fourth key
     @param key5 the fifth key
     @param exp5 the mapping associated to the fifth key
     @param key6 the sixth key
     @param exp6 the mapping associated to the sixth key
     @param key7 the seventh key
     @param exp7 the mapping associated to the seventh key
     @param key8 the eighth key
     @param exp8 the mapping associated to the eighth key
     @param key9 the ninth key
     @param exp9 the mapping associated to the ninth key
     @param <O>  the type of the map values
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapVal<O> of(final String key1,
                                   final Val<? extends O> exp1,
                                   final String key2,
                                   final Val<? extends O> exp2,
                                   final String key3,
                                   final Val<? extends O> exp3,
                                   final String key4,
                                   final Val<? extends O> exp4,
                                   final String key5,
                                   final Val<? extends O> exp5,
                                   final String key6,
                                   final Val<? extends O> exp6,
                                   final String key7,
                                   final Val<? extends O> exp7,
                                   final String key8,
                                   final Val<? extends O> exp8,
                                   final String key9,
                                   final Val<? extends O> exp9
                                  ) {
        MapVal<O> obj = new MapVal<>();
        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(exp1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(exp2)
                                       )
                                   .put(requireNonNull(key3),
                                        requireNonNull(exp3)
                                       )
                                   .put(requireNonNull(key4),
                                        requireNonNull(exp4)
                                       )
                                   .put(requireNonNull(key5),
                                        requireNonNull(exp5)
                                       )
                                   .put(requireNonNull(key6),
                                        requireNonNull(exp6)
                                       )
                                   .put(requireNonNull(key7),
                                        requireNonNull(exp7)
                                       )
                                   .put(requireNonNull(key8),
                                        requireNonNull(exp8)
                                       )
                                   .put(requireNonNull(key9),
                                        requireNonNull(exp9)
                                       );
        return obj;


    }


    /**
     static factory method to create a JsObjFuture of ten mappings

     @param key1  the first key
     @param exp1  the mapping associated to the first key
     @param key2  the second key
     @param exp2  the mapping associated to the second key
     @param key3  the third key
     @param exp3  the mapping associated to the third key
     @param key4  the fourth key
     @param exp4  the mapping associated to the fourth key
     @param key5  the fifth key
     @param exp5  the mapping associated to the fifth key
     @param key6  the sixth key
     @param exp6  the mapping associated to the sixth key
     @param key7  the seventh key
     @param exp7  the mapping associated to the seventh key
     @param key8  the eighth key
     @param exp8  the mapping associated to the eighth key
     @param key9  the ninth key
     @param exp9  the mapping associated to the ninth key
     @param key10 the tenth key
     @param exp10 the mapping associated to the tenth key
     @param <O>   the type of the map values
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapVal<O> of(final String key1,
                                   final Val<? extends O> exp1,
                                   final String key2,
                                   final Val<? extends O> exp2,
                                   final String key3,
                                   final Val<? extends O> exp3,
                                   final String key4,
                                   final Val<? extends O> exp4,
                                   final String key5,
                                   final Val<? extends O> exp5,
                                   final String key6,
                                   final Val<? extends O> exp6,
                                   final String key7,
                                   final Val<? extends O> exp7,
                                   final String key8,
                                   final Val<? extends O> exp8,
                                   final String key9,
                                   final Val<? extends O> exp9,
                                   final String key10,
                                   final Val<? extends O> exp10
                                  ) {
        MapVal<O> obj = new MapVal<>();
        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(exp1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(exp2)
                                       )
                                   .put(requireNonNull(key3),
                                        requireNonNull(exp3)
                                       )
                                   .put(requireNonNull(key4),
                                        requireNonNull(exp4)
                                       )
                                   .put(requireNonNull(key5),
                                        requireNonNull(exp5)
                                       )
                                   .put(requireNonNull(key6),
                                        requireNonNull(exp6)
                                       )
                                   .put(requireNonNull(key7),
                                        requireNonNull(exp7)
                                       )
                                   .put(requireNonNull(key8),
                                        requireNonNull(exp8)
                                       )
                                   .put(requireNonNull(key9),
                                        requireNonNull(exp9)
                                       )
                                   .put(requireNonNull(key10),
                                        requireNonNull(exp10)
                                       );
        return obj;

    }


    /**
     static factory method to create a JsObjFuture of eleven mappings

     @param key1  the first key
     @param exp1  the mapping associated to the first key
     @param key2  the second key
     @param exp2  the mapping associated to the second key
     @param key3  the third key
     @param exp3  the mapping associated to the third key
     @param key4  the fourth key
     @param exp4  the mapping associated to the fourth key
     @param key5  the fifth key
     @param exp5  the mapping associated to the fifth key
     @param key6  the sixth key
     @param exp6  the mapping associated to the sixth key
     @param key7  the seventh key
     @param exp7  the mapping associated to the seventh key
     @param key8  the eighth key
     @param exp8  the mapping associated to the eighth key
     @param key9  the ninth key
     @param exp9  the mapping associated to the ninth key
     @param key10 the tenth key
     @param exp10 the mapping associated to the eleventh key
     @param key11 the tenth key
     @param exp11 the mapping associated to the eleventh key
     @param <O>   the type of the map values
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapVal<O> of(final String key1,
                                   final Val<? extends O> exp1,
                                   final String key2,
                                   final Val<? extends O> exp2,
                                   final String key3,
                                   final Val<? extends O> exp3,
                                   final String key4,
                                   final Val<? extends O> exp4,
                                   final String key5,
                                   final Val<? extends O> exp5,
                                   final String key6,
                                   final Val<? extends O> exp6,
                                   final String key7,
                                   final Val<? extends O> exp7,
                                   final String key8,
                                   final Val<? extends O> exp8,
                                   final String key9,
                                   final Val<? extends O> exp9,
                                   final String key10,
                                   final Val<? extends O> exp10,
                                   final String key11,
                                   final Val<? extends O> exp11
                                  ) {
        MapVal<O> obj = new MapVal<>();

        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(exp1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(exp2)
                                       )
                                   .put(requireNonNull(key3),
                                        requireNonNull(exp3)
                                       )
                                   .put(requireNonNull(key4),
                                        requireNonNull(exp4)
                                       )
                                   .put(requireNonNull(key5),
                                        requireNonNull(exp5)
                                       )
                                   .put(requireNonNull(key6),
                                        requireNonNull(exp6)
                                       )
                                   .put(requireNonNull(key7),
                                        requireNonNull(exp7)
                                       )
                                   .put(requireNonNull(key8),
                                        requireNonNull(exp8)
                                       )
                                   .put(requireNonNull(key9),
                                        requireNonNull(exp9)
                                       )
                                   .put(requireNonNull(key10),
                                        requireNonNull(exp10)
                                       )
                                   .put(requireNonNull(key11),
                                        requireNonNull(exp11)
                                       );

        return obj;

    }


    /**
     static factory method to create a JsObjFuture of twelve mappings

     @param key1  the first key
     @param exp1  the mapping associated to the first key
     @param key2  the second key
     @param exp2  the mapping associated to the second key
     @param key3  the third key
     @param exp3  the mapping associated to the third key
     @param key4  the fourth key
     @param exp4  the mapping associated to the fourth key
     @param key5  the fifth key
     @param exp5  the mapping associated to the fifth key
     @param key6  the sixth key
     @param exp6  the mapping associated to the sixth key
     @param key7  the seventh key
     @param exp7  the mapping associated to the seventh key
     @param key8  the eighth key
     @param exp8  the mapping associated to the eighth key
     @param key9  the ninth key
     @param exp9  the mapping associated to the ninth key
     @param key10 the tenth key
     @param exp10 the mapping associated to the eleventh key
     @param key11 the eleventh key
     @param exp11 the mapping associated to the eleventh key
     @param key12 the twelfth key
     @param exp12 the mapping associated to the twelfth key
     @param <O>   the type of the map values
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapVal<O> of(final String key1,
                                   final Val<? extends O> exp1,
                                   final String key2,
                                   final Val<? extends O> exp2,
                                   final String key3,
                                   final Val<? extends O> exp3,
                                   final String key4,
                                   final Val<? extends O> exp4,
                                   final String key5,
                                   final Val<? extends O> exp5,
                                   final String key6,
                                   final Val<? extends O> exp6,
                                   final String key7,
                                   final Val<? extends O> exp7,
                                   final String key8,
                                   final Val<? extends O> exp8,
                                   final String key9,
                                   final Val<? extends O> exp9,
                                   final String key10,
                                   final Val<? extends O> exp10,
                                   final String key11,
                                   final Val<? extends O> exp11,
                                   final String key12,
                                   final Val<? extends O> exp12
                                  ) {
        MapVal<O> obj = new MapVal<>();

        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(exp1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(exp2)
                                       )
                                   .put(requireNonNull(key3),
                                        requireNonNull(exp3)
                                       )
                                   .put(requireNonNull(key4),
                                        requireNonNull(exp4)
                                       )
                                   .put(requireNonNull(key5),
                                        requireNonNull(exp5)
                                       )
                                   .put(requireNonNull(key6),
                                        requireNonNull(exp6)
                                       )
                                   .put(requireNonNull(key7),
                                        requireNonNull(exp7)
                                       )
                                   .put(requireNonNull(key8),
                                        requireNonNull(exp8)
                                       )
                                   .put(requireNonNull(key9),
                                        requireNonNull(exp9)
                                       )
                                   .put(requireNonNull(key10),
                                        requireNonNull(exp10)
                                       )
                                   .put(requireNonNull(key11),
                                        requireNonNull(exp11)
                                       )
                                   .put(requireNonNull(key12),
                                        requireNonNull(exp12)
                                       );

        return obj;

    }

    /**
     static factory method to create a JsObjFuture of thirteen mappings

     @param key1  the first key
     @param exp1  the mapping associated to the first key
     @param key2  the second key
     @param exp2  the mapping associated to the second key
     @param key3  the third key
     @param exp3  the mapping associated to the third key
     @param key4  the fourth key
     @param exp4  the mapping associated to the fourth key
     @param key5  the fifth key
     @param exp5  the mapping associated to the fifth key
     @param key6  the sixth key
     @param exp6  the mapping associated to the sixth key
     @param key7  the seventh key
     @param exp7  the mapping associated to the seventh key
     @param key8  the eighth key
     @param exp8  the mapping associated to the eighth key
     @param key9  the ninth key
     @param exp9  the mapping associated to the ninth key
     @param key10 the tenth key
     @param exp10 the mapping associated to the eleventh key
     @param key11 the eleventh key
     @param exp11 the mapping associated to the eleventh key
     @param key12 the twelfth key
     @param exp12 the mapping associated to the twelfth key,
     @param key13 the thirteenth key
     @param exp13 the mapping associated to the thirteenth key
     @param <O>   the type of the map values
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapVal<O> of(final String key1,
                                   final Val<? extends O> exp1,
                                   final String key2,
                                   final Val<? extends O> exp2,
                                   final String key3,
                                   final Val<? extends O> exp3,
                                   final String key4,
                                   final Val<? extends O> exp4,
                                   final String key5,
                                   final Val<? extends O> exp5,
                                   final String key6,
                                   final Val<? extends O> exp6,
                                   final String key7,
                                   final Val<? extends O> exp7,
                                   final String key8,
                                   final Val<? extends O> exp8,
                                   final String key9,
                                   final Val<? extends O> exp9,
                                   final String key10,
                                   final Val<? extends O> exp10,
                                   final String key11,
                                   final Val<? extends O> exp11,
                                   final String key12,
                                   final Val<? extends O> exp12,
                                   final String key13,
                                   final Val<? extends O> exp13
                                  ) {
        MapVal<O> obj = new MapVal<>();

        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(exp1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(exp2)
                                       )
                                   .put(requireNonNull(key3),
                                        requireNonNull(exp3)
                                       )
                                   .put(requireNonNull(key4),
                                        requireNonNull(exp4)
                                       )
                                   .put(requireNonNull(key5),
                                        requireNonNull(exp5)
                                       )
                                   .put(requireNonNull(key6),
                                        requireNonNull(exp6)
                                       )
                                   .put(requireNonNull(key7),
                                        requireNonNull(exp7)
                                       )
                                   .put(requireNonNull(key8),
                                        requireNonNull(exp8)
                                       )
                                   .put(requireNonNull(key9),
                                        requireNonNull(exp9)
                                       )
                                   .put(requireNonNull(key10),
                                        requireNonNull(exp10)
                                       )
                                   .put(requireNonNull(key11),
                                        requireNonNull(exp11)
                                       )
                                   .put(requireNonNull(key12),
                                        requireNonNull(exp12)
                                       )
                                   .put(requireNonNull(key13),
                                        requireNonNull(exp13)
                                       );

        return obj;
    }


    /**
     static factory method to create a JsObjFuture of fourteen mappings

     @param key1  the first key
     @param exp1  the mapping associated to the first key
     @param key2  the second key
     @param exp2  the mapping associated to the second key
     @param key3  the third key
     @param exp3  the mapping associated to the third key
     @param key4  the fourth key
     @param exp4  the mapping associated to the fourth key
     @param key5  the fifth key
     @param exp5  the mapping associated to the fifth key
     @param key6  the sixth key
     @param exp6  the mapping associated to the sixth key
     @param key7  the seventh key
     @param exp7  the mapping associated to the seventh key
     @param key8  the eighth key
     @param exp8  the mapping associated to the eighth key
     @param key9  the ninth key
     @param exp9  the mapping associated to the ninth key
     @param key10 the tenth key
     @param exp10 the mapping associated to the eleventh key
     @param key11 the eleventh key
     @param exp11 the mapping associated to the eleventh key
     @param key12 the twelfth key
     @param exp12 the mapping associated to the twelfth key,
     @param key13 the thirteenth key
     @param exp13 the mapping associated to the thirteenth key
     @param key14 the fourteenth key
     @param exp14 the mapping associated to the fourteenth key
     @param <O>   the type of the map values
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapVal<O> of(final String key1,
                                   final Val<? extends O> exp1,
                                   final String key2,
                                   final Val<? extends O> exp2,
                                   final String key3,
                                   final Val<? extends O> exp3,
                                   final String key4,
                                   final Val<? extends O> exp4,
                                   final String key5,
                                   final Val<? extends O> exp5,
                                   final String key6,
                                   final Val<? extends O> exp6,
                                   final String key7,
                                   final Val<? extends O> exp7,
                                   final String key8,
                                   final Val<? extends O> exp8,
                                   final String key9,
                                   final Val<? extends O> exp9,
                                   final String key10,
                                   final Val<? extends O> exp10,
                                   final String key11,
                                   final Val<? extends O> exp11,
                                   final String key12,
                                   final Val<? extends O> exp12,
                                   final String key13,
                                   final Val<? extends O> exp13,
                                   final String key14,
                                   final Val<? extends O> exp14
                                  ) {
        MapVal<O> obj = new MapVal<>();

        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(exp1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(exp2)
                                       )
                                   .put(requireNonNull(key3),
                                        requireNonNull(exp3)
                                       )
                                   .put(requireNonNull(key4),
                                        requireNonNull(exp4)
                                       )
                                   .put(requireNonNull(key5),
                                        requireNonNull(exp5)
                                       )
                                   .put(requireNonNull(key6),
                                        requireNonNull(exp6)
                                       )
                                   .put(requireNonNull(key7),
                                        requireNonNull(exp7)
                                       )
                                   .put(requireNonNull(key8),
                                        requireNonNull(exp8)
                                       )
                                   .put(requireNonNull(key9),
                                        requireNonNull(exp9)
                                       )
                                   .put(requireNonNull(key10),
                                        requireNonNull(exp10)
                                       )
                                   .put(requireNonNull(key11),
                                        requireNonNull(exp11)
                                       )
                                   .put(requireNonNull(key12),
                                        requireNonNull(exp12)
                                       )
                                   .put(requireNonNull(key13),
                                        requireNonNull(exp13)
                                       )
                                   .put(requireNonNull(key14),
                                        requireNonNull(exp14)
                                       );

        return obj;

    }


    /**
     static factory method to create a JsObjFuture of fifteen mappings

     @param key1  the first key
     @param exp1  the mapping associated to the first key
     @param key2  the second key
     @param exp2  the mapping associated to the second key
     @param key3  the third key
     @param exp3  the mapping associated to the third key
     @param key4  the fourth key
     @param exp4  the mapping associated to the fourth key
     @param key5  the fifth key
     @param exp5  the mapping associated to the fifth key
     @param key6  the sixth key
     @param exp6  the mapping associated to the sixth key
     @param key7  the seventh key
     @param exp7  the mapping associated to the seventh key
     @param key8  the eighth key
     @param exp8  the mapping associated to the eighth key
     @param key9  the ninth key
     @param exp9  the mapping associated to the ninth key
     @param key10 the tenth key
     @param exp10 the mapping associated to the eleventh key
     @param key11 the eleventh key
     @param exp11 the mapping associated to the eleventh key
     @param key12 the twelfth key
     @param exp12 the mapping associated to the twelfth key,
     @param key13 the thirteenth key
     @param exp13 the mapping associated to the thirteenth key
     @param key14 the fourteenth key
     @param exp14 the mapping associated to the fourteenth key
     @param key15 the fifteenth key
     @param exp15 the mapping associated to the fifteenth key
     @param <O>   the type of the map values
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapVal<O> of(final String key1,
                                   final Val<? extends O> exp1,
                                   final String key2,
                                   final Val<? extends O> exp2,
                                   final String key3,
                                   final Val<? extends O> exp3,
                                   final String key4,
                                   final Val<? extends O> exp4,
                                   final String key5,
                                   final Val<? extends O> exp5,
                                   final String key6,
                                   final Val<? extends O> exp6,
                                   final String key7,
                                   final Val<? extends O> exp7,
                                   final String key8,
                                   final Val<? extends O> exp8,
                                   final String key9,
                                   final Val<? extends O> exp9,
                                   final String key10,
                                   final Val<? extends O> exp10,
                                   final String key11,
                                   final Val<? extends O> exp11,
                                   final String key12,
                                   final Val<? extends O> exp12,
                                   final String key13,
                                   final Val<? extends O> exp13,
                                   final String key14,
                                   final Val<? extends O> exp14,
                                   final String key15,
                                   final Val<? extends O> exp15
                                  ) {
        MapVal<O> obj = new MapVal<>();

        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(exp1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(exp2)
                                       )
                                   .put(requireNonNull(key3),
                                        requireNonNull(exp3)
                                       )
                                   .put(requireNonNull(key4),
                                        requireNonNull(exp4)
                                       )
                                   .put(requireNonNull(key5),
                                        requireNonNull(exp5)
                                       )
                                   .put(requireNonNull(key6),
                                        requireNonNull(exp6)
                                       )
                                   .put(requireNonNull(key7),
                                        requireNonNull(exp7)
                                       )
                                   .put(requireNonNull(key8),
                                        requireNonNull(exp8)
                                       )
                                   .put(requireNonNull(key9),
                                        requireNonNull(exp9)
                                       )
                                   .put(requireNonNull(key10),
                                        requireNonNull(exp10)
                                       )
                                   .put(requireNonNull(key11),
                                        requireNonNull(exp11)
                                       )
                                   .put(requireNonNull(key12),
                                        requireNonNull(exp12)
                                       )
                                   .put(requireNonNull(key13),
                                        requireNonNull(exp13)
                                       )
                                   .put(requireNonNull(key14),
                                        requireNonNull(exp14)
                                       )
                                   .put(requireNonNull(key15),
                                        requireNonNull(exp15)
                                       );


        return obj;

    }


    /**
     returns a new object future inserting the given future at the given key

     @param key the given key
     @param exp the given future
     @return a new JsObjFuture
     */
    public MapVal<O> set(final String key,
                         final Val<? extends O> exp
                        ) {
        return new MapVal<>(bindings.put(requireNonNull(key),
                                         requireNonNull(exp)
                                        ));
    }

    public boolean isEmpty() {
        return bindings.isEmpty();
    }


    @Override
    public <P> Val<P> map(final Function<Map<String, O>, P> fn) {
        if (fn == null)
            return Cons.failure(new NullPointerException("fn is null"));
        return Cons.of(() -> get().map(fn));
    }


    @Override
    public Val<Map<String, O>> retry(final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        return new MapVal<>(bindings.mapValues(it -> it.retry(attempts)));
    }


    @Override
    public Val<Map<String, O>> retry(final int attempts,
                                     final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));

        return new MapVal<>(bindings.mapValues(it -> it.retry(attempts,
                                                              actionBeforeRetry
                                                             )
                                              ));
    }

    @Override
    public Val<Map<String, O>> retryIf(final Predicate<Throwable> predicate,
                                       final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));


        return new MapVal<>(bindings.mapValues(it -> it.retryIf(predicate,
                                                                attempts
                                                               ))
        );

    }


    @Override
    public Val<Map<String, O>> retryIf(final Predicate<Throwable> predicate,
                                       final int attempts,
                                       final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {

        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));
        if (actionBeforeRetry == null)
            return Cons.failure(new NullPointerException("actionBeforeRetry is null"));

        return new MapVal<>(bindings.mapValues(it -> it.retryIf(predicate,
                                                                attempts,
                                                                actionBeforeRetry
                                                               )));
    }


    @Override
    public Future<Map<String, O>> get() {

        List<String> keys = bindings.keysIterator()
                                    .toList();
        @SuppressWarnings({"rawtypes"})
        Map<String, Future> futures = bindings.map((k, v) -> new Tuple2<>(k,
                                                                          v.get()
                                                   )
                                                  );
        return CompositeFuture.all(futures.values()
                                          .toJavaList()
                                  )
                              .map(r -> {
                                  Map<String, O> result = LinkedHashMap.empty();
                                  java.util.List<O> list = r.result()
                                                            .list();
                                  for (int i = 0; i < list.size(); i++) {
                                      result = result.put(new Tuple2<>(keys.get(i),
                                                                       list.get(i)
                                                          )
                                                         );

                                  }

                                  return result;
                              });
    }
}
