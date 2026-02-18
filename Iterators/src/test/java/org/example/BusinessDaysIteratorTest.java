package org.example;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BusinessDaysIteratorTest {

    @Test
    void shouldSkipWeekend() {
        Iterator<LocalDate> iter =
                new BusinessDaysIterator(LocalDate.of(2022, 1, 1)); // Saturday

        assertEquals(LocalDate.of(2022, 1, 3), iter.next()); // Monday
    }

    @Test
    void shouldReturnNextBusinessDaySequentially() {
        Iterator<LocalDate> iter =
                new BusinessDaysIterator(LocalDate.of(2022, 1, 7)); // Friday

        assertEquals(LocalDate.of(2022, 1, 10), iter.next()); // Monday
        assertEquals(LocalDate.of(2022, 1, 11), iter.next()); // Tuesday
    }
}
