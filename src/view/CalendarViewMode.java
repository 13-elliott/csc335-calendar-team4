package view;

import controller.NoSuchCalendarException;
import javafx.scene.Node;

import java.time.LocalDate;
import java.util.Set;

/**
 * interface for implementing a new calendar viewing mode
 *
 * @author Kitty Elliott
 */
public interface CalendarViewMode {

    /**
     * get the parent node for this view mode
     *
     * @return the parent node for this view mode
     */
    Node getNode();

    /**
     * Change the range of time being shown by this view to
     * a range of time which include the given DateTime.
     *
     * @param date a point in time which this
     */
    void setDate(LocalDate date);

    /**
     * get the first date within the range of time being shown
     *
     * @return the first date within the range of time being shown
     */
    LocalDate getDate();

    /**
     * Set which calendars' events will be displayed.
     *
     * @param calNames a set of names of calendars to display
     * @throws NoSuchCalendarException if the given set is not a
     *                                 subset of {@link controller.CalendarController#getCalendarNames}
     */
    void setVisibleCalendars(Set<String> calNames) throws NoSuchCalendarException;
}
