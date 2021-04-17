package be.twofold.tinyseq;

import java.util.Collections;
import java.util.Iterator;
import java.util.function.Function;

final class FlatMapItr<E, R> implements Iterator<R> {
    private final Iterator<E> iterator;
    private final Function<? super E, ? extends Iterable<? extends R>> mapper;
    private Iterator<? extends R> itemIterator = Collections.emptyIterator();

    FlatMapItr(Iterator<E> iterator, Function<? super E, ? extends Iterable<? extends R>> mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        while (true) {
            if (itemIterator.hasNext()) {
                return true;
            }
            if (!iterator.hasNext()) {
                return false;
            }
            itemIterator = mapper.apply(iterator.next()).iterator();
        }
    }

    @Override
    public R next() {
        return itemIterator.next();
    }
}
