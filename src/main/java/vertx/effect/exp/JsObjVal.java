package vertx.effect.exp;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.TreeMap;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import jsonvalues.JsObj;
import jsonvalues.JsValue;
import vertx.effect.Val;
import vertx.effect.core.AbstractVal;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 Represents a supplier of a vertx future which result is a json object. It has the same
 recursive structure as a json object. Each key has a future associated that it's
 executed asynchronously. When all the futures are completed, all the results are combined into
 a json object.
 */
public final class JsObjVal extends AbstractVal<JsObj> {
    private static final String ATTEMPTS_LOWER_THAN_ONE_ERROR = "attempts < 1";

    private Map<String, Val<? extends JsValue>> bindings = TreeMap.empty();

    private JsObjVal() {
    }

    private JsObjVal(final Map<String, Val<? extends JsValue>> bindings) {
        this.bindings = bindings;
    }

    /**
     static factory method to create a JsObjFuture of one mapping

     @param key the key
     @param val the mapping associated to the key
     @return a JsObjFuture
     */
    public static JsObjVal of(final String key,
                              final Val<? extends JsValue> val
                             ) {
        JsObjVal obj = new JsObjVal();
        obj.bindings = obj.bindings.put(key,
                                        val
                                       );
        return obj;
    }


    /**
     static factory method to create a JsObjFuture of one mapping

     @param key1 the first key
     @param val1 the mapping associated to the first key
     @param key2 the second key
     @param val2 the mapping associated to the second key
     @return a JsObjFuture
     */
    public static JsObjVal of(final String key1,
                              final Val<? extends JsValue> val1,
                              final String key2,
                              final Val<? extends JsValue> val2
                             ) {
        JsObjVal obj = JsObjVal.of(key1,
                                   val1
                                  );
        obj.bindings = obj.bindings.put(requireNonNull(key2),
                                        requireNonNull(val2)
                                       );
        return obj;
    }

