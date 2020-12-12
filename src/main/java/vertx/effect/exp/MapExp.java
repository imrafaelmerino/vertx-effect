package vertx.effect.exp;

import io.vavr.collection.LinkedHashMap;
import vertx.effect.Val;
import vertx.effect.core.AbstractVal;

import static java.util.Objects.requireNonNull;


public abstract class MapExp<O> extends AbstractVal<io.vavr.collection.Map<String, O>> {

    protected static final String ATTEMPTS_LOWER_THAN_ONE_ERROR = "attempts < 1";

    protected io.vavr.collection.Map<String, Val<? extends O>> bindings = LinkedHashMap.empty();



    public boolean isEmpty() {
        return bindings.isEmpty();
    }

    @SuppressWarnings({"unchecked"})
    public static <O> MapExp<O> parallel(){
        return ParallelMapExp.EMPTY;
    }
    /**
     static factory method to create a JsObjFuture of one mapping

     @param key the key
     @param exp the mapping associated to the key
     @param <O> the type of the map values
     @return a JsObjFuture
     */
    public static <O> MapExp<O> parallel(final String key,
                                         final Val<? extends O> exp
                                        ) {
        ParallelMapExp<O> obj = new ParallelMapExp<>();
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
    public static <O> MapExp<O> parallel(final String key1,
                                         final Val<? extends O> exp1,
                                         final String key2,
                                         final Val<? extends O> exp2
                                        ) {
        ParallelMapExp<O> obj = new ParallelMapExp<>();
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
    public static <O> MapExp<O> parallel(final String key1,
                                         final Val<? extends O> exp1,
                                         final String key2,
                                         final Val<? extends O> exp2,
                                         final String key3,
                                         final Val<? extends O> exp3
                                        ) {
        ParallelMapExp<O> obj = new ParallelMapExp<>();
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
    public static <O> MapExp<O> parallel(final String key1,
                                         final Val<? extends O> exp1,
                                         final String key2,
                                         final Val<? extends O> exp2,
                                         final String key3,
                                         final Val<? extends O> exp3,
                                         final String key4,
                                         final Val<? extends O> exp4
                                        ) {
        ParallelMapExp<O> obj = new ParallelMapExp<>();
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
    public static <O> MapExp<O> parallel(final String key1,
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
        ParallelMapExp<O> obj = new ParallelMapExp<>();
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
    public static <O> MapExp<O> parallel(final String key1,
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

        ParallelMapExp<O> obj = new ParallelMapExp<>();
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
    public static <O> MapExp<O> parallel(final String key1,
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
        ParallelMapExp<O> obj = new ParallelMapExp<>();
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
    public static <O> MapExp<O> parallel(final String key1,
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
        ParallelMapExp<O> obj = new ParallelMapExp<>();
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
    public static <O> MapExp<O> parallel(final String key1,
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
        ParallelMapExp<O> obj = new ParallelMapExp<>();
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
    public static <O> MapExp<O> parallel(final String key1,
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
        ParallelMapExp<O> obj = new ParallelMapExp<>();
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
    public static <O> MapExp<O> parallel(final String key1,
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
        ParallelMapExp<O> obj = new ParallelMapExp<>();

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
    public static <O> MapExp<O> parallel(final String key1,
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
        ParallelMapExp<O> obj = new ParallelMapExp<>();

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
    public static <O> MapExp<O> parallel(final String key1,
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
        ParallelMapExp<O> obj = new ParallelMapExp<>();

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
    public static <O> MapExp<O> parallel(final String key1,
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
        ParallelMapExp<O> obj = new ParallelMapExp<>();

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
    public static <O> MapExp<O> parallel(final String key1,
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
        ParallelMapExp<O> obj = new ParallelMapExp<>();

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

    public abstract MapExp<O> set(final String key,
                                  final Val<? extends O> exp
                                 );

    @SuppressWarnings({"unchecked"})
    public static <O> MapExp<O> sequential(){
        return SequentialMapExp.EMPTY;
    }

    /**
     static factory method to create a JsObjFuture of one mapping

     @param key the key
     @param exp the mapping associated to the key
     @param <O> the type of the map values
     @return a JsObjFuture
     */
    public static <O> MapExp<O> sequential(final String key,
                                           final Val<? extends O> exp
                                          ) {
        SequentialMapExp<O> obj = new SequentialMapExp<>();
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
    public static <O> MapExp<O> sequential(final String key1,
                                           final Val<? extends O> exp1,
                                           final String key2,
                                           final Val<? extends O> exp2
                                          ) {
        SequentialMapExp<O> obj = new SequentialMapExp<>();
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
    public static <O> MapExp<O> sequential(final String key1,
                                           final Val<? extends O> exp1,
                                           final String key2,
                                           final Val<? extends O> exp2,
                                           final String key3,
                                           final Val<? extends O> exp3
                                          ) {
        SequentialMapExp<O> obj = new SequentialMapExp<>();
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
    public static <O> MapExp<O> sequential(final String key1,
                                           final Val<? extends O> exp1,
                                           final String key2,
                                           final Val<? extends O> exp2,
                                           final String key3,
                                           final Val<? extends O> exp3,
                                           final String key4,
                                           final Val<? extends O> exp4
                                          ) {
        SequentialMapExp<O> obj = new SequentialMapExp<>();
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
    public static <O> MapExp<O> sequential(final String key1,
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
        SequentialMapExp<O> obj = new SequentialMapExp<>();
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
    public static <O> MapExp<O> sequential(final String key1,
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

        SequentialMapExp<O> obj = new SequentialMapExp<>();
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
    public static <O> MapExp<O> sequential(final String key1,
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
        SequentialMapExp<O> obj = new SequentialMapExp<>();
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
    public static <O> MapExp<O> sequential(final String key1,
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
        SequentialMapExp<O> obj = new SequentialMapExp<>();
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
    public static <O> MapExp<O> sequential(final String key1,
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
        SequentialMapExp<O> obj = new SequentialMapExp<>();
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
    public static <O> MapExp<O> sequential(final String key1,
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
        SequentialMapExp<O> obj = new SequentialMapExp<>();
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
    public static <O> MapExp<O> sequential(final String key1,
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
        SequentialMapExp<O> obj = new SequentialMapExp<>();

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
    public static <O> MapExp<O> sequential(final String key1,
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
        SequentialMapExp<O> obj = new SequentialMapExp<>();

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
    public static <O> MapExp<O> sequential(final String key1,
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
        SequentialMapExp<O> obj = new SequentialMapExp<>();

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
    public static <O> MapExp<O> sequential(final String key1,
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
        SequentialMapExp<O> obj = new SequentialMapExp<>();

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
    public static <O> MapExp<O> sequential(final String key1,
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
        SequentialMapExp<O> obj = new SequentialMapExp<>();

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


}
