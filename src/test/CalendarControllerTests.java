package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import controller.CalendarAlreadyExistsException;
import controller.CalendarController;
import controller.NoSuchCalendarException;
import model.CalendarEvent;
import model.CalendarModel;

public class CalendarControllerTests {
	/**
	 * Tests default calendar addition and getCalendarNames()
	 */
	@Test
	public void testCalendarDefault() {
		CalendarController cont1 = new CalendarController();
		Set<String> set1 = new HashSet<String>();
		set1.add("Default");
		assertEquals(cont1.getCalendarNames(), set1);
	}
	
	/**
	 * Tests createNewCalendar()
	 * @throws CalendarAlreadyExistsException 
	 */
	@Test
	public void testCreateCalendar() throws CalendarAlreadyExistsException {
		CalendarController cont1 = new CalendarController();
		Set<String> set1 = new HashSet<String>();
		set1.add("Default");
		cont1.createNewCalendar("calendar1");
		set1.add("calendar1");
		assertEquals(cont1.getCalendarNames(), set1);
		assertThrows(CalendarAlreadyExistsException.class,
		           () -> {
		        	   cont1.createNewCalendar("calendar1");
		           });
	}
	
	/**
	 * Tests deleteCalendar()
	 * @throws CalendarAlreadyExistsException 
	 */
	@Test
	public void testDeleteCal() throws CalendarAlreadyExistsException {
		CalendarController cont1 = new CalendarController();
		cont1.createNewCalendar("cal1");
		assertTrue(cont1.deleteCalendar("cal1"));
		assertFalse(cont1.deleteCalendar("cal2"));
	}
	
	/**
	 * Tests renameCalendar()
	 * @throws NoSuchCalendarException 
	 */
	@Test
	public void testRenameCal() throws CalendarAlreadyExistsException, NoSuchCalendarException {
		CalendarController cont1 = new CalendarController();
		cont1.createNewCalendar("cal1");
		assertThrows(NoSuchCalendarException.class,
		           () -> {
		        	   cont1.renameCalendar("hey", "not a real name");
		           });
		assertThrows(CalendarAlreadyExistsException.class,
		           () -> {
		        	   cont1.renameCalendar("cal1", "Default");
		           });
		Set<String> set1 = new HashSet<String>();
		set1.add("Default");
		set1.add("newCal");
		cont1.renameCalendar("newCal", "cal1");
		assertEquals(set1,cont1.getCalendarNames());
		
	}
	
	/**
	 * Tests addEvent()
	 * @throws NoSuchCalendarException 
	 */
	@Test
	public void testAddEvent() throws NoSuchCalendarException {
		CalendarController cont1 = new CalendarController();
		LocalDateTime time = LocalDateTime.of(2020, Month.APRIL, 1, 2,30,20,40);
		CalendarEvent event = new CalendarEvent("This is an event", time);
		assertThrows(NoSuchCalendarException.class,
		           () -> {
		       			cont1.addEvent("not a calendar", event);
		           });
		cont1.addEvent("Default", event);
	}
	
	/**
	 * Tests removeEvent()
	 * @throws NoSuchCalendarException 
	 */
	@Test
	public void testRemoveEvent() throws NoSuchCalendarException {
		CalendarController cont1 = new CalendarController();
		LocalDateTime time = LocalDateTime.of(2020, Month.APRIL, 1, 2,30,20,40);
		CalendarEvent event = new CalendarEvent("This is an event", time);
		cont1.addEvent("Default", event);
		assertThrows(NoSuchCalendarException.class,
		           () -> {
		       			cont1.removeEvent("not a calendar", event);
		           });
		cont1.removeEvent("Default", event);
	}
	
	/**
	 * Tests getEventsInYear()
	 * @throws NoSuchCalendarException 
	 */
	@Test
	public void testGetEventsInYear() throws NoSuchCalendarException {
		CalendarController cont1 = new CalendarController();
		LocalDateTime time1 = LocalDateTime.of(2020, Month.APRIL, 1, 2,30,20,40);
		LocalDateTime time2 = LocalDateTime.of(2020, Month.APRIL, 2, 2,30,20,40);
		LocalDateTime time3 = LocalDateTime.of(2020, Month.APRIL, 3, 2,30,20,40);
		
		CalendarEvent event1 = new CalendarEvent("event1", time1);
		CalendarEvent event2 = new CalendarEvent("event2", time2);
		CalendarEvent event3 = new CalendarEvent("event3", time3);
		CalendarEvent[] events = {event1,event2,event3};
		
		cont1.addEvent("Default", event1);
		cont1.addEvent("Default", event2);
		cont1.addEvent("Default", event3);
		
		assertThrows(NoSuchCalendarException.class,
		           () -> {
		       			cont1.getEventsInYear("not a calendar", 2020);
		           });
		
		assertTrue(events[0].equals(cont1.getEventsInYear("Default", 2020)[0]));

	}
	
