package controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarController {
	
	private List<CalendarEvent> events = new ArrayList<CalendarEvent>();
	
	
	/**
	 * Creates a CalendarEvent object to represent a calendar event and 
	 * stores it in the events list. 
	 * If the endTime is before the startTime, false will be returned and
	 * the event will not be created.
	 * @param title -- event title
	 * @param date -- event date
	 * @param startTime -- event start time
	 * @param endTime -- event end time
	 * @param location -- event location
	 * @param notes -- notes about the event
	 * @return true if event successfully created, and false otherwise
	 */
	public boolean createEvent(String title, Date date, Date startTime, Date endTime, String location, String notes) {
		if (checkDate(startTime, endTime) == false) {
			return false;
		}
		CalendarEvent event = new CalendarEvent(title, date, startTime, endTime, location, notes);
		events.add(event);
		return true;
		
	}
	
	/**
	 * Ensures that the end date is after the start date
	 * @param start -- the start date of the event
	 * @param end -- the end date
	 * @return true if the start date is before the end date, false if otherwise
	 */
	private boolean checkDate(Date start, Date end) {
		if (end.compareTo(start) < 0)
			return false;
		return true;
	}
	
	
	
	
	
	
}
