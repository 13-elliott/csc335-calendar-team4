package view;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import model.CalendarEvent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

/**
 * a modal dialog which can produce new or edit existing CalendarEvent objects
 * instantiation of/changes to CalendarEvents only happens when the dialog is
 * invoked via the blocking {@link Dialog#showAndWait()} call. If the user closes
 * the window by any means other than the OK button, no object is created and all
 * changes are discarded.
 *
 * @author Kitty Elliott
 */
public class EventDialog extends Dialog<Pair<String, CalendarEvent>> {

    private static final int
            MAX_YEAR_LEN = 4,
            MAX_NOTE_AREA_WID = 375, MAX_NOTE_AREA_HEI = 100;

    private final CalendarEvent event;
    private final TextField titleEntryField, locationEntryField, yearField;
    private final TextArea notesEntryArea;
    private final ChoiceBox<String>
            daySelector, monthSelector,
            startHourSelector, startMinuteSelector,
            endHourSelector, endMinuteSelector,
            calendarSelector;
    private LocalDate date;
    private LocalTime start, end;

    /**
     * Constructor.
     * If given a CalendarEvent object, then that event will be loaded, and
     * will be updated with the information the user inputs into this EventDialog
     * if the user clicks OK. This can be determined if the return value of
     * {@link #showAndWait()} {@link java.util.Optional#isPresent() is present}.
     * <p>
     * If given a Date object, the initial date and start/end times
     * will be derived from that Date, and {@link #showAndWait()}
     * will {@link java.util.Optional optionally} return a {@link Pair}
     * with the name of the selected calendar as its key and the CalendarEvent
     * as its value.
     * If given null, then the current date and time will be used to set the
     * initial values.
     *
     * @param seed              either a {@link CalendarEvent}, {@link LocalDate}, or null
     * @param selectedCalendar  the name of a calendar which will be the initial selection.
     *                          Must be a member of possibleCalendars, or else null, in which
     *                          case the first name from the sorted members of possibleCalendars
     *                          will be the initial calendar selection.
     * @param possibleCalendars a set of calendar names to choose from.
     * @see #getResult(ButtonType)
     */
    private EventDialog(Object seed, String selectedCalendar, Set<String> possibleCalendars) {
        super();

        if (seed instanceof CalendarEvent) {
            event = (CalendarEvent) seed;
            date = event.getDate();
            start = event.getStartTime();
            end = event.getEndTime();
        } else if (seed instanceof LocalDate) {
            event = null;
            date = (LocalDate) seed;
            start = LocalTime.now();
            end = LocalTime.now();
        } else {
            // initial values will be current date+time
            event = null;
            date = LocalDate.now();
            start = LocalTime.now();
            end = LocalTime.now();
        }

        titleEntryField = new TextField();
        locationEntryField = new TextField();
        notesEntryArea = new TextArea();

        yearField = new TextField();
        yearField.setTextFormatter(new TextFormatter<>(EventDialog::yearFormatter));
        yearField.setOnKeyPressed(this::yearKeyHandler);

        daySelector = new ChoiceBox<>();
        monthSelector = new ChoiceBox<>();
        startHourSelector = new ChoiceBox<>();
        startMinuteSelector = new ChoiceBox<>();
        endHourSelector = new ChoiceBox<>();
        endMinuteSelector = new ChoiceBox<>();

        calendarSelector = new ChoiceBox<>();
        possibleCalendars.stream().sorted()
                .forEachOrdered(calendarSelector.getItems()::add);
        if (selectedCalendar == null) {
            calendarSelector.getSelectionModel().selectFirst();
        } else {
            calendarSelector.getSelectionModel().select(selectedCalendar);
        }

        setupTimeElements();
        fillNonTimeElements();
        this.setResultConverter(this::getResult);
        this.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        this.getDialogPane().lookupButton(ButtonType.OK)
                // show error popup and don't close the dialog
                // if OK was clicked while year or title is blank
                .addEventFilter(ActionEvent.ACTION, e -> {
                    if (titleEntryField.getText().trim().isEmpty()
                            || yearField.getText().isEmpty()) {
                        e.consume();
                        new Alert(Alert.AlertType.ERROR, "Neither Title nor Year may be blank")
                                .showAndWait();
                    }
                    if (end.isBefore(start)) {
                        e.consume();
                        new Alert(Alert.AlertType.ERROR, "End Time must not be before Start Time")
                                .showAndWait();
                    }
                });
        constructGUI();
    }

