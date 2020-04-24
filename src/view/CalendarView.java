package view;
import java.util.Date;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.CalendarEvent;
import model.CalendarModel;

public class CalendarView extends Application {
	private String[] months;
	private int[] days;
	private String[] weekDays;
	private int year;
	private int month;
	private CalendarModel model;
	private GridPane grid;
	private BorderPane b;

    @SuppressWarnings("deprecation")
	@Override
    public void start(Stage calendarStage) throws Exception {
    	
    	Date d = new Date();
    	
    	year = d.getYear() + 1900;
    	    	
    	month = d.getMonth();
    	
		
    	months = new String[]{"January", "February", "March", "April", "May", "June", "July",
    			"August", "September", "October", "November", "December"};
    	
    	days = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    	
		weekDays = new String[] {"Sun","Mon", "Tue", "Wed", "Thu", 
				"Fri", "Sat"};
				
		model = new CalendarModel();
		
		// Create grid
		grid = new GridPane();
		grid.setGridLinesVisible(true);
		
		// Create borderpane
    	b = new BorderPane();
    	
    	Scene scene = drawMonth();
		
		
		// Click on grid and get the row/col coordinates
		grid.setOnMouseClicked(event -> {
			// get the row and col that is clicked on
			for(Node node: grid.getChildren()) {
                if(node instanceof TilePane) {
                    if(node.getBoundsInParent().contains(event.getX(), event.getY())) {
                        System.out.println(GridPane.getRowIndex(node));
                        System.out.println(GridPane.getColumnIndex(node));
                        //clickedX = GridPane.getRowIndex(node);
                        //clickedY = GridPane.getColumnIndex(node);
                    }
                }
			}
		});
		
		
		// Set up main stage
		calendarStage.setTitle("Calender");
		calendarStage.setScene(scene);
		calendarStage.show();
    }
    
    @SuppressWarnings("deprecation")
	public Scene drawMonth() {
    	int day = 1;
    	
    	boolean flag = false;
    	    	    	
    	Date beg = new Date(2020, month, 1);
    	
    	System.out.println(beg);
		
		GridPane dayNames = new GridPane();
		
		Label title = new Label(months[month] + " " + year + "");
		title.setFont( new Font(50));
		
		
		for(int i = 0; i < 7;i++) {
			Label l = new Label(weekDays[i]);
			dayNames.add(l, i, 0);
			GridPane.setMargin(l, new Insets(1,28,1,28));
		}
		VBox vbox = new VBox();
		vbox.getChildren().add(title);
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().add(dayNames);
		
    	// initialize the board
		outerloop:
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 7; j++) {
				TilePane t = new TilePane();
				t.setPrefHeight(100);
				if(j == beg.getDay() -1) {
					flag = true;
				}
				
				if(flag == true && day <= days[beg.getMonth()]) {
					Label l = new Label(day+"");
					CalendarEvent[] events = model.getEventsInDay(year, beg.getMonth(), day);
					t.getChildren().add(l);
					// Loop through events and add them
					if(events.length != 0) {
						for(int k = 0; i < events.length; i++) {
							Button button = new Button(events[k].getTitle());
							t.getChildren().add(button);
						}
					}
					day++;
				}
				grid.add(t, j, i);
				
				if(day > days[beg.getMonth()]) {
					break outerloop;
				}
			}
		}
		
		b.setTop(vbox);
		b.setCenter(grid);
		Scene ret = new Scene(b);
		return ret;
    }
}
