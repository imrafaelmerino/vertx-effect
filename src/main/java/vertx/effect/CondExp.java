package vertx.effect;


import java.util.ArrayList;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * This expression is made up of different predicates. Each predicate has an associated consequence. The computed value
 * of the consequence of the first predicate that is true is returned. The predicates of the expression can be evaluated
 * either in parallel or sequentially.
 *
 * @param <O> the type of the effect returned by this expression.
 */
public abstract sealed class CondExp<O> extends Exp<O> permits CondExpPar, CondExpSeq {


    /**
     * It creates a Cond expression which predicates will be executed in parallel. Once all the predicates are
     * evaluated, it computes and returns the consequence of the first one that is true. If no predicate is evaluated to
     * true, it returns the specified default value.
     *
     * @param predicate1   the first predicate
     * @param consequence1 the consequence of the first predicate
     * @param predicate2   the second predicate
     * @param consequence2 the consequence of the second predicate
     * @param otherwise    the default value returned if no predicate is true
     * @param <E>          the type of the expression
     * @return a Cond expression
     */
    public static <E> CondExp<E> par(final VIO<Boolean> predicate1,
                                     final Supplier<VIO<E>> consequence1,
                                     final VIO<Boolean> predicate2,
                                     final Supplier<VIO<E>> consequence2,
                                     final Supplier<VIO<E>> otherwise
                                    ) {

        var predicates = new ArrayList<VIO<Boolean>>();
        var consequences = new ArrayList<Supplier<VIO<E>>>();
        predicates.add(requireNonNull(predicate1));
        predicates.add(requireNonNull(predicate2));
        consequences.add(requireNonNull(consequence1));
        consequences.add(requireNonNull(consequence2));
        return new CondExpPar<>(predicates,
                                consequences,
                                requireNonNull(otherwise)
        );

    }

    /**
     * It creates a Cond expression which predicates will be executed in parallel. Once all the predicates are
     * evaluated, it computes and returns the consequence of the first one that is true. If no predicate is evaluated to
     * true, it returns {@link VIO#NULL}
     *
     * @param predicate1   the first predicate
     * @param consequence1 the consequence of the first predicate
     * @param predicate2   the second predicate
     * @param consequence2 the consequence of the second predicate
     * @param <E>          the type of the expression
     * @return a Cond expression
     */
    public static <E> CondExp<E> par(final VIO<Boolean> predicate1,
                                     final Supplier<VIO<E>> consequence1,
                                     final VIO<Boolean> predicate2,
                                     final Supplier<VIO<E>> consequence2
                                    ) {

        var predicates = new ArrayList<VIO<Boolean>>();
        var consequences = new ArrayList<Supplier<VIO<E>>>();
        predicates.add(requireNonNull(predicate1));
        predicates.add(requireNonNull(predicate2));
        consequences.add(requireNonNull(consequence1));
        consequences.add(requireNonNull(consequence2));
        return new CondExpPar<>(predicates,
                                consequences
        );

    }

    /**
     * It creates a Cond expression which predicates will be executed in parallel. Once all the predicates are
     * evaluated, it computes and returns the consequence of the first one that is true. If no predicate is evaluated to
     * true, it returns the specified default value.
     *
     * @param predicate1   the first predicate
     * @param consequence1 the consequence of the first predicate
     * @param predicate2   the second predicate
     * @param consequence2 the consequence of the second predicate
     * @param predicate3   the third predicate
     * @param consequence3 the consequence of the third predicate
     * @param otherwise    the default value returned if no predicate is true
     * @param <E>          the type of the expression
     * @return a Cond expression
     */
    public static <E> CondExp<E> par(final VIO<Boolean> predicate1,
                                     final Supplier<VIO<E>> consequence1,
                                     final VIO<Boolean> predicate2,
                                     final Supplier<VIO<E>> consequence2,
                                     final VIO<Boolean> predicate3,
                                     final Supplier<VIO<E>> consequence3,
                                     final Supplier<VIO<E>> otherwise
                                    ) {

        var predicates = new ArrayList<VIO<Boolean>>();
        var consequences = new ArrayList<Supplier<VIO<E>>>();
        predicates.add(requireNonNull(predicate1));
        predicates.add(requireNonNull(predicate2));
        predicates.add(requireNonNull(predicate3));
        consequences.add(requireNonNull(consequence1));
        consequences.add(requireNonNull(consequence2));
        consequences.add(requireNonNull(consequence3));

        return new CondExpPar<>(predicates,
                                consequences,
                                requireNonNull(otherwise)
        );

    }