    /**
     * Create a new instance of this class to edit the given event.
     *
     * @param event             the event to be edited. must not be null.
     * @param selectedCalendar  the name of the calendar the given event belongs to
     * @param possibleCalendars a set of calendar names that the event could belong to
     * @return the EventDialog object which will edit the event when
     * called via the blocking {@link #showAndWait()} method
     * @throws IllegalArgumentException if any argument is null,
     *                                  or selectedCalendar is not a member of possibleCalendars
     */
    public static EventDialog editEvent(
            CalendarEvent event,
            String selectedCalendar,
            Set<String> possibleCalendars) {
        final String message;
        if (event == null) {
            message = "CalendarEvent to be edited must not be null";
        } else if (selectedCalendar == null) {
            message = "selectedCalendar must not be null";
        } else if (possibleCalendars == null || possibleCalendars.isEmpty()) {
            message = "The set of possible calendars must not be null or empty";
        } else if (!possibleCalendars.contains(selectedCalendar)) {
            message = String.format(
                    "Given a calendar name which was not in the set of possible calendars: %s",
                    selectedCalendar);
        } else {
            return new EventDialog(event, selectedCalendar, possibleCalendars);
        }
        throw new IllegalArgumentException(message);
    }

    /**
     * create a new instance of this class to create a new CalendarEvent.
     *
     * @param possibleCalendars a set of calendar names which the event could be assigned to
     * @return the EventDialog object which will return the new event when called via the
     * blocking {@link #showAndWait()} method.
     */
    public static EventDialog newEvent(Set<String> possibleCalendars) {
        if (possibleCalendars == null || possibleCalendars.isEmpty()) {
            throw new IllegalArgumentException("The set of possible calendars must not be null or empty");
        }
        return new EventDialog(null, null, possibleCalendars);
    }

    /**
     * create a new instance of this class to create a new CalendarEvent.
     *
     * @param dateTime          the date and time from which the initial date and time values
     *                          of the EventDialog will be derived.
     * @param possibleCalendars a set of calendar names which the event could be assigned to
     * @return the EventDialog object which will return the new event when called via the
     * blocking {@link #showAndWait()} method.
     */
    public static EventDialog newEventAt(LocalDate dateTime, Set<String> possibleCalendars) {
        return new EventDialog(dateTime, null, possibleCalendars);
    }

    /**
     * intercepts changes to the yearField TextField.
     * If the change contains any non-digit characters, those are filtered out.
     *
     * @param chg represents the change
     * @return the possibly altered
     */
    private static TextFormatter.Change yearFormatter(TextFormatter.Change chg) {
        if ((chg.isAdded() || chg.isReplaced())) {
            String chgText = chg.getText();
            StringBuilder onlyDigits = new StringBuilder();
            // filter out all non-digit chars,
            chgText.chars().filter(Character::isDigit)
                    // pushing the remaining chars into the string builder
                    .forEach(i -> onlyDigits.append((char) i));
            if (onlyDigits.length() != chgText.length()) {
                // only update the text if anything was filtered out
                chg.setText(onlyDigits.toString());
            }
        }
        String newText = chg.getControlNewText();
        if (newText.isEmpty()) {
            chg.setText("0");
        } else if (newText.length() > MAX_YEAR_LEN) {
            // reject changes that would put the text length over the maximum
            return null;
        }
        return chg;
    }

    /**
     * key event handler for yearField.
     * Increments the year when up arrow is pressed.
     * Decrements to a lower bound of zero if down arrow is pressed.
     *
     * @param e the key event
     */
    private void yearKeyHandler(KeyEvent e) {
        int increment;
        switch (e.getCode()) {
            case KP_UP:
            case UP:
                increment = 1;
                break;
            case KP_DOWN:
            case DOWN:
                increment = -1;
                break;
            default:
                return;
        }
        int newYear = Integer.parseInt(yearField.getText()) + increment;
        if (newYear >= 0) {
            yearField.setText(String.valueOf(newYear));
        }
    }

