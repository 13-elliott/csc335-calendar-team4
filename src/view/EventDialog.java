package view;

import javafx.scene.control.*;
import model.CalendarEvent;

import java.util.Calendar;
import java.util.List;

public class EventDialog extends Dialog<CalendarEvent> {

//    public static final int
//            DEFAULT_WIDTH = 300,
//            DEFAULT_HEIGHT = 500;

    private final CalendarEvent event;
    private final TextField titleEntryField, locationEntryField, yearField;
    private final TextArea notesEntryArea;
    private final ChoiceBox<Integer>
            daySelector, monthSelector,
            startHourSelector, startMinuteSelector,
            endHourSelector, endMinuteSelector;
    private final Calendar date, start, end;

    public EventDialog() {
        this(null);
    }

    public EventDialog(CalendarEvent event) {
        super();
        this.event = event;

        date = Calendar.getInstance();
        start = Calendar.getInstance();
        end = Calendar.getInstance();

        titleEntryField = new TextField();
        locationEntryField = new TextField();
        notesEntryArea = new TextArea();

        yearField = new TextField();
        yearField.setTextFormatter(new TextFormatter<>(EventDialog::yearFormatter));

        daySelector = new ChoiceBox<>();
        monthSelector = new ChoiceBox<>();
        startHourSelector = new ChoiceBox<>();
        startMinuteSelector = new ChoiceBox<>();
        endHourSelector = new ChoiceBox<>();
        endMinuteSelector = new ChoiceBox<>();

        Label
                titleLabel = new Label("Title"),
                dateLabel = new Label("Date"),
                startLabel = new Label("Start Time"),
                endLabel = new Label("End Time"),
                locationLabel = new Label("Location"),
                notesLabel = new Label("Notes");

        setupTimeElements();
        fillNonTimeElements();
        this.setResultConverter(this::getResult);

        this.getDialogPane().getChildren().addAll(
                // todo: add the children
        );
    }

    /**
     * intercepts changes to the yearField TextField. If the change contains
     * any non-digit characters, those are filtered out.
     *
     * @param chg represents the change
     * @return the possibly altered
     */
    private static TextFormatter.Change yearFormatter(TextFormatter.Change chg) {
        if ((chg.isAdded() || chg.isReplaced())) {
            StringBuilder onlyDigits = new StringBuilder();
            // filter out all non-digit chars,
            chg.getText().chars().filter(Character::isDigit)
                    // pushing them into the string builder
                    .forEach(i -> onlyDigits.append((char) i));
            if (onlyDigits.length() != chg.getText().length()) {
                // only update the text if anything was filtered out
                chg.setText(onlyDigits.toString());
            }
        }
        return chg;
    }

    /**
     * set starting values for the contents of non-time-related graphical elements
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
     * set the starting values for time-related graphical elements
     */
    private void setupTimeElements() {
        for (int i = 1; i <= 24; i++) {
            startHourSelector.getItems().add(i);
            endHourSelector.getItems().add(i);
        }
        for (int i = 1; i <= 60; i++) {
            startMinuteSelector.getItems().add(i);
            endMinuteSelector.getItems().add(i);
        }
        for (int i = 1; i <= 12; i++) {
            monthSelector.getItems().add(i);
        }

        monthSelector.getSelectionModel().selectedItemProperty().addListener(
                (a, b, c) -> updateNumDaysInMonth()
        );
        yearField.textProperty().addListener(
                (a, b, c) -> updateNumDaysInMonth()
        );
        if (event != null) {
            date.setTime(event.getDate());
            start.setTime(event.getStartTime());
            end.setTime(event.getEndTime());
        }
        yearField.setText(String.valueOf(date.get(Calendar.YEAR)));
        monthSelector.setValue(date.get(Calendar.MONTH));
        daySelector.setValue(date.get(Calendar.DAY_OF_MONTH));

        startHourSelector.setValue(start.get(Calendar.HOUR_OF_DAY));
        startMinuteSelector.setValue(start.get(Calendar.MINUTE));

        endHourSelector.setValue(end.get(Calendar.HOUR_OF_DAY));
        endMinuteSelector.setValue(end.get(Calendar.MINUTE));
    }

    /**
     * called when the month or year fields are updated. updates the number of days
     * available for selection in the monthSelector ChoiceBox.
     */
    private void updateNumDaysInMonth() {
        // change range of available day choices based on selected month
        Calendar tempCal = Calendar.getInstance();
        tempCal.set(Calendar.MONTH, monthSelector.getSelectionModel().getSelectedIndex());
        int year = yearField.getText().isEmpty() ?
                date.get(Calendar.YEAR) : Integer.parseInt(yearField.getText());
        tempCal.set(Calendar.YEAR, year);

        final int numDays = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        List<Integer> selectorList = daySelector.getItems();
        final int diff = selectorList.size() - numDays;
        if (diff > 0) {
            for (int i = 0; i < diff; i++) {
                // remove the extra days
                selectorList.remove(selectorList.size() - 1);
            }
        } else if (diff < 0) {
            for (int i = selectorList.size() + 1; i <= numDays; i++) {
                // append the missing days
                selectorList.add(i);
            }
        } else {
            return;
        }
        // if changed, reset selected day to the first
        daySelector.setValue(1);
    }

    /**
     * the "result converter" for this Dialog object.
     * Returns an event if changes were committed.
     *
     * @param bt the type of button that this is in response to.
     * @return a CalendarEvent object if changes were committed. Otherwise, null.
     */
    private CalendarEvent getResult(ButtonType bt) {
        if (bt == ButtonType.OK) {
            if (event == null) {
                return new CalendarEvent(
                        titleEntryField.getText(),
                        date.getTime(),
                        start.getTime(),
                        end.getTime(),
                        locationEntryField.getText(),
                        notesEntryArea.getText()
                );
            } else {
                // TODO: mutate event
                return event;
            }
        }
        return null;
        // TODO: handle dialog window closing
    }
}