    /**
     * It creates a Cond expression which predicates will be executed in parallel. Once all the predicates are
     * evaluated, it computes and returns the consequence of the first one that is true. If no predicate is evaluated to
     * true, it returns {@link VIO#NULL}
     *
     * @param predicate1   the first predicate
     * @param consequence1 the consequence of the first predicate
     * @param predicate2   the second predicate
     * @param consequence2 the consequence of the second predicate
     * @param predicate3   the third predicate
     * @param consequence3 the consequence of the third predicate
     * @param <E>          the type of the expression
     * @return a Cond expression
     */
    public static <E> CondExp<E> par(final VIO<Boolean> predicate1,
                                     final Supplier<VIO<E>> consequence1,
                                     final VIO<Boolean> predicate2,
                                     final Supplier<VIO<E>> consequence2,
                                     final VIO<Boolean> predicate3,
                                     final Supplier<VIO<E>> consequence3
                                    ) {

        var predicates = new ArrayList<VIO<Boolean>>();
        var consequences = new ArrayList<Supplier<VIO<E>>>();
        predicates.add(requireNonNull(predicate1));
        predicates.add(requireNonNull(predicate2));
        predicates.add(requireNonNull(predicate3));
        consequences.add(requireNonNull(consequence1));
        consequences.add(requireNonNull(consequence2));
        consequences.add(requireNonNull(consequence3));

        return new CondExpPar<>(predicates,
                                consequences
        );

    }

    /**
     * It creates a Cond expression which predicates will be executed in parallel. Once all the predicates are
     * evaluated, it computes and returns the consequence of the first one that is true. If no predicate is evaluated to
     * true, it returns the specified default value.
     *
     * @param predicate1   the first predicate
     * @param consequence1 the consequence of the first predicate
     * @param predicate2   the second predicate
     * @param consequence2 the consequence of the second predicate
     * @param predicate3   the third predicate
     * @param consequence3 the consequence of the third predicate
     * @param predicate4   the forth predicate
     * @param consequence4 the consequence of the forth predicate
     * @param otherwise    the default value returned if no predicate is true
     * @param <E>          the type of the expression
     * @return a Cond expression
     */
    public static <E> CondExpPar<E> par(final VIO<Boolean> predicate1,
                                        final Supplier<VIO<E>> consequence1,
                                        final VIO<Boolean> predicate2,
                                        final Supplier<VIO<E>> consequence2,
                                        final VIO<Boolean> predicate3,
                                        final Supplier<VIO<E>> consequence3,
                                        final VIO<Boolean> predicate4,
                                        final Supplier<VIO<E>> consequence4,
                                        final Supplier<VIO<E>> otherwise
                                       ) {

        var predicates = new ArrayList<VIO<Boolean>>();
        var consequences = new ArrayList<Supplier<VIO<E>>>();
        predicates.add(requireNonNull(predicate1));
        predicates.add(requireNonNull(predicate2));
        predicates.add(requireNonNull(predicate3));
        predicates.add(requireNonNull(predicate4));
        consequences.add(requireNonNull(consequence1));
        consequences.add(requireNonNull(consequence2));
        consequences.add(requireNonNull(consequence3));
        consequences.add(requireNonNull(consequence4));

        return new CondExpPar<>(predicates,
                                consequences,
                                requireNonNull(otherwise)
        );

    }


    /**
     * It creates a Cond expression which predicates will be executed in parallel. Once all the predicates are
     * evaluated, it computes and returns the consequence of the first one that is true. If no predicate is evaluated to
     * true, it returns {@link VIO#NULL}
     *
     * @param predicate1   the first predicate
     * @param consequence1 the consequence of the first predicate
     * @param predicate2   the second predicate
     * @param consequence2 the consequence of the second predicate
     * @param predicate3   the third predicate
     * @param consequence3 the consequence of the third predicate
     * @param predicate4   the forth predicate
     * @param consequence4 the consequence of the forth predicate
     * @param <E>          the type of the expression
     * @return a Cond expression
     */
    public static <E> CondExp<E> par(final VIO<Boolean> predicate1,
                                     final Supplier<VIO<E>> consequence1,
                                     final VIO<Boolean> predicate2,
                                     final Supplier<VIO<E>> consequence2,
                                     final VIO<Boolean> predicate3,
                                     final Supplier<VIO<E>> consequence3,
                                     final VIO<Boolean> predicate4,
                                     final Supplier<VIO<E>> consequence4
                                    ) {

        var predicates = new ArrayList<VIO<Boolean>>();
        var consequences = new ArrayList<Supplier<VIO<E>>>();
        predicates.add(requireNonNull(predicate1));
        predicates.add(requireNonNull(predicate2));
        predicates.add(requireNonNull(predicate3));
        predicates.add(requireNonNull(predicate4));
        consequences.add(requireNonNull(consequence1));
        consequences.add(requireNonNull(consequence2));
        consequences.add(requireNonNull(consequence3));
        consequences.add(requireNonNull(consequence4));

        return new CondExpPar<>(predicates,
                                consequences
        );

    }


