package be.twofold.tinyseq;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

class SeqTest {

    private final Seq<String> emptySeq = Seq.seq(Collections.emptyIterator());
    private final Seq<String> nonEmptySeq = Seq.seq(Arrays.asList("one", "two", "three", "four", "five"));
    private final Seq<String> nonEmptyNullSeq = Seq.seq(Arrays.asList(null, null, "foo", null, null));

    @Test
    void testFromIterable() {
        assertThatNullPointerException()
            .isThrownBy(() -> Seq.seq((Iterable<?>) null));
    }

    @Test
    void testFirst() {
        assertThat(nonEmptySeq.first()).isEqualTo("one");
        assertThatExceptionOfType(NoSuchElementException.class)
            .isThrownBy(emptySeq::first);
        assertThat(nonEmptyNullSeq.first()).isNull();
    }

    @Test
    void testFirstOptional() {
        assertThat(nonEmptySeq.firstOptional()).hasValue("one");
        assertThat(emptySeq.firstOptional()).isEmpty();
        assertThat(nonEmptyNullSeq.firstOptional()).isEmpty();
    }

    @Test
    void testLast() {
        assertThat(nonEmptySeq.last()).isEqualTo("five");
        assertThatExceptionOfType(NoSuchElementException.class)
            .isThrownBy(emptySeq::last);
        assertThat(nonEmptyNullSeq.last()).isNull();
    }

    @Test
    void testLastOptional() {
        assertThat(nonEmptySeq.lastOptional()).hasValue("five");
        assertThat(emptySeq.lastOptional()).isEmpty();
        assertThat(nonEmptyNullSeq.lastOptional()).isEmpty();
    }

}