    /**
     * put together the scene graph for this object's DialogPane
     */
    private void constructGUI() {
        final BorderPane titleBP, calBP, dateBP, startBP, endBP, locationBP;
        final HBox timeHB;

        titleBP = new BorderPane();
        titleBP.setLeft(new Label("Title: "));
        titleBP.setCenter(titleEntryField);

        calBP = new BorderPane();
        calBP.setLeft(new Label("Calendar: "));
        calBP.setCenter(calendarSelector);

        dateBP = new BorderPane();
        dateBP.setLeft(new Label("Date: "));
        HBox dateHB = new HBox();
        dateHB.setAlignment(Pos.CENTER);
        dateHB.getChildren().addAll(
                monthSelector, daySelector, yearField
        );
        dateBP.setCenter(dateHB);
        yearField.setPromptText("Year");

        // start and end times
        final String timeSeparator = ":";
        startBP = new BorderPane();
        startBP.setLeft(new Label("Start Time: "));
        startBP.setLeft(new Label("Start Time: "));
        startBP.setCenter(new HBox(startHourSelector, new Label(timeSeparator), startMinuteSelector));
        ((HBox) startBP.getCenter()).setAlignment(Pos.CENTER);
        endBP = new BorderPane();
        endBP.setLeft(new Label("End Time: "));
        endBP.setCenter(new HBox(endHourSelector, new Label(timeSeparator), endMinuteSelector));
        ((HBox) endBP.getCenter()).setAlignment(Pos.CENTER);
        timeHB = new HBox(startBP, endBP);

        locationBP = new BorderPane();
        locationBP.setLeft(new Label("Location: "));
        locationBP.setCenter(locationEntryField);

        notesEntryArea.setPromptText("Notes");
        notesEntryArea.setMaxSize(MAX_NOTE_AREA_WID, MAX_NOTE_AREA_HEI);

        VBox mainColumn = new VBox(titleBP, calBP, dateBP, timeHB, locationBP, notesEntryArea);
        mainColumn.setAlignment(Pos.TOP_CENTER);
        this.setTitle("Event Editor");
        this.getDialogPane().setContent(mainColumn);
    }

    /**
     * set starting values for the contents of non-time-related graphical elements.
     * If the CalendarEvent is not null, its title, location, and notes will be loaded.
     */
    private void fillNonTimeElements() {
        if (event == null) {
            return;
        }
        titleEntryField.setText(event.getTitle());

        String location = event.getLocation();
        if (location != null) {
            locationEntryField.setText(location);
        }
        String notes = event.getNotes();
        if (notes != null) {
            notesEntryArea.setText(notes);
        }
    }

