package controller;

import model.CalendarEvent;
import model.CalendarModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

/**
 * 
 * @author mollyopheim
 *
 */
public class CalendarController {
	private HashMap<String, CalendarModel> map = new HashMap<>();
	
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
	public boolean createEvent(String title, LocalDate date, LocalTime startTime, LocalTime endTime, String location, String notes) {
		if (!checkDate(startTime, endTime)) {
			return false;
		}
		CalendarEvent event = new CalendarEvent(title, date, startTime, endTime, location, notes);
		return true;
	}

	/**
	 * Ensures that the end date is after the start date
	 * @param start -- the start date of the event
	 * @param end -- the end date
	 * @return true if the start date is before the end date, false if otherwise
	 */
	private boolean checkDate(LocalTime start, LocalTime end) {
		return start.isBefore(end);
	}
	
	/**
	 * This method adds a new CalendarModel object to the map
	 * of CalendarModel's
	 * 
	 * @param newModel -- the new CalendarModel object
	 * @param name -- the name of the new CalendarModel
	 */
	public void addCalendarModel(CalendarModel newModel, String name) {
		map.put(name, newModel);
	}
	
	/**
	 * Getter for a CalendarModel object in the CalendarModel map
	 * 
	 * @param name -- the String associated with the CalendarModel to
	 * get from the CalendarModel map
	 * @return the CalendarModel object found, null if no CalendarModel
	 * is found with the name
	 */
	public CalendarModel getCalendarModel(String name) {
		return map.get(name);
	}
}
