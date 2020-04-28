package view;

import javafx.scene.Node;
import javafx.scene.control.Label;

import java.time.LocalDate;

public class WeekView implements CalendarViewMode {
    @Override
    public Node getNode() {
        return new Label("Test: WeekView");
    }

    @Override
    public LocalDate getDate() {
        return LocalDate.now();
    }

    @Override
    public void setDate(LocalDate date) {

    }
}
