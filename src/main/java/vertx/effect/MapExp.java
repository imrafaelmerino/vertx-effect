package vertx.effect;


import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;


public abstract sealed class MapExp<O> extends Exp<Map<String, O>> permits MapExpPar, MapExpSeq {

    protected Map<String, VIO<? extends O>> bindings = new HashMap<>();

    @SuppressWarnings({"unchecked"})
    public static <O> MapExp<O> par() {
        return MapExpPar.EMPTY;
    }

    /**
     * static factory method to create a MapExp of one mapping
     *
     * @param key the key
     * @param exp the mapping associated to the key
     * @param <O> the type of the map values
     * @return a MapExp
     */
    public static <O> MapExp<O> par(final String key,
                                    final VIO<? extends O> exp
                                   ) {
        MapExpPar<O> obj = new MapExpPar<>();
        obj.bindings.put(requireNonNull(key),
                         requireNonNull(exp)
                        );
        return obj;
    }

    /**
     * static factory method to create a MapExp of one mapping
     *
     * @param key1 the first key
     * @param exp1 the mapping associated to the first key
     * @param key2 the second key
     * @param exp2 the mapping associated to the second key
     * @param <O>  the type of the map values
     * @return a MapExp
     */
    public static <O> MapExp<O> par(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2
                                   ) {
        MapExp<O> map = par(key1, exp1);

        map.bindings.put(requireNonNull(key2),
                         requireNonNull(exp2)
                        );
        return map;
    }

