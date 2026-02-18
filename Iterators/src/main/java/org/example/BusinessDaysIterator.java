package org.example;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BusinessDaysIterator implements Iterator<LocalDate> {

    private LocalDate currentDate;

    public BusinessDaysIterator(LocalDate startDate) {
        this.currentDate = startDate;
    }

    @Override
    public boolean hasNext() {
        return true; // infinite iterator
    }

    @Override
    public LocalDate next() {
        if (currentDate == null) {
            throw new NoSuchElementException();
        }

        // move to next day first
        currentDate = currentDate.plusDays(1);

        // skip weekends
        while (isWeekend(currentDate)) {
            currentDate = currentDate.plusDays(1);
        }

        return currentDate;
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }
    public static void main(String[] args) {
        Iterator<LocalDate> iter=new BusinessDaysIterator(LocalDate.of(2022,1,1));
        System.out.println(iter.next());
        System.out.println(iter.next());
    }
}

