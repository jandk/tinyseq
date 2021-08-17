package be.twofold.tinyseq;

import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

import static be.twofold.tinyseq.SeqHelper.*;

@FunctionalInterface
public interface Seq<T> extends Iterable<T> {

    static <T> Seq<T> seq(Iterable<T> iterable) {
        return iterable::iterator;
    }

    static <T> Seq<T> seq(Iterator<T> iterator) {
        return ((Seq<T>) () -> iterator).once();
    }

    @SafeVarargs
    static <T> Seq<T> of(T... elements) {
        return seq(Arrays.asList(elements));
    }

    // region drop/take

    default Seq<T> drop(int count) {
        return () -> new DropItr<>(iterator(), count);
    }

    default Seq<T> take(int count) {
        return () -> new TakeItr<>(iterator(), count);
    }

    // endregion

    default Seq<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");

        return () -> new FilterItr<>(iterator(), predicate);
    }

    default <R> Seq<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");

        return () -> new FlatMapItr<>(iterator(), mapper);
    }

    default <R> Seq<R> map(Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");

        return () -> new MapItr<>(iterator(), mapper);
    }

    default <R> Seq<R> mapIndexed(BiFunction<Integer, ? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");

        AtomicInteger index = new AtomicInteger();
        return map(t -> mapper.apply(index.getAndIncrement(), t));
    }

    default Seq<T> once() {
        if (this instanceof OnceSeq) {
            return this;
        }
        return new OnceSeq<>(this);
    }


    default boolean any(Predicate<? super T> predicate) {
        for (T element : this) {
            if (predicate.test(element)) {
                return true;
            }
        }
        return false;
    }

    default boolean none(Predicate<? super T> predicate) {
        for (T element : this) {
            if (predicate.test(element)) {
                return false;
            }
        }
        return true;
    }

    default boolean all(Predicate<? super T> predicate) {
        for (T element : this) {
            if (!predicate.test(element)) {
                return false;
            }
        }
        return true;
    }

    default int count() {
        int count = 0;
        for (T ignored : this) {
            count++;
        }
        return count;
    }

    default int count(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");

        int count = 0;
        for (T element : this) {
            if (predicate.test(element)) {
                count++;
            }
        }
        return count;
    }

    // region aggregates

    default int min(ToIntFunction<? super T> mapper) {
        return SeqHelper.reduce(nonEmptyIterator(this), mapper, Integer::min);
    }

    default long min(ToLongFunction<? super T> mapper) {
        return SeqHelper.reduce(nonEmptyIterator(this), mapper, Long::min);
    }

    default double min(ToDoubleFunction<? super T> mapper) {
        return SeqHelper.reduce(nonEmptyIterator(this), mapper, Double::min);
    }

    default OptionalInt minOptional(ToIntFunction<? super T> mapper) {
        return optionalIterator(this)
            .map(it -> OptionalInt.of(SeqHelper.reduce(it, mapper, Integer::min)))
            .orElseGet(OptionalInt::empty);
    }

    default OptionalLong minOptional(ToLongFunction<? super T> mapper) {
        return optionalIterator(this)
            .map(it -> OptionalLong.of(SeqHelper.reduce(it, mapper, Long::min)))
            .orElseGet(OptionalLong::empty);
    }

    default OptionalDouble minOptional(ToDoubleFunction<? super T> mapper) {
        return optionalIterator(this)
            .map(it -> OptionalDouble.of(SeqHelper.reduce(it, mapper, Double::min)))
            .orElseGet(OptionalDouble::empty);
    }

    default int max(ToIntFunction<? super T> mapper) {
        return SeqHelper.reduce(nonEmptyIterator(this), mapper, Integer::max);
    }

    default long max(ToLongFunction<? super T> mapper) {
        return SeqHelper.reduce(nonEmptyIterator(this), mapper, Long::max);
    }

    default double max(ToDoubleFunction<? super T> mapper) {
        return SeqHelper.reduce(nonEmptyIterator(this), mapper, Double::max);
    }

    default OptionalInt maxOptional(ToIntFunction<? super T> mapper) {
        return optionalIterator(this)
            .map(it -> OptionalInt.of(SeqHelper.reduce(it, mapper, Integer::max)))
            .orElseGet(OptionalInt::empty);
    }

    default OptionalLong maxOptional(ToLongFunction<? super T> mapper) {
        return optionalIterator(this)
            .map(it -> OptionalLong.of(SeqHelper.reduce(it, mapper, Long::max)))
            .orElseGet(OptionalLong::empty);
    }

    default OptionalDouble maxOptional(ToDoubleFunction<? super T> mapper) {
        return optionalIterator(this)
            .map(it -> OptionalDouble.of(SeqHelper.reduce(it, mapper, Double::max)))
            .orElseGet(OptionalDouble::empty);
    }

    default int sum(ToIntFunction<? super T> mapper) {
        int sum = 0;
        for (T element : this) {
            sum += mapper.applyAsInt(element);
        }
        return sum;
    }

    default long sum(ToLongFunction<? super T> mapper) {
        long sum = 0;
        for (T element : this) {
            sum += mapper.applyAsLong(element);
        }
        return sum;
    }

    default double sum(ToDoubleFunction<? super T> mapper) {
        double sum = 0;
        for (T element : this) {
            sum += mapper.applyAsDouble(element);
        }
        return sum;
    }

    default double average(ToIntFunction<? super T> mapper) {
        int count = 0;
        double sum = 0.0;
        for (T element : this) {
            sum += mapper.applyAsInt(element);
            count++;
        }
        return count == 0 ? Double.NaN : sum / count;
    }

    default double average(ToLongFunction<? super T> mapper) {
        int count = 0;
        double sum = 0.0;
        for (T element : this) {
            sum += mapper.applyAsLong(element);
            count++;
        }
        return count == 0 ? Double.NaN : sum / count;
    }

    default double average(ToDoubleFunction<? super T> mapper) {
        int count = 0;
        double sum = 0.0;
        for (T element : this) {
            sum += mapper.applyAsDouble(element);
            count++;
        }
        return count == 0 ? Double.NaN : sum / count;
    }

    default IntSummaryStatistics statistics(ToIntFunction<? super T> mapper) {
        IntSummaryStatistics statistics = new IntSummaryStatistics();
        for (T element : this) {
            statistics.accept(mapper.applyAsInt(element));
        }
        return statistics;
    }

    default LongSummaryStatistics statistics(ToLongFunction<? super T> mapper) {
        LongSummaryStatistics statistics = new LongSummaryStatistics();
        for (T element : this) {
            statistics.accept(mapper.applyAsLong(element));
        }
        return statistics;
    }

    default DoubleSummaryStatistics statistics(ToDoubleFunction<? super T> mapper) {
        DoubleSummaryStatistics statistics = new DoubleSummaryStatistics();
        for (T element : this) {
            statistics.accept(mapper.applyAsDouble(element));
        }
        return statistics;
    }

    // endregion

    // region fold/reduce

    default <U> U fold(U initial, BiFunction<? super U, ? super T, ? extends U> function) {
        U result = initial;
        for (T element : this) {
            result = function.apply(result, element);
        }
        return result;
    }

    default T reduce(BinaryOperator<T> operator) {
        return SeqHelper.reduce(
            nonEmptyIterator(this), operator
        );
    }

    default Optional<T> reduceOptional(BinaryOperator<T> operator) {
        return optionalIterator(this)
            .map(it -> SeqHelper.reduce(it, operator));
    }

    // endregion

    // region first/last

    default T first() {
        return nonEmptyIterator(this)
            .next();
    }

    default Optional<T> firstOptional() {
        return optionalIterator(this)
            .map(Iterator::next);
    }

    default T last() {
        return SeqHelper.last(
            nonEmptyIterator(this)
        );
    }

    default Optional<T> lastOptional() {
        return optionalIterator(this)
            .map(SeqHelper::last);
    }

    // endregion

    // region toCollection

    default <C extends Collection<? super T>> C toCollection(C destination) {
        Objects.requireNonNull(destination, "destination is null");

        for (T element : this) {
            destination.add(element);
        }

        return destination;
    }

    default List<T> toList() {
        return toCollection(new ArrayList<>());
    }

    default Set<T> toSet() {
        return toCollection(new HashSet<>());
    }

    // endregion

}
