package view;

import controller.CalendarController;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class CalendarView extends Application {
    private Stage stage;
    private CalendarController controller;
    private CalendarViewMode month, day, week,
            current;
    private VBox mainColumn;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        controller = new CalendarController();
        month = new MonthView(/* TODO: controller */);
        day = new DayView(/* TODO: controller */);
        week = new WeekView(/* TODO: controller */);

        Menu m = new Menu("View");
        MenuItem monthItem = new MenuItem("Month");
        MenuItem weekItem = new MenuItem("Week");
        MenuItem dayItem = new MenuItem("Day");
        monthItem.setOnAction(e -> switchTo(month));
        weekItem.setOnAction(e -> switchTo(week));
        dayItem.setOnAction(e -> switchTo(day));

        m.getItems().addAll(monthItem, dayItem, weekItem);
        stage.setTitle("Calendar");
        mainColumn = new VBox(
                new MenuBar(m),
                month.getNode()
        );
        current = month;
        stage.setScene(new Scene(mainColumn));
        stage.show();
    }

    private void switchTo(CalendarViewMode target) {
        if (target == current) return;

        target.setDate(current.getDate());

        List<Node> children = mainColumn.getChildren();
        children.remove(children.size() - 1);
        children.add(target.getNode());
        current = target;
        stage.sizeToScene();
    }
}