    /**
     * static factory method to create a MapExp of three mappings
     *
     * @param key1 the first key
     * @param exp1 the mapping associated to the first key
     * @param key2 the second key
     * @param exp2 the mapping associated to the second key
     * @param key3 the third key
     * @param exp3 the mapping associated to the third key
     * @param <O>  the type of the map values
     * @return a MapExp
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapExp<O> par(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2,
                                    final String key3,
                                    final VIO<? extends O> exp3
                                   ) {
        MapExp<O> map = par(key1, exp1,
                            key2, exp2
                           );

        map.bindings.put(requireNonNull(key3),
                         requireNonNull(exp3)
                        );
        return map;
    }

    /**
     * static factory method to create a MapExp of four mappings
     *
     * @param key1 the first key
     * @param exp1 the mapping associated to the first key
     * @param key2 the second key
     * @param exp2 the mapping associated to the second key
     * @param key3 the third key
     * @param exp3 the mapping associated to the third key
     * @param key4 the fourth key
     * @param exp4 the mapping associated to the fourth key
     * @param <O>  the type of the map values
     * @return a MapExp
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapExp<O> par(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2,
                                    final String key3,
                                    final VIO<? extends O> exp3,
                                    final String key4,
                                    final VIO<? extends O> exp4
                                   ) {
        MapExp<O> map = par(key1, exp1,
                            key2, exp2,
                            key3, exp3
                           );

        map.bindings.put(requireNonNull(key4),
                         requireNonNull(exp4)
                        );
        return map;
    }

    /**
     * static factory method to create a MapExp of five mappings
     *
     * @param key1 the first key
     * @param exp1 the mapping associated to the first key
     * @param key2 the second key
     * @param exp2 the mapping associated to the second key
     * @param key3 the third key
     * @param exp3 the mapping associated to the third key
     * @param key4 the fourth key
     * @param exp4 the mapping associated to the fourth key
     * @param key5 the fifth key
     * @param exp5 the mapping associated to the fifth key
     * @param <O>  the type of the map values
     * @return a MapExp
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapExp<O> par(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2,
                                    final String key3,
                                    final VIO<? extends O> exp3,
                                    final String key4,
                                    final VIO<? extends O> exp4,
                                    final String key5,
                                    final VIO<? extends O> exp5
                                   ) {
        MapExp<O> map = par(key1, exp1,
                            key2, exp2,
                            key3, exp3,
                            key4, exp4
                           );

        map.bindings.put(requireNonNull(key5),
                         requireNonNull(exp5)
                        );
        return map;
    }

    /**
     * static factory method to create a MapExp of six mappings
     *
     * @param key1 the first key
     * @param exp1 the mapping associated to the first key
     * @param key2 the second key
     * @param exp2 the mapping associated to the second key
     * @param key3 the third key
     * @param exp3 the mapping associated to the third key
     * @param key4 the fourth key
     * @param exp4 the mapping associated to the fourth key
     * @param key5 the fifth key
     * @param exp5 the mapping associated to the fifth key
     * @param key6 the sixth key
     * @param exp6 the mapping associated to the sixth key
     * @param <O>  the type of the map values
     * @return a MapExp
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapExp<O> par(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2,
                                    final String key3,
                                    final VIO<? extends O> exp3,
                                    final String key4,
                                    final VIO<? extends O> exp4,
                                    final String key5,
                                    final VIO<? extends O> exp5,
                                    final String key6,
                                    final VIO<? extends O> exp6
                                   ) {

        MapExp<O> map = par(key1, exp1,
                            key2, exp2,
                            key3, exp3,
                            key4, exp4,
                            key5, exp5
                           );

        map.bindings.put(requireNonNull(key6),
                         requireNonNull(exp6)
                        );
        return map;
    }

    /**
     * static factory method to create a MapExp of seven mappings
     *
     * @param key1 the first key
     * @param exp1 the mapping associated to the first key
     * @param key2 the second key
     * @param exp2 the mapping associated to the second key
     * @param key3 the third key
     * @param exp3 the mapping associated to the third key
     * @param key4 the fourth key
     * @param exp4 the mapping associated to the fourth key
     * @param key5 the fifth key
     * @param exp5 the mapping associated to the fifth key
     * @param key6 the sixth key
     * @param exp6 the mapping associated to the sixth key
     * @param key7 the seventh key
     * @param exp7 the mapping associated to the seventh key
     * @param <O>  the type of the map values
     * @return a MapExp
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapExp<O> par(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2,
                                    final String key3,
                                    final VIO<? extends O> exp3,
                                    final String key4,
                                    final VIO<? extends O> exp4,
                                    final String key5,
                                    final VIO<? extends O> exp5,
                                    final String key6,
                                    final VIO<? extends O> exp6,
                                    final String key7,
                                    final VIO<? extends O> exp7
                                   ) {
        MapExp<O> map = par(key1, exp1,
                            key2, exp2,
                            key3, exp3,
                            key4, exp4,
                            key5, exp5,
                            key6, exp6
                           );

        map.bindings.put(requireNonNull(key7),
                         requireNonNull(exp7)
                        );
        return map;
    }

    /**
     * static factory method to create a MapExp of eight mappings
     *
     * @param key1 the first key
     * @param exp1 the mapping associated to the first key
     * @param key2 the second key
     * @param exp2 the mapping associated to the second key
     * @param key3 the third key
     * @param exp3 the mapping associated to the third key
     * @param key4 the fourth key
     * @param exp4 the mapping associated to the fourth key
     * @param key5 the fifth key
     * @param exp5 the mapping associated to the fifth key
     * @param key6 the sixth key
     * @param exp6 the mapping associated to the sixth key
     * @param key7 the seventh key
     * @param exp7 the mapping associated to the seventh key
     * @param key8 the eighth key
     * @param exp8 the mapping associated to the eighth key
     * @param <O>  the type of the map values
     * @return a MapExp
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapExp<O> par(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2,
                                    final String key3,
                                    final VIO<? extends O> exp3,
                                    final String key4,
                                    final VIO<? extends O> exp4,
                                    final String key5,
                                    final VIO<? extends O> exp5,
                                    final String key6,
                                    final VIO<? extends O> exp6,
                                    final String key7,
                                    final VIO<? extends O> exp7,
                                    final String key8,
                                    final VIO<? extends O> exp8
                                   ) {
        MapExp<O> map = par(key1, exp1,
                            key2, exp2,
                            key3, exp3,
                            key4, exp4,
                            key5, exp5,
                            key6, exp6,
                            key7, exp7
                           );

        map.bindings.put(requireNonNull(key8),
                         requireNonNull(exp8)
                        );
        return map;

    }

    /**
     * static factory method to create a MapExp of nine mappings
     *
     * @param key1 the first key
     * @param exp1 the mapping associated to the first key
     * @param key2 the second key
     * @param exp2 the mapping associated to the second key
     * @param key3 the third key
     * @param exp3 the mapping associated to the third key
     * @param key4 the fourth key
     * @param exp4 the mapping associated to the fourth key
     * @param key5 the fifth key
     * @param exp5 the mapping associated to the fifth key
     * @param key6 the sixth key
     * @param exp6 the mapping associated to the sixth key
     * @param key7 the seventh key
     * @param exp7 the mapping associated to the seventh key
     * @param key8 the eighth key
     * @param exp8 the mapping associated to the eighth key
     * @param key9 the ninth key
     * @param exp9 the mapping associated to the ninth key
     * @param <O>  the type of the map values
     * @return a MapExp
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapExp<O> par(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2,
                                    final String key3,
                                    final VIO<? extends O> exp3,
                                    final String key4,
                                    final VIO<? extends O> exp4,
                                    final String key5,
                                    final VIO<? extends O> exp5,
                                    final String key6,
                                    final VIO<? extends O> exp6,
                                    final String key7,
                                    final VIO<? extends O> exp7,
                                    final String key8,
                                    final VIO<? extends O> exp8,
                                    final String key9,
                                    final VIO<? extends O> exp9
                                   ) {
        MapExp<O> map = par(key1, exp1,
                            key2, exp2,
                            key3, exp3,
                            key4, exp4,
                            key5, exp5,
                            key6, exp6,
                            key7, exp7,
                            key8, exp8
                           );

        map.bindings.put(requireNonNull(key9),
                         requireNonNull(exp9)
                        );
        return map;
    }

    /**
     * static factory method to create a MapExp of ten mappings
     *
     * @param key1  the first key
     * @param exp1  the mapping associated to the first key
     * @param key2  the second key
     * @param exp2  the mapping associated to the second key
     * @param key3  the third key
     * @param exp3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param exp4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param exp5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param exp6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param exp7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param exp8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param exp9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param exp10 the mapping associated to the tenth key
     * @param <O>   the type of the map values
     * @return a MapExp
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapExp<O> par(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2,
                                    final String key3,
                                    final VIO<? extends O> exp3,
                                    final String key4,
                                    final VIO<? extends O> exp4,
                                    final String key5,
                                    final VIO<? extends O> exp5,
                                    final String key6,
                                    final VIO<? extends O> exp6,
                                    final String key7,
                                    final VIO<? extends O> exp7,
                                    final String key8,
                                    final VIO<? extends O> exp8,
                                    final String key9,
                                    final VIO<? extends O> exp9,
                                    final String key10,
                                    final VIO<? extends O> exp10
                                   ) {
        MapExp<O> map = par(key1, exp1,
                            key2, exp2,
                            key3, exp3,
                            key4, exp4,
                            key5, exp5,
                            key6, exp6,
                            key7, exp7,
                            key8, exp8,
                            key9, exp9
                           );

        map.bindings.put(requireNonNull(key10),
                         requireNonNull(exp10)
                        );
        return map;
    }

    /**
     * static factory method to create a MapExp of eleven mappings
     *
     * @param key1  the first key
     * @param exp1  the mapping associated to the first key
     * @param key2  the second key
     * @param exp2  the mapping associated to the second key
     * @param key3  the third key
     * @param exp3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param exp4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param exp5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param exp6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param exp7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param exp8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param exp9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param exp10 the mapping associated to the eleventh key
     * @param key11 the tenth key
     * @param exp11 the mapping associated to the eleventh key
     * @param <O>   the type of the map values
     * @return a MapExp
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapExp<O> par(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2,
                                    final String key3,
                                    final VIO<? extends O> exp3,
                                    final String key4,
                                    final VIO<? extends O> exp4,
                                    final String key5,
                                    final VIO<? extends O> exp5,
                                    final String key6,
                                    final VIO<? extends O> exp6,
                                    final String key7,
                                    final VIO<? extends O> exp7,
                                    final String key8,
                                    final VIO<? extends O> exp8,
                                    final String key9,
                                    final VIO<? extends O> exp9,
                                    final String key10,
                                    final VIO<? extends O> exp10,
                                    final String key11,
                                    final VIO<? extends O> exp11
                                   ) {
        MapExp<O> map = par(key1, exp1,
                            key2, exp2,
                            key3, exp3,
                            key4, exp4,
                            key5, exp5,
                            key6, exp6,
                            key7, exp7,
                            key8, exp8,
                            key9, exp9,
                            key10, exp10
                           );

        map.bindings.put(requireNonNull(key11),
                         requireNonNull(exp11)
                        );
        return map;
    }

    /**
     * static factory method to create a MapExp of twelve mappings
     *
     * @param key1  the first key
     * @param exp1  the mapping associated to the first key
     * @param key2  the second key
     * @param exp2  the mapping associated to the second key
     * @param key3  the third key
     * @param exp3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param exp4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param exp5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param exp6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param exp7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param exp8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param exp9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param exp10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param exp11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param exp12 the mapping associated to the twelfth key
     * @param <O>   the type of the map values
     * @return a MapExp
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapExp<O> par(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2,
                                    final String key3,
                                    final VIO<? extends O> exp3,
                                    final String key4,
                                    final VIO<? extends O> exp4,
                                    final String key5,
                                    final VIO<? extends O> exp5,
                                    final String key6,
                                    final VIO<? extends O> exp6,
                                    final String key7,
                                    final VIO<? extends O> exp7,
                                    final String key8,
                                    final VIO<? extends O> exp8,
                                    final String key9,
                                    final VIO<? extends O> exp9,
                                    final String key10,
                                    final VIO<? extends O> exp10,
                                    final String key11,
                                    final VIO<? extends O> exp11,
                                    final String key12,
                                    final VIO<? extends O> exp12
                                   ) {
        MapExp<O> map = par(key1, exp1,
                            key2, exp2,
                            key3, exp3,
                            key4, exp4,
                            key5, exp5,
                            key6, exp6,
                            key7, exp7,
                            key8, exp8,
                            key9, exp9,
                            key10, exp10,
                            key11, exp11
                           );

        map.bindings.put(requireNonNull(key12),
                         requireNonNull(exp12)
                        );
        return map;
    }

    /**
     * static factory method to create a MapExp of thirteen mappings
     *
     * @param key1  the first key
     * @param exp1  the mapping associated to the first key
     * @param key2  the second key
     * @param exp2  the mapping associated to the second key
     * @param key3  the third key
     * @param exp3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param exp4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param exp5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param exp6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param exp7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param exp8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param exp9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param exp10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param exp11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param exp12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param exp13 the mapping associated to the thirteenth key
     * @param <O>   the type of the map values
     * @return a MapExp
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapExp<O> par(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2,
                                    final String key3,
                                    final VIO<? extends O> exp3,
                                    final String key4,
                                    final VIO<? extends O> exp4,
                                    final String key5,
                                    final VIO<? extends O> exp5,
                                    final String key6,
                                    final VIO<? extends O> exp6,
                                    final String key7,
                                    final VIO<? extends O> exp7,
                                    final String key8,
                                    final VIO<? extends O> exp8,
                                    final String key9,
                                    final VIO<? extends O> exp9,
                                    final String key10,
                                    final VIO<? extends O> exp10,
                                    final String key11,
                                    final VIO<? extends O> exp11,
                                    final String key12,
                                    final VIO<? extends O> exp12,
                                    final String key13,
                                    final VIO<? extends O> exp13
                                   ) {
        MapExp<O> map = par(key1, exp1,
                            key2, exp2,
                            key3, exp3,
                            key4, exp4,
                            key5, exp5,
                            key6, exp6,
                            key7, exp7,
                            key8, exp8,
                            key9, exp9,
                            key10, exp10,
                            key11, exp11,
                            key12, exp12
                           );

        map.bindings.put(requireNonNull(key13),
                         requireNonNull(exp13)
                        );
        return map;
    }

    /**
     * static factory method to create a MapExp of fourteen mappings
     *
     * @param key1  the first key
     * @param exp1  the mapping associated to the first key
     * @param key2  the second key
     * @param exp2  the mapping associated to the second key
     * @param key3  the third key
     * @param exp3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param exp4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param exp5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param exp6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param exp7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param exp8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param exp9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param exp10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param exp11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param exp12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param exp13 the mapping associated to the thirteenth key
     * @param key14 the fourteenth key
     * @param exp14 the mapping associated to the fourteenth key
     * @param <O>   the type of the map values
     * @return a MapExp
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapExp<O> par(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2,
                                    final String key3,
                                    final VIO<? extends O> exp3,
                                    final String key4,
                                    final VIO<? extends O> exp4,
                                    final String key5,
                                    final VIO<? extends O> exp5,
                                    final String key6,
                                    final VIO<? extends O> exp6,
                                    final String key7,
                                    final VIO<? extends O> exp7,
                                    final String key8,
                                    final VIO<? extends O> exp8,
                                    final String key9,
                                    final VIO<? extends O> exp9,
                                    final String key10,
                                    final VIO<? extends O> exp10,
                                    final String key11,
                                    final VIO<? extends O> exp11,
                                    final String key12,
                                    final VIO<? extends O> exp12,
                                    final String key13,
                                    final VIO<? extends O> exp13,
                                    final String key14,
                                    final VIO<? extends O> exp14
                                   ) {
        MapExp<O> map = par(key1, exp1,
                            key2, exp2,
                            key3, exp3,
                            key4, exp4,
                            key5, exp5,
                            key6, exp6,
                            key7, exp7,
                            key8, exp8,
                            key9, exp9,
                            key10, exp10,
                            key11, exp11,
                            key12, exp12,
                            key13, exp13
                           );

        map.bindings.put(requireNonNull(key14),
                         requireNonNull(exp14)
                        );
        return map;

    }

    /**
     * static factory method to create a MapExp of fifteen mappings
     *
     * @param key1  the first key
     * @param exp1  the mapping associated to the first key
     * @param key2  the second key
     * @param exp2  the mapping associated to the second key
     * @param key3  the third key
     * @param exp3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param exp4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param exp5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param exp6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param exp7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param exp8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param exp9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param exp10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param exp11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param exp12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param exp13 the mapping associated to the thirteenth key
     * @param key14 the fourteenth key
     * @param exp14 the mapping associated to the fourteenth key
     * @param key15 the fifteenth key
     * @param exp15 the mapping associated to the fifteenth key
     * @param <O>   the type of the map values
     * @return a MapExp
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapExp<O> par(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2,
                                    final String key3,
                                    final VIO<? extends O> exp3,
                                    final String key4,
                                    final VIO<? extends O> exp4,
                                    final String key5,
                                    final VIO<? extends O> exp5,
                                    final String key6,
                                    final VIO<? extends O> exp6,
                                    final String key7,
                                    final VIO<? extends O> exp7,
                                    final String key8,
                                    final VIO<? extends O> exp8,
                                    final String key9,
                                    final VIO<? extends O> exp9,
                                    final String key10,
                                    final VIO<? extends O> exp10,
                                    final String key11,
                                    final VIO<? extends O> exp11,
                                    final String key12,
                                    final VIO<? extends O> exp12,
                                    final String key13,
                                    final VIO<? extends O> exp13,
                                    final String key14,
                                    final VIO<? extends O> exp14,
                                    final String key15,
                                    final VIO<? extends O> exp15
                                   ) {
        MapExp<O> map = par(key1, exp1,
                            key2, exp2,
                            key3, exp3,
                            key4, exp4,
                            key5, exp5,
                            key6, exp6,
                            key7, exp7,
                            key8, exp8,
                            key9, exp9,
                            key10, exp10,
                            key11, exp11,
                            key12, exp12,
                            key13, exp13,
                            key14, exp14
                           );

        map.bindings.put(requireNonNull(key15),
                         requireNonNull(exp15)
                        );
        return map;

    }

    @SuppressWarnings({"unchecked"})
    public static <O> MapExp<O> seq() {
        return MapExpSeq.EMPTY;
    }

    /**
     * static factory method to create a MapExp of one mapping
     *
     * @param key the key
     * @param exp the mapping associated to the key
     * @param <O> the type of the map values
     * @return a MapExp
     */
    public static <O> MapExp<O> seq(final String key,
                                    final VIO<? extends O> exp
                                   ) {
        MapExpSeq<O> obj = new MapExpSeq<>();
        obj.bindings.put(requireNonNull(key),
                         requireNonNull(exp)
                        );
        return obj;
    }

