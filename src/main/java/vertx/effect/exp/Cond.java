package vertx.effect.exp;


import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import vertx.effect.RetryPolicy;
import vertx.effect.core.AbstractVal;
import vertx.effect.Val;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;

public final class Cond<O> extends AbstractVal<O> {

    private final List<Val<Boolean>> tests;
    private final List<Val<O>> consequences;
    private static final String ATTEMPTS_LOWER_THAN_ONE_ERROR = "attempts < 1";
    private Val<O> otherwise;


    public static <O> Cond<O> of(final Val<Boolean> test1,
                                 final Val<O> consequent1,
                                 final Val<Boolean> test2,
                                 final Val<O> consequent2,
                                 final Val<O> otherwise) {

        List<Val<Boolean>> tests        = new ArrayList<>();
        List<Val<O>>       consequences = new ArrayList<>();
        tests.add(requireNonNull(test1));
        tests.add(requireNonNull(test2));
        consequences.add(requireNonNull(consequent1));
        consequences.add(requireNonNull(consequent2));
        return new Cond<>(tests,
                          consequences,
                          otherwise
        );

    }

    public static <O> Cond<O> of(final Val<Boolean> test1,
                                 final Val<O> consequent1,
                                 final Val<Boolean> test2,
                                 final Val<O> consequent2) {

        List<Val<Boolean>> tests        = new ArrayList<>();
        List<Val<O>>       consequences = new ArrayList<>();
        tests.add(requireNonNull(test1));
        tests.add(requireNonNull(test2));
        consequences.add(requireNonNull(consequent1));
        consequences.add(requireNonNull(consequent2));
        return new Cond<>(tests,
                          consequences
        );

    }


    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static <O> Cond<O> of(final Val<Boolean> test1,
                                 final Val<O> consequent1,
                                 final Val<Boolean> test2,
                                 final Val<O> consequent2,
                                 final Val<Boolean> test3,
                                 final Val<O> consequent3,
                                 final Val<O> otherwise) {

        List<Val<Boolean>> tests       = new ArrayList<>();
        List<Val<O>>       consequents = new ArrayList<>();
        tests.add(requireNonNull(test1));
        tests.add(requireNonNull(test2));
        tests.add(requireNonNull(test3));
        consequents.add(requireNonNull(consequent1));
        consequents.add(requireNonNull(consequent2));
        consequents.add(requireNonNull(consequent3));

        return new Cond<>(tests,
                          consequents,
                          otherwise
        );

    }

    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static <O> Cond<O> of(final Val<Boolean> test1,
                                 final Val<O> consequent1,
                                 final Val<Boolean> test2,
                                 final Val<O> consequent2,
                                 final Val<Boolean> test3,
                                 final Val<O> consequent3) {

        List<Val<Boolean>> tests       = new ArrayList<>();
        List<Val<O>>       consequents = new ArrayList<>();
        tests.add(requireNonNull(test1));
        tests.add(requireNonNull(test2));
        tests.add(requireNonNull(test3));
        consequents.add(requireNonNull(consequent1));
        consequents.add(requireNonNull(consequent2));
        consequents.add(requireNonNull(consequent3));

        return new Cond<>(tests,
                          consequents
        );

    }

    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static <O> Cond<O> of(final Val<Boolean> test1,
                                 final Val<O> consequent1,
                                 final Val<Boolean> test2,
                                 final Val<O> consequent2,
                                 final Val<Boolean> test3,
                                 final Val<O> consequent3,
                                 final Val<Boolean> test4,
                                 final Val<O> consequent4,
                                 final Val<O> otherwise) {

        List<Val<Boolean>> tests       = new ArrayList<>();
        List<Val<O>>       consequents = new ArrayList<>();
        tests.add(requireNonNull(test1));
        tests.add(requireNonNull(test2));
        tests.add(requireNonNull(test3));
        tests.add(requireNonNull(test4));
        consequents.add(requireNonNull(consequent1));
        consequents.add(requireNonNull(consequent2));
        consequents.add(requireNonNull(consequent3));
        consequents.add(requireNonNull(consequent4));

        return new Cond<>(tests,
                          consequents,
                          otherwise
        );

    }

    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static <O> Cond<O> of(final Val<Boolean> test1,
                                 final Val<O> consequent1,
                                 final Val<Boolean> test2,
                                 final Val<O> consequent2,
                                 final Val<Boolean> test3,
                                 final Val<O> consequent3,
                                 final Val<Boolean> test4,
                                 final Val<O> consequent4) {

        List<Val<Boolean>> tests = new ArrayList<>();
        List<Val<O>> consequents = new ArrayList<>();
        tests.add(requireNonNull(test1));
        tests.add(requireNonNull(test2));
        tests.add(requireNonNull(test3));
        tests.add(requireNonNull(test4));
        consequents.add(requireNonNull(consequent1));
        consequents.add(requireNonNull(consequent2));
        consequents.add(requireNonNull(consequent3));
        consequents.add(requireNonNull(consequent4));

        return new Cond<>(tests,
                          consequents
        );

    }

    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static <O> Cond<O> of(final Val<Boolean> test1,
                                 final Val<O> consequent1,
                                 final Val<Boolean> test2,
                                 final Val<O> consequent2,
                                 final Val<Boolean> test3,
                                 final Val<O> consequent3,
                                 final Val<Boolean> test4,
                                 final Val<O> consequent4,
                                 final Val<Boolean> test5,
                                 final Val<O> consequent5,
                                 final Val<O> otherwise) {


        List<Val<Boolean>> tests       = new ArrayList<>();
        List<Val<O>>       consequents = new ArrayList<>();
        tests.add(requireNonNull(test1));
        tests.add(requireNonNull(test2));
        tests.add(requireNonNull(test3));
        tests.add(requireNonNull(test4));
        tests.add(requireNonNull(test5));
        consequents.add(requireNonNull(consequent1));
        consequents.add(requireNonNull(consequent2));
        consequents.add(requireNonNull(consequent3));
        consequents.add(requireNonNull(consequent4));
        consequents.add(requireNonNull(consequent5));

        return new Cond<>(tests,
                          consequents,
                          otherwise
        );


    }

    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static <O> Cond<O> of(final Val<Boolean> test1,
                                 final Val<O> consequent1,
                                 final Val<Boolean> test2,
                                 final Val<O> consequent2,
                                 final Val<Boolean> test3,
                                 final Val<O> consequent3,
                                 final Val<Boolean> test4,
                                 final Val<O> consequent4,
                                 final Val<Boolean> test5,
                                 final Val<O> consequent5) {


        List<Val<Boolean>> tests       = new ArrayList<>();
        List<Val<O>>       consequents = new ArrayList<>();
        tests.add(requireNonNull(test1));
        tests.add(requireNonNull(test2));
        tests.add(requireNonNull(test3));
        tests.add(requireNonNull(test4));
        tests.add(requireNonNull(test5));
        consequents.add(requireNonNull(consequent1));
        consequents.add(requireNonNull(consequent2));
        consequents.add(requireNonNull(consequent3));
        consequents.add(requireNonNull(consequent4));
        consequents.add(requireNonNull(consequent5));

        return new Cond<>(tests,
                          consequents
        );


    }

