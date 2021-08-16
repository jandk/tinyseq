package be.twofold.tinyseq;

import java.util.*;
import java.util.function.*;

final class FilterItr<E> implements Iterator<E> {
    private final Iterator<E> iterator;
    private final Predicate<? super E> predicate;
    private int state = 0; // 0 = not ready, 1 = ready, 2 = done
    private E next;

    FilterItr(Iterator<E> iterator, Predicate<? super E> predicate) {
        this.iterator = Objects.requireNonNull(iterator, "iterator is null");
        this.predicate = Objects.requireNonNull(predicate, "predicate is null");
    }

    @Override
    public boolean hasNext() {
        if (state == 0) {
            while (iterator.hasNext()) {
                E element = iterator.next();
                if (predicate.test(element)) {
                    next = element;
                    state = 1;
                    return true;
                }
            }
            next = null;
            state = 2;
            return false;
        }
        return state == 1;
    }

    @Override
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        state = 0;
        E result = next;
        next = null;
        return result;
    }
}
