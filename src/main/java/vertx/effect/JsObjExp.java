package vertx.effect;

import jsonvalues.JsObj;
import jsonvalues.JsValue;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * Represents a supplier of a vertx future which result is a json object. It has the same recursive structure as a json
 * object. Each key has a future associated that it's executed asynchronously. When all the futures are completed, all
 * the results are combined into a json object.
 */
public abstract sealed class JsObjExp extends Exp<JsObj> permits JsObjExpPar, JsObjExpSeq {

    Map<String, VIO<? extends JsValue>> bindings = new LinkedHashMap<>();

    /**
     * static factory method to create a JsObjFuture of one mapping
     *
     * @param key the key
     * @param val the mapping associated to the key
     * @return a JsObjFuture
     */
    public static JsObjExp par(final String key,
                               final VIO<? extends JsValue> val
                              ) {
        JsObjExpPar obj = new JsObjExpPar();
        obj.bindings.put(key,
                         val
                        );
        return obj;
    }


    /**
     * static factory method to create a JsObjFuture of one mapping
     *
     * @param key1 the first key
     * @param val1 the mapping associated to the first key
     * @param key2 the second key
     * @param val2 the mapping associated to the second key
     * @return a JsObjFuture
     */
    public static JsObjExp par(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2
                              ) {
        JsObjExp obj = par(key1, val1);
        obj.bindings.put(key2, val2);
        return obj;
    }