	/**
	 * Tests getEventsInMonth()
	 * @throws NoSuchCalendarException 
	 */
	@Test
	public void testGetEventsInMonth() throws NoSuchCalendarException {
		CalendarController cont1 = new CalendarController();
		LocalDateTime time1 = LocalDateTime.of(2020, Month.APRIL, 1, 2,30,20,40);
		LocalDateTime time2 = LocalDateTime.of(2020, Month.MARCH, 2, 2,30,20,40);
		LocalDateTime time3 = LocalDateTime.of(2020, Month.FEBRUARY, 3, 2,30,20,40);
		
		CalendarEvent event1 = new CalendarEvent("event1", time1);
		CalendarEvent event2 = new CalendarEvent("event2", time2);
		CalendarEvent event3 = new CalendarEvent("event3", time3);
		CalendarEvent[] events = {event1};
		
		cont1.addEvent("Default", event1);
		cont1.addEvent("Default", event2);
		cont1.addEvent("Default", event3);
		
		assertThrows(NoSuchCalendarException.class,
		           () -> {
		       			cont1.getEventsInMonth("not a calendar", 2020, 4);
		           });
		
		assertTrue(events[0].equals(cont1.getEventsInMonth("Default", 2020,4)[0]));
	}
	
	/**
	 * Tests getEventsInDay()
	 * @throws NoSuchCalendarException 
	 */
	@Test
	public void testGetEventsInDay() throws NoSuchCalendarException {
		CalendarController cont1 = new CalendarController();
		LocalDateTime time1 = LocalDateTime.of(2020, Month.APRIL, 1, 2,30,20,40);
		LocalDateTime time2 = LocalDateTime.of(2020, Month.APRIL, 1, 4,30,20,40);
		LocalDateTime time3 = LocalDateTime.of(2020, Month.FEBRUARY, 3, 2,30,20,40);
		
		CalendarEvent event1 = new CalendarEvent("event1", time1);
		CalendarEvent event2 = new CalendarEvent("event2", time2);
		CalendarEvent event3 = new CalendarEvent("event3", time3);
		CalendarEvent[] events = {event1,event2};
		
		cont1.addEvent("Default", event1);
		cont1.addEvent("Default", event2);
		cont1.addEvent("Default", event3);
		
		LocalDate x = LocalDate.of(2020, 4, 1);
		
		assertThrows(NoSuchCalendarException.class,
		           () -> {
		       			cont1.getEventsInDay("not a calendar", x);
		           });
		
		assertTrue(events[0].equals(cont1.getEventsInDay("Default", x)[0]));
		assertTrue(events[1].equals(cont1.getEventsInDay("Default", x)[1]));

	}
	
	/**
	 * Tests getEventsInHour()
	 * @throws NoSuchCalendarException 
	 */
	@Test
	public void testGetEventsInHour() throws NoSuchCalendarException {
		CalendarController cont1 = new CalendarController();
		LocalDateTime time1 = LocalDateTime.of(2020, Month.APRIL, 1, 2,30,20,40);
		LocalDateTime time2 = LocalDateTime.of(2020, Month.APRIL, 1, 4,30,20,40);
		LocalDateTime time3 = LocalDateTime.of(2020, Month.FEBRUARY, 3, 2,30,20,40);
		
		CalendarEvent event1 = new CalendarEvent("event1", time1);
		CalendarEvent event2 = new CalendarEvent("event2", time2);
		CalendarEvent event3 = new CalendarEvent("event3", time3);
		CalendarEvent[] events = {event1};
		
		cont1.addEvent("Default", event1);
		cont1.addEvent("Default", event2);
		cont1.addEvent("Default", event3);
		
		LocalDateTime x = LocalDateTime.of(2020, Month.APRIL, 1, 2, 30,20,40);
		
		assertThrows(NoSuchCalendarException.class,
		           () -> {
		       			cont1.getEventsInHour("not a calendar", x);
		           });
		
		assertTrue(events[0].equals(cont1.getEventsInHour("Default", x)[0]));
	}
	
	/**
	 * Tests getEventsInRange()
	 * @throws NoSuchCalendarException 
	 */
	@Test
	public void testGetEventsInRange() throws NoSuchCalendarException {
		CalendarController cont1 = new CalendarController();
		LocalDateTime time1 = LocalDateTime.of(2020, Month.APRIL, 1, 2,30,20,40);
		LocalDateTime time2 = LocalDateTime.of(2020, Month.APRIL, 1, 4,30,20,40);
		LocalDateTime time3 = LocalDateTime.of(2020, Month.FEBRUARY, 3, 2,30,20,40);
		CalendarModel model = new CalendarModel();
		
		CalendarEvent event1 = new CalendarEvent("event1", time1);
		CalendarEvent event2 = new CalendarEvent("event2", time2);
		CalendarEvent event3 = new CalendarEvent("event3", time3);
		model.markModified(event3);

		CalendarEvent[] events = {event1,event2};
		
		cont1.addEvent("Default", event1);
		cont1.addEvent("Default", event2);
		cont1.addEvent("Default", event3);
		
		LocalDateTime x = LocalDateTime.of(2020, Month.APRIL, 1, 2, 0,0,0);
		LocalDateTime y = LocalDateTime.of(2020, Month.APRIL, 1, 5, 0,0,0);
		
		assertThrows(NoSuchCalendarException.class,
		           () -> {
		       			cont1.getEventsInRange("not a calendar", x,y);
		           });
		
		assertTrue(events[0].equals(cont1.getEventsInRange("Default", x,y)[0]));
		assertTrue(events[1].equals(cont1.getEventsInRange("Default", x,y)[1]));
	}
}

