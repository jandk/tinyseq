package be.twofold.tinyseq;

import java.util.*;

final class DistinctItr<E> implements Iterator<E> {
    private final Set<E> seen = new HashSet<>();
    private final Iterator<E> iterator;
    private int state = 0;
    private E next;

    DistinctItr(Iterator<E> iterator) {
        this.iterator = Objects.requireNonNull(iterator, "iterator is null");
    }

    @Override
    public boolean hasNext() {
        if (state == 0) {
            while (iterator.hasNext()) {
                E value = iterator.next();
                if (seen.add(value)) {
                    state = 1;
                    next = value;
                    return true;
                }
            }
            state = 2;
            next = null;
            return false;
        }
        return state != 2;
    }

    @Override
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        state = 0;
        return next;
    }
}