    /**
     * It creates a Cond expression which predicates will be executed in parallel. Once all the predicates are
     * evaluated, it computes and returns the consequence of the first one that is true. If no predicate is evaluated to
     * true, it returns the specified default value.
     *
     * @param predicate1   the first predicate
     * @param consequence1 the consequence of the first predicate
     * @param predicate2   the second predicate
     * @param consequence2 the consequence of the second predicate
     * @param predicate3   the third predicate
     * @param consequence3 the consequence of the third predicate
     * @param predicate4   the forth predicate
     * @param consequence4 the consequence of the forth predicate
     * @param predicate5   the firth predicate
     * @param consequence5 the consequence of the firth predicate
     * @param otherwise    the default value returned if no predicate is true
     * @param <E>          the type of the expression
     * @return a Cond expression
     */
    public static <E> CondExp<E> par(final VIO<Boolean> predicate1,
                                     final Supplier<VIO<E>> consequence1,
                                     final VIO<Boolean> predicate2,
                                     final Supplier<VIO<E>> consequence2,
                                     final VIO<Boolean> predicate3,
                                     final Supplier<VIO<E>> consequence3,
                                     final VIO<Boolean> predicate4,
                                     final Supplier<VIO<E>> consequence4,
                                     final VIO<Boolean> predicate5,
                                     final Supplier<VIO<E>> consequence5,
                                     final Supplier<VIO<E>> otherwise
                                    ) {


        var predicates = new ArrayList<VIO<Boolean>>();
        var consequences = new ArrayList<Supplier<VIO<E>>>();
        predicates.add(requireNonNull(predicate1));
        predicates.add(requireNonNull(predicate2));
        predicates.add(requireNonNull(predicate3));
        predicates.add(requireNonNull(predicate4));
        predicates.add(requireNonNull(predicate5));
        consequences.add(requireNonNull(consequence1));
        consequences.add(requireNonNull(consequence2));
        consequences.add(requireNonNull(consequence3));
        consequences.add(requireNonNull(consequence4));
        consequences.add(requireNonNull(consequence5));

        return new CondExpPar<>(predicates,
                                consequences,
                                requireNonNull(otherwise)
        );


    }

    /**
     * It creates a Cond expression which predicates will be executed in parallel. Once all the predicates are
     * evaluated, it computes and returns the consequence of the first one that is true. If no predicate is evaluated to
     * true, it returns {@link VIO#NULL}
     *
     * @param predicate1   the first predicate
     * @param consequence1 the consequence of the first predicate
     * @param predicate2   the second predicate
     * @param consequence2 the consequence of the second predicate
     * @param predicate3   the third predicate
     * @param consequence3 the consequence of the third predicate
     * @param predicate4   the forth predicate
     * @param consequence4 the consequence of the forth predicate
     * @param predicate5   the firth predicate
     * @param consequence5 the consequence of the firth predicate
     * @param <E>          the type of the expression
     * @return a Cond expression
     */
    public static <E> CondExp<E> par(final VIO<Boolean> predicate1,
                                     final Supplier<VIO<E>> consequence1,
                                     final VIO<Boolean> predicate2,
                                     final Supplier<VIO<E>> consequence2,
                                     final VIO<Boolean> predicate3,
                                     final Supplier<VIO<E>> consequence3,
                                     final VIO<Boolean> predicate4,
                                     final Supplier<VIO<E>> consequence4,
                                     final VIO<Boolean> predicate5,
                                     final Supplier<VIO<E>> consequence5
                                    ) {


        var predicates = new ArrayList<VIO<Boolean>>();
        var consequences = new ArrayList<Supplier<VIO<E>>>();
        predicates.add(requireNonNull(predicate1));
        predicates.add(requireNonNull(predicate2));
        predicates.add(requireNonNull(predicate3));
        predicates.add(requireNonNull(predicate4));
        predicates.add(requireNonNull(predicate5));
        consequences.add(requireNonNull(consequence1));
        consequences.add(requireNonNull(consequence2));
        consequences.add(requireNonNull(consequence3));
        consequences.add(requireNonNull(consequence4));
        consequences.add(requireNonNull(consequence5));

        return new CondExpPar<>(predicates,
                                consequences
        );


    }