    /**
     * static factory method to create a JsObjFuture of three mappings
     *
     * @param key1 the first key
     * @param val1 the mapping associated to the first key
     * @param key2 the second key
     * @param val2 the mapping associated to the second key
     * @param key3 the third key
     * @param val3 the mapping associated to the third key
     * @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjExp par(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2,
                               final String key3,
                               final VIO<? extends JsValue> val3
                              ) {
        JsObjExp obj = par(key1, val1,
                           key2, val2
                          );
        obj.bindings.put(key3, val3);
        return obj;
    }


    /**
     * static factory method to create a JsObjFuture of four mappings
     *
     * @param key1 the first key
     * @param val1 the mapping associated to the first key
     * @param key2 the second key
     * @param val2 the mapping associated to the second key
     * @param key3 the third key
     * @param val3 the mapping associated to the third key
     * @param key4 the fourth key
     * @param val4 the mapping associated to the fourth key
     * @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjExp par(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2,
                               final String key3,
                               final VIO<? extends JsValue> val3,
                               final String key4,
                               final VIO<? extends JsValue> val4
                              ) {
        JsObjExp obj = par(key1, val1,
                           key2, val2,
                           key3, val3
                          );
        obj.bindings.put(key4, val4);
        return obj;
    }


    /**
     * static factory method to create a JsObjFuture of five mappings
     *
     * @param key1 the first key
     * @param val1 the mapping associated to the first key
     * @param key2 the second key
     * @param val2 the mapping associated to the second key
     * @param key3 the third key
     * @param val3 the mapping associated to the third key
     * @param key4 the fourth key
     * @param val4 the mapping associated to the fourth key
     * @param key5 the fifth key
     * @param val5 the mapping associated to the fifth key
     * @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjExp par(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2,
                               final String key3,
                               final VIO<? extends JsValue> val3,
                               final String key4,
                               final VIO<? extends JsValue> val4,
                               final String key5,
                               final VIO<? extends JsValue> val5
                              ) {
        JsObjExp obj = par(key1, val1,
                           key2, val2,
                           key3, val3,
                           key4, val4
                          );
        obj.bindings.put(key5, val5);
        return obj;
    }


    /**
     * static factory method to create a JsObjFuture of six mappings
     *
     * @param key1 the first key
     * @param val1 the mapping associated to the first key
     * @param key2 the second key
     * @param val2 the mapping associated to the second key
     * @param key3 the third key
     * @param val3 the mapping associated to the third key
     * @param key4 the fourth key
     * @param val4 the mapping associated to the fourth key
     * @param key5 the fifth key
     * @param val5 the mapping associated to the fifth key
     * @param key6 the sixth key
     * @param val6 the mapping associated to the sixth key
     * @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjExp par(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2,
                               final String key3,
                               final VIO<? extends JsValue> val3,
                               final String key4,
                               final VIO<? extends JsValue> val4,
                               final String key5,
                               final VIO<? extends JsValue> val5,
                               final String key6,
                               final VIO<? extends JsValue> val6
                              ) {
        JsObjExp obj = par(key1, val1,
                           key2, val2,
                           key3, val3,
                           key4, val4,
                           key5, val5
                          );
        obj.bindings.put(key6, val6);
        return obj;
    }


    /**
     * static factory method to create a JsObjFuture of seven mappings
     *
     * @param key1 the first key
     * @param val1 the mapping associated to the first key
     * @param key2 the second key
     * @param val2 the mapping associated to the second key
     * @param key3 the third key
     * @param val3 the mapping associated to the third key
     * @param key4 the fourth key
     * @param val4 the mapping associated to the fourth key
     * @param key5 the fifth key
     * @param val5 the mapping associated to the fifth key
     * @param key6 the sixth key
     * @param val6 the mapping associated to the sixth key
     * @param key7 the seventh key
     * @param val7 the mapping associated to the seventh key
     * @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjExp par(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2,
                               final String key3,
                               final VIO<? extends JsValue> val3,
                               final String key4,
                               final VIO<? extends JsValue> val4,
                               final String key5,
                               final VIO<? extends JsValue> val5,
                               final String key6,
                               final VIO<? extends JsValue> val6,
                               final String key7,
                               final VIO<? extends JsValue> val7
                              ) {
        JsObjExp obj = par(key1, val1,
                           key2, val2,
                           key3, val3,
                           key4, val4,
                           key5, val5,
                           key6, val6
                          );
        obj.bindings.put(key7, val7);
        return obj;
    }


    /**
     * static factory method to create a JsObjFuture of eight mappings
     *
     * @param key1 the first key
     * @param val1 the mapping associated to the first key
     * @param key2 the second key
     * @param val2 the mapping associated to the second key
     * @param key3 the third key
     * @param val3 the mapping associated to the third key
     * @param key4 the fourth key
     * @param val4 the mapping associated to the fourth key
     * @param key5 the fifth key
     * @param val5 the mapping associated to the fifth key
     * @param key6 the sixth key
     * @param val6 the mapping associated to the sixth key
     * @param key7 the seventh key
     * @param val7 the mapping associated to the seventh key
     * @param key8 the eighth key
     * @param val8 the mapping associated to the eighth key
     * @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjExp par(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2,
                               final String key3,
                               final VIO<? extends JsValue> val3,
                               final String key4,
                               final VIO<? extends JsValue> val4,
                               final String key5,
                               final VIO<? extends JsValue> val5,
                               final String key6,
                               final VIO<? extends JsValue> val6,
                               final String key7,
                               final VIO<? extends JsValue> val7,
                               final String key8,
                               final VIO<? extends JsValue> val8
                              ) {
        JsObjExp obj = par(key1, val1,
                           key2, val2,
                           key3, val3,
                           key4, val4,
                           key5, val5,
                           key6, val6,
                           key7, val7
                          );
        obj.bindings.put(key8, val8);
        return obj;

    }


    /**
     * static factory method to create a JsObjFuture of nine mappings
     *
     * @param key1 the first key
     * @param val1 the mapping associated to the first key
     * @param key2 the second key
     * @param val2 the mapping associated to the second key
     * @param key3 the third key
     * @param val3 the mapping associated to the third key
     * @param key4 the fourth key
     * @param val4 the mapping associated to the fourth key
     * @param key5 the fifth key
     * @param val5 the mapping associated to the fifth key
     * @param key6 the sixth key
     * @param val6 the mapping associated to the sixth key
     * @param key7 the seventh key
     * @param val7 the mapping associated to the seventh key
     * @param key8 the eighth key
     * @param val8 the mapping associated to the eighth key
     * @param key9 the ninth key
     * @param val9 the mapping associated to the ninth key
     * @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjExp par(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2,
                               final String key3,
                               final VIO<? extends JsValue> val3,
                               final String key4,
                               final VIO<? extends JsValue> val4,
                               final String key5,
                               final VIO<? extends JsValue> val5,
                               final String key6,
                               final VIO<? extends JsValue> val6,
                               final String key7,
                               final VIO<? extends JsValue> val7,
                               final String key8,
                               final VIO<? extends JsValue> val8,
                               final String key9,
                               final VIO<? extends JsValue> val9
                              ) {
        JsObjExp obj = par(key1, val1,
                           key2, val2,
                           key3, val3,
                           key4, val4,
                           key5, val5,
                           key6, val6,
                           key7, val7,
                           key8, val8
                          );
        obj.bindings.put(key9, val9);
        return obj;
    }


    /**
     * static factory method to create a JsObjFuture of ten mappings
     *
     * @param key1  the first key
     * @param val1  the mapping associated to the first key
     * @param key2  the second key
     * @param val2  the mapping associated to the second key
     * @param key3  the third key
     * @param val3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param val4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param val5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param val6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param val7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param val8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param val9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param val10 the mapping associated to the tenth key
     * @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjExp par(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2,
                               final String key3,
                               final VIO<? extends JsValue> val3,
                               final String key4,
                               final VIO<? extends JsValue> val4,
                               final String key5,
                               final VIO<? extends JsValue> val5,
                               final String key6,
                               final VIO<? extends JsValue> val6,
                               final String key7,
                               final VIO<? extends JsValue> val7,
                               final String key8,
                               final VIO<? extends JsValue> val8,
                               final String key9,
                               final VIO<? extends JsValue> val9,
                               final String key10,
                               final VIO<? extends JsValue> val10
                              ) {
        JsObjExp obj = par(key1, val1,
                           key2, val2,
                           key3, val3,
                           key4, val4,
                           key5, val5,
                           key6, val6,
                           key7, val7,
                           key8, val8,
                           key9, val9
                          );
        obj.bindings.put(key10, val10);
        return obj;
    }


    /**
     * static factory method to create a JsObjFuture of eleven mappings
     *
     * @param key1  the first key
     * @param val1  the mapping associated to the first key
     * @param key2  the second key
     * @param val2  the mapping associated to the second key
     * @param key3  the third key
     * @param val3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param val4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param val5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param val6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param val7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param val8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param val9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param val10 the mapping associated to the eleventh key
     * @param key11 the tenth key
     * @param val11 the mapping associated to the eleventh key
     * @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjExp par(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2,
                               final String key3,
                               final VIO<? extends JsValue> val3,
                               final String key4,
                               final VIO<? extends JsValue> val4,
                               final String key5,
                               final VIO<? extends JsValue> val5,
                               final String key6,
                               final VIO<? extends JsValue> val6,
                               final String key7,
                               final VIO<? extends JsValue> val7,
                               final String key8,
                               final VIO<? extends JsValue> val8,
                               final String key9,
                               final VIO<? extends JsValue> val9,
                               final String key10,
                               final VIO<? extends JsValue> val10,
                               final String key11,
                               final VIO<? extends JsValue> val11
                              ) {
        JsObjExp obj = par(key1, val1,
                           key2, val2,
                           key3, val3,
                           key4, val4,
                           key5, val5,
                           key6, val6,
                           key7, val7,
                           key8, val8,
                           key9, val9,
                           key10, val10
                          );
        obj.bindings.put(key11, val11);
        return obj;
    }


    /**
     * static factory method to create a JsObjFuture of twelve mappings
     *
     * @param key1  the first key
     * @param val1  the mapping associated to the first key
     * @param key2  the second key
     * @param val2  the mapping associated to the second key
     * @param key3  the third key
     * @param val3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param val4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param val5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param val6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param val7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param val8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param val9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param val10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param val11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param val12 the mapping associated to the twelfth key
     * @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjExp par(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2,
                               final String key3,
                               final VIO<? extends JsValue> val3,
                               final String key4,
                               final VIO<? extends JsValue> val4,
                               final String key5,
                               final VIO<? extends JsValue> val5,
                               final String key6,
                               final VIO<? extends JsValue> val6,
                               final String key7,
                               final VIO<? extends JsValue> val7,
                               final String key8,
                               final VIO<? extends JsValue> val8,
                               final String key9,
                               final VIO<? extends JsValue> val9,
                               final String key10,
                               final VIO<? extends JsValue> val10,
                               final String key11,
                               final VIO<? extends JsValue> val11,
                               final String key12,
                               final VIO<? extends JsValue> val12
                              ) {
        JsObjExp obj = par(key1, val1,
                           key2, val2,
                           key3, val3,
                           key4, val4,
                           key5, val5,
                           key6, val6,
                           key7, val7,
                           key8, val8,
                           key9, val9,
                           key10, val10,
                           key11, val11
                          );
        obj.bindings.put(key12, val12);
        return obj;

    }

    /**
     * static factory method to create a JsObjFuture of thirteen mappings
     *
     * @param key1  the first key
     * @param val1  the mapping associated to the first key
     * @param key2  the second key
     * @param val2  the mapping associated to the second key
     * @param key3  the third key
     * @param val3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param val4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param val5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param val6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param val7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param val8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param val9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param val10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param val11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param val12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param val13 the mapping associated to the thirteenth key
     * @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjExp par(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2,
                               final String key3,
                               final VIO<? extends JsValue> val3,
                               final String key4,
                               final VIO<? extends JsValue> val4,
                               final String key5,
                               final VIO<? extends JsValue> val5,
                               final String key6,
                               final VIO<? extends JsValue> val6,
                               final String key7,
                               final VIO<? extends JsValue> val7,
                               final String key8,
                               final VIO<? extends JsValue> val8,
                               final String key9,
                               final VIO<? extends JsValue> val9,
                               final String key10,
                               final VIO<? extends JsValue> val10,
                               final String key11,
                               final VIO<? extends JsValue> val11,
                               final String key12,
                               final VIO<? extends JsValue> val12,
                               final String key13,
                               final VIO<? extends JsValue> val13
                              ) {
        JsObjExp obj = par(key1, val1,
                           key2, val2,
                           key3, val3,
                           key4, val4,
                           key5, val5,
                           key6, val6,
                           key7, val7,
                           key8, val8,
                           key9, val9,
                           key10, val10,
                           key11, val11,
                           key12, val12
                          );
        obj.bindings.put(key13, val13);
        return obj;
    }


    /**
     * static factory method to create a JsObjFuture of fourteen mappings
     *
     * @param key1  the first key
     * @param val1  the mapping associated to the first key
     * @param key2  the second key
     * @param val2  the mapping associated to the second key
     * @param key3  the third key
     * @param val3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param val4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param val5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param val6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param val7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param val8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param val9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param val10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param val11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param val12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param val13 the mapping associated to the thirteenth key
     * @param key14 the fourteenth key
     * @param val14 the mapping associated to the fourteenth key
     * @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjExp par(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2,
                               final String key3,
                               final VIO<? extends JsValue> val3,
                               final String key4,
                               final VIO<? extends JsValue> val4,
                               final String key5,
                               final VIO<? extends JsValue> val5,
                               final String key6,
                               final VIO<? extends JsValue> val6,
                               final String key7,
                               final VIO<? extends JsValue> val7,
                               final String key8,
                               final VIO<? extends JsValue> val8,
                               final String key9,
                               final VIO<? extends JsValue> val9,
                               final String key10,
                               final VIO<? extends JsValue> val10,
                               final String key11,
                               final VIO<? extends JsValue> val11,
                               final String key12,
                               final VIO<? extends JsValue> val12,
                               final String key13,
                               final VIO<? extends JsValue> val13,
                               final String key14,
                               final VIO<? extends JsValue> val14
                              ) {
        JsObjExp obj = par(key1, val1,
                           key2, val2,
                           key3, val3,
                           key4, val4,
                           key5, val5,
                           key6, val6,
                           key7, val7,
                           key8, val8,
                           key9, val9,
                           key10, val10,
                           key11, val11,
                           key12, val12,
                           key13, val13
                          );
        obj.bindings.put(key14, val14);
        return obj;
    }


    /**
     * static factory method to create a JsObjFuture of fifteen mappings
     *
     * @param key1  the first key
     * @param val1  the mapping associated to the first key
     * @param key2  the second key
     * @param val2  the mapping associated to the second key
     * @param key3  the third key
     * @param val3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param val4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param val5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param val6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param val7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param val8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param val9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param val10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param val11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param val12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param val13 the mapping associated to the thirteenth key
     * @param key14 the fourteenth key
     * @param val14 the mapping associated to the fourteenth key
     * @param key15 the fifteenth key
     * @param val15 the mapping associated to the fifteenth key
     * @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjExp par(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2,
                               final String key3,
                               final VIO<? extends JsValue> val3,
                               final String key4,
                               final VIO<? extends JsValue> val4,
                               final String key5,
                               final VIO<? extends JsValue> val5,
                               final String key6,
                               final VIO<? extends JsValue> val6,
                               final String key7,
                               final VIO<? extends JsValue> val7,
                               final String key8,
                               final VIO<? extends JsValue> val8,
                               final String key9,
                               final VIO<? extends JsValue> val9,
                               final String key10,
                               final VIO<? extends JsValue> val10,
                               final String key11,
                               final VIO<? extends JsValue> val11,
                               final String key12,
                               final VIO<? extends JsValue> val12,
                               final String key13,
                               final VIO<? extends JsValue> val13,
                               final String key14,
                               final VIO<? extends JsValue> val14,
                               final String key15,
                               final VIO<? extends JsValue> val15
                              ) {
        JsObjExp obj = par(key1, val1,
                           key2, val2,
                           key3, val3,
                           key4, val4,
                           key5, val5,
                           key6, val6,
                           key7, val7,
                           key8, val8,
                           key9, val9,
                           key10, val10,
                           key11, val11,
                           key12, val12,
                           key13, val13,
                           key14, val14
                          );
        obj.bindings.put(key15, val15);
        return obj;

    }

    /**
     * static factory method to create a JsObjFuture of fifteen mappings
     *
     * @param key1  the first key
     * @param val1  the mapping associated to the first key
     * @param key2  the second key
     * @param val2  the mapping associated to the second key
     * @param key3  the third key
     * @param val3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param val4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param val5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param val6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param val7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param val8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param val9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param val10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param val11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param val12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param val13 the mapping associated to the thirteenth key
     * @param key14 the fourteenth key
     * @param val14 the mapping associated to the fourteenth key
     * @param key15 the fifteenth key
     * @param val15 the mapping associated to the fifteenth key
     * @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjExp seq(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2,
                               final String key3,
                               final VIO<? extends JsValue> val3,
                               final String key4,
                               final VIO<? extends JsValue> val4,
                               final String key5,
                               final VIO<? extends JsValue> val5,
                               final String key6,
                               final VIO<? extends JsValue> val6,
                               final String key7,
                               final VIO<? extends JsValue> val7,
                               final String key8,
                               final VIO<? extends JsValue> val8,
                               final String key9,
                               final VIO<? extends JsValue> val9,
                               final String key10,
                               final VIO<? extends JsValue> val10,
                               final String key11,
                               final VIO<? extends JsValue> val11,
                               final String key12,
                               final VIO<? extends JsValue> val12,
                               final String key13,
                               final VIO<? extends JsValue> val13,
                               final String key14,
                               final VIO<? extends JsValue> val14,
                               final String key15,
                               final VIO<? extends JsValue> val15
                              ) {
        JsObjExp obj = seq(key1, val1,
                           key2, val2,
                           key3, val3,
                           key4, val4,
                           key5, val5,
                           key6, val6,
                           key7, val7,
                           key8, val8,
                           key9, val9,
                           key10, val10,
                           key11, val11,
                           key12, val12,
                           key13, val13,
                           key14, val14
                          );
        obj.bindings.put(key15, val15);
        return obj;

    }

    /**
     * static factory method to create a JsObjFuture of fourteen mappings
     *
     * @param key1  the first key
     * @param val1  the mapping associated to the first key
     * @param key2  the second key
     * @param val2  the mapping associated to the second key
     * @param key3  the third key
     * @param val3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param val4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param val5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param val6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param val7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param val8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param val9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param val10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param val11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param val12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param val13 the mapping associated to the thirteenth key
     * @param key14 the fourteenth key
     * @param val14 the mapping associated to the fourteenth key
     * @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjExp seq(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2,
                               final String key3,
                               final VIO<? extends JsValue> val3,
                               final String key4,
                               final VIO<? extends JsValue> val4,
                               final String key5,
                               final VIO<? extends JsValue> val5,
                               final String key6,
                               final VIO<? extends JsValue> val6,
                               final String key7,
                               final VIO<? extends JsValue> val7,
                               final String key8,
                               final VIO<? extends JsValue> val8,
                               final String key9,
                               final VIO<? extends JsValue> val9,
                               final String key10,
                               final VIO<? extends JsValue> val10,
                               final String key11,
                               final VIO<? extends JsValue> val11,
                               final String key12,
                               final VIO<? extends JsValue> val12,
                               final String key13,
                               final VIO<? extends JsValue> val13,
                               final String key14,
                               final VIO<? extends JsValue> val14
                              ) {
        JsObjExp obj = seq(key1, val1,
                           key2, val2,
                           key3, val3,
                           key4, val4,
                           key5, val5,
                           key6, val6,
                           key7, val7,
                           key8, val8,
                           key9, val9,
                           key10, val10,
                           key11, val11,
                           key12, val12,
                           key13, val13
                          );
        obj.bindings.put(key14, val14);
        return obj;
    }

    /**
     * static factory method to create a JsObjFuture of thirteen mappings
     *
     * @param key1  the first key
     * @param val1  the mapping associated to the first key
     * @param key2  the second key
     * @param val2  the mapping associated to the second key
     * @param key3  the third key
     * @param val3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param val4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param val5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param val6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param val7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param val8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param val9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param val10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param val11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param val12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param val13 the mapping associated to the thirteenth key
     * @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjExp seq(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2,
                               final String key3,
                               final VIO<? extends JsValue> val3,
                               final String key4,
                               final VIO<? extends JsValue> val4,
                               final String key5,
                               final VIO<? extends JsValue> val5,
                               final String key6,
                               final VIO<? extends JsValue> val6,
                               final String key7,
                               final VIO<? extends JsValue> val7,
                               final String key8,
                               final VIO<? extends JsValue> val8,
                               final String key9,
                               final VIO<? extends JsValue> val9,
                               final String key10,
                               final VIO<? extends JsValue> val10,
                               final String key11,
                               final VIO<? extends JsValue> val11,
                               final String key12,
                               final VIO<? extends JsValue> val12,
                               final String key13,
                               final VIO<? extends JsValue> val13
                              ) {
        JsObjExp obj = seq(key1, val1,
                           key2, val2,
                           key3, val3,
                           key4, val4,
                           key5, val5,
                           key6, val6,
                           key7, val7,
                           key8, val8,
                           key9, val9,
                           key10, val10,
                           key11, val11,
                           key12, val12
                          );
        obj.bindings.put(key13, val13);
        return obj;

    }

    /**
     * static factory method to create a JsObjFuture of twelve mappings
     *
     * @param key1  the first key
     * @param val1  the mapping associated to the first key
     * @param key2  the second key
     * @param val2  the mapping associated to the second key
     * @param key3  the third key
     * @param val3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param val4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param val5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param val6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param val7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param val8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param val9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param val10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param val11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param val12 the mapping associated to the twelfth key
     * @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjExp seq(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2,
                               final String key3,
                               final VIO<? extends JsValue> val3,
                               final String key4,
                               final VIO<? extends JsValue> val4,
                               final String key5,
                               final VIO<? extends JsValue> val5,
                               final String key6,
                               final VIO<? extends JsValue> val6,
                               final String key7,
                               final VIO<? extends JsValue> val7,
                               final String key8,
                               final VIO<? extends JsValue> val8,
                               final String key9,
                               final VIO<? extends JsValue> val9,
                               final String key10,
                               final VIO<? extends JsValue> val10,
                               final String key11,
                               final VIO<? extends JsValue> val11,
                               final String key12,
                               final VIO<? extends JsValue> val12
                              ) {
        JsObjExp obj = seq(key1, val1,
                           key2, val2,
                           key3, val3,
                           key4, val4,
                           key5, val5,
                           key6, val6,
                           key7, val7,
                           key8, val8,
                           key9, val9,
                           key10, val10,
                           key11, val11
                          );
        obj.bindings.put(key12, val12);
        return obj;

    }

    /**
     * static factory method to create a JsObjFuture of eleven mappings
     *
     * @param key1  the first key
     * @param val1  the mapping associated to the first key
     * @param key2  the second key
     * @param val2  the mapping associated to the second key
     * @param key3  the third key
     * @param val3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param val4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param val5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param val6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param val7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param val8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param val9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param val10 the mapping associated to the eleventh key
     * @param key11 the tenth key
     * @param val11 the mapping associated to the eleventh key
     * @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjExp seq(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2,
                               final String key3,
                               final VIO<? extends JsValue> val3,
                               final String key4,
                               final VIO<? extends JsValue> val4,
                               final String key5,
                               final VIO<? extends JsValue> val5,
                               final String key6,
                               final VIO<? extends JsValue> val6,
                               final String key7,
                               final VIO<? extends JsValue> val7,
                               final String key8,
                               final VIO<? extends JsValue> val8,
                               final String key9,
                               final VIO<? extends JsValue> val9,
                               final String key10,
                               final VIO<? extends JsValue> val10,
                               final String key11,
                               final VIO<? extends JsValue> val11
                              ) {
        JsObjExp obj = seq(key1, val1,
                           key2, val2,
                           key3, val3,
                           key4, val4,
                           key5, val5,
                           key6, val6,
                           key7, val7,
                           key8, val8,
                           key9, val9,
                           key10, val10
                          );
        obj.bindings.put(key11, val11);
        return obj;

    }

    /**
     * static factory method to create a JsObjFuture of ten mappings
     *
     * @param key1  the first key
     * @param val1  the mapping associated to the first key
     * @param key2  the second key
     * @param val2  the mapping associated to the second key
     * @param key3  the third key
     * @param val3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param val4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param val5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param val6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param val7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param val8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param val9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param val10 the mapping associated to the tenth key
     * @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjExp seq(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2,
                               final String key3,
                               final VIO<? extends JsValue> val3,
                               final String key4,
                               final VIO<? extends JsValue> val4,
                               final String key5,
                               final VIO<? extends JsValue> val5,
                               final String key6,
                               final VIO<? extends JsValue> val6,
                               final String key7,
                               final VIO<? extends JsValue> val7,
                               final String key8,
                               final VIO<? extends JsValue> val8,
                               final String key9,
                               final VIO<? extends JsValue> val9,
                               final String key10,
                               final VIO<? extends JsValue> val10
                              ) {
        JsObjExp obj = seq(key1, val1,
                           key2, val2,
                           key3, val3,
                           key4, val4,
                           key5, val5,
                           key6, val6,
                           key7, val7,
                           key8, val8,
                           key9, val9
                          );
        obj.bindings.put(key10, val10);
        return obj;
    }

    /**
     * static factory method to create a JsObjFuture of nine mappings
     *
     * @param key1 the first key
     * @param val1 the mapping associated to the first key
     * @param key2 the second key
     * @param val2 the mapping associated to the second key
     * @param key3 the third key
     * @param val3 the mapping associated to the third key
     * @param key4 the fourth key
     * @param val4 the mapping associated to the fourth key
     * @param key5 the fifth key
     * @param val5 the mapping associated to the fifth key
     * @param key6 the sixth key
     * @param val6 the mapping associated to the sixth key
     * @param key7 the seventh key
     * @param val7 the mapping associated to the seventh key
     * @param key8 the eighth key
     * @param val8 the mapping associated to the eighth key
     * @param key9 the ninth key
     * @param val9 the mapping associated to the ninth key
     * @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjExp seq(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2,
                               final String key3,
                               final VIO<? extends JsValue> val3,
                               final String key4,
                               final VIO<? extends JsValue> val4,
                               final String key5,
                               final VIO<? extends JsValue> val5,
                               final String key6,
                               final VIO<? extends JsValue> val6,
                               final String key7,
                               final VIO<? extends JsValue> val7,
                               final String key8,
                               final VIO<? extends JsValue> val8,
                               final String key9,
                               final VIO<? extends JsValue> val9
                              ) {
        JsObjExp obj = seq(key1, val1,
                           key2, val2,
                           key3, val3,
                           key4, val4,
                           key5, val5,
                           key6, val6,
                           key7, val7,
                           key8, val8
                          );
        obj.bindings.put(key9, val9);
        return obj;

    }

    /**
     * static factory method to create a JsObjFuture of eight mappings
     *
     * @param key1 the first key
     * @param val1 the mapping associated to the first key
     * @param key2 the second key
     * @param val2 the mapping associated to the second key
     * @param key3 the third key
     * @param val3 the mapping associated to the third key
     * @param key4 the fourth key
     * @param val4 the mapping associated to the fourth key
     * @param key5 the fifth key
     * @param val5 the mapping associated to the fifth key
     * @param key6 the sixth key
     * @param val6 the mapping associated to the sixth key
     * @param key7 the seventh key
     * @param val7 the mapping associated to the seventh key
     * @param key8 the eighth key
     * @param val8 the mapping associated to the eighth key
     * @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjExp seq(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2,
                               final String key3,
                               final VIO<? extends JsValue> val3,
                               final String key4,
                               final VIO<? extends JsValue> val4,
                               final String key5,
                               final VIO<? extends JsValue> val5,
                               final String key6,
                               final VIO<? extends JsValue> val6,
                               final String key7,
                               final VIO<? extends JsValue> val7,
                               final String key8,
                               final VIO<? extends JsValue> val8
                              ) {
        JsObjExp obj = seq(key1, val1,
                           key2, val2,
                           key3, val3,
                           key4, val4,
                           key5, val5,
                           key6, val6,
                           key7, val7
                          );
        obj.bindings.put(key8, val8);
        return obj;
    }

    /**
     * static factory method to create a JsObjFuture of seven mappings
     *
     * @param key1 the first key
     * @param val1 the mapping associated to the first key
     * @param key2 the second key
     * @param val2 the mapping associated to the second key
     * @param key3 the third key
     * @param val3 the mapping associated to the third key
     * @param key4 the fourth key
     * @param val4 the mapping associated to the fourth key
     * @param key5 the fifth key
     * @param val5 the mapping associated to the fifth key
     * @param key6 the sixth key
     * @param val6 the mapping associated to the sixth key
     * @param key7 the seventh key
     * @param val7 the mapping associated to the seventh key
     * @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjExp seq(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2,
                               final String key3,
                               final VIO<? extends JsValue> val3,
                               final String key4,
                               final VIO<? extends JsValue> val4,
                               final String key5,
                               final VIO<? extends JsValue> val5,
                               final String key6,
                               final VIO<? extends JsValue> val6,
                               final String key7,
                               final VIO<? extends JsValue> val7
                              ) {
        JsObjExp obj = seq(key1, val1,
                           key2, val2,
                           key3, val3,
                           key4, val4,
                           key5, val5,
                           key6, val6
                          );
        obj.bindings.put(key7, val7);
        return obj;
    }

    /**
     * static factory method to create a JsObjFuture of six mappings
     *
     * @param key1 the first key
     * @param val1 the mapping associated to the first key
     * @param key2 the second key
     * @param val2 the mapping associated to the second key
     * @param key3 the third key
     * @param val3 the mapping associated to the third key
     * @param key4 the fourth key
     * @param val4 the mapping associated to the fourth key
     * @param key5 the fifth key
     * @param val5 the mapping associated to the fifth key
     * @param key6 the sixth key
     * @param val6 the mapping associated to the sixth key
     * @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjExp seq(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2,
                               final String key3,
                               final VIO<? extends JsValue> val3,
                               final String key4,
                               final VIO<? extends JsValue> val4,
                               final String key5,
                               final VIO<? extends JsValue> val5,
                               final String key6,
                               final VIO<? extends JsValue> val6
                              ) {
        JsObjExp obj = seq(key1, val1,
                           key2, val2,
                           key3, val3,
                           key4, val4,
                           key5, val5
                          );
        obj.bindings.put(key6, val6);
        return obj;
    }

    /**
     * static factory method to create a JsObjFuture of five mappings
     *
     * @param key1 the first key
     * @param val1 the mapping associated to the first key
     * @param key2 the second key
     * @param val2 the mapping associated to the second key
     * @param key3 the third key
     * @param val3 the mapping associated to the third key
     * @param key4 the fourth key
     * @param val4 the mapping associated to the fourth key
     * @param key5 the fifth key
     * @param val5 the mapping associated to the fifth key
     * @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjExp seq(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2,
                               final String key3,
                               final VIO<? extends JsValue> val3,
                               final String key4,
                               final VIO<? extends JsValue> val4,
                               final String key5,
                               final VIO<? extends JsValue> val5
                              ) {
        JsObjExp obj = seq(key1, val1,
                           key2, val2,
                           key3, val3,
                           key4, val4
                          );
        obj.bindings.put(key5, val5);
        return obj;
    }

    /**
     * static factory method to create a JsObjFuture of four mappings
     *
     * @param key1 the first key
     * @param val1 the mapping associated to the first key
     * @param key2 the second key
     * @param val2 the mapping associated to the second key
     * @param key3 the third key
     * @param val3 the mapping associated to the third key
     * @param key4 the fourth key
     * @param val4 the mapping associated to the fourth key
     * @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjExp seq(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2,
                               final String key3,
                               final VIO<? extends JsValue> val3,
                               final String key4,
                               final VIO<? extends JsValue> val4
                              ) {
        JsObjExp obj = seq(key1, val1,
                           key2, val2,
                           key3, val3
                          );
        obj.bindings.put(key4, val4);
        return obj;
    }

    /**
     * static factory method to create a JsObjFuture of three mappings
     *
     * @param key1 the first key
     * @param val1 the mapping associated to the first key
     * @param key2 the second key
     * @param val2 the mapping associated to the second key
     * @param key3 the third key
     * @param val3 the mapping associated to the third key
     * @return a JsObjFuture
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjExp seq(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2,
                               final String key3,
                               final VIO<? extends JsValue> val3
                              ) {
        JsObjExp obj = seq(key1, val1,
                           key2, val2
                          );
        obj.bindings.put(key3, val3);
        return obj;
    }

    /**
     * static factory method to create a JsObjFuture of one mapping
     *
     * @param key1 the first key
     * @param val1 the mapping associated to the first key
     * @param key2 the second key
     * @param val2 the mapping associated to the second key
     * @return a JsObjFuture
     */
    public static JsObjExp seq(final String key1,
                               final VIO<? extends JsValue> val1,
                               final String key2,
                               final VIO<? extends JsValue> val2
                              ) {
        JsObjExp obj = seq(key1, val1);
        obj.bindings.put(key2, val2);
        return obj;
    }

    /**
     * static factory method to create a JsObjFuture of one mapping
     *
     * @param key the key
     * @param val the mapping associated to the key
     * @return a JsObjFuture
     */
    public static JsObjExp seq(final String key,
                               final VIO<? extends JsValue> val
                              ) {
        JsObjExpSeq obj = new JsObjExpSeq();
        obj.bindings.put(requireNonNull(key),
                         requireNonNull(val)
                        );
        return obj;
    }

    public abstract JsObjExp set(final String key,
                                 final VIO<? extends JsValue> val
                                );

}
