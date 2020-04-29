package controller;

import model.CalendarEvent;
import model.CalendarModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
	 * Initializes the CalendarController to have one default CalendarModel
	 * in the map that keeps track of the CalendarModel objects.
	 */
	public CalendarController() {
		CalendarModel newModel = new CalendarModel();
		map.put("Default", newModel);
	}
	
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
	
	/**
	 * Removes a CalendarModel from the dictionary that holds the
	 * different calendars and their names
	 * @param name -- the name of the CalendarModel to be removed
	 */
	public void removeCalendarModel(String name) {
		map.remove(name);
	}
	
	/**
	 * Renames a CalendarModel in the map that keeps track of all of the
	 * different calendars
	 * @param newName -- the new name for the CalendarModel
	 * @param oldName -- the old name of the CalendarModel
	 */
	public void renameCalendarModel(String newName, String oldName) {
		CalendarModel cal = map.get(oldName);
		map.remove(oldName);
		map.put(newName, cal);
	}
	
	/**
	 * Takes a CalendarModel and adds a new event to it
	 * 
	 * @param curModel -- the CalendarModel to add the event to
	 * @param newEvent -- the CalendarEvent to add to the CalendarModel
	 */
	public void addEvent(CalendarModel curModel, CalendarEvent newEvent) {
		curModel.addEvent(newEvent);
	}
	
	/**
	 * Takes a CalendarModel and removes an event from it
	 * 
	 * @param curModel -- the CalendarModel to add the event to
	 * @param newEvent -- the CalendarEvent to add to the CalendarModel
	 */
	public void removeEvent(CalendarModel curModel, CalendarEvent newEvent) {
		curModel.removeEvent(newEvent);
	}
	
	/**
	 * Looks for events within a year for a certain calendar
	 * @param curModel -- the current model
	 * @param year -- the year to get events from
	 * @return the events found in that year
	 */
	public CalendarEvent[] getEventsInYear(CalendarModel curModel, int year) {
		return curModel.getEventsInYear(year);
	}
	
	/**
	 * Looks for events within a month for a certain calendar 
	 *
	 * @param curModel -- the current model
	 * @param year -- the year to get events from
	 * @param month -- the month to get events from
	 * @return the events found in that month
	 */
	public CalendarEvent[] getEventsInMonth(CalendarModel curModel, int year, int month) {
		return curModel.getEventsInMonth(year, month);
	}
	
	/**
	 * Looks for events within a day for a certain calendar 
	 * 
	 * @param curModel -- the current model
	 * @param year -- the year to get events from
	 * @param month -- the month to get events from
	 * @param day -- the day to get events from
	 * @return the events found in that month
	 */
	public CalendarEvent[] getEventsInDay(CalendarModel curModel, int year, int month, int day) {
		return curModel.getEventsInDay(year, month, day);
	}
	
	public CalendarEvent[] getEventsInDay(CalendarModel curModel, LocalDate day) {
		return curModel.getEventsInDay(day);
	}
	
	/**
	 * Looks for events within an hour for a certain calendar 
	 * 
	 * @param curModel -- the current model
	 * @param year -- the year to get events from
	 * @param month -- the month to get events from
	 * @param day -- the day to get events from
	 * @param hour -- the hour to get events from
	 * @return the events found in that hour
	 */
	public CalendarEvent[] getEventsInHour(CalendarModel curModel, int year, int month, int day, int hour) {
		return curModel.getEventsInHour(year, month, day, hour);
	}
	
	public CalendarEvent[] getEventsInHour(CalendarModel curModel, LocalDateTime time) {
		return curModel.getEventsInHour(time);
	}
	
	/**
	 * Gets the list of events in a current time range
	 * 
	 * @param curModel -- the current model
	 * @param before -- the LocalDateTime for the start of the search
	 * @param after -- the LocalDateTime for the end of the search
	 * @return the events found in that range
	 */
	public CalendarEvent[] getEventsInRange(CalendarModel curModel, LocalDateTime before, LocalDateTime after) {
		return curModel.getEventsInRange(before, after);
	}
	
}
