package model;

import java.io.Serializable;
import java.util.Date;

public class CalendarEvent implements Serializable {
    private String title;
    private Date date;
    private Date startTime;
    private Date endTime;
    private String location;
    private String notes;

    public CalendarEvent(String title, Date date, Date startTime, Date endTime, String location, String notes) {
        this.title = title;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.notes = notes;
    }

    public CalendarEvent(String title, Date date) {
        this.title = title;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getLocation() {
        return location;
    }

    public String getNotes() {
        return notes;
    }
}
