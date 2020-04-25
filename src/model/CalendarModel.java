package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A representation of a calendar of events
 * @author Jessica Coan
 */
public class CalendarModel implements Serializable {
    private static final long serialVersionUID = 5184911405741555741L;
    private List<CalendarEvent> events = new ArrayList<>();

    /**
     * Gets all the events within a specific year
     * @param year the year to get events from
     * @return an array with all the events in that year
     */
    public CalendarEvent[] getEventsInYear(int year) {
        Calendar before = getCalendar(year, 1, 1);
        before.add(Calendar.SECOND, -1);
        Calendar after = getCalendar(year, 1, 1);
        after.add(Calendar.YEAR, 1);

        return getEventsInRange(before, after);
    }

    /**
     * Gets all the events in a month and year
     * NOTE: Month numbering in Java Calendar starts at 0.
     * @param year year of month
     * @param month month to get events from
     * @return an array of events in that month
     */
    public CalendarEvent[] getEventsInMonth(int year, int month) {
        Calendar before = getCalendar(year, month, 1);
        before.add(Calendar.SECOND, -1);
        Calendar after = getCalendar(year, month, 1);
        after.add(Calendar.MONTH, 1);

        return getEventsInRange(before, after);
    }

    /**
     * Gets all the events on a particular day
     * NOTE: Numbering of months and days in Java Calendar start at 0
     * @param year year of day
     * @param month month of day
     * @param day day to get events from
     * @return an array of all the events on that day
     */
    public CalendarEvent[] getEventsInDay(int year, int month, int day) {
        Calendar before = getCalendar(year, month, day);
        before.add(Calendar.SECOND, -1);
        Calendar after = getCalendar(year, month, day);
        after.add(Calendar.DAY_OF_MONTH, 1);

        return getEventsInRange(before, after);
    }

    /**
     * Get all the events that occur within a specific hour
     * NOTE: Numbering of month, day, and hour start at 0
     * @param year year of hour
     * @param month month of hour
     * @param day day of hour
     * @param hour hour to get events from
     * @return an array of all the events that occur in that hour
     */
    public CalendarEvent[] getEventsInHour(int year, int month, int day, int hour) {
        Calendar before = getCalendar(year, month, day, hour, 0);
        before.add(Calendar.SECOND, -1);
        Calendar after = getCalendar(year, month, day, hour, 0);
        after.add(Calendar.HOUR_OF_DAY, 1);

        return getEventsInRange(before, after);
    }

    /**
     * Given a start date on a Calendar and an end date, find all events that occur after the start and before the end
     * @param before start date Calendar
     * @param after end date Calendar
     * @return all the events that occur within the given range
     */
    public CalendarEvent[] getEventsInRange(Calendar before, Calendar after) {
        return events.parallelStream()
                .filter(event -> isDateInRange(event.getDate(), before, after))
                .toArray(CalendarEvent[]::new);
    }

    /**
     * Add a CalendarEvent to this calendar
     * @param event event to add
     */
    public void addEvent(CalendarEvent event) {
        events.add(event);
    }

    /**
     * Remove a CalendarEvent from this calendar
     * @param event event to remove
     */
    public void removeEvent(CalendarEvent event) {
        events.remove(event);
    }

    /**
     * Checks if a given Date is between two Calendar dates
     * @param date date to check
     * @param before start date to check between
     * @param after end date to check between
     * @return true if the given Date is between the two Calendar dates
     */
    private boolean isDateInRange(Date date, Calendar before, Calendar after) {
        Calendar eventCal = Calendar.getInstance();
        //Comparing a Date to a Calendar doesn't work, so this is what's required.
        eventCal.setTime(date);
        return before.before(eventCal) && after.after(eventCal);
    }

    /**
     * Returns a Calendar object set to a specific year, month, and day set
     * @param year year of the calendar
     * @param month month of the calendar
     * @param day day of the calendar
     * @return a calendar set to year, month, day, hour 0, minute 0
     */
    private Calendar getCalendar(int year, int month, int day) {
        return getCalendar(year, month, day, 0, 0);
    }

    /**
     * Returns a Calendar object set to a specific year, month, day, hour, and minute
     * @param year year of the calendar
     * @param month month of the calendar
     * @param day day of the calendar
     * @param hour hour of the calendar
     * @param minute minute of the calendar
     * @return a Calendar object set to year, month, day, hour, minute
     */
    private Calendar getCalendar(int year, int month, int day, int hour, int minute) {
        Calendar ret = Calendar.getInstance();
        //12:00 AM January 1st 2020 is represented as YEAR=2020, MONTH=0, DAY_OF_MONTH=0, HOUR_OF_DAY=0, MINUTE=0
        //If we want to pass in 'April 20', that'd have to be passed into the Calendar as 3, 19
        ret.set(year, month, day, hour, minute);
        return ret;
    }
}
