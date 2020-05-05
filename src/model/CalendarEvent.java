package model;

import javafx.scene.paint.Color;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Represents a calendar event
 *
 * @author Jessica Coan
 * @author Kitty Elliott
 */
public class CalendarEvent implements Serializable {

    public static final Color DEFAULT_COLOR = Color.LIGHTGRAY;

    private static final long serialVersionUID = -3059578212481803086L;
    private String title;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private String notes;
    private java.awt.Color color;

    /**
     * construct a new event with the default color.
     *
     * @param title     title of the event
     * @param date      date of the event
     * @param startTime when the event starts
     * @param endTime   when the event ends
     * @param location  where the event takes place. can be null
     * @param notes     misc. notes on the event. can be null
     */
    public CalendarEvent(String title, LocalDate date, LocalTime startTime, LocalTime endTime, String location, String notes) {
        this.title = title;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.notes = notes;
    }

    /**
     * construct a new event
     *
     * @param title     title of the event
     * @param date      date of the event
     * @param startTime when the event starts
     * @param endTime   when the event ends
     * @param location  where the event takes place. can be null
     * @param notes     misc. notes on the event. can be null
     * @param color     the color of the event. if null, defaults to {@link #DEFAULT_COLOR}
     */
    public CalendarEvent(String title, LocalDate date, LocalTime startTime, LocalTime endTime,
                         String location, String notes, Color color) {
        this(title, date, startTime, endTime, location, notes);
        setColor(color);
    }

    /**
     * construct a new event with the default color at the specified time
     *
     * @param title title of the event
     * @param date  the date of the event, as well as the start and end time.
     *              Note that the start and end times will be equal.
     */
    public CalendarEvent(String title, LocalDateTime date) {
        this.title = title;
        this.date = date.toLocalDate();
        this.startTime = date.toLocalTime();
    }

    /**
     * @return the event's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the new title of the event. must not be null.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the date of the event
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @param date the new date of the event. must not be null.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * @return the time at which the event starts
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the new start time of the event. must not be null.
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the time at which the event ends
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the new end time of the event. must not be null.
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * @return where the event occurs. may be null.
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the new location of the event. can be null.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return misc. notes on the event. may be null.
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes the new notes for the event. can be null.
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return the event's color.
     */
    public Color getColor() {
        return color == null
                ? DEFAULT_COLOR
                : Color.rgb(color.getRed(), color.getBlue(), color.getGreen());
    }

    /**
     * @param color the new color of the event. If null, then {@link #DEFAULT_COLOR} is used.
     */
    public void setColor(Color color) {
        if (color == null || color.equals(DEFAULT_COLOR)) {
            this.color = null;
        } else {
            int r = (int) (color.getRed() * 255f);
            int b = (int) (color.getBlue() * 255f);
            int g = (int) (color.getGreen() * 255f);
            this.color = new java.awt.Color(r, b, g);
        }
    }
}
