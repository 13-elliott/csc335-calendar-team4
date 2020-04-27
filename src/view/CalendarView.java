package view;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.CalendarEvent;
import model.CalendarModel;

public class CalendarView extends Application implements java.util.Observer{
	private String[] months;
	private int[] days;
	private String[] weekDays;
	private int year;
	private int month;
	private CalendarModel model;
	private GridPane grid;
	private Button forward;
	private Button backward;
	private Scene scene;
	private ArrayList<TilePane> panes;
	private Label title;
	
	/** Overridden Update method from observable
	 * 
	 * This method is called when the model is changed
	 * 
	 * @param o, an observable object
	 * @param arg, the model object
	 * @return none
	 */
	@Override
	// This will Change depending on view later on
	public void update(Observable o, Object arg) {
		drawMonth();
	}
	
	/** The start method overridden from Application
	 * 
	 * This method is called when the Calendar class
	 * calls it's launch method. This is the main
	 * method of the program and holds all of the 
	 * initialization of the GUI and it's event handelers.
	 * 
	 * @param calendarStage, the main stage of the program.
	 * @return none
	 */
	@SuppressWarnings("deprecation")
	@Override
    public void start(Stage calendarStage) throws Exception {
    	// Initialize current day and lists to help with construction
    	Date d = new Date();
    	year = d.getYear() + 1900; 	    	
    	month = d.getMonth();	
    	months = new String[]{"January", "February", "March", "April", "May", "June", "July",
    			"August", "September", "October", "November", "December"};	
    	days = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}; 	
		weekDays = new String[] {"Sun","Mon", "Tue", "Wed", "Thu", 
				"Fri", "Sat"};
		// initialize model	
		model = new CalendarModel();
		
		// Check for leap year
		if(year % 4 == 0 && year % 100 != 0) {
			days[1] = 29;
		}
		else if(year % 4 == 0 && year % 100 == 0 && year % 400 == 0) {
			days[1] = 29;
		}
		else {
			days[1] = 28;
		}
		
		// Label on Calendar with all the weekdays as well as month/year label
		GridPane dayNames = new GridPane();
		title = new Label();
		title.setFont( new Font(50));	
		for(int i = 0; i < 7;i++) {
			Label l = new Label(weekDays[i]);
			dayNames.add(l, i, 0);
			GridPane.setMargin(l, new Insets(1,28,1,28));
		}
		
		// initialize buttons
		forward = new Button("->");
    	backward = new Button("<-");
    	HBox hbox = new HBox();
		hbox.getChildren().add(backward);
		hbox.getChildren().add(forward);
		hbox.setSpacing(492);
		
		// vBox to help layout
		VBox vbox = new VBox();
		vbox.getChildren().add(hbox);
		vbox.getChildren().add(title);
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().add(dayNames);
				
		// Create grid
		grid = new GridPane();
		grid.setGridLinesVisible(true);
		
		// List of panes created
		panes = new ArrayList<TilePane>();
				
		// Create borderpane
    	BorderPane b = new BorderPane();
    	
