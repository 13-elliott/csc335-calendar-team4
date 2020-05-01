package view;

import controller.NoSuchCalendarException;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.util.Set;

public class DayView implements CalendarViewMode {

    @Override
    public Node getNode() {
        return new Label("Test: DayView");
    }

    @Override
    public LocalDate getDate() {
        return LocalDate.now();
    }

    @Override
    public void setVisibleCalendars(Set<String> calNames) throws NoSuchCalendarException {
        // TODO
    }

    @Override
    public void setDate(LocalDate date) {

    }
}
