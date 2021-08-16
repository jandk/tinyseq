package be.twofold.tinyseq;

import java.util.*;
import java.util.function.*;

final class FlatMapItr<E, R> implements Iterator<R> {
    private final Iterator<E> iterator;
    private final Function<? super E, ? extends Iterable<? extends R>> mapper;
    private Iterator<? extends R> elementIterator = Collections.emptyIterator();

    FlatMapItr(Iterator<E> iterator, Function<? super E, ? extends Iterable<? extends R>> mapper) {
        this.iterator = Objects.requireNonNull(iterator, "iterator is null");
        this.mapper = Objects.requireNonNull(mapper, "mapper is null");
    }

    @Override
    public boolean hasNext() {
        while (true) {
            if (elementIterator.hasNext()) {
                return true;
            }
            if (!iterator.hasNext()) {
                return false;
            }
            elementIterator = mapper.apply(iterator.next()).iterator();
        }
    }

    @Override
    public R next() {
        return elementIterator.next();
    }
}