    /**
     * It creates a Cond expression which predicates will be executed in parallel. Once all the predicates are
     * evaluated, it computes and returns the consequence of the first one that is true. If no predicate is evaluated to
     * true, it returns the specified default value.
     *
     * @param predicate1   the first predicate
     * @param consequence1 the consequence of the first predicate
     * @param predicate2   the second predicate
     * @param consequence2 the consequence of the second predicate
     * @param predicate3   the third predicate
     * @param consequence3 the consequence of the third predicate
     * @param predicate4   the forth predicate
     * @param consequence4 the consequence of the forth predicate
     * @param predicate5   the firth predicate
     * @param consequence5 the consequence of the firth predicate
     * @param predicate6   the sixth predicate
     * @param consequence6 the consequence of the sixth predicate
     * @param otherwise    the default value returned if no predicate is true
     * @param <E>          the type of the expression
     * @return a Cond expression
     */
    public static <E> CondExp<E> par(final VIO<Boolean> predicate1,
                                     final Supplier<VIO<E>> consequence1,
                                     final VIO<Boolean> predicate2,
                                     final Supplier<VIO<E>> consequence2,
                                     final VIO<Boolean> predicate3,
                                     final Supplier<VIO<E>> consequence3,
                                     final VIO<Boolean> predicate4,
                                     final Supplier<VIO<E>> consequence4,
                                     final VIO<Boolean> predicate5,
                                     final Supplier<VIO<E>> consequence5,
                                     final VIO<Boolean> predicate6,
                                     final Supplier<VIO<E>> consequence6,
                                     final Supplier<VIO<E>> otherwise
                                    ) {

        var predicates = new ArrayList<VIO<Boolean>>();
        var consequences = new ArrayList<Supplier<VIO<E>>>();
        predicates.add(requireNonNull(predicate1));
        predicates.add(requireNonNull(predicate2));
        predicates.add(requireNonNull(predicate3));
        predicates.add(requireNonNull(predicate4));
        predicates.add(requireNonNull(predicate5));
        predicates.add(requireNonNull(predicate6));
        consequences.add(requireNonNull(consequence1));
        consequences.add(requireNonNull(consequence2));
        consequences.add(requireNonNull(consequence3));
        consequences.add(requireNonNull(consequence4));
        consequences.add(requireNonNull(consequence5));
        consequences.add(requireNonNull(consequence6));

        return new CondExpPar<>(predicates,
                                consequences,
                                requireNonNull(otherwise)
        );
    }

    /**
     * It creates a Cond expression which predicates will be executed in parallel. Once all the predicates are
     * evaluated, it computes and returns the consequence of the first one that is true. If no predicate is evaluated to
     * true, it returns {@link VIO#NULL}
     *
     * @param predicate1   the first predicate
     * @param consequence1 the consequence of the first predicate
     * @param predicate2   the second predicate
     * @param consequence2 the consequence of the second predicate
     * @param predicate3   the third predicate
     * @param consequence3 the consequence of the third predicate
     * @param predicate4   the forth predicate
     * @param consequence4 the consequence of the forth predicate
     * @param predicate5   the firth predicate
     * @param consequence5 the consequence of the firth predicate
     * @param predicate6   the sixth predicate
     * @param consequence6 the consequence of the sixth predicate
     * @param <E>          the type of the expression
     * @return a Cond expression
     */
    public static <E> CondExp<E> par(final VIO<Boolean> predicate1,
                                     final Supplier<VIO<E>> consequence1,
                                     final VIO<Boolean> predicate2,
                                     final Supplier<VIO<E>> consequence2,
                                     final VIO<Boolean> predicate3,
                                     final Supplier<VIO<E>> consequence3,
                                     final VIO<Boolean> predicate4,
                                     final Supplier<VIO<E>> consequence4,
                                     final VIO<Boolean> predicate5,
                                     final Supplier<VIO<E>> consequence5,
                                     final VIO<Boolean> predicate6,
                                     final Supplier<VIO<E>> consequence6
                                    ) {

        var predicates = new ArrayList<VIO<Boolean>>();
        var consequences = new ArrayList<Supplier<VIO<E>>>();
        predicates.add(requireNonNull(predicate1));
        predicates.add(requireNonNull(predicate2));
        predicates.add(requireNonNull(predicate3));
        predicates.add(requireNonNull(predicate4));
        predicates.add(requireNonNull(predicate5));
        predicates.add(requireNonNull(predicate6));
        consequences.add(requireNonNull(consequence1));
        consequences.add(requireNonNull(consequence2));
        consequences.add(requireNonNull(consequence3));
        consequences.add(requireNonNull(consequence4));
        consequences.add(requireNonNull(consequence5));
        consequences.add(requireNonNull(consequence6));

        return new CondExpPar<>(predicates,
                                consequences
        );
    }


