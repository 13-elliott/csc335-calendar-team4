package model;

import javafx.scene.paint.Color;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CalendarEvent implements Serializable {

    private static final long serialVersionUID = -3059578212481803086L;
    private String title;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private String notes;
    private java.awt.Color color;

    public CalendarEvent(String title, LocalDate date, LocalTime startTime, LocalTime endTime, String location, String notes) {
        this.title = title;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.notes = notes;
    }

    public CalendarEvent(String title, LocalDate date, LocalTime startTime, LocalTime endTime,
                         String location, String notes, Color color) {
        this(title, date, startTime, endTime, location, notes);
        setColor(color);
    }

    public CalendarEvent(String title, LocalDateTime date) {
        this.title = title;
        this.date = date.toLocalDate();
        this.startTime = date.toLocalTime();
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getLocation() {
        return location;
    }

    public String getNotes() {
        return notes;
    }

    public Color getColor() {
        return color == null
                ? null
                : Color.rgb(color.getRed(), color.getBlue(), color.getGreen());
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    private void setColor(Color color) {
        if (color == null) {
            this.color = null;
        } else {
            int r = (int) (color.getRed() * 255f);
            int b = (int) (color.getBlue() * 255f);
            int g = (int) (color.getGreen() * 255f);
            this.color = new java.awt.Color(r, b, g);
        }
    }
}
