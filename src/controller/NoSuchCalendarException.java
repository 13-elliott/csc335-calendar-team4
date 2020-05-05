package controller;

/**
 * Indicates that no calendar with the given name exists
 *
 * @author Kitty Elliott
 */
public class NoSuchCalendarException extends Exception {

    /**
     * @param name the name which was used to query for a non-existant calendar
     */
    public NoSuchCalendarException(String name) {
        super(String.format("No calendar exists with the name \"%s\"", name));
    }
}
