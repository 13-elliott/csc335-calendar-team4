package view;

import javafx.scene.Node;
import javafx.scene.control.Label;

import java.time.LocalDate;

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
    public void setDate(LocalDate date) {

    }
}
