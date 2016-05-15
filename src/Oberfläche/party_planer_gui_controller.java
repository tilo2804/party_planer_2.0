package Oberfl�che;

import java.io.IOException;

import Simulation.Simulation;
import Speicher.Speicher;
import data.Room;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class party_planer_gui_controller {
	
	@FXML Button buttonStart;
	@FXML Button buttonPause;
	@FXML Button buttonStopp;
	@FXML Button buttonNextStep;
	@FXML Button buttonNextIteration;

    @FXML StackPane main;
    @FXML GridPane zimmerGrid;
    @FXML ListView legende;
    @FXML VBox rightBox;

    @FXML Tab configurationTab;
    @FXML Tab disabledTab;

    // TODO: get room size from config
    final int roomWidth = 600; // Zimmerlänge
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
    
    Speicher speicher;
    Simulation simulation;
    
    public party_planer_gui_controller(Stage primaryStage) {
    	Scene scene = initScene();
    	showScene(primaryStage, scene);
    	Room room = new Room(15, 10);
    	speicher = new Speicher(room);
        simulation = new Simulation(speicher,this);
        drawSquares();
    }
    
    public void refresh() {
    	
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
 /*   protected void drawGuests() {
        // add the guests to the squares
        for (int i = 0; i < guests.length; i++) {
            // get the current guests's data
            int guestPosX = (int)guests[i][2];
            int guestPosY = (int)guests[i][3];
            String guestLetter = guests[i][0].toString().substring(0, 1);
            // create a new square with background and letter
            Pane square = drawRoomSquare("guest");
            Label letter = createRoomLetter(guestLetter, "guest");
            // add to the room the square and the guests's letter
            zimmerGrid.add(square, guestPosX, guestPosY);
            zimmerGrid.add(letter, guestPosX, guestPosY);
            // align the letter in center
            zimmerGrid.setHalignment(letter, HPos.CENTER);
        }
    }
*/
    /**
     * Add a legend with guests names
     */
 /*   protected void drawLegend() {
        ObservableList<String> legendItems = FXCollections.observableArrayList();

        for (int i = 0; i < guests.length; i++) {
            String name = guests[i][0].toString();
            String text = name.substring(0, 1) + ": " + name;
            legendItems.add(text);
        }

        legende.setItems(legendItems);

        rightBox.setPrefWidth(rightColumnWidth);
    }

*/
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

    @FXML private void buttonStartAction(ActionEvent event) {
    	simulation.startSimulation();
    }

    @FXML private void buttonPauseAction(ActionEvent event) {
    	simulation.pauseSimulation();
    }

    @FXML private void buttonStopAction(ActionEvent event) {
    	simulation.stopSimulation();
    }

    @FXML private void buttonNextIterationAction(ActionEvent event) {
    	simulation.nextIteration();
    }

    @FXML private void buttonNextStepAction(ActionEvent event) {
    	simulation.nextGuest();
    }

}
