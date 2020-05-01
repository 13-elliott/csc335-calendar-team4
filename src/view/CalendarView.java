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

import java.util.*;

/**
 * @author Kitty Elliott
 */
public class CalendarView extends Application {
    private Stage stage;
    private CalendarController controller;
    private CalendarViewMode month, day, week,
            current;
    private Set<String> currentlyVisibleCals;
    private VBox mainColumn;

    /**
     * @param stage represents the main application window.
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;

        controller = new CalendarController();
        month = new MonthView(controller);
        currentlyVisibleCals = controller.getCalendarNames();
        day = new DayView(/* TODO: controller */);
        week = new WeekView(/* TODO: controller */);

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

        Menu changeMenu = new Menu("Change");
        MenuItem visibleCalsMenuItem = new MenuItem("Visible Calendars");
        visibleCalsMenuItem.setOnAction(this::changeVisibleCals);
        MenuItem renameCalItem = new MenuItem("Rename a calendar");
        renameCalItem.setOnAction(this::renameCalendar);
        MenuItem deleteCalItem = new MenuItem("Delete a calendar");
        deleteCalItem.setOnAction(this::deleteCalendar);
        changeMenu.getItems().addAll(visibleCalsMenuItem, renameCalItem, deleteCalItem);

        return new MenuBar(viewMenu, createMenu, changeMenu);
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

    private Optional<String> selectOneCalendar(String prompt) {
        ChoiceDialog<String> cd = new ChoiceDialog<>();
        cd.setTitle("Select Calendar");
        cd.getDialogPane().setHeaderText(prompt);
        cd.getItems().addAll(controller.getCalendarNames());
        cd.getDialogPane().lookupButton(ButtonType.OK).addEventFilter(ActionEvent.ACTION,
                okEvent -> {
                    if (cd.getSelectedItem() == null) {
                        okEvent.consume();
                        new Alert(Alert.AlertType.ERROR, "You must make a selection")
                                .showAndWait();
                    }
                });
        return cd.showAndWait();
    }

    private Optional<String> getNewCalendarName(String prompt) {
        TextInputDialog in = new TextInputDialog();
        in.setTitle("Name Calendar");
        in.setHeaderText(prompt);
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
        return in.showAndWait();
    }

    private void createCalendar(ActionEvent e) {
        getNewCalendarName("Please enter a name for the new calendar").ifPresent(newName -> {
            try {
                controller.createNewCalendar(newName);
                currentlyVisibleCals.add(newName);
                current.setVisibleCalendars(currentlyVisibleCals);
            } catch (CalendarAlreadyExistsException | NoSuchCalendarException ex) {
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

    private void changeVisibleCals(ActionEvent actionEvent) {
        Dialog<Set<String>> multipleSelectDialog = new Dialog<>();
        {   // setup the dialog
            ListView<String> lView = new ListView<>();
            MultipleSelectionModel<String> selMod = lView.getSelectionModel();
            selMod.setSelectionMode(SelectionMode.MULTIPLE);
            for (String name : controller.getCalendarNames()) {
                lView.getItems().add(name);
                if (currentlyVisibleCals.contains(name))
                    selMod.select(name);
            }
            multipleSelectDialog.setResultConverter(bt -> {
                if (bt.equals(ButtonType.OK))
                    return new HashSet<>(selMod.getSelectedItems().filtered(Objects::nonNull));
                else return null;
            });
            DialogPane dPane = multipleSelectDialog.getDialogPane();
            dPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            dPane.setContent(lView);
        }

        multipleSelectDialog.showAndWait()
                .ifPresent(newSet -> {
                    currentlyVisibleCals = newSet;
                    try {
                        current.setVisibleCalendars(newSet);
                    } catch (NoSuchCalendarException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void renameCalendar(ActionEvent actionEvent) {
        selectOneCalendar("Select a calendar to rename")
                .ifPresent(oldName ->
                        getNewCalendarName(String.format("Please enter a new name for \"%s\"", oldName))
                                .ifPresent(newName -> {
                                    try {
                                        controller.renameCalendar(newName, oldName);
                                        currentlyVisibleCals.remove(oldName);
                                        currentlyVisibleCals.add(newName);
                                        current.setVisibleCalendars(currentlyVisibleCals);
                                    } catch (CalendarAlreadyExistsException | NoSuchCalendarException e) {
                                        e.printStackTrace();
                                    }
                                })
                );
    }

    private void deleteCalendar(ActionEvent actionEvent) {
        if (controller.getCalendarNames().size() <= 1) {
            new Alert(Alert.AlertType.ERROR, "Cannot delete the only remaining calendar")
                    .showAndWait();
            return;
        }
        selectOneCalendar("Select a calendar to delete").ifPresent(toDelete -> {
            controller.deleteCalendar(toDelete);
            currentlyVisibleCals.remove(toDelete);
            try {
                current.setVisibleCalendars(currentlyVisibleCals);
            } catch (NoSuchCalendarException e) {
                e.printStackTrace();
            }
        });
    }
}
