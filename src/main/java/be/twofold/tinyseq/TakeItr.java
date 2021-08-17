package be.twofold.tinyseq;

import java.util.*;

final class TakeItr<E> implements Iterator<E> {
    private final Iterator<E> iterator;
    private int count;

    TakeItr(Iterator<E> iterator, int count) {
        this.iterator = Objects.requireNonNull(iterator, "iterator is null");
        if (count < 0) {
            throw new IllegalArgumentException("count < 0");
        }
        this.count = count;
    }

    @Override
    public boolean hasNext() {
        return count > 0 && iterator.hasNext();
    }

    @Override
    public E next() {
        if (count == 0) {
            throw new NoSuchElementException();
        }
        count--;
        return iterator.next();
    }
}