    /**
     * It creates a Cond expression which predicates will be executed sequentially. Each predicate is evaluated at a
     * time and when a predicate is true, its consequence is computed and returned  (the rest of predicates (if any) are
     * not evaluated).If no predicate is evaluated to true, it returns the specified default value.
     *
     * @param predicate1   the first predicate
     * @param consequence1 the consequence of the first predicate
     * @param predicate2   the second predicate
     * @param consequence2 the consequence of the second predicate
     * @param otherwise    the default value returned if no predicate is true
     * @param <E>          the type of the expression
     * @return a Cond expression
     */
    public static <E> CondExp<E> seq(final VIO<Boolean> predicate1,
                                     final Supplier<VIO<E>> consequence1,
                                     final VIO<Boolean> predicate2,
                                     final Supplier<VIO<E>> consequence2,
                                     final Supplier<VIO<E>> otherwise
                                    ) {

        var predicates = new ArrayList<VIO<Boolean>>();
        var consequences = new ArrayList<Supplier<VIO<E>>>();
        predicates.add(requireNonNull(predicate1));
        predicates.add(requireNonNull(predicate2));
        consequences.add(requireNonNull(consequence1));
        consequences.add(requireNonNull(consequence2));
        return new CondExpSeq<>(predicates,
                                consequences,
                                requireNonNull(otherwise)
        );

    }

    /**
     * It creates a Cond expression which predicates will be executed sequentially. Each predicate is evaluated at a
     * time and when a predicate is true, its consequence is computed and returned  (the rest of predicates (if any) are
     * not evaluated).If no predicate is evaluated to true, it returns {@link VIO#NULL}.
     *
     * @param predicate1   the first predicate
     * @param consequence1 the consequence of the first predicate
     * @param predicate2   the second predicate
     * @param consequence2 the consequence of the second predicate
     * @param <E>          the type of the expression
     * @return a Cond expression
     */
    public static <E> CondExp<E> seq(final VIO<Boolean> predicate1,
                                     final Supplier<VIO<E>> consequence1,
                                     final VIO<Boolean> predicate2,
                                     final Supplier<VIO<E>> consequence2
                                    ) {

        var predicates = new ArrayList<VIO<Boolean>>();
        var consequences = new ArrayList<Supplier<VIO<E>>>();
        predicates.add(requireNonNull(predicate1));
        predicates.add(requireNonNull(predicate2));
        consequences.add(requireNonNull(consequence1));
        consequences.add(requireNonNull(consequence2));
        return new CondExpSeq<>(predicates,
                                consequences
        );

    }


    /**
     * It creates a Cond expression which predicates will be executed sequentially. Each predicate is evaluated at a
     * time and when a predicate is true, its consequence is computed and returned  (the rest of predicates (if any) are
     * not evaluated).If no predicate is evaluated to true, it returns the specified default value.
     *
     * @param predicate1   the first predicate
     * @param consequence1 the consequence of the first predicate
     * @param predicate2   the second predicate
     * @param consequence2 the consequence of the second predicate
     * @param predicate3   the third predicate
     * @param consequence3 the consequence of the third predicate
     * @param otherwise    the default value returned if no predicate is true
     * @param <E>          the type of the expression
     * @return a Cond expression
     */
    public static <E> CondExp<E> seq(final VIO<Boolean> predicate1,
                                     final Supplier<VIO<E>> consequence1,
                                     final VIO<Boolean> predicate2,
                                     final Supplier<VIO<E>> consequence2,
                                     final VIO<Boolean> predicate3,
                                     final Supplier<VIO<E>> consequence3,
                                     final Supplier<VIO<E>> otherwise
                                    ) {

        var predicates = new ArrayList<VIO<Boolean>>();
        var consequences = new ArrayList<Supplier<VIO<E>>>();
        predicates.add(requireNonNull(predicate1));
        predicates.add(requireNonNull(predicate2));
        predicates.add(requireNonNull(predicate3));
        consequences.add(requireNonNull(consequence1));
        consequences.add(requireNonNull(consequence2));
        consequences.add(requireNonNull(consequence3));

        return new CondExpSeq<>(predicates,
                                consequences,
                                requireNonNull(otherwise)
        );

    }