    public Cond(final List<Val<Boolean>> tests,
                final List<Val<O>> consequences,
                final Val<O> otherwise) {
        this(tests,
             consequences
            );
        this.otherwise = otherwise;
    }


    public Cond(final List<Val<Boolean>> tests,
                final List<Val<O>> consequences) {
        this.tests = requireNonNull(tests);
        this.consequences = requireNonNull(consequences);
    }

    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static <O> Cond<O> of(final Val<Boolean> test1,
                                 final Val<O> consequent1,
                                 final Val<Boolean> test2,
                                 final Val<O> consequent2,
                                 final Val<Boolean> test3,
                                 final Val<O> consequent3,
                                 final Val<Boolean> test4,
                                 final Val<O> consequent4,
                                 final Val<Boolean> test5,
                                 final Val<O> consequent5,
                                 final Val<Boolean> test6,
                                 final Val<O> consequent6,
                                 final Val<O> otherwise) {

        List<Val<Boolean>> tests       = new ArrayList<>();
        List<Val<O>>       consequents = new ArrayList<>();
        tests.add(requireNonNull(test1));
        tests.add(requireNonNull(test2));
        tests.add(requireNonNull(test3));
        tests.add(requireNonNull(test4));
        tests.add(requireNonNull(test5));
        tests.add(requireNonNull(test6));
        consequents.add(requireNonNull(consequent1));
        consequents.add(requireNonNull(consequent2));
        consequents.add(requireNonNull(consequent3));
        consequents.add(requireNonNull(consequent4));
        consequents.add(requireNonNull(consequent5));
        consequents.add(requireNonNull(consequent6));

        return new Cond<>(tests,
                          consequents,
                          otherwise
        );
    }

    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static <O> Cond<O> of(final Val<Boolean> test1,
                                 final Val<O> consequent1,
                                 final Val<Boolean> test2,
                                 final Val<O> consequent2,
                                 final Val<Boolean> test3,
                                 final Val<O> consequent3,
                                 final Val<Boolean> test4,
                                 final Val<O> consequent4,
                                 final Val<Boolean> test5,
                                 final Val<O> consequent5,
                                 final Val<Boolean> test6,
                                 final Val<O> consequent6) {

        List<Val<Boolean>> tests       = new ArrayList<>();
        List<Val<O>>       consequents = new ArrayList<>();
        tests.add(requireNonNull(test1));
        tests.add(requireNonNull(test2));
        tests.add(requireNonNull(test3));
        tests.add(requireNonNull(test4));
        tests.add(requireNonNull(test5));
        tests.add(requireNonNull(test6));
        consequents.add(requireNonNull(consequent1));
        consequents.add(requireNonNull(consequent2));
        consequents.add(requireNonNull(consequent3));
        consequents.add(requireNonNull(consequent4));
        consequents.add(requireNonNull(consequent5));
        consequents.add(requireNonNull(consequent6));

        return new Cond<>(tests,
                          consequents
        );
    }