    /**
     static factory method to create a JsObjFuture of three mappings

     @param key1 the first key
     @param val1 the mapping associated to the first key
     @param key2 the second key
     @param val2 the mapping associated to the second key
     @param key3 the third key
     @param val3 the mapping associated to the third key
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjVal of(final String key1,
                              final Val<? extends JsValue> val1,
                              final String key2,
                              final Val<? extends JsValue> val2,
                              final String key3,
                              final Val<? extends JsValue> val3
                             ) {
        JsObjVal obj = JsObjVal.of(key1,
                                   val1,
                                   key2,
                                   val2
                                  );
        obj.bindings = obj.bindings.put(requireNonNull(key3),
                                        requireNonNull(val3)
                                       );
        return obj;
    }


    /**
     static factory method to create a JsObjFuture of four mappings

     @param key1 the first key
     @param val1 the mapping associated to the first key
     @param key2 the second key
     @param val2 the mapping associated to the second key
     @param key3 the third key
     @param val3 the mapping associated to the third key
     @param key4 the fourth key
     @param val4 the mapping associated to the fourth key
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjVal of(final String key1,
                              final Val<? extends JsValue> val1,
                              final String key2,
                              final Val<? extends JsValue> val2,
                              final String key3,
                              final Val<? extends JsValue> val3,
                              final String key4,
                              final Val<? extends JsValue> val4
                             ) {
        JsObjVal obj = JsObjVal.of(key1,
                                   val1,
                                   key2,
                                   val2,
                                   key3,
                                   val3
                                  );
        obj.bindings = obj.bindings.put(requireNonNull(key4),
                                        requireNonNull(val4)
                                       );
        return obj;
    }


    /**
     static factory method to create a JsObjFuture of five mappings

     @param key1 the first key
     @param val1 the mapping associated to the first key
     @param key2 the second key
     @param val2 the mapping associated to the second key
     @param key3 the third key
     @param val3 the mapping associated to the third key
     @param key4 the fourth key
     @param val4 the mapping associated to the fourth key
     @param key5 the fifth key
     @param val5 the mapping associated to the fifth key
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjVal of(final String key1,
                              final Val<? extends JsValue> val1,
                              final String key2,
                              final Val<? extends JsValue> val2,
                              final String key3,
                              final Val<? extends JsValue> val3,
                              final String key4,
                              final Val<? extends JsValue> val4,
                              final String key5,
                              final Val<? extends JsValue> val5
                             ) {
        JsObjVal obj = JsObjVal.of(key1,
                                   val1,
                                   key2,
                                   val2,
                                   key3,
                                   val3,
                                   key4,
                                   val4
                                  );
        obj.bindings = obj.bindings.put(requireNonNull(key5),
                                        requireNonNull(val5)
                                       );
        return obj;
    }


    /**
     static factory method to create a JsObjFuture of six mappings

     @param key1 the first key
     @param val1 the mapping associated to the first key
     @param key2 the second key
     @param val2 the mapping associated to the second key
     @param key3 the third key
     @param val3 the mapping associated to the third key
     @param key4 the fourth key
     @param val4 the mapping associated to the fourth key
     @param key5 the fifth key
     @param val5 the mapping associated to the fifth key
     @param key6 the sixth key
     @param val6 the mapping associated to the sixth key
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjVal of(final String key1,
                              final Val<? extends JsValue> val1,
                              final String key2,
                              final Val<? extends JsValue> val2,
                              final String key3,
                              final Val<? extends JsValue> val3,
                              final String key4,
                              final Val<? extends JsValue> val4,
                              final String key5,
                              final Val<? extends JsValue> val5,
                              final String key6,
                              final Val<? extends JsValue> val6
                             ) {
        JsObjVal obj = JsObjVal.of(key1,
                                   val1,
                                   key2,
                                   val2,
                                   key3,
                                   val3,
                                   key4,
                                   val4,
                                   key5,
                                   val5
                                  );
        obj.bindings = obj.bindings.put(requireNonNull(key6),
                                        requireNonNull(val6)
                                       );
        return obj;
    }


    /**
     static factory method to create a JsObjFuture of seven mappings

     @param key1 the first key
     @param val1 the mapping associated to the first key
     @param key2 the second key
     @param val2 the mapping associated to the second key
     @param key3 the third key
     @param val3 the mapping associated to the third key
     @param key4 the fourth key
     @param val4 the mapping associated to the fourth key
     @param key5 the fifth key
     @param val5 the mapping associated to the fifth key
     @param key6 the sixth key
     @param val6 the mapping associated to the sixth key
     @param key7 the seventh key
     @param val7 the mapping associated to the seventh key
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjVal of(final String key1,
                              final Val<? extends JsValue> val1,
                              final String key2,
                              final Val<? extends JsValue> val2,
                              final String key3,
                              final Val<? extends JsValue> val3,
                              final String key4,
                              final Val<? extends JsValue> val4,
                              final String key5,
                              final Val<? extends JsValue> val5,
                              final String key6,
                              final Val<? extends JsValue> val6,
                              final String key7,
                              final Val<? extends JsValue> val7
                             ) {
        JsObjVal obj = JsObjVal.of(key1,
                                   val1,
                                   key2,
                                   val2,
                                   key3,
                                   val3,
                                   key4,
                                   val4,
                                   key5,
                                   val5,
                                   key6,
                                   val6
                                  );
        obj.bindings = obj.bindings.put(requireNonNull(key7),
                                        requireNonNull(val7)
                                       );
        return obj;
    }


    /**
     static factory method to create a JsObjFuture of eight mappings

     @param key1 the first key
     @param val1 the mapping associated to the first key
     @param key2 the second key
     @param val2 the mapping associated to the second key
     @param key3 the third key
     @param val3 the mapping associated to the third key
     @param key4 the fourth key
     @param val4 the mapping associated to the fourth key
     @param key5 the fifth key
     @param val5 the mapping associated to the fifth key
     @param key6 the sixth key
     @param val6 the mapping associated to the sixth key
     @param key7 the seventh key
     @param val7 the mapping associated to the seventh key
     @param key8 the eighth key
     @param val8 the mapping associated to the eighth key
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjVal of(final String key1,
                              final Val<? extends JsValue> val1,
                              final String key2,
                              final Val<? extends JsValue> val2,
                              final String key3,
                              final Val<? extends JsValue> val3,
                              final String key4,
                              final Val<? extends JsValue> val4,
                              final String key5,
                              final Val<? extends JsValue> val5,
                              final String key6,
                              final Val<? extends JsValue> val6,
                              final String key7,
                              final Val<? extends JsValue> val7,
                              final String key8,
                              final Val<? extends JsValue> val8
                             ) {
        JsObjVal obj = JsObjVal.of(key1,
                                   val1,
                                   key2,
                                   val2,
                                   key3,
                                   val3,
                                   key4,
                                   val4,
                                   key5,
                                   val5,
                                   key6,
                                   val6,
                                   key7,
                                   val7
                                  );
        obj.bindings = obj.bindings.put(requireNonNull(key8),
                                        requireNonNull(val8)
                                       );
        return obj;

    }


    /**
     static factory method to create a JsObjFuture of nine mappings

     @param key1 the first key
     @param val1 the mapping associated to the first key
     @param key2 the second key
     @param val2 the mapping associated to the second key
     @param key3 the third key
     @param val3 the mapping associated to the third key
     @param key4 the fourth key
     @param val4 the mapping associated to the fourth key
     @param key5 the fifth key
     @param val5 the mapping associated to the fifth key
     @param key6 the sixth key
     @param val6 the mapping associated to the sixth key
     @param key7 the seventh key
     @param val7 the mapping associated to the seventh key
     @param key8 the eighth key
     @param val8 the mapping associated to the eighth key
     @param key9 the ninth key
     @param val9 the mapping associated to the ninth key
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjVal of(final String key1,
                              final Val<? extends JsValue> val1,
                              final String key2,
                              final Val<? extends JsValue> val2,
                              final String key3,
                              final Val<? extends JsValue> val3,
                              final String key4,
                              final Val<? extends JsValue> val4,
                              final String key5,
                              final Val<? extends JsValue> val5,
                              final String key6,
                              final Val<? extends JsValue> val6,
                              final String key7,
                              final Val<? extends JsValue> val7,
                              final String key8,
                              final Val<? extends JsValue> val8,
                              final String key9,
                              final Val<? extends JsValue> val9
                             ) {
        JsObjVal obj = JsObjVal.of(key1,
                                   val1,
                                   key2,
                                   val2,
                                   key3,
                                   val3,
                                   key4,
                                   val4,
                                   key5,
                                   val5,
                                   key6,
                                   val6,
                                   key7,
                                   val7,
                                   key8,
                                   val8
                                  );
        obj.bindings = obj.bindings.put(requireNonNull(key9),
                                        requireNonNull(val9)
                                       );
        return obj;


    }


    /**
     static factory method to create a JsObjFuture of ten mappings

     @param key1  the first key
     @param val1  the mapping associated to the first key
     @param key2  the second key
     @param val2  the mapping associated to the second key
     @param key3  the third key
     @param val3  the mapping associated to the third key
     @param key4  the fourth key
     @param val4  the mapping associated to the fourth key
     @param key5  the fifth key
     @param val5  the mapping associated to the fifth key
     @param key6  the sixth key
     @param val6  the mapping associated to the sixth key
     @param key7  the seventh key
     @param val7  the mapping associated to the seventh key
     @param key8  the eighth key
     @param val8  the mapping associated to the eighth key
     @param key9  the ninth key
     @param val9  the mapping associated to the ninth key
     @param key10 the tenth key
     @param val10 the mapping associated to the tenth key
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjVal of(final String key1,
                              final Val<? extends JsValue> val1,
                              final String key2,
                              final Val<? extends JsValue> val2,
                              final String key3,
                              final Val<? extends JsValue> val3,
                              final String key4,
                              final Val<? extends JsValue> val4,
                              final String key5,
                              final Val<? extends JsValue> val5,
                              final String key6,
                              final Val<? extends JsValue> val6,
                              final String key7,
                              final Val<? extends JsValue> val7,
                              final String key8,
                              final Val<? extends JsValue> val8,
                              final String key9,
                              final Val<? extends JsValue> val9,
                              final String key10,
                              final Val<? extends JsValue> val10
                             ) {
        JsObjVal obj = JsObjVal.of(key1,
                                   val1,
                                   key2,
                                   val2,
                                   key3,
                                   val3,
                                   key4,
                                   val4,
                                   key5,
                                   val5,
                                   key6,
                                   val6,
                                   key7,
                                   val7,
                                   key8,
                                   val8,
                                   key9,
                                   val9
                                  );
        obj.bindings = obj.bindings.put(requireNonNull(key10),
                                        requireNonNull(val10)
                                       );
        return obj;

    }


    /**
     static factory method to create a JsObjFuture of eleven mappings

     @param key1  the first key
     @param fut1  the mapping associated to the first key
     @param key2  the second key
     @param fut2  the mapping associated to the second key
     @param key3  the third key
     @param fut3  the mapping associated to the third key
     @param key4  the fourth key
     @param fut4  the mapping associated to the fourth key
     @param key5  the fifth key
     @param fut5  the mapping associated to the fifth key
     @param key6  the sixth key
     @param fut6  the mapping associated to the sixth key
     @param key7  the seventh key
     @param fut7  the mapping associated to the seventh key
     @param key8  the eighth key
     @param fut8  the mapping associated to the eighth key
     @param key9  the ninth key
     @param fut9  the mapping associated to the ninth key
     @param key10 the tenth key
     @param fut10 the mapping associated to the eleventh key
     @param key11 the tenth key
     @param fut11 the mapping associated to the eleventh key
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjVal of(final String key1,
                              final Val<? extends JsValue> fut1,
                              final String key2,
                              final Val<? extends JsValue> fut2,
                              final String key3,
                              final Val<? extends JsValue> fut3,
                              final String key4,
                              final Val<? extends JsValue> fut4,
                              final String key5,
                              final Val<? extends JsValue> fut5,
                              final String key6,
                              final Val<? extends JsValue> fut6,
                              final String key7,
                              final Val<? extends JsValue> fut7,
                              final String key8,
                              final Val<? extends JsValue> fut8,
                              final String key9,
                              final Val<? extends JsValue> fut9,
                              final String key10,
                              final Val<? extends JsValue> fut10,
                              final String key11,
                              final Val<? extends JsValue> fut11
                             ) {
        JsObjVal obj = JsObjVal.of(key1,
                                   fut1,
                                   key2,
                                   fut2,
                                   key3,
                                   fut3,
                                   key4,
                                   fut4,
                                   key5,
                                   fut5,
                                   key6,
                                   fut6,
                                   key7,
                                   fut7,
                                   key8,
                                   fut8,
                                   key9,
                                   fut9,
                                   key10,
                                   fut10
                                  );
        obj.bindings = obj.bindings.put(requireNonNull(key11),
                                        requireNonNull(fut11)
                                       );
        return obj;

    }


    /**
     static factory method to create a JsObjFuture of twelve mappings

     @param key1  the first key
     @param val1  the mapping associated to the first key
     @param key2  the second key
     @param val2  the mapping associated to the second key
     @param key3  the third key
     @param val3  the mapping associated to the third key
     @param key4  the fourth key
     @param val4  the mapping associated to the fourth key
     @param key5  the fifth key
     @param val5  the mapping associated to the fifth key
     @param key6  the sixth key
     @param val6  the mapping associated to the sixth key
     @param key7  the seventh key
     @param val7  the mapping associated to the seventh key
     @param key8  the eighth key
     @param val8  the mapping associated to the eighth key
     @param key9  the ninth key
     @param val9  the mapping associated to the ninth key
     @param key10 the tenth key
     @param val10 the mapping associated to the eleventh key
     @param key11 the eleventh key
     @param val11 the mapping associated to the eleventh key
     @param key12 the twelfth key
     @param val12 the mapping associated to the twelfth key
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjVal of(final String key1,
                              final Val<? extends JsValue> val1,
                              final String key2,
                              final Val<? extends JsValue> val2,
                              final String key3,
                              final Val<? extends JsValue> val3,
                              final String key4,
                              final Val<? extends JsValue> val4,
                              final String key5,
                              final Val<? extends JsValue> val5,
                              final String key6,
                              final Val<? extends JsValue> val6,
                              final String key7,
                              final Val<? extends JsValue> val7,
                              final String key8,
                              final Val<? extends JsValue> val8,
                              final String key9,
                              final Val<? extends JsValue> val9,
                              final String key10,
                              final Val<? extends JsValue> val10,
                              final String key11,
                              final Val<? extends JsValue> val11,
                              final String key12,
                              final Val<? extends JsValue> val12
                             ) {
        JsObjVal obj = JsObjVal.of(key1,
                                   val1,
                                   key2,
                                   val2,
                                   key3,
                                   val3,
                                   key4,
                                   val4,
                                   key5,
                                   val5,
                                   key6,
                                   val6,
                                   key7,
                                   val7,
                                   key8,
                                   val8,
                                   key9,
                                   val9,
                                   key10,
                                   val10,
                                   key11,
                                   val11
                                  );
        obj.bindings = obj.bindings.put(requireNonNull(key12),
                                        requireNonNull(val12)
                                       );
        return obj;

    }

    /**
     static factory method to create a JsObjFuture of thirteen mappings

     @param key1  the first key
     @param val1  the mapping associated to the first key
     @param key2  the second key
     @param val2  the mapping associated to the second key
     @param key3  the third key
     @param val3  the mapping associated to the third key
     @param key4  the fourth key
     @param val4  the mapping associated to the fourth key
     @param key5  the fifth key
     @param val5  the mapping associated to the fifth key
     @param key6  the sixth key
     @param val6  the mapping associated to the sixth key
     @param key7  the seventh key
     @param val7  the mapping associated to the seventh key
     @param key8  the eighth key
     @param val8  the mapping associated to the eighth key
     @param key9  the ninth key
     @param val9  the mapping associated to the ninth key
     @param key10 the tenth key
     @param val10 the mapping associated to the eleventh key
     @param key11 the eleventh key
     @param val11 the mapping associated to the eleventh key
     @param key12 the twelfth key
     @param val12 the mapping associated to the twelfth key,
     @param key13 the thirteenth key
     @param val13 the mapping associated to the thirteenth key
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjVal of(final String key1,
                              final Val<? extends JsValue> val1,
                              final String key2,
                              final Val<? extends JsValue> val2,
                              final String key3,
                              final Val<? extends JsValue> val3,
                              final String key4,
                              final Val<? extends JsValue> val4,
                              final String key5,
                              final Val<? extends JsValue> val5,
                              final String key6,
                              final Val<? extends JsValue> val6,
                              final String key7,
                              final Val<? extends JsValue> val7,
                              final String key8,
                              final Val<? extends JsValue> val8,
                              final String key9,
                              final Val<? extends JsValue> val9,
                              final String key10,
                              final Val<? extends JsValue> val10,
                              final String key11,
                              final Val<? extends JsValue> val11,
                              final String key12,
                              final Val<? extends JsValue> val12,
                              final String key13,
                              final Val<? extends JsValue> val13
                             ) {
        JsObjVal obj = JsObjVal.of(key1,
                                   val1,
                                   key2,
                                   val2,
                                   key3,
                                   val3,
                                   key4,
                                   val4,
                                   key5,
                                   val5,
                                   key6,
                                   val6,
                                   key7,
                                   val7,
                                   key8,
                                   val8,
                                   key9,
                                   val9,
                                   key10,
                                   val10,
                                   key11,
                                   val11,
                                   key12,
                                   val12
                                  );
        obj.bindings = obj.bindings.put(requireNonNull(key13),
                                        requireNonNull(val13)
                                       );
        return obj;
    }


    /**
     static factory method to create a JsObjFuture of fourteen mappings

     @param key1  the first key
     @param val1  the mapping associated to the first key
     @param key2  the second key
     @param val2  the mapping associated to the second key
     @param key3  the third key
     @param val3  the mapping associated to the third key
     @param key4  the fourth key
     @param val4  the mapping associated to the fourth key
     @param key5  the fifth key
     @param val5  the mapping associated to the fifth key
     @param key6  the sixth key
     @param val6  the mapping associated to the sixth key
     @param key7  the seventh key
     @param val7  the mapping associated to the seventh key
     @param key8  the eighth key
     @param val8  the mapping associated to the eighth key
     @param key9  the ninth key
     @param val9  the mapping associated to the ninth key
     @param key10 the tenth key
     @param val10 the mapping associated to the eleventh key
     @param key11 the eleventh key
     @param val11 the mapping associated to the eleventh key
     @param key12 the twelfth key
     @param val12 the mapping associated to the twelfth key,
     @param key13 the thirteenth key
     @param val13 the mapping associated to the thirteenth key
     @param key14 the fourteenth key
     @param val14 the mapping associated to the fourteenth key
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjVal of(final String key1,
                              final Val<? extends JsValue> val1,
                              final String key2,
                              final Val<? extends JsValue> val2,
                              final String key3,
                              final Val<? extends JsValue> val3,
                              final String key4,
                              final Val<? extends JsValue> val4,
                              final String key5,
                              final Val<? extends JsValue> val5,
                              final String key6,
                              final Val<? extends JsValue> val6,
                              final String key7,
                              final Val<? extends JsValue> val7,
                              final String key8,
                              final Val<? extends JsValue> val8,
                              final String key9,
                              final Val<? extends JsValue> val9,
                              final String key10,
                              final Val<? extends JsValue> val10,
                              final String key11,
                              final Val<? extends JsValue> val11,
                              final String key12,
                              final Val<? extends JsValue> val12,
                              final String key13,
                              final Val<? extends JsValue> val13,
                              final String key14,
                              final Val<? extends JsValue> val14
                             ) {
        JsObjVal obj = JsObjVal.of(key1,
                                   val1,
                                   key2,
                                   val2,
                                   key3,
                                   val3,
                                   key4,
                                   val4,
                                   key5,
                                   val5,
                                   key6,
                                   val6,
                                   key7,
                                   val7,
                                   key8,
                                   val8,
                                   key9,
                                   val9,
                                   key10,
                                   val10,
                                   key11,
                                   val11,
                                   key12,
                                   val12,
                                   key13,
                                   val13
                                  );
        obj.bindings = obj.bindings.put(requireNonNull(key14),
                                        requireNonNull(val14)
                                       );
        return obj;

    }


    /**
     static factory method to create a JsObjFuture of fifteen mappings

     @param key1  the first key
     @param val1  the mapping associated to the first key
     @param key2  the second key
     @param val2  the mapping associated to the second key
     @param key3  the third key
     @param val3  the mapping associated to the third key
     @param key4  the fourth key
     @param val4  the mapping associated to the fourth key
     @param key5  the fifth key
     @param val5  the mapping associated to the fifth key
     @param key6  the sixth key
     @param val6  the mapping associated to the sixth key
     @param key7  the seventh key
     @param val7  the mapping associated to the seventh key
     @param key8  the eighth key
     @param val8  the mapping associated to the eighth key
     @param key9  the ninth key
     @param val9  the mapping associated to the ninth key
     @param key10 the tenth key
     @param val10 the mapping associated to the eleventh key
     @param key11 the eleventh key
     @param val11 the mapping associated to the eleventh key
     @param key12 the twelfth key
     @param val12 the mapping associated to the twelfth key,
     @param key13 the thirteenth key
     @param val13 the mapping associated to the thirteenth key
     @param key14 the fourteenth key
     @param val14 the mapping associated to the fourteenth key
     @param key15 the fifteenth key
     @param val15 the mapping associated to the fifteenth key
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjVal of(final String key1,
                              final Val<? extends JsValue> val1,
                              final String key2,
                              final Val<? extends JsValue> val2,
                              final String key3,
                              final Val<? extends JsValue> val3,
                              final String key4,
                              final Val<? extends JsValue> val4,
                              final String key5,
                              final Val<? extends JsValue> val5,
                              final String key6,
                              final Val<? extends JsValue> val6,
                              final String key7,
                              final Val<? extends JsValue> val7,
                              final String key8,
                              final Val<? extends JsValue> val8,
                              final String key9,
                              final Val<? extends JsValue> val9,
                              final String key10,
                              final Val<? extends JsValue> val10,
                              final String key11,
                              final Val<? extends JsValue> val11,
                              final String key12,
                              final Val<? extends JsValue> val12,
                              final String key13,
                              final Val<? extends JsValue> val13,
                              final String key14,
                              final Val<? extends JsValue> val14,
                              final String key15,
                              final Val<? extends JsValue> val15
                             ) {
        JsObjVal obj = JsObjVal.of(key1,
                                   val1,
                                   key2,
                                   val2,
                                   key3,
                                   val3,
                                   key4,
                                   val4,
                                   key5,
                                   val5,
                                   key6,
                                   val6,
                                   key7,
                                   val7,
                                   key8,
                                   val8,
                                   key9,
                                   val9,
                                   key10,
                                   val10,
                                   key11,
                                   val11,
                                   key12,
                                   val12,
                                   key13,
                                   val13,
                                   key14,
                                   val14
                                  );
        obj.bindings = obj.bindings.put(requireNonNull(key15),
                                        requireNonNull(val15)
                                       );
        return obj;

    }


    /**
     returns a new object future inserting the given future at the given key

     @param key    the given key
     @param future the given future
     @return a new JsObjFuture
     */
    public JsObjVal set(final String key,
                        final Val<? extends JsValue> future
                       ) {
        final Map<String, Val<? extends JsValue>> a = bindings.put(requireNonNull(key),
                                                                   requireNonNull(future)
                                                                  );
        return new JsObjVal(a);
    }