    /**
     * initialize values for time-related graphical elements
     */
    private void setupTimeElements() {
        final int numDays = date.lengthOfMonth();
        for (int i = 0; i < 60; i++) {
            String formatted = String.format("%02d", i);
            String unformatted = String.valueOf(i);
            if (i < 24) {  // hour
                startHourSelector.getItems().add(formatted);
                endHourSelector.getItems().add(formatted);
            }
            if (1 <= i) {
                if (i <= 12) // month
                    monthSelector.getItems().add(unformatted);
                if (i <= numDays) // day
                    daySelector.getItems().add(unformatted);
            }
            // minute
            startMinuteSelector.getItems().add(formatted);
            endMinuteSelector.getItems().add(formatted);
        }

        yearField.setText(String.valueOf(date.getYear()));
        // - 1 to account for SelectionModel being zero-indexed
        monthSelector.getSelectionModel().select(date.getMonthValue() - 1);
        daySelector.getSelectionModel().select(date.getDayOfMonth() - 1);
        yearField.textProperty().addListener((a, b, newVal) -> {
            updateNumDaysInMonth();
            date = date.withYear(Integer.parseInt(newVal));
        });
        monthSelector.getSelectionModel().selectedIndexProperty()
                .addListener((a, b, newVal) -> {
                    updateNumDaysInMonth();
                    // + 1 to account for SelectionModel being zero-indexed
                    date = date.withMonth(newVal.intValue() + 1);
                });
        daySelector.getSelectionModel().selectedIndexProperty().addListener(
                // + 1 to account for SelectionModel being zero-indexed
                (a, b, newVal) -> date = date.withDayOfMonth(newVal.intValue() + 1)
        );

        startHourSelector.getSelectionModel().select(start.getHour());
        startMinuteSelector.getSelectionModel().select(start.getMinute());
        startHourSelector.getSelectionModel().selectedIndexProperty()
                .addListener((a, b, newVal) -> start = start.withHour(newVal.intValue()));
        startMinuteSelector.getSelectionModel().selectedIndexProperty()
                .addListener((a, b, newVal) -> start = start.withMinute(newVal.intValue()));

        endHourSelector.getSelectionModel().select(end.getHour());
        endMinuteSelector.getSelectionModel().select(end.getMinute());
        endHourSelector.getSelectionModel().selectedIndexProperty()
                .addListener((a, b, newVal) -> end = end.withHour(newVal.intValue()));
        endMinuteSelector.getSelectionModel().selectedIndexProperty()
                .addListener((a, b, newVal) -> end = end.withMinute(newVal.intValue()));
    }

    /**
     * called when the month or year fields are updated. updates the number of days
     * available for selection in the monthSelector ChoiceBox.
     */
    private void updateNumDaysInMonth() {
        // change range of available day choices based on selected month
        final int numDays = LocalDate.of(
                Integer.parseInt(yearField.getText()),
                // account for zero-indexed selection model
                monthSelector.getSelectionModel().getSelectedIndex() + 1,
                1 // we only care about the year and month
        ).lengthOfMonth();
        if (daySelector.getSelectionModel().getSelectedIndex() >= numDays) {
            daySelector.getSelectionModel().selectFirst();
        }
        List<String> selectorList = daySelector.getItems();
        final int diff = selectorList.size() - numDays;
        if (diff > 0) {
            for (int i = 0; i < diff; i++) {
                // remove the extra days
                selectorList.remove(selectorList.size() - 1);
            }
        } else if (diff < 0) {
            for (int i = selectorList.size() + 1; i <= numDays; i++) {
                // append the missing days
                selectorList.add(String.valueOf(i));
            }
        }
    }

    /**
     * maps empty or whitespace-only strings to null
     *
     * @param s any string
     * @return the given string unchanged, or null if that
     * string is empty or contains only whitespace
     */
    private String nullIfBlank(String s) {
        return s.trim().isEmpty() ? null : s;
    }

    /**
     * the "result converter" for this Dialog object.
     * Returns an event if changes were committed. Used to produce
     * the value returned by this.showAndWait()
     *
     * @param bt the type of button that this is in response to.
     * @return a Pair whose {@link Pair#getKey() key} is the name of the calendar that
     * the event was assigned to and whose {@link Pair#getValue() value} is the newly
     * created or edited CalendarEvent object.
     */
    private Pair<String, CalendarEvent> getResult(ButtonType bt) {
        if (bt == ButtonType.OK) {
            String selectedCalendar = calendarSelector.getSelectionModel().getSelectedItem();
            if (event == null) {
                return new Pair<>(
                        selectedCalendar,
                        new CalendarEvent(
                                titleEntryField.getText(),
                                date,
                                start,
                                end,
                                nullIfBlank(locationEntryField.getText()),
                                nullIfBlank(notesEntryArea.getText())
                        )
                );
            } else {
                event.setTitle(titleEntryField.getText());
                event.setDate(date);
                event.setStartTime(start);
                event.setEndTime(end);
                event.setLocation(nullIfBlank(locationEntryField.getText()));
                event.setNotes(nullIfBlank(notesEntryArea.getText()));
                return new Pair<>(selectedCalendar, event);
            }
        }
        return null;
    }
}
