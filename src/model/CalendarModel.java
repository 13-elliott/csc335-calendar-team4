package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarModel implements Serializable {
    private List<CalendarEvent> events = new ArrayList<>();

    public List<CalendarEvent> getEventsInYear(int year) {
        return null;
    }

    public List<CalendarEvent> getEventsInMonth(int year, int month) {
        return null;
    }

    public List<CalendarEvent> getEventsInDay(int year, int month, int day) {
        return null;
    }
}