    /**
     it triggers the execution of all the completable futures, combining the results into a JsObj

     @return a Future of a json object
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Future<JsObj> get() {

        List<String> keys = bindings.keysIterator()
                                    .toList();


        java.util.List futures = bindings.values()
                                         .map(Supplier::get)
                                         .toJavaList();
        return CompositeFuture.all(futures)
                              .map(r -> {
                                  JsObj result = JsObj.empty();
                                  java.util.List<?> list = r.result()
                                                            .list();
                                  for (int i = 0; i < list.size(); i++) {
                                      result = result.set(keys.get(i),
                                                          ((JsValue) list.get(i))
                                                         );
                                  }

                                  return result;

                              });
    }


    @Override
    public <P> Val<P> map(final Function<JsObj, P> fn) {
        if (fn == null)
            return Cons.failure(new NullPointerException("fn is null"));
        return Cons.of(() -> get().map(fn));
    }


    @Override
    public Val<JsObj> retry(final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));

        return new JsObjVal(bindings.mapValues(it -> it.retry(attempts)));
    }


    @Override
    public Val<JsObj> retry(final int attempts,
                            final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));

        if (actionBeforeRetry == null)
            return Cons.failure(new NullPointerException("actionBeforeRetry is null"));

        return new JsObjVal(bindings.mapValues(it -> it.retry(attempts,
                                                              actionBeforeRetry
                                                             )));
    }

    @Override
    public Val<JsObj> retryIf(final Predicate<Throwable> predicate,
                              final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));
        return new JsObjVal(bindings.mapValues(it -> it.retryIf(predicate,
                                                                attempts
                                                               )));

    }


    @Override
    public Val<JsObj> retryIf(final Predicate<Throwable> predicate,
                              final int attempts,
                              final BiFunction<Throwable, Integer, Val<Void>> actionBeforeRetry) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));
        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));
        if (actionBeforeRetry == null)
            return Cons.failure(new NullPointerException("actionBeforeRetry is null"));

        return new JsObjVal(bindings.mapValues(it -> it.retryIf(predicate,
                                                                attempts,
                                                                actionBeforeRetry
                                                               )));
    }

}
