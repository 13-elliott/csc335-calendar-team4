package controller;

import java.util.Date;
import model.CalendarEvent;

public class CalendarController {
	
	public CalendarController() {
		
	}
	
	
	/**
	 * Creates a CalendarEvent object to represent a calendar event
	 * @param title -- event title
	 * @param date -- event date
	 * @param startTime -- event start time
	 * @param endTime -- event end time
	 * @param location -- event location
	 * @param notes -- notes about the event
	 * @return the CalendarEvent object created
	 */
	public CalendarEvent createEvent(String title, Date date, Date startTime, Date endTime, String location, String notes) {
		CalendarEvent event = new CalendarEvent(title, date, startTime, endTime, location, notes);
		return event;
	}
	
	
	
}
