package be.twofold.tinyseq;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface Seq<T> extends Iterable<T> {

    static <T> Seq<T> seq(Iterable<T> iterable) {
        return iterable::iterator;
    }

    static <T> Seq<T> seq(Iterator<T> iterator) {
        return ((Seq<T>) () -> iterator).once();
    }


    //
    // Intermediate operations
    //

    default Seq<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate");

        return () -> new FilterItr<>(iterator(), predicate);
    }

    default <R> Seq<R> flatMap(Function<? super T, ? extends Seq<? extends R>> mapper) {
        Objects.requireNonNull(mapper, "mapper");

        return () -> new FlatMapItr<>(iterator(), mapper);
    }

    default <R> Seq<R> map(Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper, "mapper");

        return () -> new MapItr<>(iterator(), mapper);
    }

    default Seq<T> once() {
        if (this instanceof OnceSeq) {
            return this;
        }
        return new OnceSeq<>(this);
    }


    //
    // Terminal operations
    //


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

    default boolean none(Predicate<? super T> predicate) {
        for (T element : this) {
            if (predicate.test(element)) {
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


    default T first() {
        return nonEmptyIterator().next();
    }

    default T last() {
        Iterator<T> iterator = nonEmptyIterator();

        T last = iterator.next();
        while (iterator.hasNext()) {
            last = iterator.next();
        }
        return last;
    }


    //
    // toCollection/toMap
    //

    default <C extends Collection<? super T>> C toCollection(C destination) {
        Objects.requireNonNull(destination, "destination");

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


    //
    // Helpers
    //

    default Iterator<T> nonEmptyIterator() {
        Iterator<T> iterator = iterator();
        if (!iterator.hasNext()) {
            throw new NoSuchElementException("Empty seq");
        }
        return iterator;
    }

}
