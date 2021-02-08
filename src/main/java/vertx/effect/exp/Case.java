package vertx.effect.exp;

import io.vertx.core.Future;
import vertx.effect.RetryPolicy;
import vertx.effect.Val;

import java.util.List;
import java.util.function.Predicate;


public final class Case<I, O> extends Exp<O> {

    private final Val<I> keyVal;
    private Cond<O> cond;

    public Case<I, O> of(final I key1,
                         final Val<O> consequent1,
                         final I key2,
                         final Val<O> consequent2) {


        Val<Boolean> predicate1 = keyVal.map(key1::equals);
        Val<Boolean> predicate2 = keyVal.map(key2::equals);

        this.cond = Cond.of(predicate1,
                            consequent1,
                            predicate2,
                            consequent2
                           );

        return this;
    }

    public Case<I, O> of(final I key1,
                         final Val<O> consequent1,
                         final I key2,
                         final Val<O> consequent2,
                         final Val<O> otherwise) {


        Val<Boolean> predicate1 = keyVal.map(key1::equals);
        Val<Boolean> predicate2 = keyVal.map(key2::equals);

        this.cond = Cond.of(predicate1,
                            consequent1,
                            predicate2,
                            consequent2,
                            otherwise
                           );

        return this;
    }

    public Case<I, O> of(final List<I> keyList1,
                         final Val<O> consequent1,
                         final List<I> keyList2,
                         final Val<O> consequent2
                        ) {

        Val<Boolean> predicate1 = keyVal.map(keyList1::contains);
        Val<Boolean> predicate2 = keyVal.map(keyList2::contains);

        cond = Cond.of(predicate1,
                       consequent1,
                       predicate2,
                       consequent2
                      );

        return this;
    }

    public Case<I, O> of(final List<I> keyList1,
                         final Val<O> consequent1,
                         final List<I> keyList2,
                         final Val<O> consequent2,
                         final Val<O> otherwise
                        ) {

        Val<Boolean> predicate1 = keyVal.map(keyList1::contains);
        Val<Boolean> predicate2 = keyVal.map(keyList2::contains);

        cond = Cond.of(predicate1,
                       consequent1,
                       predicate2,
                       consequent2,
                       otherwise
                      );

        return this;
    }

    public Case<I, O> of(final List<I> keyList1,
                         final Val<O> consequent1,
                         final List<I> keyList2,
                         final Val<O> consequent2,
                         final List<I> keyList3,
                         final Val<O> consequent3) {

        Val<Boolean> predicate1 = keyVal.map(keyList1::contains);
        Val<Boolean> predicate2 = keyVal.map(keyList2::contains);
        Val<Boolean> predicate3 = keyVal.map(keyList3::contains);

        cond = Cond.of(predicate1,
                       consequent1,
                       predicate2,
                       consequent2,
                       predicate3,
                       consequent3
                      );

        return this;
    }


    public Case<I, O> of(final List<I> keyList1,
                         final Val<O> consequent1,
                         final List<I> keyList2,
                         final Val<O> consequent2,
                         final List<I> keyList3,
                         final Val<O> consequent3,
                         final Val<O> otherwise) {

        Val<Boolean> predicate1 = keyVal.map(keyList1::contains);
        Val<Boolean> predicate2 = keyVal.map(keyList2::contains);
        Val<Boolean> predicate3 = keyVal.map(keyList3::contains);

        this.cond = Cond.of(predicate1,
                            consequent1,
                            predicate2,
                            consequent2,
                            predicate3,
                            consequent3,
                            otherwise
                           );

        return this;
    }

    public Case<I, O> of(final I key1,
                         final Val<O> consequent1,
                         final I key2,
                         final Val<O> consequent2,
                         final I key3,
                         final Val<O> consequent3) {

        Val<Boolean> predicate1 = keyVal.map(key1::equals);
        Val<Boolean> predicate2 = keyVal.map(key2::equals);
        Val<Boolean> predicate3 = keyVal.map(key3::equals);

        cond = Cond.of(predicate1,
                       consequent1,
                       predicate2,
                       consequent2,
                       predicate3,
                       consequent3
                      );

        return this;

    }

