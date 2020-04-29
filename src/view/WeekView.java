package view;

import controller.NoSuchCalendarException;
import controller.CalendarController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import model.CalendarEvent;
import model.CalendarModel;

import java.time.LocalDate;
import java.util.Set;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class WeekView implements CalendarViewMode {
    private static final int ROW_HEIGHT = 50;

    private LocalDate currentView;
    private final CalendarController controller;
    private final BorderPane root;
    private final GridPane days;
    private final Label weekLabel;
    private final List<Region> dayRegions = new ArrayList<>();
    //TODO: replace with controller functionality
    private CalendarModel testModel = new CalendarModel();

    public WeekView(CalendarController controller) {
        this.controller = controller;
        root = new BorderPane();
        days = new GridPane();
        days.setPadding(new Insets(0));
        days.setGridLinesVisible(true);
        currentView = getStartOfWeek(LocalDate.now());

        //Add day labels
        String[] weekDays = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
                "Friday", "Saturday"};
        days.getColumnConstraints().add(new ColumnConstraints(75));
        for (int i = 0; i < weekDays.length; i++) {
            ColumnConstraints con = new ColumnConstraints(100, 135, 200);
            con.setHgrow(Priority.ALWAYS);
            days.getColumnConstraints().add(con);

            Label l = new Label(weekDays[i]);
            l.setFont(new Font(15));
            HBox hb = new HBox(l);
            hb.setAlignment(Pos.CENTER);
            days.add(hb, i + 1, 0);
            Region r = new Region();
            r.setStyle("-fx-background-color: white; -fx-border-color: black");
            days.add(r, i + 1, 1, 1, 24);
            dayRegions.add(r);
        }

        //Add hour labels
        days.getRowConstraints().add(new RowConstraints(25));
        for (int i = 0; i < 24; i++) {
            RowConstraints con = new RowConstraints(ROW_HEIGHT);
            con.setVgrow(Priority.ALWAYS);
            days.getRowConstraints().add(con);

            Label l = new Label(String.format("%02d:00", i));
            l.setFont(new Font(15));
            days.add(l, 0, i + 1);
        }

        VBox top = new VBox();
        Button forward = new Button("->");
        forward.setOnMouseClicked(event -> {
            currentView = currentView.plusDays(7);
            drawWeek();
        });

        Button backward = new Button("<-");
        backward.setOnMouseClicked(event -> {
            currentView = currentView.minusDays(7);
            drawWeek();
        });
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        HBox buttons = new HBox();
        buttons.getChildren().addAll(backward, filler, forward);

        weekLabel = new Label("Week of ");
        HBox weekBox = new HBox();
        weekBox.setAlignment(Pos.CENTER);
        weekLabel.setFont(new Font(35));
        weekBox.getChildren().add(weekLabel);


        top.getChildren().addAll(buttons, weekBox);
        root.setTop(top);


        days.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        ScrollPane scroll = new ScrollPane();
        scroll.setPrefSize(750, 750);
        scroll.setContent(days);
        root.setCenter(scroll);
        testModel.addEvent(new CalendarEvent("14-14:30", LocalDate.now(), LocalTime.of(14, 0), LocalTime.of(14, 30), null, null));
        testModel.addEvent(new CalendarEvent("12-13:30", LocalDate.now(), LocalTime.of(12, 0), LocalTime.of(13, 30), null, null));
        testModel.addEvent(new CalendarEvent("14:30-15:30", LocalDate.now(), LocalTime.of(14, 30), LocalTime.of(15, 30), null, null));
    }

    /**
     * Draws the events in the week
     */
    private void drawWeek() {
        String start = currentView.getMonthValue() + "/" + currentView.getDayOfMonth();
        LocalDate endDate = currentView.plusDays(6);
        String end = endDate.getMonthValue() + "/" + endDate.getDayOfMonth();
        weekLabel.setText(String.format("Week of %s - %s", start, end));

        for (Region dayRegion : dayRegions) {
            dayRegion.setStyle("-fx-background-color:white");
        }

        days.getChildren().removeIf(Button.class::isInstance);

        for (int i = 0; i < 7; i++) {
            Label l = getLabel(i + 1, 0);
            if (l == null) continue;
            String day = l.getText().split(" ")[0];
            LocalDate date = currentView.plusDays(i);
            l.setText(String.format("%s %d/%d", day, date.getMonthValue(), date.getDayOfMonth()));

            if (date.isEqual(LocalDate.now())) dayRegions.get(i).setStyle("-fx-background-color:aqua");
        }

        CalendarEvent[] events = testModel.getEventsInRange(currentView.atStartOfDay(),
                endDate.atTime(23, 59, 59));
        for (CalendarEvent e : events) {
            //Do some math to figure out where to put the button
            int col = e.getDate().compareTo(currentView) + 1;
            int row = e.getStartTime().getHour() + 1;
            float diff = (e.getEndTime().getHour() + (e.getEndTime().getMinute() / 60f)) -
                    (e.getStartTime().getHour() + (e.getStartTime().getMinute() / 60f));
            int rowSpan = (int) diff + 1;
            diff += 0.05f;

            //Create the button that will act as our event view
            Button b = new Button(e.getTitle());
            b.setTranslateY(ROW_HEIGHT / 2f * e.getStartTime().getMinute() / 60f - 10); //10 is a magic number to fudge the button into a good looking place
            b.setPadding(new Insets(5));
            b.setTextAlignment(TextAlignment.CENTER);
            b.setMaxHeight(diff * ROW_HEIGHT);
            b.setPrefHeight(Double.MAX_VALUE);
            b.setMaxWidth(Double.MAX_VALUE);

            //TODO: Implement onclick handlers for button events

            days.add(b, col, row, 1, rowSpan);
        }
    }

    /**
     * Gets the label from an HBox located in the grid at col and row
     *
     * @param col column of the label
     * @param row row of the label
     * @return label at that row and column, null otherwise
     */
    private Label getLabel(int col, int row) {
        HBox hb = (HBox) getNodeAt(col, row);

        return (hb == null) ? null : (Label) hb.getChildren().get(0);
    }

    private Node getNodeAt(int col, int row) {
        for (Node n : days.getChildren()) {
            //Children in the GridPane can somehow not be in the GridPane?
            //getColumnIndex and getRowIndex return Integers, meaning they can be null.
            Integer getCol = GridPane.getColumnIndex(n);
            Integer getRow = GridPane.getRowIndex(n);

            if (getCol == null || getRow == null) continue;
            //I hate this bit so much, so let me know if you can simplify it
            if (row == getRow && col == getCol) return n;
        }
        return null;
    }

    private LocalDate getStartOfWeek(LocalDate date) {
        return date.minusDays(date.getDayOfWeek().getValue());
    }

    @Override
    public Node getNode() {
        return root;
    }

    @Override
    public LocalDate getDate() {
        return currentView;
    }

    @Override
    public void setVisibleCalendars(Set<String> calNames) throws NoSuchCalendarException {
        // TODO
    }

    @Override
    public void setDate(LocalDate date) {
        currentView = getStartOfWeek(date);
        drawWeek();
    }
}
