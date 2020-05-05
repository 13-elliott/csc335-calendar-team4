package controller;

import model.CalendarEvent;
import model.CalendarModel;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author mollyopheim
 */
public class CalendarController {
	private final HashMap<String, CalendarModel> map;
	public final File calFile;


	/**
	 * Initializes the CalendarController to have one default CalendarModel
	 * in the map that keeps track of the CalendarModel objects.
	 * Loads previous CalendarModel's and their respective events
	 * from the calendarFile if applicable.
	 */
	public CalendarController(File calFile) throws IOException {
		if (calFile == null) {
			throw new IllegalArgumentException("given File must not be null");
		}
		this.calFile = calFile;
		if (calFile.exists()) {
			map = loadCalendars();
		} else {
			map = new HashMap<>();
			map.put("Default", new CalendarModel());
		}
		saveCalendars();
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private HashMap<String, CalendarModel> loadCalendars() throws IOException {
		HashMap loaded;
		try {
			FileInputStream fIn = new FileInputStream(calFile);
			ObjectInputStream objIn = new ObjectInputStream(fIn);
			loaded = (HashMap) objIn.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new IOException(String.format("Error loading the calendar file at \"%s\"\n",
					calFile.getAbsolutePath()));
		}
		Set<Map.Entry> loadedEntries = loaded.entrySet();
		if (!loadedEntries.isEmpty()) {
			for (Map.Entry e : loadedEntries) {
				if (!(e.getKey() instanceof String && e.getValue() instanceof CalendarModel)) {
					throw new IOException(String.format("Calendar file at \"%s\" is corrupted.",
							calFile.getAbsolutePath()));
				}
			}
		} else {
			loaded.put("Default", new CalendarModel());
		}
		return (HashMap<String, CalendarModel>) loaded;
	}

	/**
	 * get a set containing the names of all the calendars.
	 *
	 * @return a set containing the names of all the calendars
	 */
	public Set<String> getCalendarNames() {
		return new HashSet<>(map.keySet());
	}

	/**
	 * creates and adds a new calendar with the given name
	 *
	 * @param name -- the name of the calendar
	 * @throws CalendarAlreadyExistsException if a calendar with the given name already exists
	 */
	public void createNewCalendar(String name) throws CalendarAlreadyExistsException {
		if (map.containsKey(name)) {
			throw new CalendarAlreadyExistsException(name);
		} else {
			map.put(name, new CalendarModel());
		}
	}

	/**
	 * Removes a CalendarModel from the dictionary that holds the
	 * different calendars and their names
	 *
	 * @param name -- the name of the CalendarModel to be removed
	 */
	public boolean deleteCalendar(String name) {
		return map.remove(name) != null;
	}

	/**
	 * Renames a CalendarModel in the map that keeps track of all of the
	 * different calendars
	 *
	 * @param newName -- the new name of the calendar
	 * @param oldName -- the old name of the calendar
	 * @throws CalendarAlreadyExistsException if a calendar with the given new name already exists
	 * @throws NoSuchCalendarException        if no calendar with the given old name exists
	 */
	public void renameCalendar(String newName, String oldName)
			throws CalendarAlreadyExistsException, NoSuchCalendarException {
		if (!map.containsKey(oldName)) {
			throw new NoSuchCalendarException(oldName);
		} else if (map.containsKey(newName)) {
			throw new CalendarAlreadyExistsException(newName);
		} else {
			map.put(newName, map.remove(oldName));
		}
	}

	/**
	 * Takes a CalendarModel and adds a new event to it
	 *
	 * @param calName  -- name of the calendar
	 * @param newEvent -- the CalendarEvent to add to the CalendarModel
	 * @throws NoSuchCalendarException if there is no calendar with the given name
	 */
	public void addEvent(String calName, CalendarEvent newEvent) throws NoSuchCalendarException {
		if (map.containsKey(calName)) {
			map.get(calName).addEvent(newEvent);
		} else {
			throw new NoSuchCalendarException(calName);
		}
	}

	/**
	 * Takes a CalendarModel and removes an event from it
	 *
	 * @param calName  -- name of the calendar
	 * @param newEvent -- the CalendarEvent to add to the CalendarModel
	 * @throws NoSuchCalendarException if there is no calendar with the given name
	 */
	public void removeEvent(String calName, CalendarEvent newEvent) throws NoSuchCalendarException {
		if (map.containsKey(calName)) {
			map.get(calName).removeEvent(newEvent);
		} else {
			throw new NoSuchCalendarException(calName);
		}
	}

	/**
	 * Looks for events within a year for a certain calendar
	 *
	 * @param calName -- name of the calendar
	 * @param year    -- the year to get events from
	 * @return the events found in that year
	 * @throws NoSuchCalendarException if there is no calendar with the given name
	 */
	public CalendarEvent[] getEventsInYear(String calName, int year) throws NoSuchCalendarException {
		if (map.containsKey(calName)) {
			return map.get(calName).getEventsInYear(year);
		} else {
			throw new NoSuchCalendarException(calName);
		}
	}

	/**
	 * Looks for events within a month for a certain calendar
	 *
	 * @param calName -- name of the calendar
	 * @param year    -- the year to get events from
	 * @param month   -- the month to get events from
	 * @return the events found in that month
	 * @throws NoSuchCalendarException if there is no calendar with the given name
	 */
	public CalendarEvent[] getEventsInMonth(String calName, int year, int month)
			throws NoSuchCalendarException {
		if (map.containsKey(calName)) {
			return map.get(calName).getEventsInMonth(year, month);
		} else {
			throw new NoSuchCalendarException(calName);
		}
	}

	/**
	 * Looks for events within a month for a certain calendar
	 *
	 * @param calName -- name of the calendar
	 * @param day     the date to query for events
	 * @return the events found on that day
	 * @throws NoSuchCalendarException if there is no calendar with the given name
	 */
	public CalendarEvent[] getEventsInDay(String calName, LocalDate day)
			throws NoSuchCalendarException {
		if (map.containsKey(calName)) {
			return map.get(calName).getEventsInDay(day);
		} else {
			throw new NoSuchCalendarException(calName);
		}
	}

	/**
	 * Looks for events within a month for a certain calendar
	 *
	 * @param calName -- name of the calendar
	 * @param time     the hour to query for events
	 * @return the events found on that day
	 * @throws NoSuchCalendarException if there is no calendar with the given name
	 */
	public CalendarEvent[] getEventsInHour(String calName, LocalDateTime time)
			throws NoSuchCalendarException {
		if (map.containsKey(calName)) {
			return map.get(calName).getEventsInHour(time);
		} else {
			throw new NoSuchCalendarException(calName);
		}
	}

	/**
	 * Gets the list of events in a current time range
	 *
	 * @param calName -- name of the calendar
	 * @param before  -- the LocalDateTime for the start of the search
	 * @param after   -- the LocalDateTime for the end of the search
	 * @return the events found in that range
	 */
	public CalendarEvent[] getEventsInRange(String calName, LocalDateTime before, LocalDateTime after)
			throws NoSuchCalendarException {
		if (map.containsKey(calName)) {
			return map.get(calName).getEventsInRange(before, after);
		} else {
			throw new NoSuchCalendarException(calName);
		}
	}

	/**
	 * Saves the CalendarModel objects and their respective CalendarEvents
	 * to the calendar file.
	 * This is done by converting each aspect of the CalendarEvents to a
	 * String and storing it on a separate line in the calendarFile. The
	 * end of a CalendarEvent is marked by a line with a single dash only.
	 */
	public void saveCalendars() {
		BufferedWriter writer = null;
		try {
			FileOutputStream fOut = new FileOutputStream(calFile);
			ObjectOutputStream objOut = new ObjectOutputStream(fOut);
			objOut.writeObject(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
