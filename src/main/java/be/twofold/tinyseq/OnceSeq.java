package be.twofold.tinyseq;

import java.util.*;
import java.util.concurrent.atomic.*;

final class OnceSeq<T> implements Seq<T> {
    private final AtomicReference<Seq<T>> reference;

    OnceSeq(Seq<T> seq) {
        Objects.requireNonNull(seq, "seq is null");

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