    /**
     * It creates a Cond expression which predicates will be executed sequentially. Each predicate is evaluated at a
     * time and when a predicate is true, its consequence is computed and returned  (the rest of predicates (if any) are
     * not evaluated).If no predicate is evaluated to true, it returns {@link VIO#NULL}.
     *
     * @param predicate1   the first predicate
     * @param consequence1 the consequence of the first predicate
     * @param predicate2   the second predicate
     * @param consequence2 the consequence of the second predicate
     * @param predicate3   the third predicate
     * @param consequence3 the consequence of the third predicate
     * @param <E>          the type of the expression
     * @return a Cond expression
     */
    public static <E> CondExp<E> seq(final VIO<Boolean> predicate1,
                                     final Supplier<VIO<E>> consequence1,
                                     final VIO<Boolean> predicate2,
                                     final Supplier<VIO<E>> consequence2,
                                     final VIO<Boolean> predicate3,
                                     final Supplier<VIO<E>> consequence3
                                    ) {

        var predicates = new ArrayList<VIO<Boolean>>();
        var consequences = new ArrayList<Supplier<VIO<E>>>();
        predicates.add(requireNonNull(predicate1));
        predicates.add(requireNonNull(predicate2));
        predicates.add(requireNonNull(predicate3));
        consequences.add(requireNonNull(consequence2));
        consequences.add(requireNonNull(consequence1));
        consequences.add(requireNonNull(consequence3));

        return new CondExpSeq<>(predicates,
                                consequences
        );

    }

    /**
     * It creates a Cond expression which predicates will be executed sequentially. Each predicate is evaluated at a
     * time and when a predicate is true, its consequence is computed and returned  (the rest of predicates (if any) are
     * not evaluated).If no predicate is evaluated to true, it returns the specified default value.
     *
     * @param predicate1   the first predicate
     * @param consequence1 the consequence of the first predicate
     * @param predicate2   the second predicate
     * @param consequence2 the consequence of the second predicate
     * @param predicate3   the third predicate
     * @param consequence3 the consequence of the third predicate
     * @param predicate4   the forth predicate
     * @param consequence4 the consequence of the forth predicate
     * @param otherwise    the default value returned if no predicate is true
     * @param <E>          the type of the expression
     * @return a Cond expression
     */
    public static <E> CondExp<E> seq(final VIO<Boolean> predicate1,
                                     final Supplier<VIO<E>> consequence1,
                                     final VIO<Boolean> predicate2,
                                     final Supplier<VIO<E>> consequence2,
                                     final VIO<Boolean> predicate3,
                                     final Supplier<VIO<E>> consequence3,
                                     final VIO<Boolean> predicate4,
                                     final Supplier<VIO<E>> consequence4,
                                     final Supplier<VIO<E>> otherwise
                                    ) {

        var predicates = new ArrayList<VIO<Boolean>>();
        var consequences = new ArrayList<Supplier<VIO<E>>>();
        predicates.add(requireNonNull(predicate1));
        predicates.add(requireNonNull(predicate2));
        predicates.add(requireNonNull(predicate3));
        predicates.add(requireNonNull(predicate4));
        consequences.add(requireNonNull(consequence1));
        consequences.add(requireNonNull(consequence2));
        consequences.add(requireNonNull(consequence3));
        consequences.add(requireNonNull(consequence4));

        return new CondExpSeq<>(predicates,
                                consequences,
                                requireNonNull(otherwise)
        );

    }

    /**
     * It creates a Cond expression which predicates will be executed sequentially. Each predicate is evaluated at a
     * time and when a predicate is true, its consequence is computed and returned  (the rest of predicates (if any) are
     * not evaluated).If no predicate is evaluated to true, it returns {@link VIO#NULL}.
     *
     * @param predicate1   the first predicate
     * @param consequence1 the consequence of the first predicate
     * @param predicate2   the second predicate
     * @param consequence2 the consequence of the second predicate
     * @param predicate3   the third predicate
     * @param consequence3 the consequence of the third predicate
     * @param predicate4   the forth predicate
     * @param consequence4 the consequence of the forth predicate
     * @param <E>          the type of the expression
     * @return a Cond expression
     */
    public static <E> CondExp<E> seq(final VIO<Boolean> predicate1,
                                     final Supplier<VIO<E>> consequence1,
                                     final VIO<Boolean> predicate2,
                                     final Supplier<VIO<E>> consequence2,
                                     final VIO<Boolean> predicate3,
                                     final Supplier<VIO<E>> consequence3,
                                     final VIO<Boolean> predicate4,
                                     final Supplier<VIO<E>> consequence4
                                    ) {

        var predicates = new ArrayList<VIO<Boolean>>();
        var consequences = new ArrayList<Supplier<VIO<E>>>();
        predicates.add(requireNonNull(predicate1));
        predicates.add(requireNonNull(predicate2));
        predicates.add(requireNonNull(predicate3));
        predicates.add(requireNonNull(predicate4));
        consequences.add(requireNonNull(consequence1));
        consequences.add(requireNonNull(consequence2));
        consequences.add(requireNonNull(consequence3));
        consequences.add(requireNonNull(consequence4));

        return new CondExpSeq<>(predicates,
                                consequences
        );

    }

