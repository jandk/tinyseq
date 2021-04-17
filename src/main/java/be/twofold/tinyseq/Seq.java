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

}