    /**
     * static factory method to create a MapExp of one mapping
     *
     * @param key1 the first key
     * @param exp1 the mapping associated to the first key
     * @param key2 the second key
     * @param exp2 the mapping associated to the second key
     * @param <O>  the type of the map values
     * @return a MapExp
     */
    public static <O> MapExp<O> seq(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2
                                   ) {
        MapExp<O> map = seq(requireNonNull(key1),
                            requireNonNull(exp1)
                           );
        map.bindings.put(requireNonNull(key2),
                         requireNonNull(exp2)
                        );
        return map;
    }

    /**
     * static factory method to create a MapExp of three mappings
     *
     * @param key1 the first key
     * @param exp1 the mapping associated to the first key
     * @param key2 the second key
     * @param exp2 the mapping associated to the second key
     * @param key3 the third key
     * @param exp3 the mapping associated to the third key
     * @param <O>  the type of the map values
     * @return a MapExp
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapExp<O> seq(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2,
                                    final String key3,
                                    final VIO<? extends O> exp3
                                   ) {
        MapExp<O> map = seq(key1, exp1,
                            key2, exp2
                           );
        map.bindings.put(requireNonNull(key3),
                         requireNonNull(exp3)
                        );
        return map;
    }

    /**
     * static factory method to create a MapExp of four mappings
     *
     * @param key1 the first key
     * @param exp1 the mapping associated to the first key
     * @param key2 the second key
     * @param exp2 the mapping associated to the second key
     * @param key3 the third key
     * @param exp3 the mapping associated to the third key
     * @param key4 the fourth key
     * @param exp4 the mapping associated to the fourth key
     * @param <O>  the type of the map values
     * @return a MapExp
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapExp<O> seq(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2,
                                    final String key3,
                                    final VIO<? extends O> exp3,
                                    final String key4,
                                    final VIO<? extends O> exp4
                                   ) {
        MapExp<O> map = seq(key1, exp1,
                            key2, exp2,
                            key3, exp3
                           );
        map.bindings.put(requireNonNull(key4),
                         requireNonNull(exp4)
                        );
        return map;
    }

    /**
     * static factory method to create a MapExp of five mappings
     *
     * @param key1 the first key
     * @param exp1 the mapping associated to the first key
     * @param key2 the second key
     * @param exp2 the mapping associated to the second key
     * @param key3 the third key
     * @param exp3 the mapping associated to the third key
     * @param key4 the fourth key
     * @param exp4 the mapping associated to the fourth key
     * @param key5 the fifth key
     * @param exp5 the mapping associated to the fifth key
     * @param <O>  the type of the map values
     * @return a MapExp
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapExp<O> seq(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2,
                                    final String key3,
                                    final VIO<? extends O> exp3,
                                    final String key4,
                                    final VIO<? extends O> exp4,
                                    final String key5,
                                    final VIO<? extends O> exp5
                                   ) {
        MapExp<O> map = seq(key1, exp1,
                            key2, exp2,
                            key3, exp3,
                            key4, exp4
                           );
        map.bindings.put(requireNonNull(key5),
                         requireNonNull(exp5)
                        );
        return map;
    }

    /**
     * static factory method to create a MapExp of six mappings
     *
     * @param key1 the first key
     * @param exp1 the mapping associated to the first key
     * @param key2 the second key
     * @param exp2 the mapping associated to the second key
     * @param key3 the third key
     * @param exp3 the mapping associated to the third key
     * @param key4 the fourth key
     * @param exp4 the mapping associated to the fourth key
     * @param key5 the fifth key
     * @param exp5 the mapping associated to the fifth key
     * @param key6 the sixth key
     * @param exp6 the mapping associated to the sixth key
     * @param <O>  the type of the map values
     * @return a MapExp
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapExp<O> seq(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2,
                                    final String key3,
                                    final VIO<? extends O> exp3,
                                    final String key4,
                                    final VIO<? extends O> exp4,
                                    final String key5,
                                    final VIO<? extends O> exp5,
                                    final String key6,
                                    final VIO<? extends O> exp6
                                   ) {

        MapExp<O> map = seq(key1, exp1,
                            key2, exp2,
                            key3, exp3,
                            key4, exp4,
                            key5, exp5
                           );
        map.bindings.put(requireNonNull(key6),
                         requireNonNull(exp6)
                        );
        return map;
    }

    /**
     * static factory method to create a MapExp of seven mappings
     *
     * @param key1 the first key
     * @param exp1 the mapping associated to the first key
     * @param key2 the second key
     * @param exp2 the mapping associated to the second key
     * @param key3 the third key
     * @param exp3 the mapping associated to the third key
     * @param key4 the fourth key
     * @param exp4 the mapping associated to the fourth key
     * @param key5 the fifth key
     * @param exp5 the mapping associated to the fifth key
     * @param key6 the sixth key
     * @param exp6 the mapping associated to the sixth key
     * @param key7 the seventh key
     * @param exp7 the mapping associated to the seventh key
     * @param <O>  the type of the map values
     * @return a MapExp
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapExp<O> seq(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2,
                                    final String key3,
                                    final VIO<? extends O> exp3,
                                    final String key4,
                                    final VIO<? extends O> exp4,
                                    final String key5,
                                    final VIO<? extends O> exp5,
                                    final String key6,
                                    final VIO<? extends O> exp6,
                                    final String key7,
                                    final VIO<? extends O> exp7
                                   ) {
        MapExp<O> map = seq(key1, exp1,
                            key2, exp2,
                            key3, exp3,
                            key4, exp4,
                            key5, exp5,
                            key6, exp6
                           );
        map.bindings.put(requireNonNull(key7),
                         requireNonNull(exp7)
                        );
        return map;
    }

    /**
     * static factory method to create a MapExp of eight mappings
     *
     * @param key1 the first key
     * @param exp1 the mapping associated to the first key
     * @param key2 the second key
     * @param exp2 the mapping associated to the second key
     * @param key3 the third key
     * @param exp3 the mapping associated to the third key
     * @param key4 the fourth key
     * @param exp4 the mapping associated to the fourth key
     * @param key5 the fifth key
     * @param exp5 the mapping associated to the fifth key
     * @param key6 the sixth key
     * @param exp6 the mapping associated to the sixth key
     * @param key7 the seventh key
     * @param exp7 the mapping associated to the seventh key
     * @param key8 the eighth key
     * @param exp8 the mapping associated to the eighth key
     * @param <O>  the type of the map values
     * @return a MapExp
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapExp<O> seq(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2,
                                    final String key3,
                                    final VIO<? extends O> exp3,
                                    final String key4,
                                    final VIO<? extends O> exp4,
                                    final String key5,
                                    final VIO<? extends O> exp5,
                                    final String key6,
                                    final VIO<? extends O> exp6,
                                    final String key7,
                                    final VIO<? extends O> exp7,
                                    final String key8,
                                    final VIO<? extends O> exp8
                                   ) {
        MapExp<O> map = seq(key1, exp1,
                            key2, exp2,
                            key3, exp3,
                            key4, exp4,
                            key5, exp5,
                            key6, exp6,
                            key7, exp7
                           );
        map.bindings.put(requireNonNull(key8),
                         requireNonNull(exp8)
                        );
        return map;
    }

    /**
     * static factory method to create a MapExp of nine mappings
     *
     * @param key1 the first key
     * @param exp1 the mapping associated to the first key
     * @param key2 the second key
     * @param exp2 the mapping associated to the second key
     * @param key3 the third key
     * @param exp3 the mapping associated to the third key
     * @param key4 the fourth key
     * @param exp4 the mapping associated to the fourth key
     * @param key5 the fifth key
     * @param exp5 the mapping associated to the fifth key
     * @param key6 the sixth key
     * @param exp6 the mapping associated to the sixth key
     * @param key7 the seventh key
     * @param exp7 the mapping associated to the seventh key
     * @param key8 the eighth key
     * @param exp8 the mapping associated to the eighth key
     * @param key9 the ninth key
     * @param exp9 the mapping associated to the ninth key
     * @param <O>  the type of the map values
     * @return a MapExp
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapExp<O> seq(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2,
                                    final String key3,
                                    final VIO<? extends O> exp3,
                                    final String key4,
                                    final VIO<? extends O> exp4,
                                    final String key5,
                                    final VIO<? extends O> exp5,
                                    final String key6,
                                    final VIO<? extends O> exp6,
                                    final String key7,
                                    final VIO<? extends O> exp7,
                                    final String key8,
                                    final VIO<? extends O> exp8,
                                    final String key9,
                                    final VIO<? extends O> exp9
                                   ) {
        MapExp<O> map = seq(key1, exp1,
                            key2, exp2,
                            key3, exp3,
                            key4, exp4,
                            key5, exp5,
                            key6, exp6,
                            key7, exp7,
                            key8, exp8
                           );
        map.bindings.put(requireNonNull(key9),
                         requireNonNull(exp9)
                        );
        return map;
    }

    /**
     * static factory method to create a MapExp of ten mappings
     *
     * @param key1  the first key
     * @param exp1  the mapping associated to the first key
     * @param key2  the second key
     * @param exp2  the mapping associated to the second key
     * @param key3  the third key
     * @param exp3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param exp4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param exp5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param exp6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param exp7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param exp8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param exp9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param exp10 the mapping associated to the tenth key
     * @param <O>   the type of the map values
     * @return a MapExp
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapExp<O> seq(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2,
                                    final String key3,
                                    final VIO<? extends O> exp3,
                                    final String key4,
                                    final VIO<? extends O> exp4,
                                    final String key5,
                                    final VIO<? extends O> exp5,
                                    final String key6,
                                    final VIO<? extends O> exp6,
                                    final String key7,
                                    final VIO<? extends O> exp7,
                                    final String key8,
                                    final VIO<? extends O> exp8,
                                    final String key9,
                                    final VIO<? extends O> exp9,
                                    final String key10,
                                    final VIO<? extends O> exp10
                                   ) {
        MapExp<O> map = seq(key1, exp1,
                            key2, exp2,
                            key3, exp3,
                            key4, exp4,
                            key5, exp5,
                            key6, exp6,
                            key7, exp7,
                            key8, exp8,
                            key9, exp9
                           );
        map.bindings.put(requireNonNull(key10),
                         requireNonNull(exp10)
                        );
        return map;

    }

    /**
     * static factory method to create a MapExp of eleven mappings
     *
     * @param key1  the first key
     * @param exp1  the mapping associated to the first key
     * @param key2  the second key
     * @param exp2  the mapping associated to the second key
     * @param key3  the third key
     * @param exp3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param exp4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param exp5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param exp6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param exp7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param exp8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param exp9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param exp10 the mapping associated to the eleventh key
     * @param key11 the tenth key
     * @param exp11 the mapping associated to the eleventh key
     * @param <O>   the type of the map values
     * @return a MapExp
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapExp<O> seq(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2,
                                    final String key3,
                                    final VIO<? extends O> exp3,
                                    final String key4,
                                    final VIO<? extends O> exp4,
                                    final String key5,
                                    final VIO<? extends O> exp5,
                                    final String key6,
                                    final VIO<? extends O> exp6,
                                    final String key7,
                                    final VIO<? extends O> exp7,
                                    final String key8,
                                    final VIO<? extends O> exp8,
                                    final String key9,
                                    final VIO<? extends O> exp9,
                                    final String key10,
                                    final VIO<? extends O> exp10,
                                    final String key11,
                                    final VIO<? extends O> exp11
                                   ) {
        MapExp<O> map = seq(key1, exp1,
                            key2, exp2,
                            key3, exp3,
                            key4, exp4,
                            key5, exp5,
                            key6, exp6,
                            key7, exp7,
                            key8, exp8,
                            key9, exp9,
                            key10, exp10
                           );
        map.bindings.put(requireNonNull(key11),
                         requireNonNull(exp11)
                        );
        return map;

    }

    /**
     * static factory method to create a MapExp of twelve mappings
     *
     * @param key1  the first key
     * @param exp1  the mapping associated to the first key
     * @param key2  the second key
     * @param exp2  the mapping associated to the second key
     * @param key3  the third key
     * @param exp3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param exp4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param exp5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param exp6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param exp7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param exp8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param exp9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param exp10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param exp11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param exp12 the mapping associated to the twelfth key
     * @param <O>   the type of the map values
     * @return a MapExp
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapExp<O> seq(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2,
                                    final String key3,
                                    final VIO<? extends O> exp3,
                                    final String key4,
                                    final VIO<? extends O> exp4,
                                    final String key5,
                                    final VIO<? extends O> exp5,
                                    final String key6,
                                    final VIO<? extends O> exp6,
                                    final String key7,
                                    final VIO<? extends O> exp7,
                                    final String key8,
                                    final VIO<? extends O> exp8,
                                    final String key9,
                                    final VIO<? extends O> exp9,
                                    final String key10,
                                    final VIO<? extends O> exp10,
                                    final String key11,
                                    final VIO<? extends O> exp11,
                                    final String key12,
                                    final VIO<? extends O> exp12
                                   ) {
        MapExp<O> map = seq(key1, exp1,
                            key2, exp2,
                            key3, exp3,
                            key4, exp4,
                            key5, exp5,
                            key6, exp6,
                            key7, exp7,
                            key8, exp8,
                            key9, exp9,
                            key10, exp10,
                            key11, exp11
                           );
        map.bindings.put(requireNonNull(key12),
                         requireNonNull(exp12)
                        );
        return map;
    }

    /**
     * static factory method to create a MapExp of thirteen mappings
     *
     * @param key1  the first key
     * @param exp1  the mapping associated to the first key
     * @param key2  the second key
     * @param exp2  the mapping associated to the second key
     * @param key3  the third key
     * @param exp3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param exp4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param exp5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param exp6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param exp7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param exp8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param exp9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param exp10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param exp11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param exp12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param exp13 the mapping associated to the thirteenth key
     * @param <O>   the type of the map values
     * @return a MapExp
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapExp<O> seq(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2,
                                    final String key3,
                                    final VIO<? extends O> exp3,
                                    final String key4,
                                    final VIO<? extends O> exp4,
                                    final String key5,
                                    final VIO<? extends O> exp5,
                                    final String key6,
                                    final VIO<? extends O> exp6,
                                    final String key7,
                                    final VIO<? extends O> exp7,
                                    final String key8,
                                    final VIO<? extends O> exp8,
                                    final String key9,
                                    final VIO<? extends O> exp9,
                                    final String key10,
                                    final VIO<? extends O> exp10,
                                    final String key11,
                                    final VIO<? extends O> exp11,
                                    final String key12,
                                    final VIO<? extends O> exp12,
                                    final String key13,
                                    final VIO<? extends O> exp13
                                   ) {
        MapExp<O> map = seq(key1, exp1,
                            key2, exp2,
                            key3, exp3,
                            key4, exp4,
                            key5, exp5,
                            key6, exp6,
                            key7, exp7,
                            key8, exp8,
                            key9, exp9,
                            key10, exp10,
                            key11, exp11,
                            key12, exp12
                           );
        map.bindings.put(requireNonNull(key13),
                         requireNonNull(exp13)
                        );
        return map;
    }

    /**
     * static factory method to create a MapExp of fourteen mappings
     *
     * @param key1  the first key
     * @param exp1  the mapping associated to the first key
     * @param key2  the second key
     * @param exp2  the mapping associated to the second key
     * @param key3  the third key
     * @param exp3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param exp4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param exp5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param exp6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param exp7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param exp8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param exp9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param exp10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param exp11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param exp12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param exp13 the mapping associated to the thirteenth key
     * @param key14 the fourteenth key
     * @param exp14 the mapping associated to the fourteenth key
     * @param <O>   the type of the map values
     * @return a MapExp
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapExp<O> seq(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2,
                                    final String key3,
                                    final VIO<? extends O> exp3,
                                    final String key4,
                                    final VIO<? extends O> exp4,
                                    final String key5,
                                    final VIO<? extends O> exp5,
                                    final String key6,
                                    final VIO<? extends O> exp6,
                                    final String key7,
                                    final VIO<? extends O> exp7,
                                    final String key8,
                                    final VIO<? extends O> exp8,
                                    final String key9,
                                    final VIO<? extends O> exp9,
                                    final String key10,
                                    final VIO<? extends O> exp10,
                                    final String key11,
                                    final VIO<? extends O> exp11,
                                    final String key12,
                                    final VIO<? extends O> exp12,
                                    final String key13,
                                    final VIO<? extends O> exp13,
                                    final String key14,
                                    final VIO<? extends O> exp14
                                   ) {
        MapExp<O> map = seq(key1, exp1,
                            key2, exp2,
                            key3, exp3,
                            key4, exp4,
                            key5, exp5,
                            key6, exp6,
                            key7, exp7,
                            key8, exp8,
                            key9, exp9,
                            key10, exp10,
                            key11, exp11,
                            key12, exp12,
                            key13, exp13
                           );
        map.bindings.put(requireNonNull(key14),
                         requireNonNull(exp14)
                        );
        return map;

    }

    /**
     * static factory method to create a MapExp of fifteen mappings
     *
     * @param key1  the first key
     * @param exp1  the mapping associated to the first key
     * @param key2  the second key
     * @param exp2  the mapping associated to the second key
     * @param key3  the third key
     * @param exp3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param exp4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param exp5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param exp6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param exp7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param exp8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param exp9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param exp10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param exp11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param exp12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param exp13 the mapping associated to the thirteenth key
     * @param key14 the fourteenth key
     * @param exp14 the mapping associated to the fourteenth key
     * @param key15 the fifteenth key
     * @param exp15 the mapping associated to the fifteenth key
     * @param <O>   the type of the map values
     * @return a MapExp
     */
    @SuppressWarnings("squid:S00107")
    public static <O> MapExp<O> seq(final String key1,
                                    final VIO<? extends O> exp1,
                                    final String key2,
                                    final VIO<? extends O> exp2,
                                    final String key3,
                                    final VIO<? extends O> exp3,
                                    final String key4,
                                    final VIO<? extends O> exp4,
                                    final String key5,
                                    final VIO<? extends O> exp5,
                                    final String key6,
                                    final VIO<? extends O> exp6,
                                    final String key7,
                                    final VIO<? extends O> exp7,
                                    final String key8,
                                    final VIO<? extends O> exp8,
                                    final String key9,
                                    final VIO<? extends O> exp9,
                                    final String key10,
                                    final VIO<? extends O> exp10,
                                    final String key11,
                                    final VIO<? extends O> exp11,
                                    final String key12,
                                    final VIO<? extends O> exp12,
                                    final String key13,
                                    final VIO<? extends O> exp13,
                                    final String key14,
                                    final VIO<? extends O> exp14,
                                    final String key15,
                                    final VIO<? extends O> exp15
                                   ) {

        MapExp<O> map = seq(key1, exp1,
                            key2, exp2,
                            key3, exp3,
                            key4, exp4,
                            key5, exp5,
                            key6, exp6,
                            key7, exp7,
                            key8, exp8,
                            key9, exp9,
                            key10, exp10,
                            key11, exp11,
                            key12, exp12,
                            key13, exp13,
                            key14, exp14
                           );
        map.bindings.put(requireNonNull(key15),
                         requireNonNull(exp15)
                        );
        return map;
    }

    public boolean isEmpty() {
        return bindings.isEmpty();
    }

    public abstract MapExp<O> set(final String key,
                                  final VIO<? extends O> exp
                                 );


}
