package be.twofold.tinyseq;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

public class SeqMinMaxSumAverageTest {

    @Test
    void testMinInt() {
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> Seq.<Integer>of().min(Integer::intValue));
        assertThat(Seq.of(2, 1, 4, 3).min(Integer::intValue)).isEqualTo(1);
    }

    @Test
    void testMinLong() {
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> Seq.<Long>of().min(Long::longValue));
        assertThat(Seq.of(2L, 1L, 4L, 3L).min(Long::longValue)).isEqualTo(1L);
    }

    @Test
    void testMinDouble() {
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> Seq.<Double>of().min(Double::doubleValue));
        assertThat(Seq.of(2.0, 1.0, 4.0, 3.0).min(Double::doubleValue)).isEqualTo(1.0);
    }

    @Test
    void testMaxInt() {
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> Seq.<Integer>of().max(Integer::intValue));
        assertThat(Seq.of(2, 1, 4, 3).max(Integer::intValue)).isEqualTo(4);
    }

    @Test
    void testMaxLong() {
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> Seq.<Long>of().max(Long::longValue));
        assertThat(Seq.of(2L, 1L, 4L, 3L).max(Long::longValue)).isEqualTo(4L);
    }

    @Test
    void testMaxDouble() {
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> Seq.<Double>of().max(Double::doubleValue));
        assertThat(Seq.of(2.0, 1.0, 4.0, 3.0).max(Double::doubleValue)).isEqualTo(4.0);
    }

    @Test
    void testSumInt() {
        assertThat(Seq.<Integer>of().sum(Integer::intValue)).isEqualTo(0);
        assertThat(Seq.of(2, 1, 4, 3).sum(Integer::intValue)).isEqualTo(10);
    }

    @Test
    void testSumLong() {
        assertThat(Seq.<Long>of().sum(Long::longValue)).isEqualTo(0);
        assertThat(Seq.of(2L, 1L, 4L, 3L).sum(Long::longValue)).isEqualTo(10L);
    }

    @Test
    void testSumDouble() {
        assertThat(Seq.<Double>of().sum(Double::doubleValue)).isEqualTo(0);
        assertThat(Seq.of(2.0, 1.0, 4.0, 3.0).sum(Double::doubleValue)).isEqualTo(10.0);
    }

    @Test
    void testAverageInt() {
        assertThat(Seq.<Integer>of().average(Integer::intValue)).isNaN();
        assertThat(Seq.of(2, 1, 4, 3).average(Integer::intValue)).isEqualTo(2.5);
    }

    @Test
    void testAverageLong() {
        assertThat(Seq.<Long>of().average(Long::longValue)).isNaN();
        assertThat(Seq.of(2L, 1L, 4L, 3L).average(Long::longValue)).isEqualTo(2.5);
    }

    @Test
    void testAverageDouble() {
        assertThat(Seq.<Double>of().average(Double::doubleValue)).isNaN();
        assertThat(Seq.of(2.0, 1.0, 4.0, 3.0).average(Double::doubleValue)).isEqualTo(2.5);
    }

}
