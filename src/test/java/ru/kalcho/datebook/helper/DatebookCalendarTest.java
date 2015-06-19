package ru.kalcho.datebook.helper;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 *
 */
public class DatebookCalendarTest {

    @Test
    public void testNextDay() throws Exception {
        LocalDateTime date = LocalDateTime.parse("2015-10-12T08:00");
        DatebookCalendar datebookCalendar = new DatebookCalendar(date);

        assertEquals(LocalDateTime.parse("2015-10-13T08:00"), datebookCalendar.getNextDay());
    }

    @Test
    public void testPrevDay() throws Exception {
        LocalDateTime date = LocalDateTime.parse("2015-10-12T08:00");

        DatebookCalendar datebookCalendar = new DatebookCalendar(date);
        assertEquals(LocalDateTime.parse("2015-10-11T08:00"), datebookCalendar.getPrevDay());
    }
}