package controller;

public class NoSuchCalendarException extends Exception {

    public NoSuchCalendarException(String name) {
        super(String.format("No calendar exists with the name \"%s\"", name));
    }
}
