package controller;

/**
 * Indicates that a calendar by the given name already exists.
 *
 * @author Kitty Elliott
 */
public class CalendarAlreadyExistsException extends Exception {

    /**
     * @param name the name of the already extant calendar
     */
    public CalendarAlreadyExistsException(String name) {
        super(String.format("A calendar already exists with the name \"%s\"", name));
    }
}