    public Case<I, O> of(final I key1,
                         final Val<O> consequent1,
                         final I key2,
                         final Val<O> consequent2,
                         final I key3,
                         final Val<O> consequent3,
                         final Val<O> otherwise) {

        Val<Boolean> predicate1 = keyVal.map(key1::equals);
        Val<Boolean> predicate2 = keyVal.map(key2::equals);
        Val<Boolean> predicate3 = keyVal.map(key3::equals);

        this.cond = Cond.of(predicate1,
                            consequent1,
                            predicate2,
                            consequent2,
                            predicate3,
                            consequent3,
                            otherwise
                           );

        return this;

    }

    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public Case<I, O> of(final I key1,
                         final Val<O> consequent1,
                         final I key2,
                         final Val<O> consequent2,
                         final I key3,
                         final Val<O> consequent3,
                         final I key4,
                         final Val<O> consequent4) {

        Val<Boolean> predicate1 = keyVal.map(key1::equals);
        Val<Boolean> predicate2 = keyVal.map(key2::equals);
        Val<Boolean> predicate3 = keyVal.map(key3::equals);
        Val<Boolean> predicate4 = keyVal.map(key4::equals);

        cond = Cond.of(predicate1,
                       consequent1,
                       predicate2,
                       consequent2,
                       predicate3,
                       consequent3,
                       predicate4,
                       consequent4
                      );

        return this;

    }

    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public Case<I, O> of(final I key1,
                         final Val<O> consequent1,
                         final I key2,
                         final Val<O> consequent2,
                         final I key3,
                         final Val<O> consequent3,
                         final I key4,
                         final Val<O> consequent4,
                         final Val<O> otherwise) {

        Val<Boolean> predicate1 = keyVal.map(key1::equals);
        Val<Boolean> predicate2 = keyVal.map(key2::equals);
        Val<Boolean> predicate3 = keyVal.map(key3::equals);
        Val<Boolean> predicate4 = keyVal.map(key4::equals);

        cond = Cond.of(predicate1,
                       consequent1,
                       predicate2,
                       consequent2,
                       predicate3,
                       consequent3,
                       predicate4,
                       consequent4,
                       otherwise
                      );

        return this;

    }

    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public Case<I, O> of(final List<I> keyList1,
                         final Val<O> consequent1,
                         final List<I> keyList2,
                         final Val<O> consequent2,
                         final List<I> keyList3,
                         final Val<O> consequent3,
                         final List<I> keyList4,
                         final Val<O> consequent4) {

        Val<Boolean> predicate1 = keyVal.map(keyList1::contains);
        Val<Boolean> predicate2 = keyVal.map(keyList2::contains);
        Val<Boolean> predicate3 = keyVal.map(keyList3::contains);
        Val<Boolean> predicate4 = keyVal.map(keyList4::contains);

        cond = Cond.of(predicate1,
                       consequent1,
                       predicate2,
                       consequent2,
                       predicate3,
                       consequent3,
                       predicate4,
                       consequent4
                      );

        return this;
    }

    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public Case<I, O> of(final List<I> keyList1,
                         final Val<O> consequent1,
                         final List<I> keyList2,
                         final Val<O> consequent2,
                         final List<I> keyList3,
                         final Val<O> consequent3,
                         final List<I> keyList4,
                         final Val<O> consequent4,
                         final Val<O> otherwise) {

        Val<Boolean> predicate1 = keyVal.map(keyList1::contains);
        Val<Boolean> predicate2 = keyVal.map(keyList2::contains);
        Val<Boolean> predicate3 = keyVal.map(keyList3::contains);
        Val<Boolean> predicate4 = keyVal.map(keyList4::contains);

        this.cond = Cond.of(predicate1,
                            consequent1,
                            predicate2,
                            consequent2,
                            predicate3,
                            consequent3,
                            predicate4,
                            consequent4,
                            otherwise
                           );

        return this;
    }

    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public Case<I, O> of(final List<I> keyList1,
                         final Val<O> consequent1,
                         final List<I> keyList2,
                         final Val<O> consequent2,
                         final List<I> keyList3,
                         final Val<O> consequent3,
                         final List<I> keyList4,
                         final Val<O> consequent4,
                         final List<I> keyList5,
                         final Val<O> consequent5) {

        Val<Boolean> predicate1 = keyVal.map(keyList1::contains);
        Val<Boolean> predicate2 = keyVal.map(keyList2::contains);
        Val<Boolean> predicate3 = keyVal.map(keyList3::contains);
        Val<Boolean> predicate4 = keyVal.map(keyList4::contains);
        Val<Boolean> predicate5 = keyVal.map(keyList5::contains);

        cond = Cond.of(predicate1,
                       consequent1,
                       predicate2,
                       consequent2,
                       predicate3,
                       consequent3,
                       predicate4,
                       consequent4,
                       predicate5,
                       consequent5
                      );

        return this;
    }

    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public Case<I, O> of(final List<I> keyList1,
                         final Val<O> consequent1,
                         final List<I> keyList2,
                         final Val<O> consequent2,
                         final List<I> keyList3,
                         final Val<O> consequent3,
                         final List<I> keyList4,
                         final Val<O> consequent4,
                         final List<I> keyList5,
                         final Val<O> consequent5,
                         final Val<O> otherwise

                        ) {

        Val<Boolean> predicate1 = keyVal.map(keyList1::contains);
        Val<Boolean> predicate2 = keyVal.map(keyList2::contains);
        Val<Boolean> predicate3 = keyVal.map(keyList3::contains);
        Val<Boolean> predicate4 = keyVal.map(keyList4::contains);
        Val<Boolean> predicate5 = keyVal.map(keyList5::contains);

        cond = Cond.of(predicate1,
                       consequent1,
                       predicate2,
                       consequent2,
                       predicate3,
                       consequent3,
                       predicate4,
                       consequent4,
                       predicate5,
                       consequent5,
                       otherwise
                      );

        return this;
    }

    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public Case<I, O> of(final I key1,
                         final Val<O> consequent1,
                         final I key2,
                         final Val<O> consequent2,
                         final I key3,
                         final Val<O> consequent3,
                         final I key4,
                         final Val<O> consequent4,
                         final I key5,
                         final Val<O> consequent5) {


        Val<Boolean> predicate1 = keyVal.map(key1::equals);
        Val<Boolean> predicate2 = keyVal.map(key2::equals);
        Val<Boolean> predicate3 = keyVal.map(key3::equals);
        Val<Boolean> predicate4 = keyVal.map(key4::equals);
        Val<Boolean> predicate5 = keyVal.map(key5::equals);

        cond = Cond.of(predicate1,
                       consequent1,
                       predicate2,
                       consequent2,
                       predicate3,
                       consequent3,
                       predicate4,
                       consequent4,
                       predicate5,
                       consequent5
                      );

        return this;
    }


    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public Case<I, O> of(final I key1,
                         final Val<O> consequent1,
                         final I key2,
                         final Val<O> consequent2,
                         final I key3,
                         final Val<O> consequent3,
                         final I key4,
                         final Val<O> consequent4,
                         final I key5,
                         final Val<O> consequent5,
                         final Val<O> otherwise) {


        Val<Boolean> predicate1 = keyVal.map(key1::equals);
        Val<Boolean> predicate2 = keyVal.map(key2::equals);
        Val<Boolean> predicate3 = keyVal.map(key3::equals);
        Val<Boolean> predicate4 = keyVal.map(key4::equals);
        Val<Boolean> predicate5 = keyVal.map(key5::equals);

        this.cond = Cond.of(predicate1,
                            consequent1,
                            predicate2,
                            consequent2,
                            predicate3,
                            consequent3,
                            predicate4,
                            consequent4,
                            predicate5,
                            consequent5,
                            otherwise
                           );

        return this;
    }