    	// Initialize board with panes
    	for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 7; j++) {	
				TilePane t = new TilePane();
				t.setPrefHeight(100);
				Label l = new Label();
				t.getChildren().add(l);
				panes.add(t);
				grid.add(t, j, i);	
			}
		}
    	
    	// draws the current month
    	drawMonth();
  		
		// Click on grid and get the row/col coordinates
		grid.setOnMouseClicked(event -> {
			// get the row and col that is clicked on
			for(Node node: grid.getChildren()) {
                if(node instanceof TilePane) {
                    if(node.getBoundsInParent().contains(event.getX(), event.getY())) {
                        System.out.println(GridPane.getRowIndex(node));
                        System.out.println(GridPane.getColumnIndex(node));
                        int clickedY = GridPane.getRowIndex(node);
                        int clickedX = GridPane.getColumnIndex(node);
                        System.out.println(getDayOnClick(clickedY,clickedX));
                    }
                }
			}
		});
		
		// Next Month
		forward.setOnAction(e -> {
			if(month + 1 > 11) {
				month = 0;
				year++;
			}
			else {
				month++;
			}
			
			// Check for leap year
			if(year % 4 == 0 && year % 100 != 0) {
				days[1] = 29;
			}
			else if(year % 4 == 0 && year % 100 == 0 && year % 400 == 0) {
				days[1] = 29;
			}
			else {
				days[1] = 28;
			}
			
		    drawMonth();
		});
		
		// Previous Month
		backward.setOnAction(e -> {
			if(month - 1 < 0) {
				month = 11;
				year--;
			}
			else {
				month--;
			}
			// Check for leap year
			if(year % 4 == 0 && year % 100 != 0) {
				days[1] = 29;
			}
			else if(year % 4 == 0 && year % 100 == 0 && year % 400 == 0) {
				days[1] = 29;
			}
			else {
				days[1] = 28;
			}
		    drawMonth();
		});
		
		b.setTop(vbox);
		b.setCenter(grid);
		scene = new Scene(b);
		
		// Set up main stage
		calendarStage.setTitle("Calender");
		calendarStage.setScene(scene);
		calendarStage.show();
    }
    
	/** This method draws the board
	 * 
	 * This method draws the month view.
	 * 
	 * @param none
	 * @return none
	 */
    @SuppressWarnings("deprecation")
	public void drawMonth() {
    	// *Remove Events will be implemented here*
    	
    	title.setText(months[month] + " " + year + "");
    	int day = 1;
    	boolean flag = false;
    	    	    	
    	Date real = new Date();
    	Date beg = new Date(year, month, 1);
    	
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 7; j++) {
				// get each pane from left to right by row
				TilePane t = panes.get(i*7 + j);
				t.setStyle("");
				// Checks if the pane is the same as the starting day
				if(j == beg.getDay() -1 || beg.getDay() == 0) {
					if(beg.getDay() == 0) {
						if(j == 6) {
							flag = true;
						}
					}
					else {
						flag = true;
					}	
				}
				// if true and theres still enough days
				if(flag == true && day <= days[beg.getMonth()]) {
					t.setPrefHeight(100);
					((Label)t.getChildren().get(0)).setText(day+"");
					if(real.getDate() == day && year == (real.getYear()+1900) && 
							months[real.getMonth()].equals(months[month])) {
						t.setStyle("-fx-background-color:aqua");
					}
					CalendarEvent[] events = model.getEventsInDay(year, month, day);
					// Loop through events and add them
					if(events.length != 0) {
						for(int k = 0; i < events.length; i++) {
							Button button = new Button(events[k].getTitle());
							t.getChildren().add(button);
						}
					}
					day++;
				}
				else {
					((Label)t.getChildren().get(0)).setText("");
					t.setStyle("-fx-background-color:white");
				}
			}
		}
    }
    
    /** This method removes events from each pane
	 * 
	 * This method removes all of the events from
	 * the panes in the grid so new events can be
	 * updated for the month.
	 * 
	 * @param none
	 * @return none
	 */
    public void removeEvents() {
    	for(int i = 0; i < 6; i++) {
    		for(int j = 0; j < 7; j++) {
    			
    		}
    	}
    }
    
    /** This method returns the day the user clicks on
	 * 
	 * This method, when given a row and column that 
	 * the user has clicked on, will return the int of
	 * the day that was clicked. If the user clicks on 
	 * a box that isn't a day, it returns -1.
	 * 
	 * @param int, the row clicked on
	 * @param int, the column clicked on
	 * @return int, the day clicked on or -1.
	 */
    public int getDayOnClick(int row, int col) {
    	TilePane t = panes.get(row*7 + col);
    	String dayClicked = ((Label)t.getChildren().get(0)).getText();
    	if(dayClicked.equals("")) {
    		return -1;
    	}
    	else {
    		return Integer.parseInt(dayClicked);
    	}
    }
}
