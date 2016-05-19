package Oberfläche;

import java.awt.Point;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import Simulation.Simulation;
import Speicher.Speicher;
import data.Configuration;
import data.Guest;
import data.Room;
import data.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
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
	@FXML private Button saveDistanceButton;
	@FXML private Button guestListfinishedButton;
	@FXML private Button saveRoomButton;
	
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
	@FXML private TextField xPosField;
	@FXML private TextField yPosField;
	@FXML private TextField distanceField;
    
	@FXML private Text guestText;
	@FXML private Text helpGuestText;
	
    @FXML private Tab configurationTab;
    @FXML private Tab simulationTab;

    @FXML private GridPane zimmerGrid;
    @FXML private Button nextStep;
    @FXML private StackPane main;
    @FXML private Text indexText;
    
    @FXML private TableView<Guest> legende;
    @FXML private TableColumn<Guest, String> nameColumn;
    @FXML private TableColumn<Guest, Float> moodColumn;

    // TODO: get room size from config
    private int roomWidth; // ZimmerlÃ¤nge
    private int roomHeight; // ZimmerhÃ¶he
    private int squareSize = 40; // ZellgrÃ¶ÃŸe

    private int width = roomWidth + 300; // Scene width = room width + legend width
    private int height = roomHeight + 300; // Scene height = room height + 300 (buttons, etc.)
    private int centerColumnWidth = roomWidth;
    private int rightColumnWidth = 290; // legend width minus 10 margin on the right

    private Speicher speicher;
    private Simulation simulation;
    
    private Iterator<Guest> mainGuestIterator;
    private Iterator<Guest> helpGuestIterator;
    private Stage primaryStage;
    private Scene scene;
    private Guest mainGuest = null, helpGuest = null;
    private MyService service;
    
    public party_planer_gui_controller(Stage primaryStage) {
    	scene = initScene();
    	configureActions();
    	this.primaryStage = primaryStage;
        showScene(primaryStage, scene);
        
    }
       
    public void refresh() {
    	zimmerGrid.getColumnConstraints().clear();
        zimmerGrid.getRowConstraints().clear();
        zimmerGrid.getChildren().clear();
        
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
    	
    	roomWidth = speicher.getRoom().getLength()*squareSize;
    	roomHeight = speicher.getRoom().getWidth()*squareSize;
    			
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
    public void drawGuests() {
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
    public void drawLegend() {
    	
    	ObservableList<Guest> legendItems = FXCollections.observableArrayList();
    	Iterator<Guest> helpIterator = speicher.getGuestList().iterator();
    	Guest helpGuest;
    	String name, mood;
    	Float partyIndex = (float)0;
    	
        for (int i = 0; i < speicher.getGuestList().size(); i++) {
            helpGuest = helpIterator.next();
            speicher.setPartyIndex(helpGuest.getMood());
            legendItems.add(helpGuest);
        }
        
        nameColumn.setCellValueFactory(new PropertyValueFactory<Guest, String>("Name"));
        moodColumn.setCellValueFactory(new PropertyValueFactory<Guest, Float>("Mood"));
        
        legende.getColumns().clear();
        legende.getColumns().add(nameColumn);
        legende.getColumns().add(moodColumn);
        
        legende.setItems(legendItems);

        legende.setPrefWidth(rightColumnWidth);
        
        indexText.setText(speicher.getCurrentPartyIndex().toString());
        
        
    }


    /**
     * Create table-squares in the room
     */
    protected void drawTable() {
    	
    	Table table;
    	
    	
    	if (!speicher.getRoom().getTables().isEmpty()) {
    		Iterator<Table> tableIterator = speicher.getRoom().getTables().iterator();
    		while(tableIterator.hasNext()) {
    			table = tableIterator.next();
	    		for (int x = (int) table.getPosition().getX(); x < table.getPosition().getX()+table.getLength(); x++) {
	                for (int y = (int) table.getPosition().getY(); y < table.getPosition().getY()+table.getHeight(); y++)
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
    	}        
    }

    private void configureActions() {
    	
    	startSimulationButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
    		public void handle(MouseEvent event) {
    			service = new MyService(party_planer_gui_controller.this, speicher, simulation);
    			service.start();
    			
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
                				if ((posY + height > speicher.getRoom().getWidth()) || 
                				(posX + length > speicher.getRoom().getLength()) || 
                				(posX < 0) || (posX >= speicher.getRoom().getLength()) || 
                				(posY < 0) || (posY >= speicher.getRoom().getWidth())) {
                					
                					Alert alert = new Alert(AlertType.WARNING);
                	    			alert.setTitle("Fehlerhafte Eingabe");
                	    			alert.setContentText("Der Tisch ist zu groß für den Raum!");
                	    			alert.showAndWait();
                				} else {
                					speicher.getRoom().setTable(new Table(length, height, new Point(posX, posY)));
                    				drawTable();
                				}
                			} catch(Exception e) {
                				tablePosYField.setText("fehlerhafte Eingabe");
                				Alert alert = new Alert(AlertType.WARNING);
                    			alert.setTitle("Fehlerhafte Eingabe");
                    			alert.setContentText("Die Eingabe enthält möglicherweise ungültige Zeichen!");
                    			alert.showAndWait();
                			}
            			} catch(Exception e) {
            				tablePosXField.setText("fehlerhafte Eingabe");
            				Alert alert = new Alert(AlertType.WARNING);
                			alert.setTitle("Fehlerhafte Eingabe");
                			alert.setContentText("Die Eingabe enthält möglicherweise ungültige Zeichen!");
                			alert.showAndWait();
            			}
        			} catch(Exception e) {
        				tableHeightField.setText("fehlerhafte Eingabe");
        				Alert alert = new Alert(AlertType.WARNING);
            			alert.setTitle("Fehlerhafte Eingabe");
            			alert.setContentText("Die Eingabe enthält möglicherweise ungültige Zeichen!");
            			alert.showAndWait();
        			}
    			} catch(Exception e) {
    				tableLengthField.setText("fehlerhafte Eingabe");
    				Alert alert = new Alert(AlertType.WARNING);
        			alert.setTitle("Fehlerhafte Eingabe");
        			alert.setContentText("Die Eingabe enthält möglicherweise ungültige Zeichen!");
        			alert.showAndWait();
    			}
    		}
    	});
    	
    	addGuestButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
    		public void handle(MouseEvent event) {
    			if(guestNameField.getText().isEmpty()) {
    				Alert alert = new Alert(AlertType.WARNING);
        			alert.setTitle("Fehlende Eingabe");
        			alert.setContentText("Der Name des Gastes wurde nicht eingegeben!");
        			alert.showAndWait();
    			}
    			else if(guestJobField.getText().isEmpty()) {
    				Alert alert = new Alert(AlertType.WARNING);
        			alert.setTitle("Fehlende Eingabe");
        			alert.setContentText("Der Beruf des Gastes wurde nicht eingegeben!");
        			alert.showAndWait();
    			}
    			else {
    				Guest helpGuest = new Guest(guestNameField.getText(),guestJobField.getText());
    				Point point = new Point();
    				point.setLocation(Integer.valueOf(xPosField.getText()), Integer.valueOf(yPosField.getText()));
    				helpGuest.setPosition(point);
    				guestNameField.setText("");
    				guestJobField.setText("");
    				xPosField.setText("");
    				yPosField.setText("");
    				if (speicher.getRoom().positionPossible(point)) {
    					speicher.getGuestList().add(helpGuest);
    					drawLegend();
        				drawGuests();
        				guestListfinishedButton.setDisable(false);
    				} else {
    					Alert alert = new Alert(AlertType.WARNING);
    	    			alert.setTitle("ungültige Eingabe");
    	    			alert.setContentText("Die angegebene Position befindet sich außerhalb des Raums!!");
    	    			alert.showAndWait();
    				}
    				
    				
    				
    			}
    		}
    	});
    	
    	guestListfinishedButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
    		public void handle(MouseEvent event) {
    			distanceField.setDisable(false);
    			if (speicher.getGuestList().isEmpty()) {  
    			Alert alert = new Alert(AlertType.WARNING);
    			alert.setTitle("Fehlende Eingabe");
    			alert.setContentText("Es wurde noch kein Gast hinzugefügt!");
    			alert.showAndWait();
    			}
    			else {
    				mainGuestIterator = speicher.getGuestList().iterator();
        			helpGuestIterator = speicher.getGuestList().iterator();
    				mainGuest = mainGuestIterator.next();
        			helpGuest = helpGuestIterator.next();
        			while(helpGuest.equals(mainGuest)) {
        				if (helpGuestIterator.hasNext()) helpGuest = helpGuestIterator.next();
        				else if (mainGuestIterator.hasNext()) mainGuest = mainGuestIterator.next();
        				else {
        					distanceField.setDisable(true);
        					saveConfigButton.setDisable(false);
        					break;
        				}
        			}
        			saveDistanceButton.setDisable(false);
        			guestText.setText(mainGuest.getName());
        			helpGuestText.setText(helpGuest.getName());
    			}
    			
    		}
    	});
    	
    	saveDistanceButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
    		public void handle(MouseEvent event) {
    			
    			
    			mainGuest.setDistances(helpGuest, Float.valueOf(distanceField.getText()));
				if (!helpGuestIterator.hasNext()) {
					if (!mainGuestIterator.hasNext()) {
						guestText.setText("no more Guests");
	    				helpGuestText.setText("no more Guests");
	    				saveConfigButton.setDisable(false);
					} else {
					mainGuest = mainGuestIterator.next();
					guestText.setText(mainGuest.getName());
					helpGuestIterator = speicher.getGuestList().iterator();
					helpGuest = helpGuestIterator.next();
					helpGuestText.setText(helpGuest.getName());
					}
				} else {
				helpGuest = helpGuestIterator.next();
				helpGuestText.setText(helpGuest.getName());
				}
    			
    			
    			
    		}
    	});
    	
    	saveConfigButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
    		public void handle(MouseEvent event) {
    			
    			
    	    	HashMap<Guest, Point> map = new HashMap<Guest, Point>();
    	    	Iterator<Guest> helpIterator = speicher.getGuestList().iterator();
    	    	Guest helpGuest;
    	    	while (helpIterator.hasNext()) {
    	    		helpGuest = helpIterator.next();
    	    		map.put(helpGuest, helpGuest.getPosition());
    	    	}
    	    	
    	    	Configuration config = new Configuration(Integer.valueOf(simulationSpeedField.getText()), Integer.valueOf(simulationIterationsField.getText()), map);
    	    	speicher.setConfig(config);
    	    	simulation = new Simulation(speicher,party_planer_gui_controller.this);
    	        refresh();
    		}
    	});
    	
    	saveRoomButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
    		public void handle(MouseEvent event) {
    			
    			Room room = new Room(Integer.valueOf(roomLengthField.getText()), Integer.valueOf(roomHeightField.getText()));
    	    	speicher = new Speicher(room);
    	    	addTableButton.setDisable(false);
    	    	addGuestButton.setDisable(false);
    		}
    	});
    	
    	
    	
    }
    
}