    public Case(final Val<I> keyVal) {
        this.keyVal = keyVal;
    }


    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public Case<I, O> of(final I key1,
                         final Val<O> consequent1,
                         final I key2,
                         final Val<O> consequent2,
                         final I key3,
                         final Val<O> consequent3,
                         final I key4,
                         final Val<O> consequent4,
                         final I key5,
                         final Val<O> consequent5,
                         final I key6,
                         final Val<O> consequent6) {

        Val<Boolean> predicate1 = keyVal.map(key1::equals);
        Val<Boolean> predicate2 = keyVal.map(key2::equals);
        Val<Boolean> predicate3 = keyVal.map(key3::equals);
        Val<Boolean> predicate4 = keyVal.map(key4::equals);
        Val<Boolean> predicate5 = keyVal.map(key5::equals);
        Val<Boolean> predicate6 = keyVal.map(key6::equals);

        cond = Cond.of(predicate1,
                       consequent1,
                       predicate2,
                       consequent2,
                       predicate3,
                       consequent3,
                       predicate4,
                       consequent4,
                       predicate5,
                       consequent5,
                       predicate6,
                       consequent6
                      );

        return this;
    }

    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public Case<I, O> of(final I key1,
                         final Val<O> consequent1,
                         final I key2,
                         final Val<O> consequent2,
                         final I key3,
                         final Val<O> consequent3,
                         final I key4,
                         final Val<O> consequent4,
                         final I key5,
                         final Val<O> consequent5,
                         final I key6,
                         final Val<O> consequent6,
                         final Val<O> otherwise) {

        Val<Boolean> predicate1 = keyVal.map(key1::equals);
        Val<Boolean> predicate2 = keyVal.map(key2::equals);
        Val<Boolean> predicate3 = keyVal.map(key3::equals);
        Val<Boolean> predicate4 = keyVal.map(key4::equals);
        Val<Boolean> predicate5 = keyVal.map(key5::equals);
        Val<Boolean> predicate6 = keyVal.map(key6::equals);

        cond = Cond.of(predicate1,
                       consequent1,
                       predicate2,
                       consequent2,
                       predicate3,
                       consequent3,
                       predicate4,
                       consequent4,
                       predicate5,
                       consequent5,
                       predicate6,
                       consequent6,
                       otherwise
                      );

        return this;
    }

    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public Case<I, O> of(final List<I> keyList1,
                         final Val<O> consequent1,
                         final List<I> keyList2,
                         final Val<O> consequent2,
                         final List<I> keyList3,
                         final Val<O> consequent3,
                         final List<I> keyList4,
                         final Val<O> consequent4,
                         final List<I> keyList5,
                         final Val<O> consequent5,
                         final List<I> keyList6,
                         final Val<O> consequent6) {

        Val<Boolean> predicate1 = keyVal.map(keyList1::contains);
        Val<Boolean> predicate2 = keyVal.map(keyList2::contains);
        Val<Boolean> predicate3 = keyVal.map(keyList3::contains);
        Val<Boolean> predicate4 = keyVal.map(keyList4::contains);
        Val<Boolean> predicate5 = keyVal.map(keyList5::contains);
        Val<Boolean> predicate6 = keyVal.map(keyList6::contains);

        cond = Cond.of(predicate1,
                       consequent1,
                       predicate2,
                       consequent2,
                       predicate3,
                       consequent3,
                       predicate4,
                       consequent4,
                       predicate5,
                       consequent5,
                       predicate6,
                       consequent6
                      );

        return this;
    }

    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public Case<I, O> of(final List<I> keyList1,
                         final Val<O> consequent1,
                         final List<I> keyList2,
                         final Val<O> consequent2,
                         final List<I> keyList3,
                         final Val<O> consequent3,
                         final List<I> keyList4,
                         final Val<O> consequent4,
                         final List<I> keyList5,
                         final Val<O> consequent5,
                         final List<I> keyList6,
                         final Val<O> consequent6,
                         final Val<O> otherwise
                        ) {

        Val<Boolean> predicate1 = keyVal.map(keyList1::contains);
        Val<Boolean> predicate2 = keyVal.map(keyList2::contains);
        Val<Boolean> predicate3 = keyVal.map(keyList3::contains);
        Val<Boolean> predicate4 = keyVal.map(keyList4::contains);
        Val<Boolean> predicate5 = keyVal.map(keyList5::contains);
        Val<Boolean> predicate6 = keyVal.map(keyList6::contains);

        cond = Cond.of(predicate1,
                       consequent1,
                       predicate2,
                       consequent2,
                       predicate3,
                       consequent3,
                       predicate4,
                       consequent4,
                       predicate5,
                       consequent5,
                       predicate6,
                       consequent6,
                       otherwise
                      );

        return this;
    }


    @Override
    public Val<O> retryEach(final RetryPolicy policy) {
        return retryEach(e -> true,
                         policy
                        );
    }

    @Override
    public Val<O> retryEach(final Predicate<Throwable> predicate,
                            final RetryPolicy policy) {
        if (policy == null) return Val.fail(new IllegalArgumentException("Cons.retry: policy is null"));
        if (predicate == null) return Val.fail(new IllegalArgumentException("Cons.retry: predicate is null"));
        return cond.retryEach(predicate,
                              policy
                             );
    }


    @Override
    public Future<O> get() {
        return cond.get();
    }


}
