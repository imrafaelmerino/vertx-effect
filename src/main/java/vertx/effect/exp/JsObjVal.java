package vertx.effect.exp;

import jsonvalues.JsObj;
import jsonvalues.JsValue;
import vertx.effect.Val;
import vertx.effect.core.AbstractVal;

import static java.util.Objects.requireNonNull;

/**
 Represents a supplier of a vertx future which result is a json object. It has the same
 recursive structure as a json object. Each key has a future associated that it's
 executed asynchronously. When all the futures are completed, all the results are combined into
 a json object.
 */
public abstract class JsObjVal extends AbstractVal<JsObj> {

    /**
     static factory method to create a JsObjFuture of one mapping

     @param key the key
     @param val the mapping associated to the key
     @return a JsObjFuture
     */
    public static JsObjVal parallel(final String key,
                                    final Val<? extends JsValue> val
                                   ) {
        ParallelJsObj obj = new ParallelJsObj();
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
    public static JsObjVal parallel(final String key1,
                                    final Val<? extends JsValue> val1,
                                    final String key2,
                                    final Val<? extends JsValue> val2
                                   ) {
        ParallelJsObj obj = new ParallelJsObj();

        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(
                                           requireNonNull(key2),
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
    public static JsObjVal parallel(final String key1,
                                    final Val<? extends JsValue> val1,
                                    final String key2,
                                    final Val<? extends JsValue> val2,
                                    final String key3,
                                    final Val<? extends JsValue> val3
                                   ) {
        ParallelJsObj obj = new ParallelJsObj();

        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(
                                           requireNonNull(key2),
                                           requireNonNull(val2)
                                       )
                                   .put(
                                           requireNonNull(key3),
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
    public static JsObjVal parallel(final String key1,
                                    final Val<? extends JsValue> val1,
                                    final String key2,
                                    final Val<? extends JsValue> val2,
                                    final String key3,
                                    final Val<? extends JsValue> val3,
                                    final String key4,
                                    final Val<? extends JsValue> val4
                                   ) {
        ParallelJsObj obj = new ParallelJsObj();

        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(
                                           requireNonNull(key2),
                                           requireNonNull(val2)
                                       )
                                   .put(
                                           requireNonNull(key3),
                                           requireNonNull(val3)
                                       )
                                   .put(
                                           requireNonNull(key4),
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
    public static JsObjVal parallel(final String key1,
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
        ParallelJsObj obj = new ParallelJsObj();

        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(
                                           requireNonNull(key2),
                                           requireNonNull(val2)
                                       )
                                   .put(
                                           requireNonNull(key3),
                                           requireNonNull(val3)
                                       )
                                   .put(
                                           requireNonNull(key4),
                                           requireNonNull(val4)
                                       )
                                   .put(
                                           requireNonNull(key5),
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
    public static JsObjVal parallel(final String key1,
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
        ParallelJsObj obj = new ParallelJsObj();

        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(
                                           requireNonNull(key2),
                                           requireNonNull(val2)
                                       )
                                   .put(
                                           requireNonNull(key3),
                                           requireNonNull(val3)
                                       )
                                   .put(
                                           requireNonNull(key4),
                                           requireNonNull(val4)
                                       )
                                   .put(
                                           requireNonNull(key5),
                                           requireNonNull(val5)
                                       )
                                   .put(
                                           requireNonNull(key6),
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
    public static JsObjVal parallel(final String key1,
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
        ParallelJsObj obj = new ParallelJsObj();

        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(
                                           requireNonNull(key2),
                                           requireNonNull(val2)
                                       )
                                   .put(
                                           requireNonNull(key3),
                                           requireNonNull(val3)
                                       )
                                   .put(
                                           requireNonNull(key4),
                                           requireNonNull(val4)
                                       )
                                   .put(
                                           requireNonNull(key5),
                                           requireNonNull(val5)
                                       )
                                   .put(
                                           requireNonNull(key6),
                                           requireNonNull(val6)
                                       )
                                   .put(
                                           requireNonNull(key7),
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
    public static JsObjVal parallel(final String key1,
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
        ParallelJsObj obj = new ParallelJsObj();

        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(
                                           requireNonNull(key2),
                                           requireNonNull(val2)
                                       )
                                   .put(
                                           requireNonNull(key3),
                                           requireNonNull(val3)
                                       )
                                   .put(
                                           requireNonNull(key4),
                                           requireNonNull(val4)
                                       )
                                   .put(
                                           requireNonNull(key5),
                                           requireNonNull(val5)
                                       )
                                   .put(
                                           requireNonNull(key6),
                                           requireNonNull(val6)
                                       )
                                   .put(
                                           requireNonNull(key7),
                                           requireNonNull(val7)
                                       )
                                   .put(
                                           requireNonNull(key8),
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
    public static JsObjVal parallel(final String key1,
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
        ParallelJsObj obj = new ParallelJsObj();

        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(
                                           requireNonNull(key2),
                                           requireNonNull(val2)
                                       )
                                   .put(
                                           requireNonNull(key3),
                                           requireNonNull(val3)
                                       )
                                   .put(
                                           requireNonNull(key4),
                                           requireNonNull(val4)
                                       )
                                   .put(
                                           requireNonNull(key5),
                                           requireNonNull(val5)
                                       )
                                   .put(
                                           requireNonNull(key6),
                                           requireNonNull(val6)
                                       )
                                   .put(
                                           requireNonNull(key7),
                                           requireNonNull(val7)
                                       )
                                   .put(
                                           requireNonNull(key8),
                                           requireNonNull(val8)
                                       )
                                   .put(
                                           requireNonNull(key9),
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
    public static JsObjVal parallel(final String key1,
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
        ParallelJsObj obj = new ParallelJsObj();

        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(
                                           requireNonNull(key2),
                                           requireNonNull(val2)
                                       )
                                   .put(
                                           requireNonNull(key3),
                                           requireNonNull(val3)
                                       )
                                   .put(
                                           requireNonNull(key4),
                                           requireNonNull(val4)
                                       )
                                   .put(
                                           requireNonNull(key5),
                                           requireNonNull(val5)
                                       )
                                   .put(
                                           requireNonNull(key6),
                                           requireNonNull(val6)
                                       )
                                   .put(
                                           requireNonNull(key7),
                                           requireNonNull(val7)
                                       )
                                   .put(
                                           requireNonNull(key8),
                                           requireNonNull(val8)
                                       )
                                   .put(
                                           requireNonNull(key9),
                                           requireNonNull(val9)
                                       )
                                   .put(
                                           requireNonNull(key10),
                                           requireNonNull(val10)
                                       );
        return obj;


    }


    /**
     static factory method to create a JsObjFuture of eleven mappings

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
     @param key11 the tenth key
     @param val11 the mapping associated to the eleventh key
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjVal parallel(final String key1,
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
                                    final Val<? extends JsValue> val11
                                   ) {
        ParallelJsObj obj = new ParallelJsObj();

        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(
                                           requireNonNull(key2),
                                           requireNonNull(val2)
                                       )
                                   .put(
                                           requireNonNull(key3),
                                           requireNonNull(val3)
                                       )
                                   .put(
                                           requireNonNull(key4),
                                           requireNonNull(val4)
                                       )
                                   .put(
                                           requireNonNull(key5),
                                           requireNonNull(val5)
                                       )
                                   .put(
                                           requireNonNull(key6),
                                           requireNonNull(val6)
                                       )
                                   .put(
                                           requireNonNull(key7),
                                           requireNonNull(val7)
                                       )
                                   .put(
                                           requireNonNull(key8),
                                           requireNonNull(val8)
                                       )
                                   .put(
                                           requireNonNull(key9),
                                           requireNonNull(val9)
                                       )
                                   .put(
                                           requireNonNull(key10),
                                           requireNonNull(val10)
                                       )
                                   .put(
                                           requireNonNull(key11),
                                           requireNonNull(val11)
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
    public static JsObjVal parallel(final String key1,
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
        ParallelJsObj obj = new ParallelJsObj();

        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(
                                           requireNonNull(key2),
                                           requireNonNull(val2)
                                       )
                                   .put(
                                           requireNonNull(key3),
                                           requireNonNull(val3)
                                       )
                                   .put(
                                           requireNonNull(key4),
                                           requireNonNull(val4)
                                       )
                                   .put(
                                           requireNonNull(key5),
                                           requireNonNull(val5)
                                       )
                                   .put(
                                           requireNonNull(key6),
                                           requireNonNull(val6)
                                       )
                                   .put(
                                           requireNonNull(key7),
                                           requireNonNull(val7)
                                       )
                                   .put(
                                           requireNonNull(key8),
                                           requireNonNull(val8)
                                       )
                                   .put(
                                           requireNonNull(key9),
                                           requireNonNull(val9)
                                       )
                                   .put(
                                           requireNonNull(key10),
                                           requireNonNull(val10)
                                       )
                                   .put(
                                           requireNonNull(key11),
                                           requireNonNull(val11)
                                       )
                                   .put(
                                           requireNonNull(key12),
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
    public static JsObjVal parallel(final String key1,
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
        ParallelJsObj obj = new ParallelJsObj();

        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(
                                           requireNonNull(key2),
                                           requireNonNull(val2)
                                       )
                                   .put(
                                           requireNonNull(key3),
                                           requireNonNull(val3)
                                       )
                                   .put(
                                           requireNonNull(key4),
                                           requireNonNull(val4)
                                       )
                                   .put(
                                           requireNonNull(key5),
                                           requireNonNull(val5)
                                       )
                                   .put(
                                           requireNonNull(key6),
                                           requireNonNull(val6)
                                       )
                                   .put(
                                           requireNonNull(key7),
                                           requireNonNull(val7)
                                       )
                                   .put(
                                           requireNonNull(key8),
                                           requireNonNull(val8)
                                       )
                                   .put(
                                           requireNonNull(key9),
                                           requireNonNull(val9)
                                       )
                                   .put(
                                           requireNonNull(key10),
                                           requireNonNull(val10)
                                       )
                                   .put(
                                           requireNonNull(key11),
                                           requireNonNull(val11)
                                       )
                                   .put(
                                           requireNonNull(key12),
                                           requireNonNull(val12)
                                       )
                                   .put(
                                           requireNonNull(key13),
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
    public static JsObjVal parallel(final String key1,
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
        ParallelJsObj obj = new ParallelJsObj();

        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(
                                           requireNonNull(key2),
                                           requireNonNull(val2)
                                       )
                                   .put(
                                           requireNonNull(key3),
                                           requireNonNull(val3)
                                       )
                                   .put(
                                           requireNonNull(key4),
                                           requireNonNull(val4)
                                       )
                                   .put(
                                           requireNonNull(key5),
                                           requireNonNull(val5)
                                       )
                                   .put(
                                           requireNonNull(key6),
                                           requireNonNull(val6)
                                       )
                                   .put(
                                           requireNonNull(key7),
                                           requireNonNull(val7)
                                       )
                                   .put(
                                           requireNonNull(key8),
                                           requireNonNull(val8)
                                       )
                                   .put(
                                           requireNonNull(key9),
                                           requireNonNull(val9)
                                       )
                                   .put(
                                           requireNonNull(key10),
                                           requireNonNull(val10)
                                       )
                                   .put(
                                           requireNonNull(key11),
                                           requireNonNull(val11)
                                       )
                                   .put(
                                           requireNonNull(key12),
                                           requireNonNull(val12)
                                       )
                                   .put(
                                           requireNonNull(key13),
                                           requireNonNull(val13)
                                       )
                                   .put(
                                           requireNonNull(key14),
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
    public static JsObjVal parallel(final String key1,
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
        ParallelJsObj obj = new ParallelJsObj();

        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(
                                           requireNonNull(key2),
                                           requireNonNull(val2)
                                       )
                                   .put(
                                           requireNonNull(key3),
                                           requireNonNull(val3)
                                       )
                                   .put(
                                           requireNonNull(key4),
                                           requireNonNull(val4)
                                       )
                                   .put(
                                           requireNonNull(key5),
                                           requireNonNull(val5)
                                       )
                                   .put(
                                           requireNonNull(key6),
                                           requireNonNull(val6)
                                       )
                                   .put(
                                           requireNonNull(key7),
                                           requireNonNull(val7)
                                       )
                                   .put(
                                           requireNonNull(key8),
                                           requireNonNull(val8)
                                       )
                                   .put(
                                           requireNonNull(key9),
                                           requireNonNull(val9)
                                       )
                                   .put(
                                           requireNonNull(key10),
                                           requireNonNull(val10)
                                       )
                                   .put(
                                           requireNonNull(key11),
                                           requireNonNull(val11)
                                       )
                                   .put(
                                           requireNonNull(key12),
                                           requireNonNull(val12)
                                       )
                                   .put(
                                           requireNonNull(key13),
                                           requireNonNull(val13)
                                       )
                                   .put(
                                           requireNonNull(key14),
                                           requireNonNull(val14)
                                       )
                                   .put(
                                           requireNonNull(key15),
                                           requireNonNull(val15)
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
    public static JsObjVal sequential(final String key1,
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
        SequentialJsObj obj = new SequentialJsObj();
        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(val2)
                                       )
                                   .put(requireNonNull(key3),
                                        requireNonNull(val3)
                                       )
                                   .put(requireNonNull(key4),
                                        requireNonNull(val4)
                                       )
                                   .put(requireNonNull(key5),
                                        requireNonNull(val5)
                                       )
                                   .put(requireNonNull(key6),
                                        requireNonNull(val6)
                                       )
                                   .put(requireNonNull(key7),
                                        requireNonNull(val7)
                                       )
                                   .put(requireNonNull(key8),
                                        requireNonNull(val8)
                                       )
                                   .put(requireNonNull(key9),
                                        requireNonNull(val9)
                                       )
                                   .put(requireNonNull(key10),
                                        requireNonNull(val10)
                                       )
                                   .put(requireNonNull(key11),
                                        requireNonNull(val11)
                                       )
                                   .put(requireNonNull(key12),
                                        requireNonNull(val12)
                                       )
                                   .put(requireNonNull(key13),
                                        requireNonNull(val13)
                                       )
                                   .put(requireNonNull(key14),
                                        requireNonNull(val14)
                                       )
                                   .put(requireNonNull(key15),
                                        requireNonNull(val15)
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
    public static JsObjVal sequential(final String key1,
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
        SequentialJsObj obj = new SequentialJsObj();
        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(val2)
                                       )
                                   .put(requireNonNull(key3),
                                        requireNonNull(val3)
                                       )
                                   .put(requireNonNull(key4),
                                        requireNonNull(val4)
                                       )
                                   .put(requireNonNull(key5),
                                        requireNonNull(val5)
                                       )
                                   .put(requireNonNull(key6),
                                        requireNonNull(val6)
                                       )
                                   .put(requireNonNull(key7),
                                        requireNonNull(val7)
                                       )
                                   .put(requireNonNull(key8),
                                        requireNonNull(val8)
                                       )
                                   .put(requireNonNull(key9),
                                        requireNonNull(val9)
                                       )
                                   .put(requireNonNull(key10),
                                        requireNonNull(val10)
                                       )
                                   .put(requireNonNull(key11),
                                        requireNonNull(val11)
                                       )
                                   .put(requireNonNull(key12),
                                        requireNonNull(val12)
                                       )
                                   .put(requireNonNull(key13),
                                        requireNonNull(val13)
                                       )
                                   .put(requireNonNull(key14),
                                        requireNonNull(val14)
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
    public static JsObjVal sequential(final String key1,
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
        SequentialJsObj obj = new SequentialJsObj();
        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(val2)
                                       )
                                   .put(requireNonNull(key3),
                                        requireNonNull(val3)
                                       )
                                   .put(requireNonNull(key4),
                                        requireNonNull(val4)
                                       )
                                   .put(requireNonNull(key5),
                                        requireNonNull(val5)
                                       )
                                   .put(requireNonNull(key6),
                                        requireNonNull(val6)
                                       )
                                   .put(requireNonNull(key7),
                                        requireNonNull(val7)
                                       )
                                   .put(requireNonNull(key8),
                                        requireNonNull(val8)
                                       )
                                   .put(requireNonNull(key9),
                                        requireNonNull(val9)
                                       )
                                   .put(requireNonNull(key10),
                                        requireNonNull(val10)
                                       )
                                   .put(requireNonNull(key11),
                                        requireNonNull(val11)
                                       )
                                   .put(requireNonNull(key12),
                                        requireNonNull(val12)
                                       )
                                   .put(requireNonNull(key13),
                                        requireNonNull(val13)
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
    public static JsObjVal sequential(final String key1,
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
        SequentialJsObj obj = new SequentialJsObj();
        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(val2)
                                       )
                                   .put(requireNonNull(key3),
                                        requireNonNull(val3)
                                       )
                                   .put(requireNonNull(key4),
                                        requireNonNull(val4)
                                       )
                                   .put(requireNonNull(key5),
                                        requireNonNull(val5)
                                       )
                                   .put(requireNonNull(key6),
                                        requireNonNull(val6)
                                       )
                                   .put(requireNonNull(key7),
                                        requireNonNull(val7)
                                       )
                                   .put(requireNonNull(key8),
                                        requireNonNull(val8)
                                       )
                                   .put(requireNonNull(key9),
                                        requireNonNull(val9)
                                       )
                                   .put(requireNonNull(key10),
                                        requireNonNull(val10)
                                       )
                                   .put(requireNonNull(key11),
                                        requireNonNull(val11)
                                       )
                                   .put(requireNonNull(key12),
                                        requireNonNull(val12)
                                       );
        return obj;

    }

    /**
     static factory method to create a JsObjFuture of eleven mappings

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
     @param key11 the tenth key
     @param val11 the mapping associated to the eleventh key
     @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjVal sequential(final String key1,
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
                                      final Val<? extends JsValue> val11
                                     ) {
        SequentialJsObj obj = new SequentialJsObj();
        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(val2)
                                       )
                                   .put(requireNonNull(key3),
                                        requireNonNull(val3)
                                       )
                                   .put(requireNonNull(key4),
                                        requireNonNull(val4)
                                       )
                                   .put(requireNonNull(key5),
                                        requireNonNull(val5)
                                       )
                                   .put(requireNonNull(key6),
                                        requireNonNull(val6)
                                       )
                                   .put(requireNonNull(key7),
                                        requireNonNull(val7)
                                       )
                                   .put(requireNonNull(key8),
                                        requireNonNull(val8)
                                       )
                                   .put(requireNonNull(key9),
                                        requireNonNull(val9)
                                       )
                                   .put(requireNonNull(key10),
                                        requireNonNull(val10)
                                       )
                                   .put(requireNonNull(key11),
                                        requireNonNull(val11)
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
    public static JsObjVal sequential(final String key1,
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
        SequentialJsObj obj = new SequentialJsObj();
        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(val2)
                                       )
                                   .put(requireNonNull(key3),
                                        requireNonNull(val3)
                                       )
                                   .put(requireNonNull(key4),
                                        requireNonNull(val4)
                                       )
                                   .put(requireNonNull(key5),
                                        requireNonNull(val5)
                                       )
                                   .put(requireNonNull(key6),
                                        requireNonNull(val6)
                                       )
                                   .put(requireNonNull(key7),
                                        requireNonNull(val7)
                                       )
                                   .put(requireNonNull(key8),
                                        requireNonNull(val8)
                                       )
                                   .put(requireNonNull(key9),
                                        requireNonNull(val9)
                                       )
                                   .put(requireNonNull(key10),
                                        requireNonNull(val10)
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
    public static JsObjVal sequential(final String key1,
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
        SequentialJsObj obj = new SequentialJsObj();
        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(val2)
                                       )
                                   .put(requireNonNull(key3),
                                        requireNonNull(val3)
                                       )
                                   .put(requireNonNull(key4),
                                        requireNonNull(val4)
                                       )
                                   .put(requireNonNull(key5),
                                        requireNonNull(val5)
                                       )
                                   .put(requireNonNull(key6),
                                        requireNonNull(val6)
                                       )
                                   .put(requireNonNull(key7),
                                        requireNonNull(val7)
                                       )
                                   .put(requireNonNull(key8),
                                        requireNonNull(val8)
                                       )
                                   .put(requireNonNull(key9),
                                        requireNonNull(val9)
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
    public static JsObjVal sequential(final String key1,
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
        SequentialJsObj obj = new SequentialJsObj();
        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(val2)
                                       )
                                   .put(requireNonNull(key3),
                                        requireNonNull(val3)
                                       )
                                   .put(requireNonNull(key4),
                                        requireNonNull(val4)
                                       )
                                   .put(requireNonNull(key5),
                                        requireNonNull(val5)
                                       )
                                   .put(requireNonNull(key6),
                                        requireNonNull(val6)
                                       )
                                   .put(requireNonNull(key7),
                                        requireNonNull(val7)
                                       )
                                   .put(requireNonNull(key8),
                                        requireNonNull(val8)
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
    public static JsObjVal sequential(final String key1,
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
        SequentialJsObj obj = new SequentialJsObj();
        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(val2)
                                       )
                                   .put(requireNonNull(key3),
                                        requireNonNull(val3)
                                       )
                                   .put(requireNonNull(key4),
                                        requireNonNull(val4)
                                       )
                                   .put(requireNonNull(key5),
                                        requireNonNull(val5)
                                       )
                                   .put(requireNonNull(key6),
                                        requireNonNull(val6)
                                       )
                                   .put(requireNonNull(key7),
                                        requireNonNull(val7)
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
    public static JsObjVal sequential(final String key1,
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
        SequentialJsObj obj = new SequentialJsObj();
        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(val2)
                                       )
                                   .put(requireNonNull(key3),
                                        requireNonNull(val3)
                                       )
                                   .put(requireNonNull(key4),
                                        requireNonNull(val4)
                                       )
                                   .put(requireNonNull(key5),
                                        requireNonNull(val5)
                                       )
                                   .put(requireNonNull(key6),
                                        requireNonNull(val6)
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
    public static JsObjVal sequential(final String key1,
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
        SequentialJsObj obj = new SequentialJsObj();
        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(val2)
                                       )
                                   .put(requireNonNull(key3),
                                        requireNonNull(val3)
                                       )
                                   .put(requireNonNull(key4),
                                        requireNonNull(val4)
                                       )
                                   .put(requireNonNull(key5),
                                        requireNonNull(val5)
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
    public static JsObjVal sequential(final String key1,
                                      final Val<? extends JsValue> val1,
                                      final String key2,
                                      final Val<? extends JsValue> val2,
                                      final String key3,
                                      final Val<? extends JsValue> val3,
                                      final String key4,
                                      final Val<? extends JsValue> val4
                                     ) {
        SequentialJsObj obj = new SequentialJsObj();
        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(val2)
                                       )
                                   .put(requireNonNull(key3),
                                        requireNonNull(val3)
                                       )
                                   .put(requireNonNull(key4),
                                        requireNonNull(val4)
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
    public static JsObjVal sequential(final String key1,
                                      final Val<? extends JsValue> val1,
                                      final String key2,
                                      final Val<? extends JsValue> val2,
                                      final String key3,
                                      final Val<? extends JsValue> val3
                                     ) {
        SequentialJsObj obj = new SequentialJsObj();
        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(val2)
                                       )
                                   .put(requireNonNull(key3),
                                        requireNonNull(val3)
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
    public static JsObjVal sequential(final String key1,
                                      final Val<? extends JsValue> val1,
                                      final String key2,
                                      final Val<? extends JsValue> val2
                                     ) {
        SequentialJsObj obj = new SequentialJsObj();
        obj.bindings = obj.bindings.put(requireNonNull(key1),
                                        requireNonNull(val1)
                                       )
                                   .put(requireNonNull(key2),
                                        requireNonNull(val2)
                                       );
        return obj;
    }

    /**
     static factory method to create a JsObjFuture of one mapping

     @param key the key
     @param val the mapping associated to the key
     @return a JsObjFuture
     */
    public static JsObjVal sequential(final String key,
                                      final Val<? extends JsValue> val
                                     ) {
        SequentialJsObj obj = new SequentialJsObj();
        obj.bindings = obj.bindings.put(requireNonNull(key),
                                        requireNonNull(val)
                                       );
        return obj;
    }

    public abstract JsObjVal set(final String key,
                                 final Val<? extends JsValue> val);

}
