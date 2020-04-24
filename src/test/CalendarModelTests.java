package test;

import model.CalendarEvent;
import model.CalendarModel;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

public class CalendarModelTests {
    @Test
    public void testGetEventsInMonth() {
        CalendarModel model = new CalendarModel();
        Calendar cal = Calendar.getInstance();
        cal.set(2020, Calendar.APRIL, 19, 3, 20);
        model.addEvent(new CalendarEvent("test", cal.getTime()));
        CalendarEvent[] events = model.getEventsInMonth(2020, Calendar.APRIL);
        assertEquals(1, events.length);
    }

    @Test
    public void testGetEventsInHour() {
        CalendarModel model = new CalendarModel();
        Calendar cal = Calendar.getInstance();
        cal.set(2020, Calendar.APRIL, 19, 3, 20);
        model.addEvent(new CalendarEvent("test", cal.getTime()));
        CalendarEvent[] events = model.getEventsInHour(2020, Calendar.APRIL, 19, 3);
        assertEquals(1, events.length);
    }
}
