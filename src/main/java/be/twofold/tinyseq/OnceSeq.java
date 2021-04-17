package be.twofold.tinyseq;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;

final class OnceSeq<T> implements Seq<T> {
    private final AtomicReference<Seq<T>> reference;

    OnceSeq(Seq<T> seq) {
        reference = new AtomicReference<>(seq);
    }

    @Override
    public Iterator<T> iterator() {
        Seq<T> seq = reference.getAndSet(null);
        if (seq == null) {
            throw new IllegalStateException("Seq can only be iterated once");
        }
        return seq.iterator();
    }
}
