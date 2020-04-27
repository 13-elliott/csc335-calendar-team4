package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * A representation of a calendar of events
 * @author Jessica Coan
 */
public class CalendarModel extends Observable implements Serializable  {
    private static final long serialVersionUID = 5184911405741555741L;
    private List<CalendarEvent> events = new ArrayList<>();

    /**
     * Gets all the events within a specific year
     * @param year the year to get events from
     * @return an array with all the events in that year
     */
    public CalendarEvent[] getEventsInYear(int year) {
        LocalDateTime before = getDateTime(year, 1, 1);
        LocalDateTime after = getDateTime(year, 1, 1);

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
        LocalDateTime before = getDateTime(year, month, 1);
        LocalDateTime after = getDateTime(year, month, 1);

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
        LocalDateTime before = getDateTime(year, month, day);
        LocalDateTime after = getDateTime(year, month, day);

        return getEventsInRange(before, after);
    }

    public CalendarEvent[] getEventsInDay(LocalDate day) {
        return getEventsInDay(day.getYear(), day.getMonthValue(), day.getDayOfMonth());
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
        LocalDateTime before = getDateTime(year, month, day, hour, 0);
        LocalDateTime after = getDateTime(year, month, day, hour, 0);

        return getEventsInRange(before, after);
    }

    public CalendarEvent[] getEventsInHour(LocalDateTime time) {
        return getEventsInHour(time.getYear(), time.getMonthValue(), time.getHour(), time.getHour());
    }

    /**
     * Given a start date on a Calendar and an end date, find all events that occur after the start and before the end
     * @param before start date Calendar
     * @param after end date Calendar
     * @return all the events that occur within the given range
     */
    public CalendarEvent[] getEventsInRange(LocalDateTime before, LocalDateTime after) {
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
        setChanged();
        notifyObservers(event);
    }

    /**
     * Remove a CalendarEvent from this calendar
     * @param event event to remove
     */
    public void removeEvent(CalendarEvent event) {
        events.remove(event);
        setChanged();
        notifyObservers();
    }

    /**
     * Mark that an event in this model has been modified, so Observers can be updated accordingly
     * @param event event that has been modified
     */
    public void markModified(CalendarEvent event) {
        setChanged();
        notifyObservers(event);
    }

    /**
     * Checks if a given Date is between two Calendar dates
     * @param date date to check
     * @param before start date to check between
     * @param after end date to check between
     * @return true if the given Date is between the two Calendar dates
     */
    private boolean isDateInRange(LocalDateTime date, LocalDateTime before, LocalDateTime after) {
        return  before.isBefore(date) && after.isAfter(date);
    }

    /**
     * Returns a Calendar object set to a specific year, month, and day set
     * @param year year of the calendar
     * @param month month of the calendar
     * @param day day of the calendar
     * @return a calendar set to year, month, day, hour 0, minute 0
     */
    private LocalDateTime getDateTime(int year, int month, int day) {
        return getDateTime(year, month, day, 0, 0);
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
    private LocalDateTime getDateTime(int year, int month, int day, int hour, int minute) {
        return LocalDateTime.of(year, month, day, hour, minute);
    }
}
