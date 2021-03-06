package be.twofold.tinyseq;

import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

import static be.twofold.tinyseq.SeqHelper.*;

@FunctionalInterface
public interface Seq<T> extends Iterable<T> {

    static <T> Seq<T> empty() {
        return Collections::emptyIterator;
    }

    @SafeVarargs
    static <T> Seq<T> of(T... elements) {
        return seq(Arrays.asList(elements));
    }

    static <T> Seq<T> seq(Iterator<T> iterator) {
        return ((Seq<T>) () -> iterator).once();
    }

    static <T> Seq<T> seq(Iterable<T> iterable) {
        return iterable::iterator;
    }

    default boolean all(Predicate<? super T> predicate) {
        for (T element : this) {
            if (!predicate.test(element)) {
                return false;
            }
        }
        return true;
    }

    default boolean any(Predicate<? super T> predicate) {
        for (T element : this) {
            if (predicate.test(element)) {
                return true;
            }
        }
        return false;
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

    default Seq<T> distinct() {
        return () -> new DistinctItr<>(iterator());
    }

    default Seq<T> drop(int count) {
        return () -> new DropItr<>(iterator(), count);
    }

    default Seq<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");

        return () -> new FilterItr<>(iterator(), predicate);
    }

    default Seq<T> filterIndexed(BiPredicate<Integer, ? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");

        AtomicInteger index = new AtomicInteger();
        return filter(t -> predicate.test(index.getAndIncrement(), t));
    }

    default T first() {
        return nonEmptyIterator(this)
            .next();
    }

    default Optional<T> firstOptional() {
        return optionalIterator(this)
            .map(Iterator::next);
    }

    default <R> Seq<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");

        return () -> new FlatMapItr<>(iterator(), mapper);
    }

    default <R> Seq<R> flatMapIndexed(BiFunction<Integer, ? super T, ? extends Iterable<? extends R>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");

        AtomicInteger index = new AtomicInteger();
        return flatMap(t -> mapper.apply(index.getAndIncrement(), t));
    }

    default <U> U fold(U initial, BiFunction<? super U, ? super T, ? extends U> function) {
        U result = initial;
        for (T element : this) {
            result = function.apply(result, element);
        }
        return result;
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

    default <R> Seq<R> map(Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");

        return () -> new MapItr<>(iterator(), mapper);
    }

    default <R> Seq<R> mapIndexed(BiFunction<Integer, ? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");

        AtomicInteger index = new AtomicInteger();
        return map(t -> mapper.apply(index.getAndIncrement(), t));
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

    default boolean none(Predicate<? super T> predicate) {
        for (T element : this) {
            if (predicate.test(element)) {
                return false;
            }
        }
        return true;
    }

    default Seq<T> onEach(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");

        return map(element -> {
            action.accept(element);
            return element;
        });
    }

    default Seq<T> onEachIndexed(BiConsumer<Integer, ? super T> action) {
        Objects.requireNonNull(action, "action is null");

        AtomicInteger index = new AtomicInteger();
        return map(element -> {
            action.accept(index.getAndIncrement(), element);
            return element;
        });
    }

    default Seq<T> once() {
        if (this instanceof OnceSeq) {
            return this;
        }
        return new OnceSeq<>(this);
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

    @SuppressWarnings("unchecked")
    default Seq<T> sorted() {
        return sorted((Comparator<? super T>) Comparator.naturalOrder());
    }

    default Seq<T> sorted(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");

        return () -> {
            List<T> list = toList();
            list.sort(comparator);
            return list.iterator();
        };
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

    default Seq<T> take(int count) {
        return () -> new TakeItr<>(iterator(), count);
    }

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

    default List<T> toUnmodifiableList() {
        ArrayList<T> list = toCollection(new ArrayList<>());
        switch (list.size()) {
            case 0:
                return Collections.emptyList();
            case 1:
                return Collections.singletonList(list.get(0));
            default:
                list.trimToSize();
                return Collections.unmodifiableList(list);
        }
    }

    default Set<T> toUnmodifiableSet() {
        Set<T> set = toCollection(new HashSet<>());
        switch (set.size()) {
            case 0:
                return Collections.emptySet();
            case 1:
                return Collections.singleton(set.iterator().next());
            default:
                return Collections.unmodifiableSet(set);
        }
    }


}
