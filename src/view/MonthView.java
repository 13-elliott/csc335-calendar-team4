package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.CalendarEvent;
import model.CalendarModel;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

import controller.CalendarController;
import controller.NoSuchCalendarException;

public class MonthView implements CalendarViewMode {
    private BorderPane outer;
    private LocalDate currentView;
    private GridPane grid;
    private CalendarModel model;
    private ArrayList<BorderPane> panes;
    private Label title;
    private CalendarController controller;

    /**
     * The start method overridden from Application
     * This method is called when the Calendar class
     * calls it's launch method. This is the main
     * method of the program and holds all of the
     * initialization of the GUI and it's event handelers.
     */
    public MonthView(CalendarController controller) {
        // Initialize current day and lists to help with construction
        currentView = LocalDate.now();
        model = new CalendarModel();
        this.controller = controller;

        // Label on Calendar with all the weekdays as well as month/year label
        GridPane dayNames = new GridPane();
        title = new Label();
        title.setFont(new Font(50));
        String[] weekDays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu",
                "Fri", "Sat"};
        for (int i = 0; i < 7; i++) {
            Label l = new Label(weekDays[i]);
            dayNames.add(l, i, 0);
            GridPane.setMargin(l, new Insets(1, 39, 1, 39));
        }

        // initialize buttons
        Button forward = new Button("->");
        Button backward = new Button("<-");
        HBox hbox = new HBox();
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        hbox.getChildren().addAll(backward, filler, forward);

        // vBox to help layout
        VBox vbox = new VBox();
        vbox.getChildren().add(hbox);
        vbox.getChildren().add(title);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(dayNames);

        // Create grid
        grid = new GridPane();

        // List of panes created
        panes = new ArrayList<>();

        // Create borderpane
        outer = new BorderPane();

        // Initialize board with panes
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                BorderPane b = new BorderPane();
                b.setPrefSize(100, 100);
                b.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                        new BorderWidths(0.5))));
                Label l = new Label();
                b.setTop(l);
                VBox eventBox = new VBox();
                b.setCenter(eventBox);
                panes.add(b);
                grid.add(b, j, i);
            }
        }

        // draws the current month
        drawMonth();

        // Click on grid and get the row/col coordinates
        grid.setOnMouseClicked(event -> {
            // get the row and col that is clicked on
            for (Node node : grid.getChildren()) {
                if (node instanceof BorderPane) {
                    if (node.getBoundsInParent().contains(event.getX(), event.getY())) {
                        int clickedY = GridPane.getRowIndex(node);
                        int clickedX = GridPane.getColumnIndex(node);
                        int day = getDayOnClick(clickedY, clickedX);
                        if (day > 0) {
                            // TODO
                            EventDialog.newEventAt(
                                    currentView.withDayOfMonth(day),
                                    controller.getCalendarNames()
                            ).showAndWait()
                                    // add the event if it was created
                                    .ifPresent(pair -> {
										try {
											controller.addEvent(
											        pair.getKey(),
											        pair.getValue()
											);
										} catch (NoSuchCalendarException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
							});
                        }
                        drawMonth();
                    }
                }
            }
        });

        // Next Month
        forward.setOnAction(e -> {
            currentView = currentView.plusMonths(1);
            drawMonth();
        });

        // Previous Month
        backward.setOnAction(e -> {
            currentView = currentView.minusMonths(1);
            drawMonth();
        });

        outer.setTop(vbox);
        outer.setCenter(grid);
    }

    /**
     * This method draws the month view.
     */
    public void drawMonth() {
        removeEvents();

        String month = currentView.getMonth().getDisplayName(TextStyle.FULL, Locale.US);
        String year = "" + currentView.getYear();
        title.setText(month + " " + year);
        
        LocalDate beg = currentView.withDayOfMonth(1);

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                int index = i * 7 + j;
                BorderPane b = panes.get(index);
                b.setStyle("");
                b.setPrefHeight(100);
                ((Label) b.getChildren().get(0)).setText("");
                // Checks if day starts on sunday
                if(index == 0 && beg.getDayOfWeek().getValue() == 7) {
                	;
                }
                else if (beg.getMonthValue() > currentView.getMonthValue()) {
                	b.setStyle("-fx-background-color:grey");
                	continue;
                }
                else if (index < beg.getDayOfWeek().getValue()) {
                	b.setStyle("-fx-background-color:grey");
                	continue;
                }
                
                ((Label) b.getChildren().get(0)).setText(beg.getDayOfMonth() + "");
                
                if (LocalDate.now().equals(beg))
                    b.setStyle("-fx-background-color:aqua");

                b.getChildren().removeIf(Button.class::isInstance);
                CalendarEvent[] events;
				try {
					events = controller.getEventsInDay("Default", beg);
					Arrays.sort(events, (a,b1) -> a.getStartTime().compareTo(b1.getStartTime()));
					for (CalendarEvent event : events) {
	                    Button button = new Button(event.getTitle());
	                    button.setPrefSize(100, 5);
	                    button.setStyle("-fx-font-size:5");
	                    ((VBox) b.getChildren().get(1)).getChildren().add(button);
	                    // TODO
	                    button.setOnMouseClicked(butt -> {
	                            System.out.println("yuh");
	                    });
	                }
				} catch (NoSuchCalendarException e) {
					e.printStackTrace();
				}
				beg = beg.plusDays(1);

            }
        }
        System.out.println("NEXT MONTH");

    }

    /**
     * This method removes events from each pane
     * This method removes all of the events from
     * the panes in the grid so new events can be
     * updated for the month.
     */
    public void removeEvents() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
            	int index = i * 7 + j;
                BorderPane t = panes.get(index);
                ((VBox) t.getChildren().get(1)).getChildren().clear();
            }
        }
    }

    /**
     * This method returns the day the user clicks on
     * <p>
     * This method, when given a row and column that
     * the user has clicked on, will return the int of
     * the day that was clicked. If the user clicks on
     * a box that isn't a day, it returns -1.
     *
     * @param row the row clicked on
     * @param col the column clicked on
     * @return the day clicked on or -1.
     */
    public int getDayOnClick(int row, int col) {
        BorderPane t = panes.get(row * 7 + col);
        String dayClicked = ((Label) t.getChildren().get(0)).getText();
        if (dayClicked.equals("")) {
            return -1;
        } else {
            return Integer.parseInt(dayClicked);
        }
    }

    @Override
    public Node getNode() {
        return outer;
    }

    @Override
    public LocalDate getDate() {
        return currentView.withDayOfMonth(1);
    }

    @Override
    public void setDate(LocalDate d) {
        currentView = d;
        drawMonth();
    }
}