    /**
     * It creates a Cond expression which predicates will be executed sequentially. Each predicate is evaluated at a
     * time and when a predicate is true, its consequence is computed and returned  (the rest of predicates (if any) are
     * not evaluated).If no predicate is evaluated to true, it returns the specified default value.
     *
     * @param predicate1   the first predicate
     * @param consequence1 the consequence of the first predicate
     * @param predicate2   the second predicate
     * @param consequence2 the consequence of the second predicate
     * @param predicate3   the third predicate
     * @param consequence3 the consequence of the third predicate
     * @param predicate4   the forth predicate
     * @param consequence4 the consequence of the forth predicate
     * @param predicate5   the firth predicate
     * @param consequence5 the consequence of the firth predicate
     * @param otherwise    the default value returned if no predicate is true
     * @param <E>          the type of the expression
     * @return a Cond expression
     */
    public static <E> CondExp<E> seq(final VIO<Boolean> predicate1,
                                     final Supplier<VIO<E>> consequence1,
                                     final VIO<Boolean> predicate2,
                                     final Supplier<VIO<E>> consequence2,
                                     final VIO<Boolean> predicate3,
                                     final Supplier<VIO<E>> consequence3,
                                     final VIO<Boolean> predicate4,
                                     final Supplier<VIO<E>> consequence4,
                                     final VIO<Boolean> predicate5,
                                     final Supplier<VIO<E>> consequence5,
                                     final Supplier<VIO<E>> otherwise
                                    ) {


        var predicates = new ArrayList<VIO<Boolean>>();
        var consequences = new ArrayList<Supplier<VIO<E>>>();
        predicates.add(requireNonNull(predicate1));
        predicates.add(requireNonNull(predicate2));
        predicates.add(requireNonNull(predicate3));
        predicates.add(requireNonNull(predicate4));
        predicates.add(requireNonNull(predicate5));
        consequences.add(requireNonNull(consequence1));
        consequences.add(requireNonNull(consequence2));
        consequences.add(requireNonNull(consequence3));
        consequences.add(requireNonNull(consequence4));
        consequences.add(requireNonNull(consequence5));

        return new CondExpSeq<>(predicates,
                                consequences,
                                requireNonNull(otherwise)
        );


    }

    /**
     * It creates a Cond expression which predicates will be executed sequentially. Each predicate is evaluated at a
     * time and when a predicate is true, its consequence is computed and returned  (the rest of predicates (if any) are
     * not evaluated).If no predicate is evaluated to true, it returns {@link VIO#NULL}.
     *
     * @param predicate1   the first predicate
     * @param consequence1 the consequence of the first predicate
     * @param predicate2   the second predicate
     * @param consequence2 the consequence of the second predicate
     * @param predicate3   the third predicate
     * @param consequence3 the consequence of the third predicate
     * @param predicate4   the forth predicate
     * @param consequence4 the consequence of the forth predicate
     * @param predicate5   the firth predicate
     * @param consequence5 the consequence of the firth predicate
     * @param <E>          the type of the expression
     * @return a Cond expression
     */
    public static <E> CondExp<E> seq(final VIO<Boolean> predicate1,
                                     final Supplier<VIO<E>> consequence1,
                                     final VIO<Boolean> predicate2,
                                     final Supplier<VIO<E>> consequence2,
                                     final VIO<Boolean> predicate3,
                                     final Supplier<VIO<E>> consequence3,
                                     final VIO<Boolean> predicate4,
                                     final Supplier<VIO<E>> consequence4,
                                     final VIO<Boolean> predicate5,
                                     final Supplier<VIO<E>> consequence5
                                    ) {


        var predicates = new ArrayList<VIO<Boolean>>();
        var consequences = new ArrayList<Supplier<VIO<E>>>();
        predicates.add(requireNonNull(predicate1));
        predicates.add(requireNonNull(predicate2));
        predicates.add(requireNonNull(predicate3));
        predicates.add(requireNonNull(predicate4));
        predicates.add(requireNonNull(predicate5));
        consequences.add(requireNonNull(consequence1));
        consequences.add(requireNonNull(consequence2));
        consequences.add(requireNonNull(consequence3));
        consequences.add(requireNonNull(consequence4));
        consequences.add(requireNonNull(consequence5));

        return new CondExpSeq<>(predicates,
                                consequences
        );


    }

