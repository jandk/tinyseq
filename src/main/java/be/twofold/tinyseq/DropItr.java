package be.twofold.tinyseq;

import java.util.*;

final class DropItr<E> implements Iterator<E> {
    private final Iterator<E> iterator;
    private int count;

    DropItr(Iterator<E> iterator, int count) {
        this.iterator = Objects.requireNonNull(iterator, "iterator is null");
        if (count < 0) {
            throw new IllegalArgumentException("count < 0");
        }
        this.count = count;
    }

    @Override
    public boolean hasNext() {
        drop();
        return iterator.hasNext();
    }

    @Override
    public E next() {
        drop();
        return iterator.next();
    }

    private void drop() {
        while (count > 0 && iterator.hasNext()) {
            iterator.next();
            count--;
        }
    }
}
