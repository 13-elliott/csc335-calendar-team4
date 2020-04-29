package controller;

public class CalendarAlreadyExistsException extends Exception {

    public CalendarAlreadyExistsException(String name) {
        super(String.format("A calendar already exists with the name \"%s\"", name));
    }
}