    @Override
    public Val<O> retry(final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));

        return new Cond<>(tests.stream()
                               .map(it -> it.retry(attempts))
                               .collect(Collectors.toList()),
                          consequences.stream()
                                      .map(it -> it.retry(attempts))
                                      .collect(Collectors.toList()
                                              ),
                          otherwise
        );
    }


    @Override
    public Val<O> retry(final int attempts,
                        final BiFunction<Throwable, Integer, Val<Void>> retryPolicy) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));

        if (retryPolicy == null)
            return Cons.failure(new NullPointerException("retryPolicy is null"));


        return new Cond<>(tests.stream()
                               .map(it -> it.retry(attempts,
                                                   retryPolicy
                                                  ))
                               .collect(Collectors.toList()),
                          consequences.stream()
                                      .map(it -> it.retry(attempts,
                                                          retryPolicy
                                                         ))
                                      .collect(Collectors.toList()),
                          otherwise
        );
    }

    @Override
    public Val<O> retry(final Predicate<Throwable> predicate,
                        final int attempts) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));

        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));

        return new Cond<>(tests.stream()
                               .map(it -> it.retry(predicate,
                                                   attempts
                                                  ))
                               .collect(Collectors.toList()),
                          consequences.stream()
                                      .map(it -> it.retry(predicate,
                                                          attempts
                                                         ))
                                      .collect(Collectors.toList()),
                          otherwise
        );
    }


    @Override
    public Val<O> retry(final Predicate<Throwable> predicate,
                        final int attempts,
                        final RetryPolicy<Throwable> retryPolicy) {
        if (attempts < 1)
            return Cons.failure(new IllegalArgumentException(ATTEMPTS_LOWER_THAN_ONE_ERROR));

        if (retryPolicy == null)
            return Cons.failure(new NullPointerException("retryPolicy is null"));

        if (predicate == null)
            return Cons.failure(new NullPointerException("predicate is null"));

        return new Cond<>(tests.stream()
                               .map(it -> it.retry(predicate,
                                                   attempts,
                                                   retryPolicy
                                                  ))
                               .collect(Collectors.toList()),
                          consequences.stream()
                                      .map(it -> it.retry(predicate,
                                                          attempts,
                                                          retryPolicy
                                                         ))
                                      .collect(Collectors.toList()),
                          otherwise
        );
    }

    @Override
    public Future<O> get() {

        return CompositeFuture.all(tests.stream()
                                        .map(Supplier::get)
                                        .collect(Collectors.toList()))
                              .flatMap(it -> {
                                  List<Object> testsResult = it.list();
                                  OptionalInt first = IntStream.range(0,
                                                                      testsResult.size()
                                                                     )
                                                               .filter(i -> {
                                                                   Object o = testsResult.get(i);
                                                                   return (Boolean) o;
                                                               })
                                                               .findFirst();
                                  if (first.isPresent()) return consequences.get(first.getAsInt())
                                                                            .get();
                                  return otherwise.get();
                              });


    }


}
