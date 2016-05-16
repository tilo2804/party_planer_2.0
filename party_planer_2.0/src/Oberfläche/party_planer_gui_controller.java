package Oberfl�che;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import Simulation.Simulation;
import Speicher.Speicher;
import data.Configuration;
import data.Guest;
import data.Room;
import data.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class party_planer_gui_controller {
	
	@FXML private Button startSimulationButton;
	@FXML private Button stoppSimulationButton;
	@FXML private Button pauseSimulationButton;
	@FXML private Button nextIterationButton;
	@FXML private Button nextStepButton;
	
	@FXML private Button addTableButton;
	@FXML private Button addGuestButton;
	@FXML private Button saveConfigButton;
	
    @FXML private TextField roomLengthField;
    @FXML private TextField tableHeightField;
    @FXML private TextField simulationIterationsField;
    @FXML private TextField guestJobField;
    @FXML private TextField guestNameField;
    @FXML private TextField tablePosXField;
    @FXML private TextField tablePosYField;
    @FXML private TextField tableLengthField;
    @FXML private TextField roomHeightField;
    @FXML private TextField simulationSpeedField;
	    
    @FXML private TableView<Guest> distanceMatrixTable;
	
    @FXML private Tab configurationTab;
    @FXML private Tab simulationTab;

    @FXML private GridPane zimmerGrid;
    @FXML private Button nextStep;
    @FXML private StackPane main;
    
    @FXML private TableView<Guest> legende;
    @FXML private TableColumn<Guest, String> nameColumn;
    @FXML private TableColumn<Guest, Integer> moodColumn;

    // TODO: get room size from config
    final int roomWidth = 800; // Zimmerlänge
    final int roomHeight = 400; // Zimmerhöhe
    final int squareSize = 40; // Zellgröße

    final int width = roomWidth + 300; // Scene width = room width + legend width
    final int height = roomHeight + 300; // Scene height = room height + 300 (buttons, etc.)
    final int centerColumnWidth = roomWidth;
    final int rightColumnWidth = 290; // legend width minus 10 margin on the right

    // TODO: get table from config
    final int tableWidth = 4; // number of squares
    final int tableHeight = 2; // number of squares
    final int tablePosX = 5;
    final int tablePosY = 4;  
    
    private Speicher speicher;
    private Simulation simulation;
    
    public party_planer_gui_controller(Stage primaryStage) {
    	Scene scene = initScene();
    	configureActions();
    	Room room = new Room(15, 10);
    	speicher = new Speicher(room);
        simulation = new Simulation(speicher,this);

        drawSquares();
        drawGuests();
        drawTable();
        drawLegend();
        
        
        showScene(primaryStage, scene);
        distanceMatrixTable.setEditable(true);
        
    }
    
    private void refreshMatrix() {
    	ObservableList<Guest> legendItems = FXCollections.observableArrayList();
    	Iterator<Guest> helpIterator = speicher.getGuestList().iterator();
    	Guest helpGuest;
    	String name;
    	distanceMatrixTable.getColumns().clear();
    	TableColumn newTableColumn = new TableColumn<Guest, String>();
        newTableColumn.setCellValueFactory(new PropertyValueFactory<Guest, String>("name"));
        distanceMatrixTable.getColumns().add(newTableColumn);
    	
        for (int i = 0; i < speicher.getGuestList().size(); i++) {
            helpGuest = helpIterator.next();
            legendItems.add(helpGuest);
            newTableColumn = new TableColumn<Guest, Integer>();
            newTableColumn.setText(helpGuest.getName());
            newTableColumn.setEditable(true);
            newTableColumn.setCellFactory(TextFieldTableCell.<Integer>forTableColumn());
            distanceMatrixTable.getColumns().add(newTableColumn);
            distanceMatrixTable.getColumns().get(distanceMatrixTable.getColumns().size()-1).setEditable(true);
        }
           
        distanceMatrixTable.setItems(legendItems);
        
        distanceMatrixTable.setPrefWidth(rightColumnWidth);
    }
   
    public void refresh() {
    	drawSquares();
        drawGuests();
        drawTable();
        drawLegend();
    }

    /**
     * Initialize FXML-Loader and create a new scene
     * @return Scene Control
     * @throws Exception
     */
    protected Scene initScene() {
        // create a loader
        FXMLLoader loader = new FXMLLoader(getClass().getResource("party_planer_gui.fxml"));

        // set this instance as its controller
        loader.setController(this);

        // create a scene
        Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return new Scene(root, width, height);
    }


    /**
     * Show created scene
     * @param primaryStage - Stage Control from start()
     * @param scene - Scene Control initialized with the method initScene()
     */
    protected void showScene(Stage primaryStage, Scene scene) {
        primaryStage.setTitle("Party Planer");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }


    /**
     * Set the room size,
     * add necessary quantity of columns and rows,
     * fill created cells with empty squares
     */
    protected void drawSquares() {
        // set the room size
        zimmerGrid.setMaxWidth(roomWidth);
        zimmerGrid.setMaxHeight(roomHeight);

        for (int col = 0; col < roomWidth/squareSize; col++) {
            ColumnConstraints column = new ColumnConstraints(squareSize);
            zimmerGrid.getColumnConstraints().add(column);
        }

        for(int i = 0; i < roomHeight/squareSize; i++) {
            RowConstraints row = new RowConstraints(squareSize);
            zimmerGrid.getRowConstraints().add(row);
        }

        // add squares to the room
        for (int col = 0; col < roomWidth/squareSize; col++) {
            for (int row = 0; row < roomHeight/squareSize; row++) {
            	Pane square = drawRoomSquare("empty");
                zimmerGrid.add(square, col, row);

            }
        }
    }


    /**
     * Create a square for the room with Pane Control
     * @param squareType - CSS-class
     * @return Pane with CSS-class
     */
    protected Pane drawRoomSquare(String squareType) {
    	Pane square = new Pane();
        square.getStyleClass().addAll("room-square", "square-"+squareType);
        return square;
    }


    /**
     * Create a letter for the room with Label Control
     * @param text - text inside the label
     * @param letterType - CSS-class
     * @return Label with text and CSS-class
     */
    protected Label createRoomLetter(String text, String letterType) {
        Label letter = new Label(text);
        letter.getStyleClass().addAll("room-label", "label-"+letterType);
        return letter;
    }


    /**
     * Create guests-squares in the room
     */
    protected void drawGuests() {
        // add the guests to the squares#
    	Iterator<Guest> helpIterator = speicher.getGuestList().iterator();
    	Guest helpGuest;
        
    	for (int i = 0; i < speicher.getGuestList().size(); i++) {
    		
    		helpGuest = helpIterator.next();
        	
            int guestPosX = (int) helpGuest.getPosition().getX();
            int guestPosY = (int) helpGuest.getPosition().getY();
            
            String guestLetter = helpGuest.getName().substring(0, 1);

            Pane square = drawRoomSquare("guest");
            Label letter = createRoomLetter(guestLetter, "guest");

            zimmerGrid.add(square, guestPosX, guestPosY);
            zimmerGrid.add(letter, guestPosX, guestPosY);

            zimmerGrid.setHalignment(letter, HPos.CENTER);
        }
    }

    /**
     * Add a legend with guests names
     */
    protected void drawLegend() {
    	
    	ObservableList<Guest> legendItems = FXCollections.observableArrayList();
    	Iterator<Guest> helpIterator = speicher.getGuestList().iterator();
    	Guest helpGuest;
    	String name, mood;
    	
        for (int i = 0; i < speicher.getGuestList().size(); i++) {
            helpGuest = helpIterator.next();
            legendItems.add(helpGuest);
        }

        nameColumn.setCellValueFactory(new PropertyValueFactory<Guest, String>("name"));
        moodColumn.setCellValueFactory(new PropertyValueFactory<Guest, Integer>("mood"));
        
        legende.setItems(legendItems);

        legende.setPrefWidth(rightColumnWidth);
    }


    /**
     * Create table-squares in the room
     */
    protected void drawTable() {
        // add the table squares
        for (int x = tablePosX; x < tablePosX+tableWidth; x++) {
            for (int y = tablePosY; y < tablePosY+tableHeight; y++)
            {
                // create a new square with background and text
            	Pane square = drawRoomSquare("table");
                Label letter = createRoomLetter("T", "table");
                // add to the room the square and the guests's letter
                zimmerGrid.add(square, x, y);
                zimmerGrid.add(letter, x, y);
                // align the letter in center
                zimmerGrid.setHalignment(letter, HPos.CENTER);
            }
        }
    }

    private void configureActions() {
    	
    	startSimulationButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
    		public void handle(MouseEvent event) {
    			simulation.startSimulation();
    		}
    	});
    	
    	pauseSimulationButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
    		public void handle(MouseEvent event) {
    			simulation.pauseSimulation();
    		}
    	});
    	
    	stoppSimulationButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
    		public void handle(MouseEvent event) {
    			simulation.stopSimulation();
    		}
    	});
    	
    	nextIterationButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
    		public void handle(MouseEvent event) {
    			simulation.nextIteration();
    		}
    	});
    	
    	nextStepButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
    		public void handle(MouseEvent event) {
    			simulation.nextGuest();
    		}
    	});
    	
    	addTableButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
    		public void handle(MouseEvent event) {
    			try {
    				Integer length =  Integer.valueOf(tableLengthField.getText());
    				try {
        				Integer height = Integer.valueOf(tableHeightField.getText());
        				try {
            				Integer posX = Integer.valueOf(tablePosXField.getText());
            				try {
                				Integer posY = Integer.valueOf(tablePosYField.getText());
                				speicher.getRoom().setTable(new Table(length, height, new Point(posX, posY)));
                			} catch(Exception e) {
                				tablePosYField.setText("fehlerhafte Eingabe");
                			}
            			} catch(Exception e) {
            				tablePosXField.setText("fehlerhafte Eingabe");
            			}
        			} catch(Exception e) {
        				tableHeightField.setText("fehlerhafte Eingabe");
        			}
    			} catch(Exception e) {
    				tableLengthField.setText("fehlerhafte Eingabe");
    			}
    		}
    	});
    	
    	addGuestButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
    		public void handle(MouseEvent event) {
    			if(guestNameField.getText().isEmpty()) guestNameField.setText("fehlende Eingabe");
    			else if(guestJobField.getText().isEmpty()) guestJobField.setText("fehlende Eingabe");
    			else {
    				speicher.getGuestList().add(new Guest(guestNameField.getText(),guestJobField.getText()));
    				refreshMatrix();
    				drawLegend();
    			}
    		}
    	});
    	
    	saveConfigButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
    		public void handle(MouseEvent event) {
    			
    			Iterator<Guest> Iterator = speicher.getGuestList().iterator();
    			Iterator<Guest> helpIterator = speicher.getGuestList().iterator();
    			Guest helpGuest;
    			
    			for (int i = 0; i < speicher.getGuestList().size(); i++) {
    	            helpGuest = Iterator.next();
    	            for (int j = 0; j < speicher.getGuestList().size(); j++) {
    	            	Guest guest = helpIterator.next();
    	            	TableColumn test = distanceMatrixTable.getColumns().get(j+1);
    	            	helpGuest.setDistances(guest, (float) distanceMatrixTable.getColumns().get(j+1).getCellData(i));
    	            	System.out.println("Gast " + helpGuest + " distance " + helpGuest.getDistance(guest));
        	        };
        	        helpIterator = speicher.getGuestList().iterator();
    	        };
    			
    		}
    	});
    	
    	
    }
    
}
