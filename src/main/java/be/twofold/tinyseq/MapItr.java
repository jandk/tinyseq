package be.twofold.tinyseq;

import java.util.Iterator;
import java.util.function.Function;

final class MapItr<E, R> implements Iterator<R> {
    private final Iterator<E> iterator;
    private final Function<? super E, ? extends R> mapper;

    MapItr(Iterator<E> iterator, Function<? super E, ? extends R> mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public R next() {
        return mapper.apply(iterator.next());
    }
}
