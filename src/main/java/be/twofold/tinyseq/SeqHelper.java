package be.twofold.tinyseq;

import java.util.*;
import java.util.function.*;

final class SeqHelper {
    private SeqHelper() {
        throw new UnsupportedOperationException();
    }

    static <T> Iterator<T> nonEmptyIterator(Seq<T> seq) {
        Iterator<T> iterator = seq.iterator();
        if (!iterator.hasNext()) {
            throw new NoSuchElementException("Empty seq");
        }
        return iterator;
    }

    static <T> Optional<Iterator<T>> optionalIterator(Seq<T> seq) {
        return Optional.of(seq.iterator())
            .filter(Iterator::hasNext);
    }

    static <T> T last(Iterator<T> iterator) {
        T last = iterator.next();
        while (iterator.hasNext()) {
            last = iterator.next();
        }
        return last;
    }

    static <T> T reduce(Iterator<T> iterator, BinaryOperator<T> operator) {
        T acc = iterator.next();
        while (iterator.hasNext()) {
            acc = operator.apply(acc, iterator.next());
        }
        return acc;
    }

    static <T> int reduce(Iterator<T> iterator, ToIntFunction<? super T> mapper, IntBinaryOperator operator) {
        int acc = mapper.applyAsInt(iterator.next());
        while (iterator.hasNext()) {
            acc = operator.applyAsInt(acc, mapper.applyAsInt(iterator.next()));
        }
        return acc;
    }

    static <T> long reduce(Iterator<T> iterator, ToLongFunction<? super T> mapper, LongBinaryOperator operator) {
        long acc = mapper.applyAsLong(iterator.next());
        while (iterator.hasNext()) {
            acc = operator.applyAsLong(acc, mapper.applyAsLong(iterator.next()));
        }
        return acc;
    }

    static <T> double reduce(Iterator<T> iterator, ToDoubleFunction<? super T> mapper, DoubleBinaryOperator operator) {
        double acc = mapper.applyAsDouble(iterator.next());
        while (iterator.hasNext()) {
            acc = operator.applyAsDouble(acc, mapper.applyAsDouble(iterator.next()));
        }
        return acc;
    }
}
