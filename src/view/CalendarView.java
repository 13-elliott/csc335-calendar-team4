package view;

import controller.CalendarAlreadyExistsException;
import controller.CalendarController;
import controller.NoSuchCalendarException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

/**
 * @author Kitty Elliott
 */
public class CalendarView extends Application {
    private Stage stage;
    private CalendarController controller;
    private CalendarViewMode month, day, week,
            current;
    private VBox mainColumn;

    /**
     * @param stage represents the main application window.
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;

        controller = new CalendarController();
        month = new MonthView(/* TODO: controller */);
        day = new DayView(/* TODO: controller */);
        week = new WeekView(controller);

        current = month;

        mainColumn = new VBox(constructMenus(), current.getNode());

        stage.setTitle("Calendar");
        stage.setScene(new Scene(mainColumn));
        stage.show();
    }

    private MenuBar constructMenus() {
        Menu viewMenu = new Menu("View");
        MenuItem monthItem = new MenuItem("Month");
        MenuItem weekItem = new MenuItem("Week");
        MenuItem dayItem = new MenuItem("Day");
        monthItem.setOnAction(e -> switchTo(month));
        weekItem.setOnAction(e -> switchTo(week));
        dayItem.setOnAction(e -> switchTo(day));
        viewMenu.getItems().addAll(monthItem, dayItem, weekItem);

        Menu createMenu = new Menu("Create");
        MenuItem createEventItem = new MenuItem("New Event");
        createEventItem.setOnAction(this::createEvent);
        MenuItem createCalItem = new MenuItem("New Calendar");
        createCalItem.setOnAction(this::createCalendar);
        createMenu.getItems().addAll(createEventItem, createCalItem);

        return new MenuBar(viewMenu, createMenu);
    }

    /**
     * Switches the current viewing mode to the provided one
     *
     * @param target the view mode to switch to
     */
    private void switchTo(CalendarViewMode target) {
        if (target == current) return; // no-op

        target.setDate(current.getDate());

        List<Node> children = mainColumn.getChildren();
        children.remove(children.size() - 1);
        children.add(target.getNode());
        current = target;
        stage.sizeToScene();
    }

    private void createCalendar(ActionEvent e) {
        TextInputDialog in = new TextInputDialog();
        in.setTitle("Name Calendar");
        in.setHeaderText("Please enter a name for the new calendar");
        // input validation:
        in.getDialogPane().lookupButton(ButtonType.OK)
                .addEventFilter(ActionEvent.ACTION, okEvent -> {
                    // if given an existing calendar name,
                    // show an error and don't close the dialog
                    if (controller.getCalendarNames()
                            .contains(in.getEditor().getText())) {
                        new Alert(Alert.AlertType.ERROR,
                                "A calendar with that name already exists")
                                .showAndWait();
                        okEvent.consume();
                    }
                });
        in.showAndWait().ifPresent(newName -> {
            try {
                controller.createNewCalendar(newName);
            } catch (CalendarAlreadyExistsException ex) {
                // handled by event filter above: should not occur
                ex.printStackTrace();
            }
        });
    }

    private void createEvent(ActionEvent e) {
        EventDialog.newEvent(controller.getCalendarNames())
                .showAndWait()
                .ifPresent(p -> {
                    try {
                        controller.addEvent(p.getKey(), p.getValue());
                        // referesh the current view
                        current.setDate(current.getDate());
                    } catch (NoSuchCalendarException ex) {
                        ex.printStackTrace();
                    }
                });
    }
}