    /**
     * It creates a Cond expression which predicates will be executed sequentially. Each predicate is evaluated at a
     * time and when a predicate is true, its consequence is computed and returned  (the rest of predicates (if any) are
     * not evaluated).If no predicate is evaluated to true, it returns the specified default value.
     *
     * @param predicate1   the first predicate
     * @param consequence1 the consequence of the first predicate
     * @param predicate2   the second predicate
     * @param consequence2 the consequence of the second predicate
     * @param predicate3   the third predicate
     * @param consequence3 the consequence of the third predicate
     * @param predicate4   the forth predicate
     * @param consequence4 the consequence of the forth predicate
     * @param predicate5   the firth predicate
     * @param consequence5 the consequence of the firth predicate
     * @param predicate6   the sixth predicate
     * @param consequence6 the consequence of the sixth predicate
     * @param otherwise    the default value returned if no predicate is true
     * @param <E>          the type of the expression
     * @return a Cond expression
     */
    public static <E> CondExp<E> seq(final VIO<Boolean> predicate1,
                                     final Supplier<VIO<E>> consequence1,
                                     final VIO<Boolean> predicate2,
                                     final Supplier<VIO<E>> consequence2,
                                     final VIO<Boolean> predicate3,
                                     final Supplier<VIO<E>> consequence3,
                                     final VIO<Boolean> predicate4,
                                     final Supplier<VIO<E>> consequence4,
                                     final VIO<Boolean> predicate5,
                                     final Supplier<VIO<E>> consequence5,
                                     final VIO<Boolean> predicate6,
                                     final Supplier<VIO<E>> consequence6,
                                     final Supplier<VIO<E>> otherwise
                                    ) {

        var predicates = new ArrayList<VIO<Boolean>>();
        var consequences = new ArrayList<Supplier<VIO<E>>>();
        predicates.add(requireNonNull(predicate1));
        predicates.add(requireNonNull(predicate2));
        predicates.add(requireNonNull(predicate3));
        predicates.add(requireNonNull(predicate4));
        predicates.add(requireNonNull(predicate5));
        predicates.add(requireNonNull(predicate6));
        consequences.add(requireNonNull(consequence1));
        consequences.add(requireNonNull(consequence2));
        consequences.add(requireNonNull(consequence3));
        consequences.add(requireNonNull(consequence4));
        consequences.add(requireNonNull(consequence5));
        consequences.add(requireNonNull(consequence6));

        return new CondExpSeq<>(predicates,
                                consequences,
                                requireNonNull(otherwise)
        );
    }

    /**
     * It creates a Cond expression which predicates will be executed sequentially. Each predicate is evaluated at a
     * time and when a predicate is true, its consequence is computed and returned  (the rest of predicates (if any) are
     * not evaluated).If no predicate is evaluated to true, it returns {@link VIO#NULL}.
     *
     * @param predicate1   the first predicate
     * @param consequence1 the consequence of the first predicate
     * @param predicate2   the second predicate
     * @param consequence2 the consequence of the second predicate
     * @param predicate3   the third predicate
     * @param consequence3 the consequence of the third predicate
     * @param predicate4   the forth predicate
     * @param consequence4 the consequence of the forth predicate
     * @param predicate5   the firth predicate
     * @param consequence5 the consequence of the firth predicate
     * @param predicate6   the sixth predicate
     * @param consequence6 the consequence of the sixth predicate
     * @param <E>          the type of the expression
     * @return a Cond expression
     */
    public static <E> CondExp<E> seq(final VIO<Boolean> predicate1,
                                     final Supplier<VIO<E>> consequence1,
                                     final VIO<Boolean> predicate2,
                                     final Supplier<VIO<E>> consequence2,
                                     final VIO<Boolean> predicate3,
                                     final Supplier<VIO<E>> consequence3,
                                     final VIO<Boolean> predicate4,
                                     final Supplier<VIO<E>> consequence4,
                                     final VIO<Boolean> predicate5,
                                     final Supplier<VIO<E>> consequence5,
                                     final VIO<Boolean> predicate6,
                                     final Supplier<VIO<E>> consequence6
                                    ) {

        var predicates = new ArrayList<VIO<Boolean>>();
        var consequences = new ArrayList<Supplier<VIO<E>>>();
        predicates.add(requireNonNull(predicate1));
        predicates.add(requireNonNull(predicate2));
        predicates.add(requireNonNull(predicate3));
        predicates.add(requireNonNull(predicate4));
        predicates.add(requireNonNull(predicate5));
        predicates.add(requireNonNull(predicate6));
        consequences.add(requireNonNull(consequence1));
        consequences.add(requireNonNull(consequence2));
        consequences.add(requireNonNull(consequence3));
        consequences.add(requireNonNull(consequence4));
        consequences.add(requireNonNull(consequence5));
        consequences.add(requireNonNull(consequence6));

        return new CondExpSeq<>(predicates,
                                consequences
        );
    }
}
