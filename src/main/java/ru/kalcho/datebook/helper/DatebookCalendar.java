package ru.kalcho.datebook.helper;

import java.time.LocalDateTime;

/**
 *
 */
public class DatebookCalendar {

    private LocalDateTime currentDate;

    public DatebookCalendar(LocalDateTime currentDate) {
        this.currentDate = currentDate;
    }

    public LocalDateTime getCurrentDate() {
        return currentDate;
    }

    public LocalDateTime getNextDay() {
        return currentDate.plusDays(1);
    }

    public LocalDateTime getPrevDay() {
        return currentDate.minusDays(1);
    }

}
