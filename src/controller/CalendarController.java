package controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author mollyopheim
 *
 */
public class CalendarController {
	
	private List<CalendarEvent> events = new ArrayList<CalendarEvent>();
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
	public boolean createEvent(String title, Date date, Date startTime, Date endTime, String location, String notes) {
		if (checkDate(startTime, endTime) == false) {
			return false;
		}
		CalendarEvent event = new CalendarEvent(title, date, startTime, endTime, location, notes);
		events.add(event);
		return true;
		
	}
	
	/**
	 * A getter for the array list of calendar events!
	 * 
	 * @return the list of CalendarEvent's
	 */
	public ArrayList<CalendarEvent> getEvents() {
		return events;
	}
	
	/**
	 * This method adds a CalendarEvent to the events ArrayList
	 * 
	 * @param newEvent -- the event to be added to the event list
	 */
	public void addExistingEvent(CalendarEvent newEvent) {
		events.add(newEvent);
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
